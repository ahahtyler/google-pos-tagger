package test_methods;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class WordSmythRelatedWords {

	public static void main (String args[]) throws IOException{
		
		String word = "power";
		
		URL link = new URL("http://www.wordsmyth.net/?level=3&ent=" + word);
		BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
		
		String relatedList = "";
		String inputLine = "";
		
		while((inputLine = in.readLine()) != null){
			relatedList = relatedList + inputLine;
		}
		
		if(relatedList.contains("part of speech")){
			relatedList = relatedList.replaceAll("\\<[^>]*>", " ");
			String[] similarWordLists = relatedList.split("part of speech");
			
			for(int i = 0; i < similarWordLists.length; i++){
				if(similarWordLists[i].contains("noun") && similarWordLists[i].contains("definition")){
					//System.out.println(similarWordLists[i]);
					String snippit = similarWordLists[i];
					snippit = snippit.substring(snippit.indexOf("similar words:"));
					//System.out.println(snippit);
					
					String[] definitions = snippit.split("definition");
					String simWordsList = "";
					for(int j = 0; j < definitions.length; j++){
						if(definitions[j].contains("similar words")){
							if(definitions[j].contains("related words")){
								
								String tempList = definitions[j].substring(definitions[j].indexOf("similar words"), definitions[j].indexOf("related words"));
								tempList = tempList.substring(tempList.indexOf(':')+1);
								tempList = tempList.trim();
								tempList = tempList.replaceAll(",  ", "");
								simWordsList = simWordsList + " " + tempList;
								//System.out.println(tempList);
							}
							else{
								String tempList = definitions[j].substring(definitions[j].indexOf("similar words"));
								tempList = tempList.substring(tempList.indexOf(':')+1);
								tempList = tempList.trim();
								tempList = tempList.replaceAll(",  ", "");
								simWordsList = simWordsList + " " + tempList;
								//System.out.println(tempList);
							}
						}
					}
					simWordsList = simWordsList.trim();
					System.out.println(simWordsList);
				}
			}
		}
		else{
			if(relatedList.contains("class=\"word\"")){
				
				relatedList = relatedList.substring(relatedList.indexOf("class=\"word\""), relatedList.indexOf("onclick=\"SaveSearchParam"));
				relatedList = relatedList.substring(relatedList.indexOf("http"));
				relatedList = relatedList.replace("\"", "");
				
				//System.out.println(relatedList);
				
				link = new URL(relatedList);
				in = new BufferedReader(new InputStreamReader(link.openStream()));
				
				inputLine = "";
				relatedList = "";
				
				while((inputLine = in.readLine()) != null){
					relatedList = relatedList + inputLine;
				}
				in.close();
				
				if(relatedList.contains("part of speech")){
					relatedList = relatedList.replaceAll("\\<[^>]*>", " ");
					String[] similarWordLists = relatedList.split("part of speech");
					
					for(int i = 0; i < similarWordLists.length; i++){
						if(similarWordLists[i].contains("noun") && similarWordLists[i].contains("definition")){
							//System.out.println(similarWordLists[i]);
							String snippit = similarWordLists[i];
							snippit = snippit.substring(snippit.indexOf("similar words:"));
							//System.out.println(snippit);
							
							String[] definitions = snippit.split("definition");
							String simWordsList = "";
							for(int j = 0; j < definitions.length; j++){
								if(definitions[j].contains("similar words")){
									if(definitions[j].contains("related words")){
										
										String tempList = definitions[j].substring(definitions[j].indexOf("similar words"), definitions[j].indexOf("related words"));
										tempList = tempList.substring(tempList.indexOf(':')+1);
										tempList = tempList.trim();
										tempList = tempList.replaceAll(",  ", "");
										simWordsList = simWordsList + " " + tempList;
										//System.out.println(tempList);
									}
									else{
										String tempList = definitions[j].substring(definitions[j].indexOf("similar words"));
										tempList = tempList.substring(tempList.indexOf(':')+1);
										tempList = tempList.trim();
										tempList = tempList.replaceAll(",  ", "");
										simWordsList = simWordsList + " " + tempList;
										//System.out.println(tempList);
									}
								}
							}
							simWordsList = simWordsList.trim();
							System.out.println(simWordsList);
						}
					}
				}
				else{
					System.out.println("No related List found");
				}
			}
			else{
				System.out.println("No related List found");
			}
		}
	}
}
