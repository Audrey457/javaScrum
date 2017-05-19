package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class UserInformationView extends JDialog {
	
	private JLabel info;
	
	public UserInformationView(Frame parentFrame, String title){
		super(parentFrame, title);
		info = new JLabel();
		this.getContentPane().add(info);
	}
	
	public void appIsProcessing(){
		this.setTitle("App is processing, please wait");
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	
	public void endOfProcess(){
		info.setText("End of processing, you can close this window now !");
		this.setTitle("Terminated, close this window");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public void showInterface(){
		this.setPreferredSize(new Dimension(400,100));
		this.pack();
		this.setLocationRelativeTo(this.getParent());
		this.setVisible(true);
	}
}
