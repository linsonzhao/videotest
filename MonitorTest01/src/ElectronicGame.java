import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.CV_RGB;
import static com.googlecode.javacv.cpp.opencv_core.CV_TERMCRIT_EPS;
import static com.googlecode.javacv.cpp.opencv_core.CV_TERMCRIT_ITER;
import static com.googlecode.javacv.cpp.opencv_core.cvAnd;
import static com.googlecode.javacv.cpp.opencv_core.cvConvertScale;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvResetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvScalarAll;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_core.cvTermCriteria;
import static com.googlecode.javacv.cpp.opencv_core.cvXorS;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_EVENT_LBUTTONDOWN;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_EVENT_LBUTTONUP;
import static com.googlecode.javacv.cpp.opencv_highgui.cvQueryFrame;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HIST_ARRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCalcBackProject;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCalcHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCreateHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetMinMaxHistValue;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import static com.googlecode.javacv.cpp.opencv_video.cvCamShift;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvConnectedComp;
import com.googlecode.javacv.cpp.opencv_imgproc.CvHistogram;


//550,410;

public class ElectronicGame {
	private Cameria cameria;
	private boolean end = false;
	private playsound plays;
	private ElectronicPanel parent;
	private IplImage normal,shut;
	//
	IplImage frame, image, hsv, hue, mask, backproject, histimg;
	IplImage[] imageArray;
	// 用HSV中的Hue分量进行跟踪
	CvHistogram hist;
	// 直方图类
	int x1 = 0, y1 = 0, x2 = 0, y2 = 0;// 选取对象的坐标
	int backproject_mode = 0;
	int select_object = 0;
	int track_object = 0;
	int show_hist = 1;
	CvPoint origin;
	CvPoint cp1, cp2;
	CvRect selection;
	CvRect track_window;
	CvBox2D track_box;
	float[] max_val = new float[1];
	int[] hdims = { 256 }; // 16
	// 划分直方图bins的个数，越多越精确
	float[][] hranges_arr = { { 0, 180 } };
	// 像素值的范围
	float[][] hranges = hranges_arr;
	// 用于初始化CvHistogram类

	CvConnectedComp track_comp;
	//
	Pointer pointer = null;
	
