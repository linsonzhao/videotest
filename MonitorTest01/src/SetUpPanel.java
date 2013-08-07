import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;




public class SetUpPanel extends JPanel{
	private JButton button_sumit;
	private JRadioButton radio_issave_false;
	private JLabel label_isSave;
	private JLabel label_ismove;
	private JRadioButton radio_issave_true;
	private JRadioButton radio_ismove_false;
	private JRadioButton radio_ismove_true;
	private JLabel label_ishead;
	private JRadioButton radio_ishead_false;
	private JRadioButton radio_ishead_true;
	private JLabel label_title;
	private JButton button_reset;
	private ButtonGroup groupIsHead;
	private ButtonGroup groupIsMove;
	private ButtonGroup groupIsSave;
	private Font labelFront =new java.awt.Font("Serif", 1, 16);
	
	private boolean isHead=true;
	private boolean isMove =false;
	private boolean isSave = true;
	
	private ImageManager im;
	private final Color color = new Color(255,255,255,150);
	public SetUpPanel(ImageManager im){
		this.im=im;
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.setOpaque(false);
				this.setLayout(null);
				this.setSize(new java.awt.Dimension(550, 420));
				
				
				{
					button_sumit = new JButton();
					this.add(button_sumit);
					button_sumit.setText("Sumit");
					button_sumit.setBounds(141, 285, 80, 30);
					button_sumit.addActionListener(new dothing());
				}
				{
					button_reset = new JButton();
					this.add(button_reset);
					button_reset.setText("Reset");
					button_reset.setBounds(297, 285, 80, 30);
				}
				{
					label_title = new JLabel();
					this.add(label_title);
					label_title.setText("\u76e3\u8996\u5668\u8a2d\u5b9a");
					label_title.setBounds(160, 23, 217, 41);
					label_title.setBackground(new java.awt.Color(255,255,255));
					label_title.setHorizontalTextPosition(SwingConstants.CENTER);
					label_title.setHorizontalAlignment(SwingConstants.CENTER);
					label_title.setFont(new java.awt.Font("Serif", 1, 30));
				}
				{
					radio_ishead_true = new JRadioButton();
					this.add(radio_ishead_true);
					radio_ishead_true.setText("Yes");
					radio_ishead_true.setBounds(254, 113, 56, 21);
					radio_ishead_true.addActionListener(new ActionListener() {
					     public void actionPerformed(ActionEvent evt) {
					    	 isHead=true;
					     }
					});
					
				}
				{
					radio_ishead_false = new JRadioButton();
					this.add(radio_ishead_false);
					radio_ishead_false.setText("No");
					radio_ishead_false.setBounds(355, 113, 56, 21);
					radio_ishead_false.addActionListener(new ActionListener() {
					     public void actionPerformed(ActionEvent evt) {
					    	 isHead=false;
					     }
					});
				}
				{
					label_ishead = new JLabel();
					this.add(label_ishead);
					label_ishead.setText("\u986f\u793a\u982d\u50cf\u6aa2\u6e2c");
					label_ishead.setBounds(128, 110, 114, 27);
					label_ishead.setFont(labelFront);
				}
				{
					radio_ismove_true = new JRadioButton();
					this.add(radio_ismove_true);
					radio_ismove_true.setText("Yes");
					radio_ismove_true.setBounds(254, 155, 56, 21);
					radio_ismove_true.addActionListener(new ActionListener() {
					     public void actionPerformed(ActionEvent evt) {
					    	 isMove=true;
					     }
					});
				}
				{
					radio_ismove_false = new JRadioButton();
					this.add(radio_ismove_false);
					radio_ismove_false.setText("No");
					radio_ismove_false.setBounds(355, 155, 56, 21);
					radio_ismove_false.addActionListener(new ActionListener() {
					     public void actionPerformed(ActionEvent evt) {
					    	 isMove=false;
					     }
					});
				}
				{
					radio_issave_true = new JRadioButton();
					this.add(radio_issave_true);
					radio_issave_true.setText("Yes");
					radio_issave_true.setBounds(254, 197, 56, 21);
					radio_issave_true.addActionListener(new ActionListener() {
					     public void actionPerformed(ActionEvent evt) {
					    	 isSave=true;
					     }
					});
				}
				{
					radio_issave_false = new JRadioButton();
					this.add(radio_issave_false);
					radio_issave_false.setText("No");
					radio_issave_false.setBounds(355, 197, 56, 21);
					radio_issave_false.addActionListener(new ActionListener() {
					     public void actionPerformed(ActionEvent evt) {
					    	 isSave=false;
					     }
					});
				}
				{
					label_ismove = new JLabel();
					this.add(label_ismove);
					label_ismove.setText("\u986f\u793a\u79fb\u52d5\u5075\u6e2c");
					label_ismove.setBounds(128, 157, 114, 27);
					label_ismove.setFont(labelFront);
				}
				{
					label_isSave = new JLabel();
					this.add(label_isSave);
					label_isSave.setText("\u5132\u5b58\u7570\u52d5\u5716\u7247");
					label_isSave.setBounds(128, 199, 114, 27);
					label_isSave.setFont(labelFront);
				}
				
				
				{
					ButtonGroup groupIsHead = new ButtonGroup();
					groupIsHead.add(radio_ishead_true);
					groupIsHead.add(radio_ishead_false);
				}
				{
					ButtonGroup groupIsMove = new ButtonGroup();
					groupIsMove.add(radio_ismove_true);
					groupIsMove.add(radio_ismove_false);
				}
				{
					ButtonGroup groupIsSave = new ButtonGroup();
					groupIsSave.add(radio_issave_true);
					groupIsSave.add(radio_issave_false);
				}
				
				
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		g.setColor(color);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		super.paintComponent(g);
	}
	
	
	private class dothing implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			im.setDrawFace(isHead);
			im.setDrawMove(isMove);
			im.setDealInfo(isSave);
			JOptionPane.showMessageDialog(null,"設定成功");
		}
	}

//	public static void main(String args[]){
//		try {
//		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//		        if ("Nimbus".equals(info.getName())) {
//		            UIManager.setLookAndFeel(info.getClassName());
//		            break;
//		        }
//		    }
//		} catch (Exception e) {
//		}
//		JFrame frame = new JFrame();
//		JPanel panel =new SetUpPanel();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setContentPane(panel);
//		frame.setSize(panel.getSize());
//		frame.setVisible(true);
//	}
}
