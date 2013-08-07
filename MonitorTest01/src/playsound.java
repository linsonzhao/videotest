import javax.sound.sampled.*;
import java.io.*;

public class playsound {
	private AudioFormat format;
	private byte[] samples;
//	private static playsound plays;
	private Thread nowThread=new Thread(new AC());
	private boolean isPlay=false;
	private boolean isAlive=false;
	
	//
	private AudioFormat format2;
	private byte[] samples2;
	
	public void playWin(){
		Thread thread = new Thread(new Runnable(){
			public void run(){
				InputStream stream = new ByteArrayInputStream(samples2);
				play(stream);
//				play(stream);
			}
		});
		thread.start();
	}
	
	public synchronized void playTheMusic(){
		if(!isAlive){
			isAlive=true;
			nowThread = new Thread(new AC());
			nowThread.start();
		}
	}
	public synchronized void stopTheMusic(){
		isAlive=false;
		nowThread.stop();
	}
	
	class AC implements Runnable{
		public void run(){
			playsound play = new playsound();
			while(true){
			play.play();
			}
		}
	}

	public playsound() {

		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(
					"argh.wav"));
			format = stream.getFormat();
			samples = getSamples(stream);
			
			stream = AudioSystem.getAudioInputStream(new File(
					"0422.wav"));
			format2 = stream.getFormat();
			samples2 = getSamples(stream);
		} catch (UnsupportedAudioFileException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		
	}

	public void play() {
		InputStream stream = new ByteArrayInputStream(getSamples());
		play(stream);
	}

	public byte[] getSamples() {
		return samples;
	}

	private byte[] getSamples(AudioInputStream audioStream) {
		int length = (int) (audioStream.getFrameLength() * format
				.getFrameSize());
		byte[] samples = new byte[length];
		DataInputStream is = new DataInputStream(audioStream);
		try {
			is.readFully(samples);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return samples;
	}

	public void play(InputStream source) {
		int bufferSize = format.getFrameSize()
				* Math.round(format.getSampleRate() / 10);
		byte[] buffer = new byte[bufferSize];
		SourceDataLine line;
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufferSize);
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
			return;
		}
		line.start();
		try {
			int numBytesRead = 0;
			while (numBytesRead != -1) {
				numBytesRead = source.read(buffer, 0, buffer.length);
				if (numBytesRead != -1) {
					line.write(buffer, 0, numBytesRead);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		line.drain();
		line.close();
	}

}