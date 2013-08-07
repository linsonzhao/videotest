import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageTypeConverter {
	public static byte[] getByte(BufferedImage image,String type){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		try {
			ImageIO.write(image, type, baos);
		} catch (IOException e) {e.printStackTrace();}  
		byte[] data = baos.toByteArray();  
		return data;
	}
}
