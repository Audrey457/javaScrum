package view;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	TopPanel topPanel = new TopPanel();
	BottomPanel bottomPanel = new BottomPanel();
	LeftPanel leftPanel = new LeftPanel();
	
	public MainFrame(){
		super();
		setTitle("Scrum forum analyzer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout(10, 10));
	}
	
	public void addPanels(){
		Container container = this.getContentPane();
		container.add(topPanel, BorderLayout.PAGE_START);
		container.add(bottomPanel, BorderLayout.PAGE_END);
		container.add(leftPanel, BorderLayout.LINE_START);
	}
	
	public void createAndShowGUI(){
		this.addPanels();
		//the 3rd next lines must remain the last instructions of the createAndShowGUI method
		this.pack();
		this.setSize((int)getToolkit().getScreenSize().getWidth(), ((int)getToolkit().getScreenSize().getHeight() - 40));

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