	public void setParent(ElectronicPanel panel){
		this.parent=panel;
	}
	public ElectronicGame(Cameria cameria){
		this.cameria=cameria;
		plays = new playsound();
		imageArray = new IplImage[1];
		//TODO add mouse listener;
		track_comp = new CvConnectedComp();
		
		try {
			normal = IplImage.createFrom(ImageIO.read((this.getClass().getResource("normal.png"))));
			shut= IplImage.createFrom(ImageIO.read((this.getClass().getResource("shut.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public IplImage doProcess(){
		if(end == true)
		{
			closeTheGame();
		}
		frame = doResize(cameria.getiplImage());
//		cvFlip(frame, frame, 1);
		if (frame == null) {
			closeTheGame();
		}

		if (image == null)
		// image为空,表明刚开始还未对image操作过,先建立一些缓冲区
		{
			image = cvCreateImage(cvGetSize(frame), 8, 3);
			image.origin(frame.origin());
			hsv = cvCreateImage(cvGetSize(frame), 8, 3);
			hue = cvCreateImage(cvGetSize(frame), 8, 1);
			mask = cvCreateImage(cvGetSize(frame), 8, 1);
			// 分配掩膜图像空间
			backproject = cvCreateImage(cvGetSize(frame), 8, 1);
			// 分配反向投影图空间,大小一样,单通道
			hist = cvCreateHist(1, hdims, CV_HIST_ARRAY, hranges, 1);
			// 分配直方图空间
		}
		cvCopy(frame, image);
		cvCvtColor(image, hsv, CV_BGR2HSV);
		if (track_object != 0)
		// track_object非零,表示有需要跟踪的物体
		{
			double _vmin = 10.0, _vmax = 256.0, smin = 30.0;

			cvInRangeS(hsv,
					cvScalar(0.0, smin, Math.min(_vmin, _vmax), 0.0),
					cvScalar(180.0, 256.0, Math.max(_vmin, _vmax), 0.0),
					mask);
			// ，只处理像素值为H：0~180，S：smin~256，V：vmin~vmax之间的部分制作掩膜板
			cvSplit(hsv, hue, null, null, null);
			// 分离H分量
			imageArray[0] = hue;
			if (track_object < 0)
			// 如果需要跟踪的物体还没有进行属性提取，则进行选取框类的图像属性提取
			{
				cvSetImageROI(imageArray[0], selection);
				// 设置原选择框为ROI
				cvSetImageROI(mask, selection);
				// 设置掩膜板选择框为ROI
				cvCalcHist(imageArray, hist, 0, mask);
				// 得到选择框内且满足掩膜板内的直方图
				cvGetMinMaxHistValue(hist, null, max_val, null, null);
				cvConvertScale(hist.bins(), hist.bins(),
						max_val[0] > 0 ? (double) 255 / max_val[0] : 0.0, 0);
				// 对直方图的数值转为0~255
				cvResetImageROI(imageArray[0]);
				// 去除ROI
				cvResetImageROI(mask);
				// 去除ROI
				track_window = selection;
				track_object = 1;
				// 置track_object为1,表明属性提取完成
			}
			cvCalcBackProject(imageArray, backproject, hist);
			// 计算hue的反向投影图
			cvAnd(backproject, mask, backproject, null);
			// 得到掩膜内的反向投影
			cvCamShift(
					backproject,
					track_window,
					cvTermCriteria(CV_TERMCRIT_EPS | CV_TERMCRIT_ITER, 10,
							1), track_comp, track_box);
			// 使用MeanShift算法对backproject中的内容进行搜索,返回跟踪结果
			track_window = track_comp.rect();
			// 得到跟踪结果的矩形框
			cp1 = cvPoint(track_window.x(), track_window.y());
			cp2 = cvPoint(track_window.x() + track_window.width(),
					track_window.y() + track_window.height());
			if (image.origin() > 0)
				track_box.angle(-track_box.angle());

			// action(track_window); //add

			cvRectangle(frame, cvPoint(0, 0), cvPoint(330, 30),
					CV_RGB(255, 255, 255), -1, CV_AA, 0);
			cvRectangle(frame, cvPoint(120, 80), cvPoint(510, 120),
					CV_RGB(255, 255, 255), -1, CV_AA, 0);
			cvRectangle(frame, cvPoint(0, 180), cvPoint(420, 220),
					CV_RGB(255, 255, 255), -1, CV_AA, 0);
			cvRectangle(frame, cvPoint(120, 260), cvPoint(510, 300),
					CV_RGB(255, 255, 255), -1, CV_AA, 0);
			
			cvRectangle(frame,cvPoint(420,310),cvPoint(510,480),CV_RGB(0,0,255),-1,CV_AA,0);
			
			
//			cvOverlayImage(frame, normal, cvPoint(100, 100), cvScalar(0,0,0,0), cvScalar(1,1,1,1)); 

			
			cvRectangle(
					frame,
					cvPoint(track_window.x() + track_window.width() / 2 - 2,
							track_window.y() + track_window.height() / 2
									- 2),
					cvPoint(track_window.x() + track_window.width() / 2 + 2,
							track_window.y() + track_window.height() / 2
									+ 2), CV_RGB(255, 0, 0), -1, CV_AA, 0);

			cvRectangle(frame, cp1, cp2, CV_RGB(0, 255, 0), 1, CV_AA, 0);
			int x = track_window.x() + track_window.width() / 2;
			int y = track_window.y() + track_window.height() / 2;
			if ((x >= 0 && x <= 330) && (y >= 0 && y <= 30)){
//				System.out.println("touched!!");
				crush();
			}
			else if ((x >= 120 && x <= 510) && (y >= 80 && y <= 120)){
//				System.out.println("touched!!");
				crush();
			}
			else if ((x >= 0 && x <= 420) && (y >= 180 && y <= 220)){
//				System.out.println("touched!!");
				crush();
			}
			else if ((x >= 120 && x <= 510) && (y >= 260 && y <= 300)){
//				System.out.println("touched!!");
				crush();
			}
			else if ((x >= 420&& x <= 510) && (y >= 310 && y <= 480)){
//				System.out.println("yes!!");
				normal();
				winTheGame();
			}else{
				normal();
			}


		}
		if (select_object == 1 && selection.width() > 0
				&& selection.height() > 0)
		// 如果正处于物体选择，画出选择框
		{
			cvSetImageROI(frame, selection);
			cvXorS(frame, cvScalarAll(255), frame, null);
			cvResetImageROI(frame);
		}
		return frame;
		
	}
	

	
	public void call(int event, int x, int y, int flags, Pointer param)
	// 鼠标回调函数,该函数用鼠标进行跟踪目标的选择
	{
		if (image == null)
			return;
//		if (image.origin() != 0)
//			y = image.height() - y;
		// 如果图像原点坐标在左下,则将其改为左上

		if (select_object == 1)
		// select_object为1,表示在用鼠标进行目标选择
		// 此时对矩形类selection用当前的鼠标位置进行设置
		{
			selection.x(Math.min(x, origin.x()));
			selection.y(Math.min(y, origin.y()));
			selection.width(selection.x() + Math.abs(x - origin.x()));
			selection.height(selection.y() + Math.abs(y - origin.y()));
			selection.x(Math.max(selection.x(), 0));
			selection.y(Math.max(selection.y(), 0));
			selection.width(Math.min(selection.width(), image.width()));
			selection.height(Math.min(selection.height(), image.height()));
			selection.width(selection.width() - selection.x());
			selection.height(selection.height() - selection.y());
		}
		switch (event) {
		case CV_EVENT_LBUTTONDOWN:
			// 鼠标按下,开始点击选择跟踪物体
			origin = cvPoint(x, y);
			selection = cvRect(0, 0, 0, 0);
			select_object = 1;
//			System.out.println("11111111111111111111111111111111");
			break;
		case CV_EVENT_LBUTTONUP:
			// 鼠标松开,完成选择跟踪物体
			select_object = 0;
			if (selection.width() > 0 && selection.height() > 0)
				// 如果选择物体有效，则打开跟踪功能
				track_object = -1;
			break;
		}
	}
	
	public void cvOverlayImage(IplImage src, IplImage overlay, CvPoint location, CvScalar S, CvScalar D)
	{
	 int x,y,i;

	  for(x=0;x < overlay.width() -10;x++)     
	//replace '-10' by whatever x position you want your overlay image to begin. 
	//say '-varX'
	    {
	        if(x+location.x()>=src.width()) continue;
	        for(y=0;y < overlay.height() -10;y++)  
	//replace '-10' by whatever y position you want your overlay image to begin.
	//say '-varY'
	        {
	            if(y+location.y()>=src.height()) continue;
	            CvScalar source = cvGet2D(src, y+location.y(), x+location.x());
	            CvScalar over = cvGet2D(overlay, y, x);
	            CvScalar merged = new CvScalar();
	            for(i=0;i<4;i++)
//	            merged.val[i] = (S.val[i]*source.val[i]+D.val[i]*over.val[i]);
	            	merged.setVal(i, S.getVal(i)*source.getVal(i)+D.getVal(i)*over.getVal(i));
	            cvSet2D(src, y+location.y(), x+location.x(), merged);
	        }
	    }
	}
	
	public void winTheGame(){
		this.track_object=0;
		parent.toDrawWin();
		plays.playWin();
	}
	public void closeTheGame(){
		//TODO
	}
	public void crush(){
		plays.playTheMusic();
	}
	public void normal(){
		plays.stopTheMusic();
	}
	
	public BufferedImage getProcessedImage(){
		return doProcess().getBufferedImage();
	}
	
	public IplImage doResize(IplImage image){
		IplImage out = cvCreateImage(new CvSize(550,413),image.depth(),image.nChannels());
//		cvPyrDown(image,out,CV_GAUSSIAN_5x5);
		cvResize(image, out, CV_INTER_LINEAR);    
//		cvReleaseImage(image);
		return out;
	}
	
}
