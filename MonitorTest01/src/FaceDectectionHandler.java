import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRenderedImage;
import java.io.InputStream;
import java.util.List;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*; 


import jjil.algorithm.Gray8Rgb;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Image;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;


public class FaceDectectionHandler {
	private int minScale;
	private int maxScale;
	private java.awt.Image paintImage;
	private boolean isDraw=false;
	private Rectangle[] rects;

	String CASCADE_FILE ="C:\\opencv\\data\\haarcascades\\haarcascade_frontalface_alt2.xml";
	CvHaarClassifierCascade cascade =new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
	
	public FaceDectectionHandler(int minScale,int maxScale,java.awt.Image paintImage){
		this.minScale=minScale;
		this.maxScale=maxScale;
		this.paintImage=paintImage;
	}
	public FaceDectectionHandler(int minScale,int maxScale){
		this(minScale, maxScale,null);
	}
	public FaceDectectionHandler(){
		this(1,40,null);
	}
	
	 
	
	public synchronized void findFaces(BufferedImage bi){
		
		IplImage origImg = IplImage.createFrom(bi);
		IplImage grayImg = IplImage.create(origImg.width(),origImg.height(), IPL_DEPTH_8U, 1);
		 
        cvCvtColor(origImg, grayImg, CV_BGR2GRAY); 
        CvMemStorage storage = CvMemStorage.create();
        
        CvSeq faces = cvHaarDetectObjects(grayImg, cascade, storage,1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
        cvClearMemStorage(storage);
        int total = faces.total();
        CvRect[] results = new CvRect[total];
        for (int i = 0; i < total; i++) {
        	results[i] = new CvRect(cvGetSeqElem(faces, i));
        }

         rects = new Rectangle[results.length];
         int i=0;
         for(CvRect r:results){
        	 rects[i]=new Rectangle(r.x(),r.y(),r.width(),r.height());
         }
         
	 }
	
	
	public synchronized BufferedImage paintTheImage(BufferedImage image){
		int midx;
		int midy;
		int imageWidth;
		int imageHeight;
		Graphics g = image.getGraphics();
		for(Rectangle rect: rects){
			if(rect==null)continue;
			rect = enlargeFaceBox(0.8f,rect);
//			g.drawRect(rect.x, rect.y, rect.width, rect.height);
				imageWidth=(int) (rect.width*1.1);
				imageHeight=(int) (imageWidth*1.4);
				midx= rect.x-imageWidth/38;
				midy= (int) (rect.y-imageHeight/5.3);
			
			g.drawImage(paintImage, midx, midy, imageWidth,imageHeight,null);
		}
		g.dispose();
		return image;
	}
	
	
	public synchronized BufferedImage paintTheImage2(BufferedImage image){
		int midx;
		int midy;
		int imageWidth;
		int imageHeight;
		Graphics g = image.getGraphics();
		for(Rectangle rect: rects){
			if(rect==null)continue;
			rect = enlargeFaceBox(0.8f,rect);
//			g.drawRect(rect.x, rect.y, rect.width, rect.height);
				imageWidth=(int) (rect.width*1.1);
				imageHeight=(int) (imageWidth*1.4);
				midx= rect.x-imageWidth/38;
				midy= (int) (rect.y-imageHeight/5.3);
			
			g.drawImage(paintImage, midx*4, midy*4, imageWidth*4,imageHeight*4,null);
		}
		g.dispose();
		return image;
	}

	
	public Rectangle enlargeFaceBox (float incPct,Rectangle re) {
		int x=re.x;
		int y=re.y;
		int w=re.width;
		int h=re.height;
	    float r = Math.max(w,h) / 2;  //Computes radius of the center diagonal
	    float theta = (float) Math.atan2(h,w);  //Computes the angle of the diagonal
	    float dx = (float) (r*incPct*Math.cos(theta)); //Finds 
	    float dy = (float) (r*incPct*Math.sin(theta));
	    return new Rectangle( (int) (x - dx), (int) (y - dy), (int) (w + 2*dx), (int) (h + 2*dy));
	}

	
	public boolean isHead(){
		if(rects.length>0){
			if(rects[0]!=null){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	 
	 
	public Rectangle[] getRects() {
		return rects;
	}
	public void setRects(Rectangle[] rects) {
		this.rects = rects;
	}
	public int getMinScale() {
			return minScale;
		}
		public void setMinScale(int minScale) {
			this.minScale = minScale;
		}
		public int getMaxScale() {
			return maxScale;
		}
		public void setMaxScale(int maxScale) {
			this.maxScale = maxScale;
		}
		public java.awt.Image getPaintImage() {
			return paintImage;
		}
		public void setPaintImage(java.awt.Image paintImage) {
			this.paintImage = paintImage;
		}
		public boolean isDraw() {
			return isDraw;
		}
		public void setDraw(boolean isDraw) {
			this.isDraw = isDraw;
		}


}
