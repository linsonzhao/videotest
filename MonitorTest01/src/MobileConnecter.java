import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class MobileConnecter {
	private ServerSocket server;
	private ServerSocket server2;
	ArrayList<SocketHolder> sockets;
	public MobileConnecter(){
		sockets=new ArrayList<SocketHolder>();
		Thread thread = new Thread(new waitSocket());
		thread.start();
	}
	
	
	public void sendToMobiles(BufferedImage image){
		Thread thread = new Thread(new  sentImage(image));
		thread.start();
	}
	
	
	private class waitSocket implements Runnable{
		public void run() {
			while(true){
				try {
					server = new ServerSocket(7777);
					server2 = new ServerSocket(7778);
					while(true){
						Socket socket = server.accept();
						Socket socket2 = server2.accept();
						sockets.add(new SocketHolder(socket,socket2));
						System.out.println("connect");
					}
				} catch (IOException e) {
					sockets.clear();
					e.printStackTrace();}
			}
		}
	}
	
	private class sentImage implements Runnable{
		BufferedImage image;
		public sentImage(BufferedImage image){
			this.image=image;
		}
		public void run(){
			for(SocketHolder socket:sockets){
				ObjectOutputStream oos;
				ObjectInputStream ois;
				try {
					oos = socket.getInfo().getOOS();
//					ImageIO.write(image,"png",socket.getOutputStream());
					oos.writeInt(1);
					oos.flush();
					
//					ois = new ObjectInputStream(socket.getInputStream());
//					ois.read
					oos= socket.getImage().getOOS();
					byte [] data =ImageTypeConverter.getByte(image, "png");
					oos.write(data);
					oos.flush();
					
				} catch (IOException e) {
					sockets.remove(socket);
//					e.printStackTrace();
					break;
				}
			}
//			System.out.println("sent");
		}
	}
}
