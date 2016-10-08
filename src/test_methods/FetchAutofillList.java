package test_methods;


import java.net.*;
import java.io.*;

public class FetchAutofillList {	
	
	static final String autofill_link = "http://suggestqueries.google.com/complete/search?client=chrome&q=";
	
    public static void main(String[] args) throws Exception {
    	URL link = null;
		BufferedReader in = null;
			
		int apperance;
		String inputLine;
		String autoList = "";
		String cleanedAutoList = "";
		String word1 = "arousing";
		String word2 = "stimuliatisdfon";
		
		String fullPhraseSing = word1 + " " + word2;
		
		String[] word2Factorial = new String[word2.length()+1];
		
		for(int j = 0; j < word2.length() + 1; j++){
			word2Factorial[j] = word2.substring(0, j);
		}
		
		for(apperance = 0; apperance < word2Factorial.length; apperance++){
			cleanedAutoList = "";
			inputLine = "";
			autoList = "";
			
			if(apperance == 0){
				link = new URL(autofill_link + word1);
			}
			else{
				link = new URL(autofill_link + word1 + "%20" + word2Factorial[apperance]);
			}
			
			in = new BufferedReader(new InputStreamReader(link.openStream()));
			
			while((inputLine = in.readLine()) != null){
				autoList = autoList + inputLine;
			}
			in.close();
			
			char[] autoListCharacters = autoList.toCharArray();
			int temp = 0;
			while(autoListCharacters[temp] != ']'){
				cleanedAutoList = cleanedAutoList + autoListCharacters[temp];
				temp++;
			}
			
			cleanedAutoList = cleanedAutoList.replace("[",  " ");
			cleanedAutoList = cleanedAutoList.replace("\"",  " ");
			cleanedAutoList = cleanedAutoList.replace("%",  " ");
			cleanedAutoList = cleanedAutoList.toLowerCase();
			
			String[] autoListElements = cleanedAutoList.split("[\\,]");
			for(int j = 0; j < autoListElements.length; j++){
				autoListElements[j] = autoListElements[j].trim();
				
				if(autoListElements[j].contains(fullPhraseSing)){
					
					System.out.println(apperance);
				
					for(int k = 0; k < autoListElements.length; k++){
						System.out.println(autoListElements[k]);
						//p.setContainsList(p.getContainsList()+autoListElements[k]+"| ");
					}
					apperance = word2Factorial.length+1;
					break;
				}
			}
		}
    }
}
