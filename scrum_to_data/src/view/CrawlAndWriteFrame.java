package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controlers.ButtonControler;
import crawler.CrawlAndWriteApp;
import databasemodel.ForumDataBase;

public class CrawlAndWriteFrame extends JFrame {
	private JButton create, update, rewrite;
	private JPanel container;
	private Dimension buttonSize;
	private CrawlAndWriteApp crawlerApp;
	private GridLayout containerLayout;
	private UserInformationView userInformationView;
	private ButtonControler controler;
	
	public CrawlAndWriteFrame(){
		super();
		setTitle("Basic crawl and database management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		userInformationView = new UserInformationView(this, "Information");
		controler = new ButtonControler(this.crawlerApp);
		
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
		this.initialize();
		if(this.controler.noDatabase()){
			JOptionPane.showMessageDialog(null, 
					"Can not connect to the database. Many possibilities, including you have not already created it" + 
			", your server is off, the name of your database is wrong... Please read README.txt",
			"Database connection failed", JOptionPane.ERROR_MESSAGE);
		}else if(!this.controler.canAccessForum()){
			JOptionPane.showMessageDialog(null, 
					"Can not access to the forum. Maybe you don't have access to Internet",
			"Forum connection failed", JOptionPane.ERROR_MESSAGE);
		}
		else{
			this.setEnabledButtons();
			this.position();
			this.addToolTips();
			this.addListeners();
		
			//the 3rd next lines must remain the last instructions of the createAndShowGUI method
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		
	}
	
	private void addListeners(){
		//create button
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
		
		//update button
		this.update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userInformationView.showInterface();
				userInformationView.appIsProcessing();
				crawlerApp.basicUpdate();
				userInformationView.endOfProcess();
			}
		});
		
		//rewrite button
		this.rewrite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userInformationView.showInterface();
				userInformationView.appIsProcessing();
				crawlerApp.crawlAndRewrite();
				userInformationView.endOfProcess();
			}
		});
		
		this.userInformationView.addWindowListener(new WindowClosedListener(this));
	}
	
	public void closeWindow(){
		this.dispose();
	}
	
	
}
