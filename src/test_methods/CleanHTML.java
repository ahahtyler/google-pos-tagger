package test_methods;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class CleanHTML {

	public static void main(String args[]) throws IOException{
		
		URL link = new URL("https://www.google.com/search?q=running+shoes");
		BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
		
		String inputLine = "";
		String webPage = "";
		
		while((inputLine = in.readLine()) != null){
			webPage = webPage + "\r\n" + inputLine;
		}
		in.close();
		
		webPage = webPage.replaceAll("\\<[^>]*>", " ");

		
		System.out.print(webPage);
	}
}
