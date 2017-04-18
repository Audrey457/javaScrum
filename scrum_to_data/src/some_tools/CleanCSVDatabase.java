package some_tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CleanCSVDatabase {
	
	File fileToRead;
	File fileToCreate;
	
	public CleanCSVDatabase(File fileToRead, File fileToCreate){
		this.fileToRead = fileToRead;
		this.fileToCreate = fileToCreate;
	}
	
	public String extractDate(String s){
		if(!s.equals("msg,Id")){
			String [] splited = s.split("               ", 2);
			System.out.println(splited[0]);
			return splited[0] + "\",\"" + splited[1];
		}
		return "date_msg,"+ s;
	}
	
	public void go(String[] irrelevant) throws IOException{
		FileReader fr = new FileReader(fileToRead);
		BufferedReader read = new BufferedReader(fr);
		FileWriter fw = new FileWriter(fileToCreate);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter writer = new PrintWriter(bw, true);
		String line;
		boolean ok;
		
		while((line = read.readLine()) != null){
			ok = true;
			for(String s : irrelevant){
				if(line.contains(s)){
					ok = false;
				}
			}
			if(ok){
				line = extractDate(line);
				writer.println(line);
			}
		}
		
		writer.close();
		bw.close();
		fw.close();
		fr.close();
		read.close();
	}
	

	public static void main(String[] args) throws IOException {
		int nbArgs = args.length;
		if(nbArgs < 3){
			System.out.println("I need 3 arguments at least !");
		}
		else{
			CleanCSVDatabase ccd = new CleanCSVDatabase(new File(args[0]), new File(args[1]));
			if (!ccd.fileToRead.exists() || ccd.fileToRead.isDirectory()){
				System.out.println("error : the 1st argument must be a path to an existing file, and the 2nd must be different of a directory path");
			}
			else{
				String [] irrelevants = new String[nbArgs-2];
				for(int i = 2; i < nbArgs; i++){
					irrelevants[i-2] = args[i];
				}
				ccd.go(irrelevants);
			}
		}
	}

}
