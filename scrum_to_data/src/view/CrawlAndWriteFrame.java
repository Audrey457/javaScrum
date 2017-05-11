package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controlers.CrawlAndWriteControler;
import crawler.CrawlAndWriteApp;
import databasemodel.ForumDataBase;

public class CrawlAndWriteFrame extends JFrame {
	private JButton create, update, rewrite;
	private JPanel container;
	private Dimension buttonSize;
	private CrawlAndWriteApp crawlerApp;
	private GridLayout containerLayout;
	private UserInformationView userInformationView;
	private CrawlAndWriteControler controler;
	
	public CrawlAndWriteFrame(){
		super();
		setTitle("Basic crawl and database management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.initialize();
	}
	
	private void initialize(){
		containerLayout = new GridLayout(0,1);
		container = new JPanel(this.containerLayout);
		create = new JButton("Crawl Scrum forum and write the database");
		update = new JButton("Crawl Scrum forum and update the database");
		rewrite = new JButton("Crawl Scrum forum and rewrite the database");
		buttonSize = rewrite.getPreferredSize();
		crawlerApp = new CrawlAndWriteApp(new ForumDataBase(
						"jdbc:mysql://localhost/scrumdata?autoReconnect=true&useSSL=false", 
						"root", ""), "https://www.scrum.org/forum/scrum-forum");
		userInformationView = new UserInformationView(this, "Please wait");
		controler = new CrawlAndWriteControler(this.crawlerApp);
		
	}
	
	private void position(){
		this.container.setPreferredSize(new Dimension(
				(int)(buttonSize.getWidth() * 2.5)+20,
                (int)(buttonSize.getHeight() * 3.5) + 20 * 2));
		this.containerLayout.setVgap(10);
		this.container.add(this.create);
		this.container.add(this.update);
		this.container.add(this.rewrite);
		this.getContentPane().add(this.container);
	}
	
	private void setEnabledButtons(){
		boolean dataBaseEmpty = this.controler.dataBaseEmpty();
		this.create.setEnabled(dataBaseEmpty);
		this.update.setEnabled(!dataBaseEmpty);
		this.rewrite.setEnabled(!dataBaseEmpty);
	}
	
	private void addToolTips(){
		this.create.setToolTipText("click this button if you want to get all the data from the Scrum forum and write it to your database");
		this.update.setToolTipText("click this button if you want to get data from the Scrum forum to update your database");
		this.rewrite.setToolTipText("Oops, made a mistake? Click this button to clean your database, get all the data from the Scrum forum and rewrite your database");
	}
	
	public void createAndShowGUI(){
		if(!this.controler.noDatabase()){
			this.setEnabledButtons();
			this.position();
			this.addToolTips();
			this.addListeners();
		
			//the 3rd next lines must remain the last instructions of the createAndShowGUI method
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}else{
			JOptionPane.showMessageDialog(null, 
					"Can not connect to the database. Many possibilities, including you have not already created it" + 
			", your server is off, the name of your database is wrong... Please read README.txt",
			"Database connection failed", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void addListeners(){
		this.create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userInformationView.showInterface();
				userInformationView.appIsProcessing();
				crawlerApp.bddFirstBuild();	
				userInformationView.endOfProcess();
				setEnabledButtons();
			}
		});
		
		this.update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//need to control
				userInformationView.showInterface();
				userInformationView.appIsProcessing();
				crawlerApp.basicUpdate();
				userInformationView.endOfProcess();
			}
		});
		
		this.rewrite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// need to control
				userInformationView.showInterface();
				userInformationView.appIsProcessing();
				crawlerApp.crawlAndRewrite();
				userInformationView.endOfProcess();
			}
		});
	}
	
	
}
