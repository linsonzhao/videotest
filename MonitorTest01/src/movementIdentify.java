import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;

public class movementIdentify extends JFrame
{
	private Color on = Color.white;
	private Color off = Color.black;
	private int side_length=10;

	private int cal[][];
	private int countCal=0;
	private int center_X=0;
	private int center_Y=0;
	
	public void setSideLength(int len){
		this.side_length=len;
	}

	public BufferedImage identify(BufferedImage exeImage)
	{

		BufferedImage exeImageOpt = new BufferedImage(exeImage.getWidth(this), exeImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);

		for(int loop1=1; loop1 < exeImage.getWidth(this)-1; loop1++)
		{
			for(int loop2=1; loop2 < exeImage.getHeight(this)-1; loop2++)
			{
				try
				{
					exeImageOpt.setRGB(loop1,loop2,off.getRGB());
				}catch(ArrayIndexOutOfBoundsException e){}
			}
		}

		cal=new int[exeImage.getWidth(this)+1][exeImage.getHeight(this)+1];
		countCal=0;
		center_X=0;
		center_Y=0;

		for(int X_coordinate=0;X_coordinate<exeImage.getWidth(this)/side_length;X_coordinate++)
		{
			for(int Y_coordinate=0;Y_coordinate<exeImage.getHeight(this)/side_length;Y_coordinate++)
			{
				int count=0;
				for(int X_pixel=0;X_pixel<=side_length;X_pixel++)
				{
					for(int Y_pixel=0;Y_pixel<=side_length;Y_pixel++)
					{
						try
						{
							if(exeImage.getRGB(X_coordinate*side_length+X_pixel,Y_coordinate*side_length+Y_pixel)==on.getRGB())count++;
						}catch(ArrayIndexOutOfBoundsException e){}
						if(count>(side_length*side_length)*0.01)break;
					}
					if(count>(side_length*side_length)*0.01)break;
				}

				if(count>(side_length*side_length)*0.01)
				{
					cal[X_coordinate][Y_coordinate]=1;
					center_X=center_X+X_coordinate;
					center_Y=center_Y+Y_coordinate;
					countCal++;
				}
				else cal[X_coordinate][Y_coordinate]=0;
			}
		}

		for(int X_coordinate=0;X_coordinate<exeImage.getWidth(this)/side_length;X_coordinate++)
		{
			for(int Y_coordinate=0;Y_coordinate<exeImage.getHeight(this)/side_length;Y_coordinate++)
			{
				if(cal[X_coordinate][Y_coordinate]==1)
				{
					try
					{
						if(cal[X_coordinate][Y_coordinate+1]==0)exeImageOpt=identify(exeImageOpt,X_coordinate*side_length,Y_coordinate*side_length+side_length,X_coordinate*side_length+side_length,Y_coordinate*side_length+side_length);
					}catch(ArrayIndexOutOfBoundsException e){}
					try
					{
						if(cal[X_coordinate][Y_coordinate-1]==0)exeImageOpt=identify(exeImageOpt,X_coordinate*side_length,Y_coordinate*side_length,X_coordinate*side_length+side_length,Y_coordinate*side_length);
					}catch(ArrayIndexOutOfBoundsException e){}
					try
					{
						if(cal[X_coordinate-1][Y_coordinate]==0)exeImageOpt=identify(exeImageOpt,X_coordinate*side_length,Y_coordinate*side_length,X_coordinate*side_length,Y_coordinate*side_length+side_length);
					}catch(ArrayIndexOutOfBoundsException e){}
					try
					{
						if(cal[X_coordinate+1][Y_coordinate]==0)exeImageOpt=identify(exeImageOpt,X_coordinate*side_length+side_length,Y_coordinate*side_length,X_coordinate*side_length+side_length,Y_coordinate*side_length+side_length);
					}catch(ArrayIndexOutOfBoundsException e){}
				}
			}
		}
		return exeImageOpt;
	}

	public BufferedImage identify(BufferedImage drawImage,int iniX,int iniY,int endX,int endY)
	{
		if(iniX>endX)
		{
			int temp=endX;
			endX=iniX;
			iniX=temp;
		}

		if(iniY>endY)
		{
			int temp=endY;
			endY=iniY;
			iniY=temp;
		}

		for(int X=iniX;X<=endX;X++)
		{
			for(int Y=iniY;Y<=endY;Y++)
			{
				try
				{
					drawImage.setRGB(X,Y,on.getRGB());
				}catch(ArrayIndexOutOfBoundsException e){}
			}
		}
		return drawImage;
	}

	public int movementDegree()
	{
		return(countCal);
	}

	public int[] movementCenter(int moveCoordinate_X,int moveCoordinate_Y)
	{
		int moveCoordinate[]={moveCoordinate_X,moveCoordinate_Y};
		try
		{
			moveCoordinate[0]=(center_X/countCal)*side_length+side_length/2;
			moveCoordinate[1]=(center_Y/countCal)*side_length+side_length/2;
		}
		catch(ArithmeticException e){}
		return(moveCoordinate);
	}

	public int getside_length()
	{
		return(side_length);
	}

	public void setside_length(int side_length)
	{
		this.side_length=side_length;
	}
}
