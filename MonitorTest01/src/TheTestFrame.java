import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TheTestFrame {
	private static int sleep=50;
	static boolean sent =false;
	public static void main(String args[]) throws IOException{
		
		
		
		
		final Cameria cameria = new CameriaHandler();
//		final ImageDifferentHandler ip= new ImageDifferentHandler(cameria);
		MobileConnecter mc = new MobileConnecter();
		final ImageManager ip = new ImageHandler(cameria, mc );
		
		System.out.println("223");
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		
		final JPanel panel = new JPanel(){};
		
		frame.setContentPane(panel);
		frame.setLayout(null);
		
		JButton button01 = new JButton("stop");
		button01.setBounds(200,500,100,50);
		button01.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ip.stopCameria();
			}
		});
		frame.getContentPane().add(button01);
		
		JButton button02 = new JButton("restart");
		button02.setBounds(300,500,100,50);
		button02.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ip.reStartCameria();
			}
		});
		frame.getContentPane().add(button02);
		
		
		JButton button03 = new JButton("pause");
		button03.setBounds(400,500,100,50);
		button03.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ip.pauseCapture();
			}
		});
		frame.getContentPane().add(button03);
		
		
		JButton button04 = new JButton("XY");
		button04.setBounds(500,500,100,50);
		button04.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ip.setXY(new Dimension(640,480));
			}
		});
		frame.getContentPane().add(button04);
		
		
		JButton button05 = new JButton("Color");
		button05.setBounds(600,500,100,50);
		button05.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ip.changeConfig(ImageProcessor.MOVE_COLOR, Color.red);
			}
		});
		frame.getContentPane().add(button05);
		
		
		JButton button06= new JButton("side length");
		button06.setBounds(600,550,100,50);
		button06.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ip.changeConfig(ImageProcessor.SIDE_LENGTH, 40);
			}
		});
		frame.getContentPane().add(button06);
		
		JButton button07= new JButton("sleep");
		button07.setBounds(500,550,100,50);
		button07.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				sleep=500;
			}
		});
		frame.getContentPane().add(button07);
		
		
		frame.setVisible(true);
		
	
		
		
		//
//		try {
//			final ServerSocket server = new ServerSocket(12121);
//			final Socket socket= server.accept();
//			System.out.println("connect");
//			final ObjectOutputStream oos=new ObjectOutputStream (socket.getOutputStream());
		
		
		
//		final FaceDectectionHandler faceD= new FaceDectectionHandler();
//		faceD.setPaintImage(ImageIO.read((faceD.getClass().getResource("face.png"))));
//		faceD.setDraw(true);
		
		//
		
		final Graphics g = panel.getGraphics();
		Thread thread01 = new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						Thread.sleep(sleep);} catch (InterruptedException e) {}
					BufferedImage image=ip.getProcessImage();
//					BufferedImage image2 =ip.getProcessedImage();
//					if(image2==null)System.out.println("xxxxxxxxxx");
//					if(image2==null){
//						image=image2;
//					}else{
//						image=faceD.findFaces(image2);
//					}
					g.drawImage(image,0,0,550,413,null);
//					if(ip.movementDegree()>3){
//						try {
////							oos.writeObject(new ImageByteContainer(ImageTypeConverter.getByte(image, "png")));
//							byte [] data =ImageTypeConverter.getByte(image, "png");
////							String info =Base64.encode(data);
////							oos.writeObject(info);
//							oos.write(data);
//							oos.flush();
//							System.out.println("write");
////							int len =data.length;
////							System.out.println(len);
////							oos.writeInt(len);
////							oos.flush();
////							oos.write(data);
////							oos.flush();
////							 ImageIO.write(image,"png",socket.getOutputStream());
////							sent=true;
//						} catch (IOException e) {e.printStackTrace();}
					}
				}
//			}
		});
		thread01.start();
//		} catch (IOException e1) {e1.printStackTrace();}
	}
}
