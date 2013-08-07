import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageHandler implements ImageManager{
	private Cameria cameria;
	private ImageDifferentHandler differentImage;
	private FaceDectectionHandler faceDectect;
	private MobileConnecter mobileConnecter;
	
	private boolean isDrawFace=true;
	private boolean isDrawMove=false;
	private boolean isDealInfo = true;
	public ImageHandler(Cameria cameria,MobileConnecter mobileConnecter){
		this.cameria=cameria;
		this.mobileConnecter=mobileConnecter;
		cameria.startCapture();
		differentImage = new ImageDifferentHandler(cameria.getBufferedImage());
		faceDectect=new FaceDectectionHandler();
		try {
			faceDectect.setPaintImage(ImageIO.read((this.getClass().getResource("face.png"))));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	
	public void changeConfig(int set, Object object) {
		//TODO stop now process
		switch(set){
		case ImageProcessor.COLOR_DIFFERENCE:
			differentImage.setColor_difference((Integer)object);
			break;
		case ImageProcessor.DIFFERENCE_TIMES:
			differentImage.setDifference_times((Integer)object);
			break;
		case ImageProcessor.MOVE_COLOR:
			differentImage.setMove((Color)object);
			break;
		case ImageProcessor.SIDE_LENGTH:
			differentImage.getMovementIdentifer().setSideLength((Integer)object);
			break;
		}
	}
	
	public boolean reStartCameria() {return cameria.reStartCameria();}
	public boolean stopCameria() {return cameria.stopCameria();}
	public boolean startCapture() {return cameria.startCapture();}
	public boolean pauseCapture() {return cameria.pauseCapture();}
	public synchronized void setXY(Dimension d) {
		cameria.setXY(d);
		differentImage.setUpImageSize(cameria.getBufferedImage());
	}

	public BufferedImage getProcessImage(){
		return doProcess(cameria.getBufferedImage());
	}
	
	@Override
	public BufferedImage doProcess(BufferedImage imageOrg) {
		faceDectect.findFaces(imageOrg);
		if(isDrawMove){
			imageOrg=differentImage.doProcess(imageOrg);
		}else{
			differentImage.doProcess(imageOrg);
		}
		
		if(isDealInfo){
			dealWithInfo(imageOrg);
		}
		
		if(isDrawFace&&imageOrg!=null){
			BufferedImage image = new BufferedImage(640,
					 480,BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(imageOrg, 0, 0, 640, 480, null);
			imageOrg=faceDectect.paintTheImage2(image);
//			imageOrg=faceDectect.paintTheImage(imageOrg);
		}
		
		
		return imageOrg;
	}

	public void dealWithInfo(BufferedImage imageOrg){
		if(differentImage.movementDegree()>5){
			fileSave save = new fileSave(imageOrg);
			if(faceDectect.isHead()){
				//TODO with new thread or not
				mobileConnecter.sendToMobiles(imageOrg);
			}
		}
	}


	public boolean isDrawFace() {return isDrawFace;}
	public void setDrawFace(boolean is) {this.isDrawFace=is;}
	public boolean isDrawMove() {return isDrawMove;}
	public void setDrawMove(boolean is) {this.isDrawMove=is;}
	public boolean isDealInfo() {return isDealInfo;}
	public void setDealInfo(boolean is) {this.isDealInfo=is;}

	
}
