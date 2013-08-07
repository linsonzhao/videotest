import java.io.Serializable;


public class ImageByteContainer implements Serializable{
	byte [] data;
	public ImageByteContainer(byte[]data){
		this.data=data;
	}
	public byte[] getByte(){
		return data;
	}
	public void setByte(byte[] data){
		this.data=data;
	}
}
