import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;


public class fileSave
{
	private	final String JPGfileName="0000";

	public fileSave(BufferedImage exeImage)
	{
		Thread thread = new Thread(new save(exeImage));
		thread.start();
	}
	
	private class save implements Runnable{
		BufferedImage exeImage;
		public save(BufferedImage exeImage){
			this.exeImage=exeImage;
		}
		public void run(){
			int count=0;
			String s;
			boolean	checkFileName=true;
			do
			{
				s=JPGfileName +	String.valueOf(count);
				s=s.substring(s.length() - JPGfileName.length());
				s = "capture"+s+".jpg";
				try
				{
					FileInputStream	fileinputstream	= new FileInputStream(s);
					fileinputstream.close();
					checkFileName =	true;
				}catch(FileNotFoundException filenotfoundexception)
				{
					checkFileName=false;
				}catch(IOException ioexception){
					System.err.println("Exception:\n" + ioexception);
				}
				count++;
			}while(checkFileName);

			try
			{
				File file = new	File(s);
				ImageIO.write(exeImage,	"jpg", file);
			}catch(IOException ioexception){}
		}
	}
}