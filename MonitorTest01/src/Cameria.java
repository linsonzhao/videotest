import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;


public interface Cameria {
	public final static String DEFAULT_DEV_NAME ="vfw:Microsoft WDM Image Capture (Win32):0";
	public final static int DEFAULT_X_RES = 160;
	public final static int DEFAULT_Y_RES = 120;
	public final static int DEFAULT_DEPTH = 24;
	public final static int CAMERIA_START_CAPTURE_START=0;
	public final static int CAMERIA_START_CAPTURE_STOP=1;
	public final static int CAMERIA_STOP=2;
	
	
//	public Image getImage();
	public BufferedImage getBufferedImage();
	public boolean startCapture();
	public boolean stopCameria();
	public boolean pauseCapture();
	public IplImage getiplImage();
	public boolean reStartCameria();
	public void setXY(Dimension d);
	public int getStates();
}
