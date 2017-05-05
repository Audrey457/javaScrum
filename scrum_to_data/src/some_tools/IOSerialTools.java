package some_tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import java_objects.Author;
import java_objects.Message;
import java_objects.Topic;

public class IOSerialTools {
	
	public static void saveMessagesAsObject(ArrayList<Message> messagesList){
		File fic = new File("saved_objects\\allMessages.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(messagesList);
			oos.close();
		}catch(IOException e){
			System.out.println("Can not write: " + e.getMessage());
		}
	}
	
	public static ArrayList<Message> readMessagesObject(File fic){
		ObjectInputStream ois;
		ArrayList<Message> am = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			am = (ArrayList<Message>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println("Can not find file: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error when trying to read: " + e.getMessage());
		} catch (ClassNotFoundException e){
			System.out.println("Class not found: " + e.getMessage());
		}
		return am;
	}
	
	public static ArrayList<Topic> readTopicsObject(File fic){
		ObjectInputStream ois;
		ArrayList<Topic> at = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			at = (ArrayList<Topic>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println("Can not find file: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error when trying to read: " + e.getMessage());
		} catch (ClassNotFoundException e){
			System.out.println("Class not found: " + e.getMessage());
		}
		return at;
	}
	
	public static LinkedHashSet<Author> readAuthorsObject(File fic){
		ObjectInputStream ois;
		LinkedHashSet<Author> hsa = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			hsa = (LinkedHashSet<Author>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println("Can not find file: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error when trying to read: " + e.getMessage());
		} catch (ClassNotFoundException e){
			System.out.println("Class not found: " + e.getMessage());
		}
		return hsa;
	}
	
	public static void saveTopicsAsObject(ArrayList<Topic> topicsList){
		File fic = new File("saved_objects\\allTopics.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(topicsList);
			oos.close();
		}catch(IOException e){
			System.out.println("Can not write: " + e.getMessage());
		}
	}
	
	public static void saveAuthorsAsObject(LinkedHashSet<Author> authorsList){
		File fic = new File("saved_objects\\allAuthors.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(authorsList);
			oos.close();
		}catch(IOException e){
			System.out.println("Can not write: " + e.getMessage());
		}
	}
}
