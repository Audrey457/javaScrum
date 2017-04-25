package some_tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java_objects.ArrayMessages;
import java_objects.ArrayTopics;
import java_objects.HashSetAuthor;

public class IOSerialTools {
	
	public static void saveMessagesAsObject(ArrayMessages allMessage){
		File fic = new File("saved_objects\\allMessages.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(allMessage);
			oos.close();
		}catch(IOException e){
			System.out.println("Can not write: " + e.getMessage());
		}
	}
	
	public static ArrayMessages readMessagesObject(File fic){
		ObjectInputStream ois;
		ArrayMessages am = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			am = (ArrayMessages) ois.readObject();
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
	
	public static ArrayTopics readTopicsObject(File fic){
		ObjectInputStream ois;
		ArrayTopics at = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			at = (ArrayTopics) ois.readObject();
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
	
	public static HashSetAuthor readAuthorsObject(File fic){
		ObjectInputStream ois;
		HashSetAuthor hsa = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fic));
			hsa = (HashSetAuthor) ois.readObject();
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
	
	public static void saveTopicsAsObject(ArrayTopics allPosts){
		File fic = new File("saved_objects\\allTopics.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(allPosts);
			oos.close();
		}catch(IOException e){
			System.out.println("Can not write: " + e.getMessage());
		}
	}
	
	public static void saveAuthorsAsObject(HashSetAuthor allAuthors){
		File fic = new File("saved_objects\\allAuthors.so");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fic));
			oos.writeObject(allAuthors);
			oos.close();
		}catch(IOException e){
			System.out.println("Can not write: " + e.getMessage());
		}
	}
}
