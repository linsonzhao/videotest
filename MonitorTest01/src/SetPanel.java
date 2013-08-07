import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;


public class SetPanel extends JPanel{
	private Image image;
	private JPanel panel;
	private ImageManager im;
	public SetPanel(Image image,ImageManager im){
		this.im=im;
		this.image=image;
		this.setLayout(null);
		this.setSize(800,600);
		this.setOpaque(false);
		
		panel = new SetUpPanel(im);
		panel.setBounds(630,530,0,0);
		
	}
	
	
	
	public void openSet(){
		//80,110,550, 420
		this.add(panel);
		Thread thread01= new Thread(new Runnable(){
	    	public void run(){
	    		TheTelevisionFrame.drawBoolean=false;
	    		synchronized(TheTelevisionFrame.drawObject){
		    		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		    		for(int i=0;i<=55;i++){
		    			panel.setBounds(80+10*(55-i),110,10*i,420);
		    		    try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
		    		}
		    		panel.setBounds(80,110,550,420);
		    		TheTelevisionFrame.drawBoolean=true;
		    		TheTelevisionFrame.drawObject.notifyAll();
	    		}
	    	}
	    });
	    thread01.start();
	}
	
	public void closeSet(){
		//80,110,550, 420
		this.add(panel);
		Thread thread01= new Thread(new Runnable(){
	    	public void run(){
	    		TheTelevisionFrame.drawBoolean=false;
	    		synchronized(TheTelevisionFrame.drawObject){
		    		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		    		for(int i=55;i>=0;i--){
		    			panel.setBounds(80+10*(55-i),110,10*i,420);
		    		    try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
		    		}
		    		panel.setBounds(630,530,0,0);
	    		TheTelevisionFrame.drawBoolean=true;
	    		TheTelevisionFrame.drawObject.notifyAll();
    		}
	    		
	    		SetPanel.this.setVisible(false);
	    		SetPanel.this.getParent().remove(SetPanel.this);
	    	}
	    });
	    thread01.start();
	}
	
	
//	@Override
//	public void paintComponent(Graphics g){
//		super.paintComponent(g);
//		g.drawImage(image, 0, 0, 800, 600,null);
//	}
	
//	public void paintComponents(Graphics g){
////		super.paintComponents(g);
//		g.drawImage(image, 0, 0, 800, 600,null);
//	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(image, 0, 0, 800, 600,null);
	}
	
}
