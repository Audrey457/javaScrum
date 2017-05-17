package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class TopPanel extends JPanel {
	JButton changeView;
	
	public TopPanel(){
		super();
		this.setLayout(new FlowLayout());
		changeView = new JButton("Change view !");
		this.addListeners();
		this.position();
	}
	
	public void addListeners(){
		changeView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
	}
	
	public void position(){
		this.add(changeView);
	}
}
