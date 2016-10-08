package test_methods;

import java.util.Arrays;

import edu.smu.tspell.wordnet.*; 


public class WordNetStem {
	public static void main(String[] args)
	{
			System.setProperty("wordnet.database.dir", "C:/Program Files/eclipse/WordNet-3.0/dict/");

			WordNetDatabase database = WordNetDatabase.getFileInstance();
			
			String s_word = "stimuli";
			s_word = s_word.toLowerCase();
			Synset[] synsets = database.getSynsets(s_word , SynsetType.NOUN);
			String list = "";
			String[] b_list = null;
			String stem = "to ";
			
			if (synsets.length > 0)
			{
				for (int i = 0; i < synsets.length; i++)
				{
					String[] wordForms = synsets[i].getWordForms();
					for (int j = 0; j < wordForms.length; j++){
						System.out.println(wordForms[j]);
						list = list + wordForms[j] + "|";
					}
				}
				//System.out.println(list);
				b_list = list.split("[\\|]");
				
				stem = "to " + getPopularElement(b_list);
				System.out.println(stem);
				/*Arrays.sort(b_list);
				for(int i = 0; i < b_list.length; i++){
					//System.out.println(b_list[i] + "_________" + s_word.substring(0, 2));
					if(b_list[i].substring(0, 2).contains(s_word.substring(0, 2))){
						
						stem = stem + b_list[i];
						System.out.println(stem);
						break;
					}
				}*/
			}
			else{
				System.err.println("No synsets exist");
			}	
	}
	
	public static String getPopularElement(String[] a)
	{
	  int count = 1, tempCount;
	  String popular = a[0];
	  String temp = "";
	  for (int i = 0; i < (a.length - 1); i++)
	  {
	    temp = a[i];
	    tempCount = 0;
	    for (int j = 1; j < a.length; j++)
	    {
	      if (temp.equals(a[j]))
	        tempCount++;
	    }
	    if (tempCount > count)
	    {
	      popular = temp;
	      count = tempCount;
	    }
	  }
	  return popular;
	}
	
}
