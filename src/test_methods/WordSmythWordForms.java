package test_methods;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class WordSmythWordForms {
	
	public static void main(String args[]) throws IOException{
		
		String stem = "companion";

		URL link = new URL("http://www.wordsmyth.net/?level=3&ent="+stem);
		BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
		
		String inputLine = "";
		String formsList = "";
		
		while((inputLine = in.readLine()) != null){
			formsList = formsList + inputLine;
		}
		in.close();
		
		if(formsList.contains("inflections:")){
			formsList = formsList.replaceAll("\\<[^>]*>", " ");
			formsList = formsList.replaceAll(",", "");
			formsList = formsList.substring(formsList.indexOf("inflections:"));
			formsList = formsList.substring(0, formsList.indexOf("definition"));
			formsList = formsList.substring(formsList.indexOf(":")+1);
			
			formsList = formsList.trim();
			formsList = formsList + " ";
			
			if(!formsList.contains(stem + " "))
				formsList = formsList + stem;
			
			//p.setFormsListWord1(formsList, k);
			System.out.println(formsList);
		}
		else{
			
			if(formsList.contains("class=\"word\"")){
				formsList = formsList.substring(formsList.indexOf("class=\"word\""), formsList.indexOf("onclick=\"SaveSearchParam"));				
				formsList = formsList.substring(formsList.indexOf("http"));
				formsList = formsList.replace("\"", "");
				System.out.println(formsList);
				
				link = new URL(formsList);
				in = new BufferedReader(new InputStreamReader(link.openStream()));
				
				inputLine = "";
				formsList = "";
				
				while((inputLine = in.readLine()) != null){
					formsList = formsList + inputLine;
				}
				in.close();
				
				if(formsList.contains("inflections:")){
					formsList = formsList.replaceAll("\\<[^>]*>", " ");
					formsList = formsList.replaceAll(",", "");
					formsList = formsList.substring(formsList.indexOf("inflections:"));
					formsList = formsList.substring(0, formsList.indexOf("definition"));
					formsList = formsList.substring(formsList.indexOf(":")+1);
					formsList = formsList.trim();
					formsList = formsList + " ";
					
					if(!formsList.contains(stem + " "))
						formsList = formsList + stem;
					
					//p.setFormsListWord1(formsList, k);
					System.out.println(formsList);
				}
				else{
					System.out.println("No formsList found");
				}
			}
			else{
				//p.setFormsListWord1("NA", k);
				System.out.println("No formsList found");
			}
		}
	}
}
