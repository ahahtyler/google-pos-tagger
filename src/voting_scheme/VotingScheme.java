package voting_scheme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class VotingScheme {
	
	final String apiKey1 = "AIzaSyCOqoQEKcUUw8EWo1tYumGIZ79y-RoOj_o";
	final String apiKey2 = "AIzaSyDvdbJio5Q-_Nx7vRYNU9LMp0PFXRQ4O_w";
	final String apiKey3 = "AIzaSyBz8JiA12jTdsLwGhnuMg4i5ZChJYn_9ks";
	final String apiKey4 = "AIzaSyAv3lnpZcT7dwdAwucei0DqrIt2bC30-xs";
	final String apiKey5 = "AIzaSyA1l_zBTq-jf58OW10Mz_eEXc8X42M-fmk";
	final String apiKey6 = "AIzaSyCi-aH-owpAlLxDsA9LGfbM-WOm5b_DR3o";
	final String apiKey7 = "AIzaSyBlHqkPLVCL5SYZr5tx5_NsAsWilx0EsIM";
	final String apiKey8 = "AIzaSyALE2ZEHnmUtrKqyewv7PEdnMQ4RKwhsds";
	final String apiKey9 = "AIzaSyD5qD_IRlQnyqu8snVE1YRD_aN95incwBo";
	final String apiKey10 = "AIzaSyCe4AMXFgG01uAW0GL_UTKHBIh8_cmnXJE";
	final String apiKey11 = "AIzaSyC8y_0miQw36T7-JOI-wrS7RcFvFngyZUQ";
	final String apiKey12 = "AIzaSyBCuCpziq55fCAL20OsRK233wDihXz4zYg";
	final String apiKey13 = "AIzaSyBVUAzap0ejijRRUbPuEA34gL1ZLw8RgqY";		

	final String autofill_link = "http://suggestqueries.google.com/complete/search?client=chrome&q=";
	final String customSearchEngineKey = "015715824305857803777:_skd84j7wog";
	final String searchURL = "https://www.googleapis.com/customsearch/v1?";
	
	int threshold = 0;
	int annotationListIndex = 0; 
	int TEST_INDEX = 0; // ONLY used in checkIfAnnotationWasCorrect method
	int apiKeyCount = 0;
	int apiKeyindex = 0;
	
	double Autofill_Constant = 0.12;
	double GlobalF_PMI = .5;
	double GlobalS_PMI = .3;
	double GlobalA_Score = .3;
	
	PrintWriter writer;
	PrintWriter printWriter;
	PrintWriter histVoteWriter;
	
	MaxentTagger tagger;
	
	File taggedCorpusFile;
	File untaggedCorpusFile;
	File correctAnnotationsFile;
	File databaseFile;
	File historicalFile;
	
	String taggedCorpusList;
	String untaggedCorpusList;
	String correctAnnotationsList;
	String databaseList;
	String historicalList;
	
	String[] apiKeys = new String[] {apiKey1,  apiKey2,  apiKey3,  apiKey4, apiKey5, apiKey6, apiKey7, apiKey8, apiKey9, apiKey10, apiKey11, apiKey12, apiKey13};
	String[] taggedCorpus;
	String[] untaggedCorpus;
	String[] correctAnnotations;
	
	boolean userInputFlag = false; 

	//Wrapper Methods
	public Phrase fetchInformation(Phrase phrase) throws IOException{
		
		//System.out.println(phrase.getWord1() + " " + phrase.getWord2());
		
		if(checkDatabase(phrase)){
			phrase = uploadFromDatabase(phrase);
		}
		else{
			
			System.out.println("NOT FOUND IN DATABASE");
			phrase = findStem(phrase);
	    	phrase = findSingular(phrase);
	        phrase = findWordForms(phrase);	
	    	phrase = findRelatedWords(phrase);
	    	
			phrase = fetchAutofillEquals(phrase);
			phrase = fetchAutofillContains(phrase);
			phrase = fetchAutofillFull(phrase);
		    phrase = fetchSearchResults(phrase);
		    
		    phrase = fetchWikipedia(phrase);
		    phrase = fetchWikiHow(phrase);
		    
		    addToDatabase(phrase);
		    saveDatabase();
		    
		    //stall();
		}
		
		phrase = fetchHistorical(phrase);
		
		return phrase;
	}
	public void initialize() throws IOException{
		printWriter = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Statistics/printed_info.txt");
		histVoteWriter = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Statistics/historical_votes.txt");
		writer = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Statistics/statiscits.txt");
		tagger = new MaxentTagger("taggers/wsj-0-18-bidirectional-nodistsim.tagger");
		
		taggedCorpusFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Corpus/tagged_corpus.txt");
		untaggedCorpusFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Corpus/untagged_corpus.txt");
		correctAnnotationsFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Annotation/annotations.txt");
		databaseFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Database/database.txt");
		historicalFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Database/historical.txt");
		
		taggedCorpusList = FileUtils.readFileToString(taggedCorpusFile);
		untaggedCorpusList = FileUtils.readFileToString(untaggedCorpusFile);
		correctAnnotationsList = FileUtils.readFileToString(correctAnnotationsFile);
		databaseList = FileUtils.readFileToString(databaseFile);
		historicalList = FileUtils.readFileToString(historicalFile);
		
		taggedCorpus = taggedCorpusList.split("[\\n]");
		untaggedCorpus = untaggedCorpusList.split("[\\n]");
		correctAnnotations = correctAnnotationsList.split("\n");

	}
	public Phrase findPatterns(Phrase p){
		
		p = checkWindow(p);
		
		p = checkAutoFillLists(p);
		
		p = checkWikipedia(p);
		
		p = checkWikiHow(p);
		
		return p;
	}
	public ArrayList<Phrase> createPhraseList() throws IOException{
		
		ArrayList<Phrase> phraseList = null;
		
		while(true){
			System.out.println("Would you like to: ");
			System.out.println("     1. Read in a sentence");
			System.out.println("     2. Read in the corpus");
			System.out.print("Enter choice: ");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line = br.readLine();
		    int answer = Integer.parseInt(line);
		  
		    if(answer == 1){
		    	userInputFlag = true;
		    	phraseList = createSentencePhraseObj();
		    	break;
		    }
		    else if(answer == 2){
		    	userInputFlag = false;
		    	phraseList = createCorpusPhraseList();
		    	break;
		    }
		    else{
		    	System.out.println("Invalid Input - Try Aagain");
		    }
		}

		return phraseList;
	}	
	public ArrayList<Phrase> assignCategorization() throws IOException{
		
		ArrayList<Phrase> phraseList = createPhraseList();
		
		int i = 0;	
		for(Phrase phrase : phraseList){
			System.out.println("Assigning info to Phrase: " + (++i));
			//checkIfAnnotationWasCorrect(phrase);
		
			phrase = fetchInformation(phrase);

			phrase = findPatterns(phrase);

			phrase = findNPMI(phrase);

			phrase = vote(phrase);

			print(phrase);
		}
		
		findStatisticPatterns(phraseList);
		//vs.calculatePrecision(phraseList);
		
		writer.close();
		printWriter.close();
		histVoteWriter.close();
		
		return phraseList;
		
	}
	
	//Main Methods
	//--Information Extraction Methods
	public void findStatisticPatterns(ArrayList<Phrase> phraseList){
		//user writer to print
		if(userInputFlag == false){
			for(Phrase phrase : phraseList){
				
				String window = phrase.getWindow();
				String[] windowWords = window.split(" ");
				String firstWord = windowWords[0].toLowerCase().trim();
			
	
				boolean AdjInfront = false;
				boolean INInfront = false;
				boolean VP1Presence = false;
				boolean WikiAuto = false;
				boolean WikiHowPage = false;
				boolean FNPMI1Point = false;
				boolean FNPMI2Point = false;
				boolean FNPMI3Point = false;
				boolean SNPMI1Point = false;
				boolean SNPMI2Point = false;
				boolean AFGreater = false;
				boolean AFLessthan = false;
				boolean PosVerb = false;
				boolean PosAdj = false;
				boolean PosNoun = false;
				boolean WikipeidaRedirect = false;
				
	
				if(firstWord.contains("_jj"))
					AdjInfront = true;
				if(firstWord.contains("_in"))
					INInfront = true;
				if(phrase.getVerbPhrase1Presence() == true)
					VP1Presence = true;
				if(phrase.getEqualsList().contains("wikipedia") || phrase.getContainsList().contains("wikipedia"))
					WikiAuto = true;
				if(phrase.getOnWikiHow())
					WikiHowPage = true;
				if(phrase.getFPMI() >= .7)
					FNPMI1Point = true;
				else if(phrase.getFPMI() >= .5)
					FNPMI2Point = true;
				else if(phrase.getFPMI() >= .3)
					FNPMI3Point = true;
				if(phrase.getSPMI() >= .7)
					SNPMI1Point = true;
				else if(phrase.getSPMI() >= .3)
					SNPMI2Point = true;
				if(phrase.getAutoFillScore() >= 0.3)
					AFGreater = true;
				else
					AFLessthan = true;
				if(phrase.getPOS1().toLowerCase().contains("v"))
					PosVerb = true;
				if(phrase.getPOS1().toLowerCase().contains("j"))
					PosAdj = true;
				if(phrase.getPOS1().toLowerCase().contains("n"))
					PosNoun = true;
				if(phrase.getOnWikipediaRedirect())
					WikipeidaRedirect = true;
	
				if(!phrase.getCorrectResult().contains("other"))
					writer.println(phrase.getWord1() + " " + phrase.getWord2() + "\t" + phrase.getDetPresence() + "\t" + phrase.getOnWikipedia() + "\t" + AdjInfront + "\t" + WikiAuto + "\t" + WikipeidaRedirect + "\t" + 
								   PosAdj + "\t" + PosNoun + "\t" + FNPMI1Point + "\t" + FNPMI2Point + "\t" + FNPMI3Point + "\t" + AFGreater + "\t" + SNPMI1Point + "\t" + 
								   SNPMI2Point + "\t" + PosVerb + "\t" + WikiHowPage + "\t" + INInfront + "\t" + AFLessthan + "\t" + VP1Presence + "\t" + phrase.getHistoricalValue() + "\t" + phrase.getCorrectResult());
		
			}
			System.out.println("-------");
		}
	}
	public void checkIfAnnotationWasCorrect(Phrase phrase){
		
		String phraseInQuestion = correctAnnotations[TEST_INDEX];
		//System.out.println("Correct Annotation " + correctAnnotations[TEST_INDEX]);
		phraseInQuestion = phraseInQuestion.substring(0, phraseInQuestion.indexOf('-'));
		phraseInQuestion = phraseInQuestion.trim();

		if(!phraseInQuestion.equals(phrase.getWord1() + " " + phrase.getWord2())){
			System.out.println("PROBLEM AT: " + TEST_INDEX);
			System.out.println("Original Sentence: " + phrase.getOriginal());
			System.out.println("Tagged Sentence: " + phrase.getTagged());
			System.out.println("Phrase in Question: " + phraseInQuestion);
			System.out.println("Original Phrase: " + phrase.getWord1() + " " + phrase.getWord2());
			stall(10000);
		}
		else{
			System.out.println("Original Sentence: " + phrase.getOriginal());
			System.out.println("Tagged Sentence: " + phrase.getTagged());
			System.out.println("Original Phrase: " + phrase.getWord1() + " " + phrase.getWord2());
		}
		
		TEST_INDEX++;
		
	}
	
	//--Voting Methods
	public Phrase vote(Phrase p){
		
		int adjVote = 0;
		int verbVote = 0;
		
		//Voting for Adj
		if(p.getDetPresence() == true)
			adjVote += 15;
		if(p.getOnWikipedia() == true)
			adjVote += 10;
		if(p.getAdjInfront() == true)
			adjVote += 4;
		if(p.getOnWikipediaRedirect() == true)
			adjVote += 3;
		if(p.getPOS1().toLowerCase().contains("j"))
			adjVote += 2;
		if(p.getPOS1().toLowerCase().contains("n"))
			adjVote += 2;
		if(p.getEqualsList().contains("wikipedia") || p.getContainsList().contains("wikipedia"))
			adjVote+= 1;
		if(p.getFPMI() >= 0.5)
			adjVote += 1;
		if(p.getAutoFillScore() >= 0.3)
			adjVote += 1;
		
		//Voting for Verb
		if(p.getSPMI() >= 0.5)
			verbVote += 3.5;
		else if(p.getSPMI() >= 0.3)
			verbVote += 3;
		if(p.getPOS1().toLowerCase().contains("v"))
			verbVote += 1;
		if(p.getINPresence() == true)
			verbVote += 1;
		if(p.getVerbPhrase1Presence() == true)
			verbVote += 3;
		
		p.setNumberOfAdjVotes(adjVote);
		p.setNumberOfVerbVotes(verbVote);
		
		if(adjVote > verbVote){
			p.setMyPick("adj");
			p.setHasTieBreaker(false);
		}
		else if(verbVote > adjVote){
			p.setMyPick("verb");
			p.setHasTieBreaker(false);
		}
		else if(verbVote == adjVote){
			p = tieBreaker(p);
			p.setHasTieBreaker(true);
		}
		
		return p;
	}
	public Phrase tieBreaker(Phrase p){
		
		//Need to scale down historical votes
		int historicalAdjVotes = p.getHistoricalAdjVotes();
		int historicalVerbVotes = p.getHistoricalVerbVotes();
		
		if(historicalAdjVotes - historicalVerbVotes > 4){
			historicalAdjVotes = 4;
			historicalVerbVotes = 0;
		}
		else if(historicalVerbVotes - historicalAdjVotes > 4){
			historicalAdjVotes = 0;
			historicalVerbVotes = 4;
		}
		
		p.setNumberOfAdjVotes(historicalAdjVotes);
		p.setNumberOfVerbVotes(historicalVerbVotes);
		p.setMyPick(p.getHistoricalValue());
		return p;
	}
	
	//--Calculation Methods
	public Phrase findNPMI(Phrase p){
		
		p = findFrequencies(p);
		
		double apperance = p.getEqualsIndex();
		double word2Length = p.getWord2().length();
		double AutofillScore = (1 - (apperance/word2Length)) * (Autofill_Constant * word2Length);
		
		p.setAutoFillScore(AutofillScore);
		
		double total = p.getWordCount();
		double f_prob_ab = p.getPhraseFrequency()/total;
		double f_prob_a = p.getWord1Frequency()/total;
		double f_prob_b = p.getWord2Frequency()/total;
		
		double s_prob_ab = p.getStemFrequency()/total;
		double s_prob_a = p.getToFrequency()/total;
		double s_prob_b = p.getWord1StemFrequency()/total;
		
		double f_inside_log = f_prob_ab/(f_prob_a * f_prob_b);
		double s_inside_log = s_prob_ab/(s_prob_a * s_prob_b);
		
		double FNPMI = Math.log(f_inside_log)/(-1 * Math.log(f_prob_ab));
		double SNPMI = Math.log(s_inside_log)/(-1 * Math.log(s_prob_ab));
		
		if(Double.isNaN(FNPMI))
			FNPMI = 0;
		if(Double.isNaN(SNPMI))
			SNPMI = 0;
		
		p.setFPMI(FNPMI);
		p.setSPMI(SNPMI);
	
		return p;
	}	
 	public Phrase findFrequencies(Phrase p){

		String searchResults = p.getSearchResults();
		
		String stemmedPhrase = "to " + p.getWord1Stem();
		String stemmedWord = p.getWord1Stem();
		
		String relatedWords = p.getRelatedListWord2().replace(" | ", "|");
		String wordForms = p.getFormsListWord1().replace(" | ", "|");
		String[] relatedWordsList = relatedWords.split("[\\|]");
		String[] wordFormsList = wordForms.split("[\\|]");
		
		String[] updatedRelatedWordsList;
		if(!p.getWord2().equals(p.getWord2Singular())){
			updatedRelatedWordsList = new String[relatedWordsList.length + 2];
			updatedRelatedWordsList[0] = p.getWord2();
			updatedRelatedWordsList[1] = p.getWord2Singular();
			for(int i = 2; i < updatedRelatedWordsList.length; i++){
				updatedRelatedWordsList[i] = relatedWordsList[i-2];
			}
		}
		else{
			updatedRelatedWordsList = new String[relatedWordsList.length + 1];
			updatedRelatedWordsList[0] = p.getWord2();
			for(int i = 1; i < updatedRelatedWordsList.length; i++){
				updatedRelatedWordsList[i] = relatedWordsList[i-1];
			}
		}
		
		ArrayList<String> phrases = new ArrayList<String>();
		
		for(int i = 0; i < wordFormsList.length; i++){
			wordFormsList[i] = wordFormsList[i].trim();
			for(int j = 0; j < updatedRelatedWordsList.length; j++){
				updatedRelatedWordsList[j] = updatedRelatedWordsList[j].trim();
				phrases.add(wordFormsList[i] + " " + updatedRelatedWordsList[j]);
			}
		}
		
		int stemCount = 0;
		int toCount = 0;
		
		int word1Count = 0;
		int word2Count = 0;
		
		int phraseCount = 0;
		int stemPhraseCount = 0;
		
		int totalWordCount = 0;
		
		stemCount = StringUtils.countMatches(searchResults, " " + stemmedWord + " ");
		toCount = StringUtils.countMatches(searchResults, " to ");
		stemPhraseCount = StringUtils.countMatches(searchResults,  " " + stemmedPhrase + " ");
		
		for(int i = 0; i < updatedRelatedWordsList.length; i++){
			word2Count = word2Count + StringUtils.countMatches(searchResults, " " + updatedRelatedWordsList[i] + " ");
		}
		for(int i = 0; i < wordFormsList.length; i++){
			word1Count = word1Count + StringUtils.countMatches(searchResults, " " + wordFormsList[i] + " ");
		}
		for(String i: phrases){
			phraseCount = phraseCount + StringUtils.countMatches(searchResults,  " " + i + " ");
		}
		
		searchResults = searchResults.replace(" | ", " ");
		totalWordCount = searchResults.split(" ").length;
		
		/*System.out.println("stem count: " + stemCount);
		System.out.println("to count: " + toCount);
		System.out.println("stem phrase count: " + stemPhraseCount);
		
		System.out.println("word 1 count: " + word1Count);
		System.out.println("word 2 count: " + word2Count);
		System.out.println("phrase count: " + phraseCount);
		
		System.out.println("total words: " + totalWordCount);
		*/
		p.setStemFrequency(stemPhraseCount);
		p.setWord1StemFrequency(stemCount);
		p.setToFrequency(toCount);
		p.setPhraseFrequency(phraseCount);
		p.setWord1Frequency(word1Count);
		p.setWord2Frequency(word2Count);
		p.setWordCout(totalWordCount);
		
		return p;
	}	
 	public void calculatePrecision(ArrayList<Phrase> phraseList){
 		if(userInputFlag == false){
 			
 		}
 	}
 	
 	//--Historical File Methods
 	public boolean checkHistoricalFile(Phrase p){
 		//String searchValue = p.getWord1() + " " + p.getWord2();
 		String searchValue = p.getWord1();
 		String[] historicalEntries = historicalList.split("[\\\n]");
 		
 		for(int i = 0; i < historicalEntries.length; i++){
 		
 			String[] historicalItems = historicalEntries[i].split("---");
 			
	 		if(historicalItems[0].equals(searchValue))
	 			return true;
 		}
 		
 		return false;
 	}
 	public void addToHistoricalFile(Phrase p) throws FileNotFoundException{
 
 		int adjVal = 0;
 		int verbVal = 0;
 		if(p.getCorrectResult().equals("verb"))
 			verbVal = 1;
 		else if(p.getCorrectResult().equals("adj"))
 			adjVal = 1;
 	
 		//historicalList = historicalList + "\r\n" + p.getWord1().toLowerCase().trim() + " " + p.getWord2().toLowerCase().trim();
 		historicalList = historicalList + "\r\n" + p.getWord1().toLowerCase().trim();
 		historicalList = historicalList + "---" + adjVal;
 		historicalList = historicalList + "---" + verbVal;
 		historicalList = historicalList + "---" + cleanSentence(p.getWindow()).trim() + "|";
 	}
 	public void saveHistoricalFile() throws IOException{
 		PrintWriter HistoricalWriter = null;
 		try{
			HistoricalWriter = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Database/historical.txt");
 		} catch (FileNotFoundException e){
 			e.printStackTrace();
 		}
 		HistoricalWriter.print(historicalList);
 		HistoricalWriter.close();
 		
 		historicalList = FileUtils.readFileToString(historicalFile);
 	}
 	public Phrase uploadFromHistoricalFile(Phrase p){
 		
 		String[] historicalIndexed = historicalList.split("[\\\n]");
 		
 		//String searchValue = p.getWord1() + " " + p.getWord2();
 		String searchValue = p.getWord1();
 		
 		for(int i = 0; i < historicalIndexed.length; i++){
 			String[] historicalEntry = historicalIndexed[i].split("---");
 			
 			if(historicalEntry[0].equals(searchValue)){
 				int adjCount = Integer.parseInt(historicalEntry[1]);
 				int verbCount = Integer.parseInt(historicalEntry[2]);
 				
 				p.setHistoricalAdjVotes(adjCount);
 				p.setHistoricalVerbVotes(verbCount);
 				
 				if(!p.getCorrectResult().equals("other"))
 					histVoteWriter.println(adjCount + "\t" + verbCount);
 				
 				if(adjCount > verbCount)
 					p.setHistoricalValue("adj");
 				else if(verbCount >=  adjCount)
 					p.setHistoricalValue("verb");
 
 			}
 		}
 		
 		return p;
 	}
 	public void updateHistoricalFile(Phrase p) throws IOException{
 		
 		String[] historicalIndexed = historicalList.split("[\\\n]");
 		
 		//String searchValue = p.getWord1() + " " + p.getWord2();
 		String searchValue = p.getWord1();
 		String newEntry = "";
 		String newHistoricalFile = "";
 		
 		for(int i = 0; i < historicalIndexed.length; i++){
 			historicalIndexed[i] = historicalIndexed[i].replace("\r", "").replace("\n", "");
 			//System.out.println(historicalIndexed[i]);
 			String[] historicalEntry = historicalIndexed[i].split("---");
 			
 			if(historicalEntry[0].equals(searchValue) && !historicalIndexed[i].contains(cleanSentence(p.getWindow()))){
 				
 				int adjCount = Integer.parseInt(historicalEntry[1]);
 				int verbCount = Integer.parseInt(historicalEntry[2]);
 				String[] windows = historicalEntry[3].split("[\\|]");
 				
 				String newWindowList = "";
 				for(int j = 0; j < windows.length; j++)
 					newWindowList = newWindowList + windows[j] + "|";
 				newWindowList = newWindowList + cleanSentence(p.getWindow()) + "|";
 				
 				if(p.getCorrectResult().equals("verb"))
 					verbCount++;
 				else if(p.getCorrectResult().equals("adj"))
 					adjCount++;
 				
 				newEntry = p.getWord1() + "---" + adjCount + "---" + verbCount + "---" + newWindowList;
 				//newEntry = p.getWord1() + " " + p.getWord2() + "---" + adjCount + "---" + verbCount + "---" + newWindowList;
 				//System.out.println(newEntry);
 				historicalIndexed[i] =  newEntry;
 			}	
 			
 			if(!historicalIndexed[i].isEmpty())
 				newHistoricalFile = newHistoricalFile + historicalIndexed[i] + "\r\n";
 		}
 		historicalList = newHistoricalFile;
 	}
 	public Phrase fetchHistorical(Phrase p) throws IOException{
 		
 		if(userInputFlag){
 			if(checkHistoricalFile(p) == true)
 				uploadFromHistoricalFile(p);
 			else{
 				p.setHistoricalValue("adj");
 				System.out.println("NOT FOUND IN HISTORIVAL DATABASE");
 			}
 		}
 		else{
	 		if(checkHistoricalFile(p) == true){
	 			updateHistoricalFile(p);
	 		}
	 		else{
	 			System.out.println("NOT FOUND IN HISTORICAL DATABASE");
	 			addToHistoricalFile(p);
	 		}
	 		saveHistoricalFile();
	 		uploadFromHistoricalFile(p);
 		}
 		return p;
 	}
 	
	//--Database Methods
	public boolean checkDatabase(Phrase p){

		String searchValue = p.getWord1().toLowerCase() + " " + p.getWord2().toLowerCase() + "---";
		if(!databaseList.contains(searchValue))
			return false;
		else
			return true;
	}	
	public Phrase uploadFromDatabase(Phrase p){
		
		String[] databaseIndexed = databaseList.split("[\\\n]");
		
		String searchValue = p.getWord1().toLowerCase() + " " + p.getWord2().toLowerCase() + "---";
		
  		for(int j = 0; j < databaseIndexed.length; j++){
			
			if(databaseIndexed[j].contains(searchValue)){
				
				String[] databaseEntry = databaseIndexed[j].split("---");
				
				p.setWord1Stem(databaseEntry[1]);
				p.setWord2Singular(databaseEntry[2]);
				p.setFormsListWord1(databaseEntry[3]);
				p.setRelatedListWord2(databaseEntry[4]);
				p.setEqualsIndex(Integer.parseInt(databaseEntry[5].trim()));
				p.setEqualsList(databaseEntry[6]);
				p.setContainsIndex(Integer.parseInt(databaseEntry[7].trim()));
				p.setContainsList(databaseEntry[8]);
				p.setFullList(databaseEntry[9]);
				p.setSearchResultLinks(databaseEntry[10]);
				p.setSearchResults(databaseEntry[11]);
				p.setWikipediaText(databaseEntry[12]);
				p.setWikiHowText(databaseEntry[13]);
				
			}
		}
	
		return p;
		
	}
	public void saveDatabase() throws IOException{
		PrintWriter DatabaseWriter = null;
		try {
			DatabaseWriter = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Database/database.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DatabaseWriter.print(databaseList);
		DatabaseWriter.close();
		
		databaseList = FileUtils.readFileToString(databaseFile);
	}
	public void addToDatabase(Phrase p) throws FileNotFoundException{
				
		databaseList = databaseList + "\r\n" + p.getWord1().toLowerCase() + " " + p.getWord2().toLowerCase();
		databaseList = databaseList + "---" + p.getWord1Stem();
		databaseList = databaseList + "---" + p.getWord2Singular();
		databaseList = databaseList + "---" + p.getFormsListWord1();
		databaseList = databaseList + "---" + p.getRelatedListWord2();
		databaseList = databaseList + "---" + p.getEqualsIndex();
		databaseList = databaseList + "---" + p.getEqualsList();
		databaseList = databaseList + "---" + p.getContainsIndex();
		databaseList = databaseList + "---" + p.getContainsList();
		databaseList = databaseList + "---" + p.getFullList();
		databaseList = databaseList + "---" + p.getSearchResultLinks();
		databaseList = databaseList + "---" + p.getSearchResults();
		databaseList = databaseList + "---" + p.getWikipediaText();
		databaseList = databaseList + "---" + p.getWikipediaText();
		
	}
	
	//--Google Methods
	public Phrase fetchSearchResults(Phrase p){
			
		String searchString = p.getWord1() + " " + p.getWord2();
    	String url = makeSearchString(searchString, 1, 10);
    	String out = readURL(url);
    	
    	String searchResults = "";
    	String searchLinks = "";
    	
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
	    				jsonItems[j] = jsonItems[j].replace("\\n", "");
	    				jsonItems[j] = jsonItems[j].toLowerCase();
	    				searchResults = searchResults +  jsonItems[j] + " | " ;
	    			}
	    			
	    			if(jsonWords[0].contains("link")){
	    				jsonItems[j] = jsonItems[j].replaceAll("\"", "");
	    				jsonItems[j] = jsonItems[j].replace("...", "");
	    				jsonItems[j] = jsonItems[j].replaceAll("[\\,]", "");
	    				jsonItems[j] = jsonItems[j].replaceAll("  ", " ");
	    				
	    				jsonItems[j] = jsonItems[j].substring(jsonItems[j].indexOf(": ") +2);
	    				jsonItems[j] = jsonItems[j].toLowerCase();
	    				searchLinks = searchLinks + jsonItems[j] + " | ";
	    			}
	    		}
    		}
    	}

    	searchLinks = searchLinks.trim();
    	searchResults = searchResults.trim();
 
		p.setSearchResults(searchResults);
		p.setSearchResultLinks(searchLinks);
	
		return p;
	}
 	public Phrase fetchAutofillFull(Phrase p) throws IOException{
		
 		URL link = null;
		BufferedReader in = null;
	
		String inputLine;
		String autoList = "";
		String cleanedAutoList = "";
		String word1 = p.getWord1().toLowerCase();
		String word2 = p.getWord2().toLowerCase();
		
		link = new URL(autofill_link + word1 + "%20" + word2);
			
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
			p.setFullList(p.getFullList()+autoListElements[j] + " |");
		}
	
		return p;
 	}
	public Phrase fetchAutofillContains(Phrase p) throws IOException{
		
		URL link = null;
		BufferedReader in = null;
			
		int apperance;
		String inputLine;
		String autoList = "";
		String cleanedAutoList = "";
		String word1 = p.getWord1().toLowerCase();
		String word2 = p.getWord2().toLowerCase();
		
		String fullPhraseSing = word1 + " " + p.getWord2Singular().toLowerCase();
		
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
					p.setContainsIndex(apperance);
					for(int k = 0; k < autoListElements.length; k++){
						p.setContainsList(p.getContainsList()+autoListElements[k]+"| ");
					}
					apperance = word2Factorial.length+1;
					break;
				}
			}
		}
		
		return p;
	}
	public Phrase fetchAutofillEquals(Phrase p) throws IOException{
		
		URL link = null;
		BufferedReader in = null;
		
		int apperance;
		String inputLine;
		String autoList = "";
		String cleanedAutoList = "";
		String word1 = p.getWord1().toLowerCase();
		String word2 = p.getWord2().toLowerCase();
		
		String fullPhrasePlur = word1 + " " + word2;
		String fullPhraseSing = word1 + " " + p.getWord2Singular().toLowerCase();
		
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
				
				String[] elementsWords = autoListElements[j].split(" ");
				
				if((autoListElements[j].equals(fullPhraseSing) && j != 0) || autoListElements[j].equals(fullPhrasePlur) || 
				  (autoListElements[j].contains(fullPhraseSing) && elementsWords.length == 2)){
					
					p.setEqualsIndex(apperance);
					for(int k = 0; k < autoListElements.length; k++){
						p.setEqualsList(p.getEqualsList()+autoListElements[k]+"| ");
					}
					apperance = word2Factorial.length+1;
					break;
				}
			}
		}
		
		return p;
	}
	
	//--Stanford Tagger/ WordNet/ WordSmyth Methods
	public Phrase findStem(Phrase p){
		
		System.setProperty("wordnet.database.dir", "C:/Program Files/eclipse/WordNet-3.0/dict/");		
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		
		String s_word = p.getWord1().toLowerCase();
		Synset[] synsets = database.getSynsets(s_word , SynsetType.VERB);
		
		String list = "";
		String stem = "";
		
		if (synsets.length > 0)
		{
			for (int i = 0; i < synsets.length; i++)
			{
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length; j++)
					list = list + wordForms[j] + "|";
			}
			stem = getPopularElement(list.split("[\\|]"));
			p.setWord1Stem(stem);
		}
		else{
			p.setWord1Stem("NA");
		}	
			
		return p;
	}
	public Phrase findRelatedWords(Phrase p) throws IOException{
		
		String word = p.getWord2().trim();
		
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
				if(similarWordLists[i].contains("noun") && similarWordLists[i].contains("definition") && similarWordLists[i].contains("similar words")){
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
								tempList = tempList.replaceAll(",  ", " | ");
								simWordsList = simWordsList + " " + tempList + " | ";
								//System.out.println(tempList);
							}
							else{
								String tempList = definitions[j].substring(definitions[j].indexOf("similar words"));
								tempList = tempList.substring(tempList.indexOf(':')+1);
								tempList = tempList.trim();
								tempList = tempList.replaceAll(",  ", " | ");
								simWordsList = simWordsList + " " + tempList + " | ";
								//System.out.println(tempList);
							}
						}
					}
					simWordsList = simWordsList.trim();
					p.setRelatedListWord2(simWordsList);
					//System.out.println(simWordsList);
				}
			}
			if(p.getRelatedListWord2().equals("")){
				p.setRelatedListWord2("NA");
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
						if(similarWordLists[i].contains("noun") && similarWordLists[i].contains("definition") && similarWordLists[i].contains("similar words:")){
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
										tempList = tempList.replaceAll(",  ", " | ");
										simWordsList = simWordsList + " " + tempList + " | ";
										//System.out.println(tempList);
									}
									else{
										String tempList = definitions[j].substring(definitions[j].indexOf("similar words"));
										tempList = tempList.substring(tempList.indexOf(':')+1);
										tempList = tempList.trim();
										tempList = tempList.replaceAll(",  ", " | ");
										simWordsList = simWordsList + " " + tempList + " | ";
										//System.out.println(tempList);
									}
								}
							}
							simWordsList = simWordsList.trim();
							p.setRelatedListWord2(simWordsList);
							//System.out.println(simWordsList);
						}
					}
					if(p.getRelatedListWord2().equals("")){
						p.setRelatedListWord2("NA");
					}
				}
				else{
					p.setRelatedListWord2("NA3");
					System.out.println("No related List found");
				}
			}
			else{
				p.setRelatedListWord2("NA4");
				System.out.println("No related List found");
			}
		}
	
		return p;
	}
	public Phrase findWordForms(Phrase p) throws IOException{

		String stem = p.getWord1Stem();
		
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
			
			formsList = formsList.replace(" ",  " | ");
			
			p.setFormsListWord1(formsList);
		}
		else{
			
			if(formsList.contains("class=\"word\"")){
				formsList = formsList.substring(formsList.indexOf("class=\"word\""), formsList.indexOf("onclick=\"SaveSearchParam"));				
				formsList = formsList.substring(formsList.indexOf("http"));
				formsList = formsList.replace("\"", "");
				
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
					
					formsList = formsList.replace(" ", " | ");
					
					p.setFormsListWord1(formsList);
					//System.out.println(formsList);
				}
				else{
					p.setFormsListWord1("NA");
					System.out.println("No formsList found");
				}
			}
			else{
				p.setFormsListWord1("NA");
				System.out.println("No formsList found");
			}
		}
	
		return p;
	}
	public Phrase checkAutoFillLists(Phrase p){
		
     	if(p.getEqualsList().toLowerCase().contains(p.getWord1().toLowerCase() + " " + p.getWord2Singular().toLowerCase() + " to ")){
			p.setVerbPhrase1Presence(true);
		}
		
		if(p.getContainsList().toLowerCase().contains(p.getWord1().toLowerCase() + " " + p.getWord2Singular().toLowerCase() + " to ")){
			p.setVerbPhrase1Presence(true);
		}
	
		return p;
	}
	public Phrase findSingular(Phrase p){
		
		System.setProperty("wordnet.database.dir", "C:/Program Files/eclipse/WordNet-3.0/dict/");
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		
		String s_word2 = p.getWord2();
		s_word2 = s_word2.toLowerCase();
		
		char lastChar = s_word2.charAt(s_word2.length()-1);
		if(lastChar == 's'){
			Synset[] synsets = database.getSynsets(s_word2 , SynsetType.NOUN);
			String list = "";
	
			String stem = "";
			
			if (synsets.length > 0)
			{
				for (int i = 0; i < synsets.length; i++)
				{
					String[] wordForms = synsets[i].getWordForms();
					for (int j = 0; j < wordForms.length; j++)
						list = list + wordForms[j] + "|";
				}
				//System.out.println(list);
				list = list.toLowerCase();
				
				stem = getPopularElement(list.split("[\\|]"));
				
				if(stem.contains(" "))
					stem = stem.substring(0, stem.indexOf(" "));
				
				p.setWord2Singular(stem);
				
			}
			else{
				p.setWord2Singular("NA");
			}	
		}
		else{
			p.setWord2Singular(s_word2);
		}
		
		return p;
	}
	public Phrase checkWindow(Phrase p){
			
		String window = p.getWindow();
		String words[] = window.split(" ");
		
		if(words[0].contains("_DT") && !words[0].toLowerCase().contains("all") && !words[0].toLowerCase().contains("those") && !words[0].toLowerCase().contains("both"))
			p.setDetPresence(true);
		
		if(words[0].contains("_IN") && !words[0].contains("of"))
			p.setINPresence(true);
		
		if(words[0].contains("_JJ"))
			p.setAdjInfront(true);
	
		return p;
	}
	public ArrayList<Phrase> createCorpusPhraseList(){
		System.setProperty("wordnet.database.dir", "C:/Program Files/eclipse/WordNet-3.0/dict/");
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		ArrayList<Phrase> phraseList = new ArrayList<Phrase>();
		int cLength = untaggedCorpus.length; 

		for(int i = 0; i < cLength; i++){
			System.out.println("Creating Phrase List: " + (i+1));
			//String taggedSentence = tagger.tagString(untaggedCorpus[i]);
			String taggedSentence = taggedCorpus[i];
			String taggedWords[] = taggedSentence.split(" ");
			
			String taggedSentenceNoPunct = taggedSentence;
			taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(",_," , "");
			taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(":_:" , "");
			taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(";_;" , "");
			String taggedWordsNoPunct[] = taggedSentenceNoPunct.split(" ");
			
			for(int j = 0; j < taggedWords.length; j++){
				
				if(j < (taggedWords.length - 3)){
					if(taggedWords[j].contains("ing_") && taggedWords[j+1].contains("_N") &&
						Character.isLowerCase(taggedWords[j].charAt(0)) && Character.isLowerCase(taggedWords[j+1].charAt(0))){
						
						int dex1 = taggedWords[j].indexOf('_');
						int dex2 = taggedWords[j+1].indexOf('_');
						
						String first = taggedWords[j].substring(0, dex1);
						String second = taggedWords[j+1].substring(0, dex2);
						
						String firstPOS = taggedWords[j].substring(dex1+1);
						String secondPOS = taggedWords[j+1].substring(dex2+1);
						
						first = first.trim();
						second = second.trim();
						
						Synset[] synsets = database.getSynsets(first, SynsetType.VERB);
						
						if(synsets.length > 0 && second.length() > 2){
							
							Phrase p = new Phrase(untaggedCorpus[i]);
							p.setTags(taggedSentence);
							
							p.setWord1(first);
							p.setWord2(second);
							
							p.setPOS1(firstPOS);
							p.setPOS2(secondPOS);
							
							if(j > 0 && j < taggedWordsNoPunct.length - 2){
								p.setWindow(taggedWordsNoPunct[j-1] + " " + taggedWordsNoPunct[j] + " " + taggedWordsNoPunct[j+1] + " " + taggedWordsNoPunct[j+2]);
								p.setWindow(p.getWindow().trim());
							}
							else if(j == 0 && j < taggedWords.length-2){
								p.setWindow(taggedWordsNoPunct[j] + " " + taggedWordsNoPunct[j+1] + " " + taggedWordsNoPunct[j+2]);
								p.setWindow(p.getWindow().trim());
							}
							else if(j == taggedWords.length -2){
								p.setWindow(taggedWordsNoPunct[j-1] + " " + taggedWordsNoPunct[j] + " " + taggedWordsNoPunct[j+1]);
								p.setWindow(p.getWindow().trim());
							}
							
							String phrase = p.getWord1() + " " + p.getWord2();
							String correctAnno = correctAnnotations[annotationListIndex].substring(0, correctAnnotations[annotationListIndex].indexOf('-')-1).trim();
							
							if(!phrase.equals(correctAnno)){
								System.out.println("Phrase: " + phrase);
								System.out.println("Correct Anno: " + correctAnno);
								System.out.println("\n" + untaggedCorpus[i]);
								System.exit(0);
							}
							
							p.setCorrectResult(correctAnnotations[annotationListIndex].substring(correctAnnotations[annotationListIndex].indexOf('-')+1).trim());
							annotationListIndex++;
							
							phraseList.add(p);
							
						}
					}	
				}	
			}	
		}	
		return phraseList;
	}
	public Phrase fetchWikipedia(Phrase p) throws IOException{

		String wikipediaText = "";
		String inputLine = ""; 
		
		String webAddress = "http://en.wikipedia.org/w/api.php?%20format=txt&action=query&titles=" + p.getWord1() + "%20" + p.getWord2() + "&prop=revisions&rvprop=content";
		
		URL link = new URL(webAddress);
		InputStream is = link.openStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		
		while((inputLine = in.readLine()) != null){
			wikipediaText = wikipediaText + inputLine;
		}
		in.close();
		
		wikipediaText = wikipediaText.toLowerCase().replaceAll("\\s+", " ");
		p.setWikipediaText(wikipediaText);
		
		return p;
		
	}
	public Phrase fetchWikiHow(Phrase p) throws IOException{
		
		String WikiHowText = "";
		String inputLine;
		
		String webAddress = "http://www.wikihow.com/Special:LSearch?search=" + p.getWord1() + "+" + p.getWord2();
		
		URL link = new URL(webAddress);
		InputStream is = link.openStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		while((inputLine = in.readLine()) != null){
			WikiHowText = WikiHowText + inputLine;}
		in.close();	
		String target = WikiHowText.replaceAll("<[^>]*>", "");
		WikiHowText = target.toLowerCase();
		if(WikiHowText.contains("results for") && WikiHowText.contains("(function(w,d,s,p,v,e,r)"))
			WikiHowText = WikiHowText.substring(WikiHowText.indexOf("results for"), WikiHowText.indexOf("(function(w,d,s,p,v,e,r)")).replaceAll("\\s+", " ");
		else
			WikiHowText = WikiHowText.replaceAll("\\s+", " ");
		
		String language = "abcdefghijklmnopqrstuvwxyz 1234567890!@#$%^&*()_+=-[]{}\"\\|;:'<>.,/?`~";
		char[] charList = WikiHowText.toCharArray();
    	String newWikiHowText = "";
    	for(int j = 0; j < charList.length; j++){
    		String letter = String.valueOf(charList[j]);
    		if(language.contains(letter)){
    			newWikiHowText = newWikiHowText + charList[j];
    		}
    	}
    	WikiHowText = newWikiHowText;
    	
    	p.setWikiHowText(WikiHowText);
		
		return p;
	}
	public Phrase checkWikipedia(Phrase p){
		
		if(p.getWikipediaText().contains("[missing] => ")){
			p.setOnWikipedia(false);
		}
		else{
			
			if(p.getWikipediaText().contains("#redirect")){
				p.setOnWikiRedirect(true);
			}
			else{
				p.setOnWikipedia(true);
				p.setOnWikiRedirect(false);
			}
		}
		return p;
	}
	public Phrase checkWikiHow(Phrase p){
		
		int phrase1Count = StringUtils.countMatches(p.getWikiHowText(), "how to " + p.getWord1Stem());
		int phrase2Count = StringUtils.countMatches(p.getWikiHowText(), "how to " + p.getWord1Stem() + " " + p.getWord2());
		phrase2Count = phrase2Count + StringUtils.countMatches(p.getWikiHowText(), "how to " + p.getWord1Stem() + " " + p.getWord2Singular());
		p.setOnWikiHow(false);
		
		if(phrase1Count < 1)
			p.setOnWikiHow(true);
		if(phrase2Count < 0)
			p.setOnWikiHow(true);
		
		//System.out.println(phrase1Count + "\t" + phrase2Count + "\t" + p.getCorrectResult());
		return p;
	}
	public ArrayList<Phrase> createSentencePhraseObj() throws IOException{
		
		System.setProperty("wordnet.database.dir", "C:/Program Files/eclipse/WordNet-3.0/dict/");
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		ArrayList<Phrase> phraseList = new ArrayList<Phrase>();
		
		System.out.print("Enter the sentence: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String sentence = br.readLine();
		
		sentence = appendPeriod(sentence);
		
		String taggedSentence = tagger.tagString(sentence);
		String taggedWords[] = taggedSentence.split(" ");
		String taggedSentenceNoPunct = taggedSentence;
		
		taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(",_," , "");
		taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(":_:" , "");
		taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(";_;" , "");
		
		String taggedWordsNoPunct[] = taggedSentenceNoPunct.split(" ");

		for(int j = 0; j < taggedWords.length; j++){
			
			if(j < (taggedWords.length - 2)){
				if(taggedWords[j].contains("ing_") && taggedWords[j+1].contains("_N") &&
					Character.isLowerCase(taggedWords[j].charAt(0)) && Character.isLowerCase(taggedWords[j+1].charAt(0))){
					
					int dex1 = taggedWords[j].indexOf('_');
					int dex2 = taggedWords[j+1].indexOf('_');
					
					String first = taggedWords[j].substring(0, dex1);
					String second = taggedWords[j+1].substring(0, dex2);
					//System.out.println(first + " " + second);
					String firstPOS = taggedWords[j].substring(dex1+1);
					String secondPOS = taggedWords[j+1].substring(dex2+1);
					
					first = first.trim();
					second = second.trim();
					
					Synset[] synsets = database.getSynsets(first, SynsetType.VERB);
					
					if(synsets.length > 0 && second.length() > 2){
						
						Phrase p = new Phrase(sentence);
						p.setTags(taggedSentence);
						
						p.setWord1(first);
						p.setWord2(second);
						
						p.setPOS1(firstPOS);
						p.setPOS2(secondPOS);
						
						if(j > 0 && j < taggedWordsNoPunct.length - 2){
							p.setWindow(taggedWordsNoPunct[j-1] + " " + taggedWordsNoPunct[j] + " " + taggedWordsNoPunct[j+1] + " " + taggedWordsNoPunct[j+2]);
							p.setWindow(p.getWindow().trim());
						}
						else if(j == 0 && j < taggedWords.length-2){
							p.setWindow(taggedWordsNoPunct[j] + " " + taggedWordsNoPunct[j+1] + " " + taggedWordsNoPunct[j+2]);
							p.setWindow(p.getWindow().trim());
						}
						else if(j == taggedWords.length -2){
							p.setWindow(taggedWordsNoPunct[j-1] + " " + taggedWordsNoPunct[j] + " " + taggedWordsNoPunct[j+1]);
							p.setWindow(p.getWindow().trim());
						}
	
						phraseList.add(p);
						
					}
				}	
			}	
		}	
	
		if(phraseList.size() == 0){
			System.out.println("\nERROR: No \"-ing Phrases\" where wound in the sentence. ");
			System.out.println("...Exiting program");
			System.exit(0);
		}
		
		return phraseList;
	}
	
	//--Helper functions
	//Helper Methods
	public void print(Phrase p){
		
		printWriter.println("----------------------------------------------------------------------------------------");
		printWriter.println("Original: " + p.getOriginal().trim());
		printWriter.println("Tagged: " + p.getTagged().trim() + "\n" );
		
		// Initial Information 
		printWriter.format("%10s %11s %10s %10s %10s %14s", "Word1*", "POS1*", "Word2*", "POS2*", "Stemmed*", "Singular*");
		printWriter.println("");
		printWriter.format("%10s %10s %11s %8s  %12s %11s", p.getWord1(), p.getPOS1(), p.getWord2(),  p.getPOS2(), p.getWord1Stem(), p.getWord2Singular());
		printWriter.println();
		printWriter.println();
		printWriter.println("Inflections: " + p.getFormsListWord1());
		printWriter.println("Related Words: " + p.getRelatedListWord2());
		printWriter.println();
		// AUtofill lists
		printWriter.println("Equals - Found in autofill on " + p.getEqualsIndex() + " letter");
		printWriter.println(" --- " + p.getEqualsList());
		printWriter.println("Contains - Found in autofill on " + p.getContainsIndex() + " letter");
		printWriter.println(" --- " + p.getContainsList());
		printWriter.println(" --- Verb Pattern 1 (w1 + w2 + to/from) Present: " + p.getVerbPhrase1Presence());
		printWriter.println("Full List");
		printWriter.println(" --- " + p.getFullList());
		printWriter.println();
		// Window Lists
		printWriter.println("Window: " + p.getWindow());
		printWriter.println(" --- DET Present: "  + p.getDetPresence());
		printWriter.println(" --- IN Present: " + p.getINPresence());
		// Search Results
		printWriter.println();
		printWriter.println("Search Result Links (10)");
		printWriter.println(" --- " + p.getSearchResultLinks());
		printWriter.println("Search Results (10)");
		printWriter.println(" --- " + p.getSearchResults());
		// Word Counts
		printWriter.println("Total words on page: " + p.getWordCount());
		printWriter.println(" --- Word1 Count: " + p.getWord1Frequency());
		printWriter.println(" --- Word2 Count: " + p.getWord2Frequency());
		printWriter.println(" --- Frequency of phrase: " + p.getPhraseFrequency());
		printWriter.println(" --- To Count: " + p.getToFrequency());
		printWriter.println(" --- Stem Count " + p.getWord1StemFrequency());
		printWriter.println(" --- Frequency of Stem phrase: " + p.getStemFrequency());
		// NPMI
		printWriter.println();
		printWriter.println("NPMI Scores");
		//PMI Info
		printWriter.println(" --- Phrase NPMI: " + p.getFPMI());
		printWriter.println(" --- Stem NPMI: " + p.getSPMI());
		printWriter.println(" --- Autofill Score:  " + p.getAutoFillScore());
		//printWriter.println();
	
		printWriter.println();
		printWriter.println("Wikipedia Text:");
		printWriter.println(" --- " + p.getWikipediaText());
		printWriter.println("WikiHow Text:");
		printWriter.println(" --- " + p.getWikiHowText());
		
		printWriter.println("Votes");
		printWriter.println(" --- Adj Votes : " + p.getNumberOfAdjVotes());
		printWriter.println(" --- Verb Votes: " + p.getNumberOfVerbVotes());
		printWriter.println(" --- Historical Adj Votes : " + p.getHistoricalAdjVotes());
		printWriter.println(" --- Historical Verb Votes: " + p.getHistoricalVerbVotes());
		
		printWriter.println("Correct Tag: " + p.getCorrectResult());
		printWriter.println("My Pick: " + p.getMyPick());
		printWriter.println();	
		
	}
	public String getPopularElement(String[] a){
		  int count = 1, tempCount;
		  String popular = a[0];
		  String temp = "";
		  for (int i = 0; i < (a.length - 1); i++){
			    temp = a[i];
			    tempCount = 0;
			    for (int j = 1; j < a.length; j++){
				      if (temp.equals(a[j]))
				    	  tempCount++;
			    }
			    if (tempCount > count){
				      popular = temp;
				      count = tempCount;
			    }
		  }
		  return popular;
	}
	public void stall(){
		
		Random rand = new Random();
		int randomNumber = rand.nextInt(10+1);
		try{
			Thread.sleep(randomNumber * 1000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
	}
	public void stall(int seconds){
		
		try{
			Thread.sleep(seconds * 1000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
	}
	public String makeSearchString (String qSearch, int start, int numOfResults){
		
		String toSearch = "";
		
		if(apiKeyCount > 99){
			apiKeyCount = 0;
			apiKeyindex++;
			toSearch = searchURL + "key=" +  apiKeys[apiKeyindex] + "&cx=" + customSearchEngineKey + "&q=";
			
		}
		else{
			apiKeyCount++;
			toSearch = searchURL + "key=" + apiKeys[apiKeyindex] + "&cx=" + customSearchEngineKey + "&q=";
		}
		System.out.println("API Key Index: " + apiKeyindex);
		System.out.println("API key Count: " + apiKeyCount);
		
		String keys[] = qSearch.split("[ ]+");
		
		for(String key:keys){
			toSearch += key + "+";
		}
		
		toSearch+="&alt=json";
		
		toSearch+="&start="+start;
		
		toSearch+="&num="+numOfResults;
		
		return toSearch;
	}
	public String readURL(String pUrl)
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
	    	//stall(120);
	    	
	    	try{
		        URL url=new URL(pUrl);
		        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        String line;
		        StringBuffer buffer=new StringBuffer();
		        while((line=br.readLine())!=null){
		            buffer.append(line);
		        }
		        return buffer.toString();
	    	}catch(Exception f){
		    	System.out.println("Google Timed Out");
		        f.printStackTrace();
	    	}
	   }
	    return null;
	}
	public String cleanSentence(String a){
		
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
	public void tagTheCorpus(){
		
		int cLength = untaggedCorpus.length; 
		
		for(int i = 0; i < cLength; i++){
			System.out.println(i);
			String taggedSentence = tagger.tagString(untaggedCorpus[i]);
			writer.println(taggedSentence);
		}
		
	}
	public void pruneDatabase(){
		String database[] = databaseList.split("[\\n]");
		
		int dlength = database.length;
		
		for(int i = 0; i < dlength; i++){
			System.out.println(i+1);
			String phrase = database[i].substring(0, database[i].indexOf('-'));
			
			if(correctAnnotationsList.contains(phrase.toLowerCase().trim())){
				
				writer.println(database[i]);
				
			}
			
		}
	}
	public String appendPeriod(String sentence){
		
		String[] words = sentence.split(" ");
		int len = words.length-1;
		if(!words[len].contains(".") && !words[len].contains("?") && !words[len].contains("!")){
			words[len] = words[len] + ".";
		}
		
		String fixed = "";
		for(int i = 0; i < words.length; i++)
			fixed = fixed + words[i] + " ";
		fixed = fixed.trim();
		
		return fixed;
	}
}
