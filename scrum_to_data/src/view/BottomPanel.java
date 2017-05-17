package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class BottomPanel extends JPanel {
	JButton dbButton;
	CrawlAndWriteFrame crawlAndWriteFrame;
	
	public BottomPanel(){
		super();
		this.setLayout(new FlowLayout());
		dbButton = new JButton("Crawl and write !");
		crawlAndWriteFrame = new CrawlAndWriteFrame();
		this.addListeners();
		this.position();
	}
	
	public void addListeners(){
		dbButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				crawlAndWriteFrame.createAndShowGUI();
			}
		});
	}
	
	public void position(){
		this.add(dbButton);
	}
}
