package sometools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import domainmodel.Author;
import domainmodel.Message;
import domainmodel.Topic;

public class IOSerialTools {
	
	private static final Logger logger = Logger.getLogger(IOSerialTools.class);
	
	//private constructor to hide the implicit public one
	private IOSerialTools(){
		
	}
	
	public static List<Message> readMessagesObject(File fic){
		ObjectInputStream ois;
		ArrayList<Message> am = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			am = (ArrayList<Message>) ois.readObject();
			ois.close();
		} catch (IOException|ClassNotFoundException e) {
			logger.fatal(e);
		} 
		return am;
	}
	
	public static List<Topic> readTopicsObject(File fic){
		ObjectInputStream ois;
		ArrayList<Topic> at = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			at = (ArrayList<Topic>) ois.readObject();
			ois.close();
		} catch (IOException|ClassNotFoundException e) {
			logger.fatal(e);
		}
		return at;
	}
	
	public static Set<Author> readAuthorsObject(File fic){
		ObjectInputStream ois;
		LinkedHashSet<Author> hsa = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			hsa = (LinkedHashSet<Author>) ois.readObject();
			ois.close();
		} catch (IOException|ClassNotFoundException e) {
			logger.fatal(e);
		}
		return hsa;
	}
	
	//ignore the smell warning : you need an ArrayList or a LinkedHashSet
	//and not a List or a Set because it must be a Serializable object
	public static void saveTopicsAsObject(ArrayList<Topic> topicsList){
		File fic = new File("saved_objects\\allTopics.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(topicsList);
			oos.close();
		}catch(IOException e){
			logger.error(e);
		}
	}
	
	public static void saveAuthorsAsObject(LinkedHashSet<Author> authorsList){
		File fic = new File("saved_objects\\allAuthors.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(authorsList);
			oos.close();
		}catch(IOException e){
			logger.error(e);
		}
	}
	
	public static void saveMessagesAsObject(ArrayList<Message> messagesList){
		File fic = new File("saved_objects\\allMessages.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(messagesList);
			oos.close();
		}catch(IOException e){
			logger.error(e);
		}
	}
}
