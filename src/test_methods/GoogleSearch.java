package test_methods;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class GoogleSearch {

	//final static String apiKey = "AIzaSyCOqoQEKcUUw8EWo1tYumGIZ79y-RoOj_o";
	//final static String apiKey = "AIzaSyDvdbJio5Q-_Nx7vRYNU9LMp0PFXRQ4O_w";
	//final static String apiKey = "AIzaSyBz8JiA12jTdsLwGhnuMg4i5ZChJYn_9ks";
	//final static String apiKey = "AIzaSyAv3lnpZcT7dwdAwucei0DqrIt2bC30-xs";
	//final static String apiKey = "AIzaSyA1l_zBTq-jf58OW10Mz_eEXc8X42M-fmk";
	//final static String apiKey = "AIzaSyCi-aH-owpAlLxDsA9LGfbM-WOm5b_DR3o";
	//final static String apiKey = "AIzaSyBlHqkPLVCL5SYZr5tx5_NsAsWilx0EsIM";
	//final static String apiKey = "AIzaSyALE2ZEHnmUtrKqyewv7PEdnMQ4RKwhsds";
	//final static String apiKey = "AIzaSyD5qD_IRlQnyqu8snVE1YRD_aN95incwBo";
	final static String apiKey = "AIzaSyCe4AMXFgG01uAW0GL_UTKHBIh8_cmnXJE";
	final static String customSearchEngineKey = "015715824305857803777:_skd84j7wog";

	final static String searchURL = "https://www.googleapis.com/customsearch/v1?";
	
	// This adds some limitations, it removes shop adds and narrows the frequency down to only search results
	// THis could harm the frequency from adds
	// It is also limited down to only .com and .org websites this can be changed to add more
	
	
    public static void main(String[] args) throws Exception{

    	String url = makeSearchString("ruling power", 1, 10);
    	String out = readURL(url);
    	
    	String searchResults = "";
    	String searchLinks = "";
    	//System.out.println(out);
    	//out = out.substring(out.indexOf("\"items\""));
    	//out = out.replaceAll("\"", " ");
    	//out = out.replaceAll("...", "");
    	
    	String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 .,!/():\\\"}{";
    	char[] outArray = out.toCharArray();
    	String newOut = "";
    	for(int i = 0; i < outArray.length; i++){
    		String letter = String.valueOf(outArray[i]);
    		if(alphabet.contains(letter)){
    			newOut = newOut + outArray[i];
    		}
    	}
    	
    	String[] items = newOut.split("},");
    	for(int i = 0; i < items.length; i++){
    		if(items[i].contains("\"kind\"")){
	    		items[i] = items[i].substring(items[i].indexOf("\"kind\":"));
	    		
	    		String[] jsonItems = items[i].split(",   ");
	    		for(int j = 0; j < jsonItems.length; j++){
	    			
	    			jsonItems[j] = jsonItems[j].trim();
	    			jsonItems[j] = jsonItems[j].replaceAll("\"metatags\":", "");
					jsonItems[j] = jsonItems[j].replaceAll("\"newsarticle\":", "");
					jsonItems[j] = jsonItems[j].replaceAll("u003", "").replaceAll("003e", "").replaceAll("039", "");
					jsonItems[j] = jsonItems[j].replaceAll("[\\{]", "").replaceAll("[\\}]", "");
					
	    			String[] jsonWords = jsonItems[j].split(" ");
	
	    			if(jsonWords[0].contains("snippet") || jsonWords[0].contains("description") || jsonWords[0].contains("title")){
	    				
	    				jsonItems[j] = jsonItems[j].replaceAll("\"", "");
	    				jsonItems[j] = jsonItems[j].replace("...", "");
	    				jsonItems[j] = jsonItems[j].replaceAll("[\\,]", "");
	    				jsonItems[j] = jsonItems[j].replaceAll("  ", " ");
	    				
	    				jsonItems[j] = jsonItems[j].substring(jsonItems[j].indexOf(": ") +2);
	    				jsonItems[j] = jsonItems[j].replace(":", "");
	    				jsonItems[j] = jsonItems[j].replace(".", "");
	    				jsonItems[j] = jsonItems[j].toLowerCase();
	    				jsonItems[j] = jsonItems[j].replace("\\n", "");
	    				searchResults = searchResults +  jsonItems[j] + " | " ;
	    				//System.out.println(jsonItems[j]);
	    			}
	    			
	    			if(jsonWords[0].contains("link")){
	    				jsonItems[j] = jsonItems[j].replaceAll("\"", "");
	    				jsonItems[j] = jsonItems[j].replace("...", "");
	    				jsonItems[j] = jsonItems[j].replaceAll("[\\,]", "");
	    				jsonItems[j] = jsonItems[j].replaceAll("  ", " ");
	    				
	    				jsonItems[j] = jsonItems[j].substring(jsonItems[j].indexOf(": ") +2);
	    				jsonItems[j] = jsonItems[j].toLowerCase();
	    				searchLinks = searchLinks + jsonItems[j] + " | ";
	    				//System.out.println("Link: " + jsonItems[j]);
	    			}
	    
	    		}
	    		
	    		//System.out.println("====================================================");
	    		//System.out.println(items[i]);
    		}
    	}
    	//System.out.println(newOut);
    	searchLinks = searchLinks.trim();
    	searchResults = searchResults.trim();
    	
    	System.out.println(searchLinks);
    	System.out.println(searchResults);
    }
	
	private static String makeSearchString (String qSearch, int start, int numOfResults){
		
		String toSearch = searchURL + "key=" + apiKey + "&cx=" + customSearchEngineKey + "&q=";
		
		String keys[] = qSearch.split("[ ]+");
		
		for(String key:keys){
			toSearch += key + "+";
		}
		
		toSearch+="&alt=json";
		
		toSearch+="&start="+start;
		
		toSearch+="&num="+numOfResults;
		
		return toSearch;
	}
	
	private static String readURL(String pUrl)
	{
	    //pUrl is the URL we created in previous step
	    try
	   {
	        URL url=new URL(pUrl);
	        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
	        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line;
	        StringBuffer buffer=new StringBuffer();
	        while((line=br.readLine())!=null){
	            buffer.append(line);
	        }
	        return buffer.toString();
	    }catch(Exception e){
	    	System.out.println("fuck that shit");
	        e.printStackTrace();
	   }
	    return null;
	}

}
