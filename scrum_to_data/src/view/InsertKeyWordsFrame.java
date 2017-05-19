package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controlers.ButtonControler;
import crawler.CrawlAndWriteApp;
import databasemodel.ForumDataBase;
import sometools.SetTranslationTools;

public class InsertKeyWordsFrame extends JFrame {
	private JButton basicInsertButton;
	private JPanel container;
	private GridLayout containerLayout;
	private Dimension buttonSize;
	private ButtonControler controler;
	private ForumDataBase forumDataBase;
	private boolean test;
	
	public InsertKeyWordsFrame(){
		super();
		setTitle("Insert key words tools");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void initialize(){
		containerLayout = new GridLayout(0,1);
		container = new JPanel(this.containerLayout);
		basicInsertButton = new JButton("Insertion from the enclosed file");
		buttonSize = basicInsertButton.getPreferredSize();
		forumDataBase = new ForumDataBase(
				"jdbc:mysql://localhost/scrumdata?autoReconnect=true&useSSL=false", 
				"root", "");
		controler = new ButtonControler(this.forumDataBase);
		test = true;
	}
	
	private void position(){
		this.container.setPreferredSize(new Dimension(
				(int)(buttonSize.getWidth() * 2.5)+20,
                (int)(buttonSize.getHeight() * 3.5) + 20 * 2));
		this.containerLayout.setVgap(10);
		this.container.add(this.basicInsertButton);
		this.getContentPane().add(this.container);
	}
	
	private void setEnabledButtons(){
		boolean keyWordsTableEmpty = this.controler.keyWordsTableEmpty();
		this.basicInsertButton.setEnabled(keyWordsTableEmpty);
	}
	
	public void addListeners(){
		this.basicInsertButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//basicInsertActionPerformed();
				basicInsertActionPerformed();
			}
		});
	}
	
	public void basicInsertActionPerformed(){
		Set<String> set = SetTranslationTools.fromFileToSet("config_files\\keywords.txt");
		forumDataBase.openDB();
		forumDataBase.getKeyWordsTable().insertKeyWordsList(set);
		forumDataBase.closeDB();
		this.setEnabledButtons();
		this.dispose();
	}
	
	public void createAndShowGUI(){
		this.initialize();
		if(this.controler.noDatabase()){
			JOptionPane.showMessageDialog(null, 
					"Can not connect to the database. Many possibilities, including you have not already created it" + 
			", your server is off, the name of your database is wrong... Please read README.txt",
			"Database connection failed", JOptionPane.ERROR_MESSAGE);
		}else{
			this.setEnabledButtons();
			this.position();
			this.addListeners();
		
			//the 3rd next lines must remain the last instructions of the createAndShowGUI method
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		
	}
}
