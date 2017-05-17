package view;
import org.apache.log4j.Logger;

public class ViewLauncher {
	
	final static Logger logger = Logger.getLogger(ViewLauncher.class);

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainFrame.createAndShowGUI();
			}
		});
	}
}
