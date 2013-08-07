import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketHolder {
	private SocketIO socketImage;
	private SocketIO socketInfo;
	
	public SocketHolder(Socket socketInfo,Socket socketImage) throws IOException{
		this.socketInfo=new SocketIO(socketInfo);
		this.socketImage=new SocketIO(socketImage);
	}
	
	public SocketIO getInfo(){
		return socketInfo;
	}
	public SocketIO getImage(){
		return socketImage;
	}
	
	
	class SocketIO{
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private Socket socket;
		public SocketIO(Socket socket) throws IOException{
			this.socket=socket;
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		}
		
		public ObjectInputStream getOIS(){
			return ois;
		}
		public ObjectOutputStream getOOS(){
			return oos;
		}
		
	}
}
