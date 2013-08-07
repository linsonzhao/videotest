import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;


public class StartMain {
	public static void main(String args[]){
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		}
//		Cameria cameria = new CameriaHandler();
		Cameria cameria = new CameriaCVHandler();
		MobileConnecter mobile = new MobileConnecter();
		ImageManager im = new ImageHandler(cameria,mobile);
		ElectronicGame game = new ElectronicGame(cameria);
		ElectronicPanel elePanel = new ElectronicPanel(game);
		new TheTelevisionFrame(im,elePanel);
	}
}
