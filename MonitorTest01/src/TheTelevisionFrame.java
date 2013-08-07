import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public class TheTelevisionFrame extends JFrame{
	private Image background;
	private ImageManager im;
	private JLayeredPane contentPane;
	private CameriaPanel cameriaPanel;
	private SetPanel setPanel;
	private backPanel backPanel;
	private boolean isDraw=false;
	private BufferedImage current= new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
	public static Object drawObject= new Object();
	public static boolean drawBoolean = true;
	private ElectronicPanel elePanel;
	public TheTelevisionFrame(ImageManager im,ElectronicPanel elePanel){
		this.im=im;
		this.elePanel=elePanel;
		try {
			background = ImageIO.read((this.getClass().getResource("frame.png")));
		} catch (IOException e) {
			background = new BufferedImage(800,
					 600,BufferedImage.TYPE_INT_ARGB);
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setSize(800, 600);
		this.setBackground(new Color(0,0,0,0));
		contentPane = new JLayeredPane();
		this.setContentPane(contentPane);
		this.setLayout(null);
		
		contentPane.add(elePanel,JLayeredPane.MODAL_LAYER);
		elePanel.setVisible(false);
		
		
		backPanel = new backPanel(background);
//		contentPane.add(new backPanel(background),JLayeredPane.PALETTE_LAYER);
		cameriaPanel = new CameriaPanel(this.im,background);
		contentPane.add(cameriaPanel,JLayeredPane.MODAL_LAYER);
		
		
		setPanel = new SetPanel(background,im);
		
		
		
		//
		JButton button = new JButton("123"){
			public void paint(Graphics g){}
		};
		button.setBounds(635,155,50,50);
		button.addActionListener(new ActionListener(){
			boolean tmp=false;
			public void actionPerformed(ActionEvent e){
				setVisiable(tmp);
				tmp=!tmp;
			}
		});
		contentPane.add(button,JLayeredPane.DRAG_LAYER);
		
		
		JButton button3 = new JButton("333"){
			public void paint(Graphics g){}
		};
		button3.setBounds(635,235,50,50);
		button3.addActionListener(new ActionListener(){
			boolean tmp=true;
			public void actionPerformed(ActionEvent e){
//				System.out.println("12212223");
				if(tmp){
					setVisiable2(2);
				}else{
					setVisiable2(0);
				}
//				setVisiable(tmp);
				tmp=!tmp;
			}
		});
		contentPane.add(button3,JLayeredPane.DRAG_LAYER);
		
		
		JButton button2 = new JButton("close"){
			public void paint(Graphics g){}
		};
		button2.setBounds(335,0,150,60);
		button2.addActionListener(new ActionListener(){
			boolean tmp=false;
			public void actionPerformed(ActionEvent e){
				setVisiable2(tmp);
				tmp=!tmp;
			}
		});
		contentPane.add(button2,JLayeredPane.DRAG_LAYER);
		
		
		
		JButton button4 = new JButton("close!!!"){
			public void paint(Graphics g){}
		};
		button4.setBounds(360,540,50,50);
		button4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(1);
			}
		});
		contentPane.add(button4,JLayeredPane.DRAG_LAYER);
		
		//
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
	
	
	public void setVisiable(boolean bool){
		if(bool){
				setPanel.closeSet();
//			setPanel.setVisible(false);
//			contentPane.remove(setPanel);
		}else{
			contentPane.add(setPanel,JLayeredPane.POPUP_LAYER);
			setPanel.setVisible(true);
			setPanel.openSet();
		}
	}
	
	public void setVisiable2(boolean bool){
		if(bool){
			cameriaPanel.setVisible(true);
			backPanel.setVisible(false);
			elePanel.setVisible(false);
			contentPane.remove(backPanel);
		}else{
			contentPane.add(backPanel,JLayeredPane.PALETTE_LAYER);
			backPanel.setVisible(true);
			cameriaPanel.setVisible(false);
			elePanel.setVisible(false);
		}
	}
	
	public void setVisiable2(int i){
		if(i==0){
			//cameria
			cameriaPanel.setVisible(true);
			backPanel.setVisible(false);
			elePanel.setVisible(false);
//			contentPane.remove(elePanel);
			contentPane.remove(backPanel);
		}else if(i==1){
			//back
//			contentPane.remove(elePanel);
			contentPane.add(backPanel,JLayeredPane.PALETTE_LAYER);
			backPanel.setVisible(true);
			cameriaPanel.setVisible(false);
			elePanel.setVisible(false);
			
		}else if(i==2){
			//ele
//			contentPane.add(elePanel,JLayeredPane.MODAL_LAYER);
			cameriaPanel.setVisible(false);
			backPanel.setVisible(false);
			elePanel.setVisible(true);
		}
	}
	
	
	class backPanel extends JPanel{
		private Image image;
		public backPanel(Image image){
			this.image=image;
			this.setSize(800,600);
			this.setOpaque(false);
		}
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(image, 0, 0, 800, 600, null);
		}
	}
	
	class CameriaPanel extends JPanel{
		private ImageManager im;
		private Image image;
		public CameriaPanel(ImageManager im,Image image){
			this.im=im;
			this.image=image;
			this.setSize(800,600);
			this.setOpaque(false);
			Thread thread = new Thread(new Runnable(){
				public void run(){
					while(true){
						synchronized (drawObject){
							if(!drawBoolean){
								try {drawObject.wait();} catch (InterruptedException e) {e.printStackTrace();}
							}
							try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
							if(CameriaPanel.this.isVisible()){
								Graphics g = CameriaPanel.this.getGraphics();
								if(g!=null){
	//								System.out.println("draw");
	//								BufferedImage tmp = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
	//								Graphics tmpg = tmp.getGraphics();
	//								tmpg.drawImage(cameriaPanel.this.im.getProcessImage(), 80, 111, 550,413,null);
	//								tmpg.drawImage(cameriaPanel.this.image,0,0,800,600,null);
	//								tmpg.dispose();
	//								g.drawImage(tmp,0,0,800,600,null);
									isDraw=true;
									CameriaPanel.this.paintImmediately(80, 111, 550,413);
								}
							}
						}
					}
				}
			});
			thread.start();
		}
		@Override
		public void paintComponent(Graphics g){
//			System.out.println("123");
			if(isDraw){
				BufferedImage tmp = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
				Graphics tmpg = tmp.getGraphics();
				tmpg.drawImage(current=this.im.getProcessImage(), 80, 111, 550,413,null);
				tmpg.drawImage(this.image,0,0,800,600,null);
				tmpg.dispose();
				g.drawImage(tmp,0,0,800,600,null);
				isDraw=false;
			}else{
				BufferedImage tmp = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
				Graphics tmpg = tmp.getGraphics();
				tmpg.drawImage(current, 80, 111, 550,413,null);
				tmpg.drawImage(this.image,0,0,800,600,null);
				tmpg.dispose();
				g.drawImage(tmp,0,0,800,600,null);
			}
		}
	}
	
}
