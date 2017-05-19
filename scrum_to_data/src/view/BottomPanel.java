package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class BottomPanel extends JPanel {
	JButton cAndWButton, calcTagsButton, insertKeyWordButton;
	CrawlAndWriteFrame crawlAndWriteFrame;
	InsertKeyWordsFrame insertKeyWordsFrame;
	
	public BottomPanel(){
		super();
		this.setLayout(new FlowLayout());
		cAndWButton = new JButton("Crawl and write");
		calcTagsButton = new JButton("Recalculate tags");
		insertKeyWordButton = new JButton("Insert key words in database");
		crawlAndWriteFrame = new CrawlAndWriteFrame();
		insertKeyWordsFrame = new InsertKeyWordsFrame();
		this.addListeners();
		this.position();
	}
	
	public void addListeners(){
		cAndWButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				crawlAndWriteFrame.createAndShowGUI();
			}
		});
		calcTagsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		insertKeyWordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				insertKeyWordsFrame.createAndShowGUI();
				
			}
		});
	}
	
	public void position(){
		this.add(cAndWButton);
		this.add(insertKeyWordButton);
		this.add(calcTagsButton);
	}
}
