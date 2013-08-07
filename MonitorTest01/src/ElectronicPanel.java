import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;




public class ElectronicPanel extends JPanel{
	private static final int LEFT_DOWN = 1;
	private static final int LEFT_UP = 4;
	private ElectronicGame game;
	private Image image;
	private boolean isDraw=false;
	private int drawWin = -1;
	private BufferedImage current= new BufferedImage(550,413,BufferedImage.TYPE_INT_ARGB);
	private Image winImage;
	public ElectronicPanel(ElectronicGame game){
		game.setParent(this);
		this.game=game;
		try {
			this.image= ImageIO.read((this.getClass().getResource("frame.png")));
			this.winImage= ImageIO.read((this.getClass().getResource("win.png")));
			} catch (IOException e1) {};
		this.setSize(800,600);
		this.setOpaque(false);
		this.addMouseListener(new MouseLis());
		this.addMouseMotionListener(new MouseDrag());
		Thread thread = new Thread(new Runnable(){
			public void run(){
				while(true){
//					synchronized (TheTelevisionFrame.drawObject){
//						if(!TheTelevisionFrame.drawBoolean){
//							try {TheTelevisionFrame.drawObject.wait();} catch (InterruptedException e) {e.printStackTrace();}
//						}
						try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
						if(ElectronicPanel.this.isVisible()){
//							Graphics g = ElectronicPanel.this.getGraphics();
//							if(g!=null){
//								System.out.println("draw");
//								BufferedImage tmp = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
//								Graphics tmpg = tmp.getGraphics();
//								tmpg.drawImage(cameriaPanel.this.im.getProcessImage(), 80, 111, 550,413,null);
//								tmpg.drawImage(cameriaPanel.this.image,0,0,800,600,null);
//								tmpg.dispose();
//								g.drawImage(tmp,0,0,800,600,null);
								isDraw=true;
//								System.out.println("kkkk");
								ElectronicPanel.this.paintImmediately(80, 111, 550,413);
//							}
						}
					}
				}
//			}
		});
		thread.start();
	}
	@Override
	public void paintComponent(Graphics g){
//		if(isDraw){
			BufferedImage tmp = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
			Graphics tmpg = tmp.getGraphics();
			Graphics2D tmpg2 = (Graphics2D) tmp.getGraphics();
			tmpg.drawImage(current=this.game.getProcessedImage(), 80, 111, 550,413,null);
			tmpg.drawImage(this.image,0,0,800,600,null);
			drawWin(tmpg2);
			tmpg.dispose();
			tmpg2.dispose();
			g.drawImage(tmp,0,0,800,600,null);
			isDraw=false;
//		}else{
//			BufferedImage tmp = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
//			Graphics tmpg = tmp.getGraphics();
//			tmpg.drawImage(current, 80, 111, 550,413,null);
//			tmpg.drawImage(this.image,0,0,800,600,null);
//			tmpg.dispose();
//			g.drawImage(tmp,0,0,800,600,null);
//		}
	}
	
	public void toDrawWin(){
		drawWin=0;
	}
	
	public void drawWin(Graphics2D g){
//		System.out.println("drawwin");
		if(drawWin>=0){
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
	                 (float)(drawWin/30.0)));

			g.drawImage(winImage, 210, 270, 280, 50, null);
//			System.out.println("real draw");
			drawWin++;
			if(drawWin==30){
				drawWin =-1;
			}
		}
	}
	class MouseLis implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void mousePressed(MouseEvent arg0) {
			game.call(LEFT_DOWN, arg0.getX()-80, arg0.getY()-113, 0, null);
//			System.out.println(",,,");
			
		}
		public void mouseReleased(MouseEvent arg0) {
			game.call(LEFT_UP, arg0.getX()-80, arg0.getY()-113, 0, null);
			
		}
		public void mouseDragged(MouseEvent arg0) {
			game.call(LEFT_UP, arg0.getX()-80, arg0.getY()-113, 0, null);
		}
	}
	
	class MouseDrag implements MouseMotionListener{
		public void mouseDragged(MouseEvent arg0) {
			game.call(9, arg0.getX()-80, arg0.getY()-113, 0, null);
		}
		public void mouseMoved(MouseEvent e) {}
	}
}
