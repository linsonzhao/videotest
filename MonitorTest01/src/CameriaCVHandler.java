import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvCreateCameraCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvQueryFrame;
import static com.googlecode.javacv.cpp.opencv_highgui.cvReleaseCapture;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GAUSSIAN_5x5;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvPyrDown;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class CameriaCVHandler implements Cameria{
	
	private CvCapture capture;
	private IplImage frame=IplImage.create(new CvSize(640,480), IPL_DEPTH_8U, 3);
	private Dimension dimensionXY =new Dimension(160,120); 

	public CameriaCVHandler(){
		capture = cvCreateCameraCapture(0);
	}
	 
	@Override
	public BufferedImage getBufferedImage() {
//		frame = cvQueryFrame(capture);
//		BufferedImage tmp = new BufferedImage(dimensionXY.width,dimensionXY.height,BufferedImage.TYPE_INT_ARGB);
//		tmp.getGraphics().drawImage(frame.getBufferedImage(),0,0,dimensionXY.width , dimensionXY.height, null);
//		return tmp;
		
		
		return doResize(getiplImage()).getBufferedImage();
	}
	
	public IplImage getiplImage(){
		frame = cvQueryFrame(capture);
		cvFlip(frame,frame,1);
		return frame;
	}

	public static IplImage doResize(IplImage image){
		IplImage out = cvCreateImage(new CvSize(cvGetSize(image).width()/4,cvGetSize(image).height()/4),image.depth(),image.nChannels());
//		cvPyrDown(image,out,CV_GAUSSIAN_5x5);
		cvResize(image, out, CV_INTER_LINEAR);    
		return out;
	}
	
	@Override
	public boolean startCapture() {
		
		return true;
	}

	@Override
	public boolean stopCameria() {
		cvReleaseCapture(capture);
		return false;
	}

	@Override
	public boolean pauseCapture() {
		return false;
	}

	@Override
	public boolean reStartCameria() {
		capture = cvCreateCameraCapture(0);
		return false;
	}

	@Override
	public void setXY(Dimension d) {
	}

	@Override
	public int getStates() {
		return 0;
	}

}
