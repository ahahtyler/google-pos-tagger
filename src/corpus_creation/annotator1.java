package corpus_creation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class annotator1 {

	public static void main (String[] args) throws IOException{
		
		Scanner reader = new Scanner(System.in);

		File taggedCorpusFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Corpus/tagged_corpus.txt");
		File annotationsFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Annotation/annotations.txt");
		File lastSentenceFile = new File("C:/USers/Tyler/Desktop/POS Corrector/Annotation/LastSentence.txt");
		
		String taggedCorpusList = FileUtils.readFileToString(taggedCorpusFile, "UTF-8");
		String annotations = FileUtils.readFileToString(annotationsFile, "UTF-8");
		String lastSentenceString = FileUtils.readFileToString(lastSentenceFile, "UTF-8");
		
		String[] taggedCorpus = taggedCorpusList.split("[\\\n]");
		
		int lastSentence = Integer.parseInt(lastSentenceString);
		int input = -1;
		
		boolean leave = false;
		
		while(!leave){
			
			// Find each phrase phrase in the sentence from the corpus
			String taggedSentence = taggedCorpus[lastSentence];
			String taggedWords[] = taggedSentence.split(" ");
			
			System.out.print(cleanSentence(taggedSentence));
			System.out.println("1:Adj -- 2:Verb -- 3:Other -- 4:Quit");
			
			for(int i = 0; i < taggedWords.length; i++){
				if(taggedWords[i].contains("ing_") && taggedWords[i+1].contains("_N")){
					
					String word1 = taggedWords[i].substring(0, taggedWords[i].indexOf('_'));
					String word2 = taggedWords[i+1].substring(0, taggedWords[i+1].indexOf('_'));
					
					System.out.println("PHRASE: " + word1 + " " + word2);
			
					input = reader.nextInt();
					
					if(input == 1){
						annotations = annotations + "\r\n" + word1 + " " + word2 + " - adj";
					}
					else if(input == 2){
						annotations = annotations + "\r\n" + word1 + " " + word2 + " - verb";
					}
					else if(input == 3){
						annotations = annotations + "\r\n" + word1 + " " + word2 + " - other";
					}
					else{
						
						PrintWriter writer = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Annotation/annotations.txt");
						writer.print(annotations);
						writer.close();
						
						PrintWriter writer1 = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Annotation/LastSentence.txt");
						writer1.print(lastSentence);
						writer1.close();
						// Write annotations to file
						// Write last Sentence interger to file
						leave = true;
						break;
					}					
				}
			}
	
			lastSentence++;
		}
		
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
