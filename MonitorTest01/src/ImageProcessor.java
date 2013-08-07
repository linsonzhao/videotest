import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;


public interface ImageProcessor {
	public static final int COLOR_DIFFERENCE=0;
	public static final int DIFFERENCE_TIMES=1;
	public static final int MOVE_COLOR=2;
	public static final int SIDE_LENGTH=3;
	public void setUpImageSize(BufferedImage image);
	public BufferedImage doProcess(BufferedImage imageOrg);
	public void changeConfig(int set,Object object);
	
	public BufferedImage getProcessedImage();
	public int movementDegree();
	public void setXY(Dimension d);
	
	
}
