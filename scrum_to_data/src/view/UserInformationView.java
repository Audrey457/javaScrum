package view;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class UserInformationView extends JDialog {
	
	private JLabel info;
		
	public UserInformationView(Frame parentFrame, String title){
		super(parentFrame, title);
		info = new JLabel("");
		this.add(info);
		this.setPreferredSize(new Dimension(400,100));
	}
	
	public void appIsProcessing(){
		info.setText("App is processing, please wait");
		info.revalidate();
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.revalidate();
	}
	
	public void endOfProcess(){
		info.setText("End of processing, you can close this window now !");
		info.revalidate();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.revalidate();
	}
	
	public void showInterface(){
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
