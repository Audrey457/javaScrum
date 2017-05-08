package view;
import org.apache.log4j.Logger;

public class ViewLauncher {
	
	final static Logger logger = Logger.getLogger(ViewLauncher.class);

	public static void main(String[] args) {
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				new MyFrame();
//			}
//		});
		
		if(logger.isDebugEnabled()){
			logger.debug("This is debug : ");
		}

		if(logger.isInfoEnabled()){
			logger.info("This is info : ");
		}

		logger.warn("This is warn : ");
		logger.error("This is error : ");
		logger.fatal("This is fatal : ");

	}
}
