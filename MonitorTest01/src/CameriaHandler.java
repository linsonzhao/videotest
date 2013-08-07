import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NotRealizedError;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.control.FormatControl;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.util.BufferToImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class CameriaHandler implements Cameria,ControllerListener{
	private static final String PROPERTIES_NAME ="video.properties";
	
	private String dev_name;
	private Dimension cameriaSize;
	private int viewDepth;
	
	private Properties videoProperties;
	private Thread captureThread;
	
	//
	private Object stateLock = new Object();
	private Object runLock = new Object();

	private Processor deviceProc = null;
	private PushBufferStream camStream;
	private PushBufferDataSource source = null;
	private BufferToImage converter;
	private Image currentImage;
	private boolean threadRunning = false;

	private MediaLocator loc;
	private DataSource formattedSource;
	private boolean FormatRGB = true;
	//
	
	public CameriaHandler(){
		getInfomationFromProperties();
		setUpCameria();
		startCameria();
		
	}
	
	
	public void getInfomationFromProperties(){
		
		//載入設定檔
		videoProperties = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(PROPERTIES_NAME));
			videoProperties.load(fis);
		} catch (IOException e) {
			System.out.println("設定檔載入錯誤");
			setDefaultProperties();
			return;
		}
		
		//讀取設定檔
		dev_name=videoProperties.getProperty("device-name", DEFAULT_DEV_NAME);
		String x_res=videoProperties.getProperty("resolution-x", DEFAULT_X_RES+"");
		String y_res=videoProperties.getProperty("resolution-y", DEFAULT_Y_RES+"");
		FormatRGB=videoProperties.getProperty("format").equals("RGB");
		try{
			viewDepth = Integer.parseInt(videoProperties.getProperty("colour-depth", DEFAULT_DEPTH+""));
			cameriaSize=new Dimension(Integer.parseInt(x_res),Integer.parseInt(y_res));
		}catch(NumberFormatException e){
			System.out.println("設定檔格式錯誤");
			setDefaultProperties();
			return;
		}
		System.out.println("設定檔讀取成功");

	}
	
	//預設設定
	public void setDefaultProperties(){
		dev_name = DEFAULT_DEV_NAME;
		FormatRGB=true;
		viewDepth=DEFAULT_DEPTH;
		cameriaSize= new Dimension (DEFAULT_X_RES,DEFAULT_Y_RES);
	}
	
	public void setUpCameria(){
		CaptureDeviceInfo device = CaptureDeviceManager.getDevice(dev_name);
		if(device==null){
			System.out.println("無法載入選設定之視訊頭");
		}
		
		VideoFormat userFormat = null ;
		Format[] cfmt = device.getFormats();

		for (int i = 0; i < cfmt.length; i++)
		{
			if (FormatRGB && cfmt[i] instanceof RGBFormat)
			{
				userFormat = (RGBFormat)cfmt[i];
				Dimension d = userFormat.getSize();
				int bitsPerPixel = ((RGBFormat)userFormat).getBitsPerPixel();

				if (cameriaSize.equals(d) && bitsPerPixel == viewDepth)
					break;

				userFormat = null;
			}else if(cfmt[i] instanceof YUVFormat) {
				userFormat = (YUVFormat)cfmt[i];
				Dimension d = userFormat.getSize();
				if (cameriaSize.equals(d))
					break;
				userFormat = null;
			}
		}
		
		if(userFormat==null){
			System.out.println("您的設定格式 與硬體裝置不符");
		}
		loc = device.getLocator();
		formattedSource = null;
		
		try{
			formattedSource = Manager.createDataSource(loc);
		}
		catch (Exception e){
			System.out.println("載入錯誤");
		}
		
		
		
		Format setFormat = null;
		FormatControl[] fmtControls =((CaptureDevice)formattedSource).getFormatControls();
		
		for (int i = 0; i < fmtControls.length; i++)
		{
			if (fmtControls[i] == null)
			continue;

			if ((setFormat = fmtControls[i].setFormat(userFormat)) != null)
				break;
		}
		
		try{
			formattedSource.connect();
		}
		catch (IOException ioe){
			System.out.println("連接錯誤");
		}
		
		try{
			deviceProc = Manager.createProcessor(formattedSource);
		}
		catch (Exception e){
			System.out.println("錯誤d001");
		}
		
		deviceProc.addControllerListener(this);
	}

	
	public void startCameria(){
		deviceProc.realize();
		
		while (deviceProc.getState() != Controller.Realized){
			synchronized (stateLock){
				try{
					stateLock.wait();
				}
				catch (InterruptedException ie){
					System.out.println("執行序發生錯誤");
				}
			}
		}
		deviceProc.start();
		
		try{
			source = (PushBufferDataSource)deviceProc.getDataOutput();
		}
		catch (NotRealizedError nre){
			System.out.println("取得cameria的output失敗");
		}
		
		
		PushBufferStream[] streams = source.getStreams();
		camStream = null;

		for (int i = 0; i < streams.length; i++){
			if (streams[i].getFormat() instanceof RGBFormat){
				camStream = streams[i];
				RGBFormat rgbf = (RGBFormat)streams[i].getFormat();
				converter = new BufferToImage(rgbf);
				break;
			}else if(streams[i].getFormat() instanceof YUVFormat){
				camStream = streams[i];
				converter = new BufferToImage((VideoFormat)streams[i].getFormat());
				break;
			}
		}

		
		System.out.println("Cameria 連接成功");
	}
	
	
	public boolean stopCameria(){
		if (captureThread!=null){
			if(captureThread.isAlive()){
//				captureThread.interrupt();
				captureThread.stop();
				threadRunning=false;
				//TODO more stop requst thing
			}else{
				captureThread=null;
			}
		}
		
		if(deviceProc!=null){
			deviceProc.close();
			deviceProc=null;
			return true;
		}
		return false;
	}
	
	public boolean reStartCameria(){
		if(deviceProc==null){
			setUpCameria();
			startCameria();
			startCapture();
			return true;
		}else{
			return startCapture();
		}
	}
	
	public boolean startCapture(){
		if (captureThread!=null){
			if(captureThread.isAlive()){
				return false;
			}else{
				captureThread=null;
			}
		}
		captureThread = new CaptureThread();
		captureThread.start();
		System.out.println("截取執行序開始");
		return true;
	}
	
	public boolean pauseCapture(){
		if (captureThread!=null){
			if(captureThread.isAlive()){
				captureThread.stop();
				threadRunning=false;
				return true;
			}
		}
		return false;
	}
	
	public void setXY(Dimension d){
		this.cameriaSize=d;
		stopCameria();
		reStartCameria();
	}

	@Override
	public void controllerUpdate(ControllerEvent arg0){
		if (arg0 instanceof RealizeCompleteEvent){
			synchronized (stateLock){
				stateLock.notifyAll();
			}
		}
	}
	
	private synchronized Image accessInternalImage(Image image){
		if (image == null){
			return currentImage;
		}

		currentImage = image;
		return null;
	}
	
	public Image getImage(){
		while (threadRunning == false){
			synchronized (runLock){
				try{
					runLock.wait();
				}
				catch (InterruptedException ie){
					// Ignore
				}
			}
		}
		return accessInternalImage(null);
	}
	
	public BufferedImage getBufferedImage(){
		return (BufferedImage)getImage();
	}
	
	private class CaptureThread extends Thread{
		public void run(){
			System.out.println("開始從Cameria截圖");
			Buffer b = new Buffer();

			while (true){
				try{
					camStream.read(b);
				}
				catch (Exception e){
					System.out.println("Cameria截圖錯誤");
				}

				Image i = converter.createImage(b);
				accessInternalImage(i);
				/*If this is the first image we've collected we need to advertise
				 *to the main thread that there is an image ready and then notify
				 *the main thread in case it is waiting on the image
				 */
				if (!threadRunning)
				{
					threadRunning = true;

					synchronized (runLock)
					{
						runLock.notifyAll();
					}
				}
			}
		}
	}

	public int getStates() {
		if(deviceProc!=null){
			if(captureThread!=null){
				if(captureThread.isAlive()){
					return Cameria.CAMERIA_START_CAPTURE_START;
				}else{
					return Cameria.CAMERIA_START_CAPTURE_STOP;
				}
			}else{
				return Cameria.CAMERIA_START_CAPTURE_STOP;
			}
		}else{
			return Cameria.CAMERIA_STOP;
		}
	}


	@Override
	public IplImage getiplImage() {
		return IplImage.createFrom(getBufferedImage());
		
	}
	
	
}
