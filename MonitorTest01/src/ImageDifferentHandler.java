import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageDifferentHandler implements ImageProcessor{
	private	int color_difference=25;
	private	int difference_times=0;
	private	BufferedImage imageOrgOld;
	private	BufferedImage imageOrg;
	


	private	BufferedImage differenceImageOld;
	private	BufferedImage differenceImage;
	private	BufferedImage diffOrgImage;
	private	int threshold =	200;
	private	Color on = Color.white;
	private	Color off = Color.black;
	private	Color move = Color.green;

	private	int cal[][];
	private	int moveCenter[]={0,0};

	private	noiceHandle noiceHandler=new noiceHandle();
	private	movementIdentify movementIdentifer=new movementIdentify();
	
	private Cameria cameria;
	
	
	public ImageDifferentHandler(BufferedImage image){
		setUpImageSize(image);
	}
	
	public ImageDifferentHandler(Cameria cameria){
		this.cameria=cameria;
		cameria.startCapture();
		setUpImageSize(cameria.getBufferedImage());
	}
	
	
	public synchronized BufferedImage getProcessedImage(){
		return doProcess(cameria.getBufferedImage());
	}
	

	
	public void setUpImageSize(BufferedImage image) {
		//TODO change default size and stop the handler ,restart
		
		this.imageOrg= image;
		cal=new	int[imageOrg.getWidth()][imageOrg.getHeight()];

		for(int	loop1 =	0; loop1 < imageOrg.getWidth(); loop1++)
			for(int	loop2 =	0; loop2 < imageOrg.getHeight(); loop2++)cal[loop1][loop2]=0;
	}
	
	public BufferedImage doProcess(BufferedImage imageOrg)
	{
//		testFrame.changeImage(0, imageOrg);
		if(imageOrgOld!=null)
		{
			this.imageOrg=imageOrg;
			differenceImage	= new BufferedImage(imageOrg.getWidth(), imageOrg.getHeight(), BufferedImage.TYPE_INT_ARGB);
			diffOrgImage = new BufferedImage(imageOrg.getWidth(), imageOrg.getHeight(), BufferedImage.TYPE_INT_ARGB);


			for(int	loop1 =	0; loop1 < imageOrg.getWidth(); loop1++)
			{
				for(int	loop2 =	0; loop2 < imageOrg.getHeight(); loop2++)
				{
					Color pixelOld = new Color(imageOrgOld.getRGB(loop1,loop2));
					Color pixel = new Color(imageOrg.getRGB(loop1,loop2));

					int numberRGB[]=new int[3];
					numberRGB[0]=Math.abs(pixelOld.getRed()-pixel.getRed());
					numberRGB[1]=Math.abs(pixelOld.getGreen()-pixel.getGreen());
					numberRGB[2]=Math.abs(pixelOld.getBlue()-pixel.getBlue());

					if(numberRGB[0]<color_difference||numberRGB[1]<color_difference||numberRGB[2]<color_difference)
					{
						//檢查 如果小於直 設為安全的off (要檢查兩次)
						if(cal[loop1][loop2]==0)
							differenceImage.setRGB(loop1,loop2,off.getRGB());
						else cal[loop1][loop2]=0;
					}
					else  //差大於difference
					{
						//如果差異大於difference_time舊社 不安全的on
						if(cal[loop1][loop2]==difference_times)differenceImage.setRGB(loop1,loop2,on.getRGB());
						else cal[loop1][loop2]++;
					}
				}
			}

			
			differenceImage=noiceHandler.noiceReduce(differenceImage);
			differenceImage=noiceHandler.imageRecover(differenceImage);
			differenceImage=movementIdentifer.identify(differenceImage);

			for(int	loop1 =	0; loop1 < imageOrg.getWidth(); loop1++)
			{
				for(int	loop2 =	0; loop2 < imageOrg.getHeight(); loop2++)
				{
					//把differenceImage複製成 diffOrgImage 並把on顏色改成move顏色
					if(differenceImage.getRGB(loop1,loop2)==on.getRGB())diffOrgImage.setRGB(loop1,loop2,move.getRGB());
					else diffOrgImage.setRGB(loop1,loop2,imageOrg.getRGB(loop1,loop2));
				}
			}

//			drawCenter();
		}
		imageOrgOld=imageOrg;
		differenceImageOld=differenceImage;
		return diffOrgImage;
	}

	public void drawCenter(){
//		畫中心點
		moveCenter=movementIdentifer.movementCenter(moveCenter[0],moveCenter[1]);
		for(int	x=-4;x<=4;x++)
		{
			for(int	y=-4;y<=4;y++)
			{
				try
				{
					if(Math.abs(Math.sqrt((double)x*(double)x+(double)y*(double)y))<=2)diffOrgImage.setRGB(moveCenter[0]+x,moveCenter[1]+y,on.getRGB());
					if(Math.abs(Math.sqrt((double)x*(double)x+(double)y*(double)y))>=4)diffOrgImage.setRGB(moveCenter[0]+x,moveCenter[1]+y,on.getRGB());

				}catch(ArrayIndexOutOfBoundsException e){}
			}
		}
	}



	public void changeConfig(int set, Object object) {
		//TODO stop now process
		switch(set){
		case ImageProcessor.COLOR_DIFFERENCE:
			this.color_difference=(Integer)object;
			break;
		case ImageProcessor.DIFFERENCE_TIMES:
			this.difference_times=(Integer)object;
			break;
		case ImageProcessor.MOVE_COLOR:
			this.move=(Color)object;
			break;
		case ImageProcessor.SIDE_LENGTH:
			this.movementIdentifer.setSideLength((Integer)object);
			break;
		}
	}

	
//TODO
	public synchronized void setXY(Dimension d) {
		imageOrgOld=null;
		cameria.setXY(d);
		setUpImageSize(cameria.getBufferedImage());
	}

	
	public int movementDegree() {
//		if(cameria.getStates()==Cameria.CAMERIA_START_CAPTURE_START){
			return movementIdentifer.movementDegree();
//		}
//		return -1;
	}
	
	
	
	//
	
	public Color getMove() {
		return move;
	}

	public void setMove(Color move) {
		this.move = move;
	}

	public movementIdentify getMovementIdentifer() {
		return movementIdentifer;
	}

	public void setMovementIdentifer(movementIdentify movementIdentifer) {
		this.movementIdentifer = movementIdentifer;
	}

	public int getColor_difference() {
		return color_difference;
	}

	public void setColor_difference(int color_difference) {
		this.color_difference = color_difference;
	}

	public int getDifference_times() {
		return difference_times;
	}

	public void setDifference_times(int difference_times) {
		this.difference_times = difference_times;
	}
	
	
	
}
