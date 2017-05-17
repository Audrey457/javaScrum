package sometools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class SetTranslationTools {
	
	private static final Logger logger = Logger.getLogger(SetTranslationTools.class);
	
	private SetTranslationTools(){
		
	}
	

	public static String fromSetToStringList(Set<String> set) {
		String str = "";
		for(String s: set){
			s.replaceAll("|", "/");
			str += s + "|";
		}
		return str;
	}
	
	public static Set<String> fromStringListToSet(String stringList, String regex){
		HashSet<String> hashSet = new HashSet<>();
		String [] words = stringList.split(regex);
		for(String s: words){
			hashSet.add(s);
		}
		return hashSet;
	}
	
	/**
	 * One word per line
	 * @param fileName an instance of String
	 * @return an instance of HashSet/<String/>
	 */
	public static Set<String> fromFileToSet(String fileName){
		HashSet<String> set = new HashSet<>();
		BufferedReader reader;
		String word;
		try{
			reader = new BufferedReader(new FileReader(fileName));
			while((word = reader.readLine()) != null){
				set.add(word);
			}
			reader.close();
		}catch(IOException e){
			logger.error(e);
		}
		return set;
	}

}
