package test_methods;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FileUtils;

import voting_scheme.Phrase;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class ReadInInformation {

	static File databaseFile;
	static String databaseList;
	static MaxentTagger tagger;
	
	public static void main (String args[]) throws IOException{
		
		PrintWriter writer = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Statistics/statiscits.txt");
		File untaggedCorpusFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Corpus/untagged_corpus.txt");
		File correctAnnotationsFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Annotation/annotations.txt");
		databaseFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Database/database.txt");
		tagger = new MaxentTagger("taggers/wsj-0-18-bidirectional-nodistsim.tagger");
		
		String untaggedCorpusList = FileUtils.readFileToString(untaggedCorpusFile);
		String correctAnnotationsList = FileUtils.readFileToString(correctAnnotationsFile);
		databaseList = FileUtils.readFileToString(databaseFile);
		String[] untaggedCorpus = untaggedCorpusList.split("[\\.]");
		String[] correctAnnotations = correctAnnotationsList.split("[\\.]");
		

		ArrayList<Phrase> phraseList = createPhraseList(untaggedCorpus);
		
		for(Phrase i : phraseList){
			//Do stuff here
		
		}
		
	}
	
	public static ArrayList<Phrase> createPhraseList(String[] untaggedCorpus){
		ArrayList<Phrase> phraseList = new ArrayList<Phrase>();
		int corpusLength = untaggedCorpus.length; corpusLength = 5;
		
		for(int i = 0; i < corpusLength; i++){
			String taggedSentence = tagger.tagString(untaggedCorpus[i]);
			
			String taggedWords[] = taggedSentence.split(" ");
			
			for(int j = 0; j < taggedWords.length; j++){
				
				if(j < (taggedWords.length - 3)){
					if(taggedWords[j].contains("ing_") && taggedWords[j+1].contains("_N")){
						
						Phrase p = new Phrase(untaggedCorpus[i]);
						p.setTags(taggedSentence);
						
						int dex1 = taggedWords[j].indexOf('_');
						int dex2 = taggedWords[j+1].indexOf('_');
						
						String first = taggedWords[j].substring(0, dex1);
						String second = taggedWords[j+1].substring(0, dex2);
						
						p.setWord1(first.trim().toLowerCase());
						p.setWord2(second.trim().toLowerCase());
						
						String firstPOS = taggedWords[j].substring(dex1+1);
						String secondPOS = taggedWords[j+1].substring(dex2+1);
						
						p.setPOS1(firstPOS);
						p.setPOS2(secondPOS);
						
						phraseList.add(p);
					}	
				}	
			}	
		}	
		return phraseList;
	}
	
	
}
