import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;

public class noiceHandle extends JFrame
{
	private	final int imageRecover_RADIUS=5;
	private	final int SCAN_TIMES=2;
	private	int scanNoiceReduce=0;
	private	int scanImageRecover=0;
	private	Color on = Color.white;
	private	Color off = Color.black;

	public BufferedImage noiceReduce(BufferedImage exeImage)
	{
		BufferedImage exeImageOpt = new	BufferedImage(exeImage.getWidth(this), exeImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);

		int loop1,loop2;
//		loop1=loop2=scanNoiceReduce+1;

		if(scanNoiceReduce==SCAN_TIMES)scanNoiceReduce=0;
		else scanNoiceReduce++;


		for(loop1 = 1; loop1 < exeImage.getWidth(this)-1;)
		{
			for(loop2 = 1; loop2 < exeImage.getHeight(this)-1;)
			{
				try
				{
					if(exeImage.getRGB(loop1,loop2)==on.getRGB()&&checkSurround(exeImage,loop1,loop2,off.getRGB())>4)
					{
						exeImageOpt.setRGB(loop1,loop2,off.getRGB());
					}
					else exeImageOpt.setRGB(loop1,loop2,exeImage.getRGB(loop1,loop2));
				}catch(ArrayIndexOutOfBoundsException e){}
				loop2=loop2+scanNoiceReduce+1;
				loop2=loop2+1;
			}
			loop1=loop1+scanNoiceReduce+1;
			loop1=loop1+1;
		}
		return exeImageOpt;
	}

	public BufferedImage imageRecover(BufferedImage	exeImage)
	{
		BufferedImage exeImageOpt = new	BufferedImage(exeImage.getWidth(this), exeImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);

		for(int	loop1=1; loop1 < exeImage.getWidth(this)-1; loop1++)
		{
			for(int	loop2=1; loop2 < exeImage.getHeight(this)-1; loop2++)
			{
				try
				{
					exeImageOpt.setRGB(loop1,loop2,off.getRGB());
				}catch(ArrayIndexOutOfBoundsException e){}
			}
		}

		for(int	loop1=1; loop1 < exeImage.getWidth(this)-1; loop1++)
		{
			for(int	loop2=1; loop2 < exeImage.getHeight(this)-1; loop2++)
			{
				if(exeImage.getRGB(loop1,loop2)==on.getRGB())
				{
					for(int	x=-imageRecover_RADIUS;x<=imageRecover_RADIUS;x++)
					{
						for(int	y=-imageRecover_RADIUS;y<=imageRecover_RADIUS;y++)
						{
							try
							{
								//畫圓 
								if(Math.sqrt((double)x*(double)x+(double)y*(double)y)<=imageRecover_RADIUS)
									exeImageOpt.setRGB(loop1+x,loop2+y,on.getRGB());
							}catch(ArrayIndexOutOfBoundsException e){}

						}
					}
				}
			}
		}

		return exeImageOpt;

	}


	public int checkSurround(BufferedImage imageTemp,int x,int y,int RGB)
	{
		int count=0;
		if(imageTemp.getRGB(x+1,y)==RGB)count++;
		if(imageTemp.getRGB(x-1,y)==RGB)count++;
		if(imageTemp.getRGB(x,y+1)==RGB)count++;
		if(imageTemp.getRGB(x,y-1)==RGB)count++;
		if(imageTemp.getRGB(x+1,y-1)==RGB)count++;
		if(imageTemp.getRGB(x-1,y+1)==RGB)count++;
		if(imageTemp.getRGB(x+1,y+1)==RGB)count++;
		if(imageTemp.getRGB(x-1,y-1)==RGB)count++;
		return(count);
	}
}