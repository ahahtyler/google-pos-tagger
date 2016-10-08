package test_methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class LowQualityVotingScheme {
	static final String autofill_link = "http://suggestqueries.google.com/complete/search?client=chrome&q=";

	public static void main (String args[]) throws IOException{
		MaxentTagger tagger = new MaxentTagger("taggers/wsj-0-18-bidirectional-nodistsim.tagger");
       	URL link = null;
        String inputLine = "";
        String autoList = "";
        String r_autoList = "";
        
    	String word1 = "The";
    	String word2 = "gem";

    	String complete = "";
    	char[] charArray = null;

		link = new URL(autofill_link + word1 + "%20" + word2);
		BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));

        while ((inputLine = in.readLine()) != null)
            autoList = autoList + inputLine;
        in.close();
        
        charArray = autoList.toCharArray();

        int j = 0;
        while(charArray[j] != ']'){
        	r_autoList = r_autoList + charArray[j];
        	j++;
        }
        
        r_autoList = r_autoList.replace("[", " ");
        r_autoList = r_autoList.replace("\"", " ");
        r_autoList = r_autoList.replace("%", "-");
        
        String taggedlist = "";
        String plist[] = r_autoList.split("[\\,]");
        for(int k = 0; k < plist.length; k++){
        	plist[k] = plist[k].trim();
        	complete = complete + plist[k] + "| ";
           	taggedlist = tagger.tagString(plist[k]) + "| ";
        }

		String tlist[] = taggedlist.split("[\\|]");
		
		int adj = 0;
		int verb = 0;
		
		for(int i = 0; i < tlist.length; i++){
			System.out.println(tlist[i]);
			if(tlist[i].contains(word1+"_N") || tlist[i].contains(word1+"_J")){
				adj++;
			}
			if(tlist[i].contains(word1+"_V")){
				verb++;
			}
		}
		
		if(adj > verb)
			System.out.println(word1 + " " + word2 + ": ADJ");
		else if(verb > adj)	
			System.out.println(word1 + " " + word2 + ": VERB");
		else
			System.out.println(word1 + " " + word2 + ": TIE");
		System.out.println("ADJ: " + adj + "   VERB: " + verb);
        
	}
	
}
