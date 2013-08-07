import java.awt.Dimension;
import java.awt.image.BufferedImage;


public interface ImageManager {
	public static final int COLOR_DIFFERENCE=0;
	public static final int DIFFERENCE_TIMES=1;
	public static final int MOVE_COLOR=2;
	public static final int SIDE_LENGTH=3;
	public BufferedImage doProcess(BufferedImage imageOrg);
	public BufferedImage getProcessImage();
	public void changeConfig(int set,Object object);
	

	public boolean reStartCameria();
	public boolean stopCameria();
	public boolean startCapture();
	public boolean pauseCapture();
	public void setXY(Dimension d);
	
	public void dealWithInfo(BufferedImage imageOrg);
	
	public boolean isDrawFace();
	public void setDrawFace(boolean is);
	public boolean isDrawMove();
	public void setDrawMove(boolean is);
	public boolean isDealInfo();
	public void setDealInfo(boolean is);
}
