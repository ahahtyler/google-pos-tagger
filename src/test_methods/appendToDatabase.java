package test_methods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class appendToDatabase {

	
	public static void main (String[] args) throws IOException{
		
		//Read in database
		//Obtain phrases from each entry
		//Do the wikipedia search (take only up to redirect/missing)
		//Do the wikihow search
		//Store database entry
		//Combine entry 
		
		File databaseFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Database/database.txt");
		String databaseList = FileUtils.readFileToString(databaseFile);
		
		String[] databaseIndexed = databaseList.split("[\\\n]"); 
		String newDatabase = "";
		String newDatabaseEntry = "";
		
		for(int i = 0; i < databaseIndexed.length; i++){
			
			System.out.println(i + " of " + databaseIndexed.length);
			String[] databaseEntry = databaseIndexed[i].split("---");
			
			String[] phrase = databaseEntry[0].split(" ");
			
			String word1 = phrase[0];
			String word2 = phrase[1];
			
			//--- WIKIPEDIA FETCH----
			String wikipediaText = "";
			String inputLine = ""; 
			String webAddress = "http://en.wikipedia.org/w/api.php?%20format=txt&action=query&titles=" + word1 + "%20" + word2 + "&prop=revisions&rvprop=content";
			URL link = new URL(webAddress);
			InputStream is = link.openStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while((inputLine = in.readLine()) != null){
				wikipediaText = wikipediaText + inputLine;}
			in.close();
			wikipediaText = wikipediaText.toLowerCase().replaceAll("\\s+", " ");
			
			//---WIKIHOW FETCH---
			String WikiHowText = "";
			inputLine = "";
			webAddress = "http://www.wikihow.com/Special:LSearch?search=" + word1 + "+" + word2;
			link = new URL(webAddress);
			is = link.openStream();
			in = new BufferedReader(new InputStreamReader(is));
			while((inputLine = in.readLine()) != null){
				WikiHowText = WikiHowText + inputLine;}
			in.close();	
			String target = WikiHowText.replaceAll("<[^>]*>", "");
			WikiHowText = target.toLowerCase();
			if(WikiHowText.contains("results for") && WikiHowText.contains("(function(w,d,s,p,v,e,r)"))
				WikiHowText = WikiHowText.substring(WikiHowText.indexOf("results for"), WikiHowText.indexOf("(function(w,d,s,p,v,e,r)")).replaceAll("\\s+", " ");
			else
				WikiHowText = WikiHowText.replaceAll("\\s+", " ");
			
			String language = "abcdefghijklmnopqrstuvwxyz 1234567890!@#$%^&*()_+=-[]{}\"\\|;:'<>.,/?`~";
			char[] charList = WikiHowText.toCharArray();
	    	String newWikiHowText = "";
	    	for(int j = 0; j < charList.length; j++){
	    		String letter = String.valueOf(charList[j]);
	    		if(language.contains(letter)){
	    			newWikiHowText = newWikiHowText + charList[j];
	    		}
	    	}
	    	WikiHowText = newWikiHowText;
	    	
			//---APPEND TO DATABASE---
			newDatabaseEntry = "";
			for(int j = 0; j < databaseEntry.length; j++){
				newDatabaseEntry = newDatabaseEntry + databaseEntry[j] + "---";
			}
			newDatabaseEntry = newDatabaseEntry.replace("\n", "").replace("\r", "");
			newDatabaseEntry = newDatabaseEntry + wikipediaText + "---" + WikiHowText + "\n";
			
			newDatabase = newDatabase + newDatabaseEntry;
			
			PrintWriter DatabaseWriter = null;
			try {
				DatabaseWriter = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Database/database.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			DatabaseWriter.print(newDatabase);
			DatabaseWriter.close();
			
		}		
	}
	
	public static void stall(int seconds){
		
		try{
			Thread.sleep(seconds * 1000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
	}
}
