package corpus_creation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;


public class GenerateCorpus {

	public static void main (String argsp[]) throws IOException{
		
		PrintWriter writer = new PrintWriter ("C:/Users/Tyler/Desktop/POS Corrector/Corpus/untagged_corpus.txt", "UTF-8");
		PrintWriter writer1 = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Corpus/tagged_corpus.txt", "UTF-8");
		
		Random generator = new Random();
		System.setProperty("wordnet.database.dir", "C:/Program Files/eclipse/WordNet-3.0/dict/");
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		boolean keep;
		int total = 0;
		
		for(int i = 1; i <=16; i++){
			try{
				File file = new File("Z:/Documents old/Wiki_Output/output"+i+".txt");
				
				String full = FileUtils.readFileToString(file, "UTF-8");
				String[] sentences = full.split("[\\\n]");
				
				int length = sentences.length;
				int j = 0;
				
				//625
				while(j < 625){
					try{
						keep = false;
						
						int randomIndex = generator.nextInt(length);
						
						String sentence = sentences[randomIndex];
						String[] words = sentence.split(" ");
						String cleaned = "";
		
						if(words.length > 1){

							//System.out.println("SENTENCE: " + sentence);
							//System.out.println("words[0] " +  words[0]);
							
							char firstLetter = words[0].charAt(0);
							
							//System.out.println("First Char: " + firstLetter + "\n");
							if(Character.isUpperCase(firstLetter) && !sentence.contains("$") && !sentence.contains("during")){ 
								for(int k = 0; k < words.length; k++){
									
									if(k < words.length - 3){
										
										if(words[k].contains("ing_") && words[k+1].contains("_N") && Character.isLowerCase(words[k].charAt(0)) && 
										   Character.isLowerCase(words[k+1].charAt(0))){
											
											cleaned = cleanSentence(sentence.trim());
											String[] cleaned_words = cleaned.split(" ");
											
											String cleaned_word = cleaned_words[k];
											cleaned_word = cleaned_word.toLowerCase();
											String lastWord = cleaned_words[cleaned_words.length-1];
											
											Synset[] synsets = database.getSynsets(cleaned_word, SynsetType.VERB);
									
											if(synsets.length > 0){
												if(lastWord.length() > 2 && !cleaned_words[k].equals("Reporting")){
													System.out.println(cleaned_word);
													keep = true;
													break;
												}
											}
											else{
												keep = false;
											}
											
										}
										
									}
								}
							}
							else{
								keep = false;
							}
							
							if(keep){
								j++;
								total++;
								System.out.println("TOTAL: " + total);
								System.out.println("KEEPING: " + cleaned);
								System.out.println();
								writer.println(cleaned + ".");
								writer1.println(sentences[randomIndex] + ".");
								sentences[randomIndex] = " ";
							}
		
						}
							
					} catch(Exception e){
						System.out.println("Indexing error");
					}
				}
				
			}catch(Exception e){
				System.out.println("File reading error");
			}
			
		}
		
		writer.close();
		writer1.close();
	}
	
	public static String cleanSentence(String a){
		
		String cleaned = a;
		
		cleaned = cleaned.replaceAll("_NNPS", "");
		cleaned = cleaned.replaceAll("_NNP", "");
		cleaned = cleaned.replaceAll("_NNS", "");
		cleaned = cleaned.replaceAll("_NN", "");
		
		cleaned = cleaned.replaceAll("_JJR", "");
		cleaned = cleaned.replaceAll("_JJS", "");
		cleaned = cleaned.replaceAll("_JJ", "");
		
		cleaned = cleaned.replaceAll("_RBR", "");
		cleaned = cleaned.replaceAll("_RBS", "");
		cleaned = cleaned.replaceAll("_RB", "");
		cleaned = cleaned.replaceAll("_RP", "");
		
		cleaned = cleaned.replaceAll("_PRP\\$", "");
		cleaned = cleaned.replaceAll("_PDT", "");
		cleaned = cleaned.replaceAll("_POS", "");
		cleaned = cleaned.replaceAll("_PRP", "");
		
		cleaned = cleaned.replaceAll("_VBD", "");
		cleaned = cleaned.replaceAll("_VBG", "");
		cleaned = cleaned.replaceAll("_VBN", "");
		cleaned = cleaned.replaceAll("_VBP", "");
		cleaned = cleaned.replaceAll("_VBZ", "");
		cleaned = cleaned.replaceAll("_VB", "");
		
		cleaned = cleaned.replaceAll("_WP\\$", "");
		cleaned = cleaned.replaceAll("_WRB", "");
		cleaned = cleaned.replaceAll("_WDT", "");
		cleaned = cleaned.replaceAll("_WP", "");
		
		cleaned = cleaned.replaceAll("_SYM", "");
		
		cleaned = cleaned.replaceAll("_CC", "");
		cleaned = cleaned.replaceAll("_CD", "");
		cleaned = cleaned.replaceAll("_DT", "");
		cleaned = cleaned.replaceAll("_EX", "");
		cleaned = cleaned.replaceAll("_FW", "");
		cleaned = cleaned.replaceAll("_IN", "");
		cleaned = cleaned.replaceAll("_LS", "");
		cleaned = cleaned.replaceAll("_MD", "");
		cleaned = cleaned.replaceAll("_TO", "");
		cleaned = cleaned.replaceAll("_UH", "");

		cleaned = cleaned.replaceAll("_, ", "");
		cleaned = cleaned.replaceAll("_: ", "");
	//	cleaned = cleaned.replaceAll("_\\$", "");
	
		cleaned = cleaned.replaceAll("``_``", "");
		cleaned = cleaned.replaceAll("''_''", "");
		cleaned = cleaned.replaceAll("_``",  "");
		cleaned = cleaned.replaceAll("_''","");
		cleaned = cleaned.replaceAll("_#", "");
		cleaned = cleaned.replaceAll("--", "");
		
		return cleaned;
		
	}
	
}
