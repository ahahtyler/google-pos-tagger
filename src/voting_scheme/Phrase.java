package voting_scheme;

public class Phrase {

	public String o_sentence;					// Original Sentence
	public String t_sentence;					// Tagged sentence
	
	public String a_list = "";					// List of autofill words - equals
	public String f_list = "";					// List of text from Google API
	public String c_list = "";					// List of autofill words - contains
	public String full_list = "";				// List of autofill words - entire phrase
	
	public String stem = "";					// Stemmed version of word 1
	public String word1 = "";					// First word (ing word)
	public String word2 = "";					// Second word
	public String s_word2 = "";					// Singular form of second word
	
	public String pos_1 = "";					// Part of speech for first word
	public String pos_2 = "";					// Part of speech for second word

	public String correct = "";					// Correct answer
	
	public int stem_count;						// Occurances of "to stemmed-version"
	public int freq_count;						// Occurances of phrase
	public int auto_list_index;					// Index at which word apperas in autofill list - equals
	public int auto_list_contains;				// Index at which word appears in autofill list - contains
	public int wordCount;						// Number of words on first search page
	
	public int fwordCount;						// Frequency of first word 
	public int swordCount;						// Frequency of second word
	public int stemCount;						// Frequency of stem word
	public int towordCount;						// Frequency of 'to'
	public int verbVote;						// Number of votes given verb
	public int adjVote;							// Number of cotes given to adj
	
	public double FPMI;							// PMI for frequency
	public double SPMI;							// PMI for stem
	public double AF;							// Stat for autofill
	
	public boolean verbPhrase1;					// Returns true if phrase (w1 + " " + w1 + " " + to) appears in autofill list
	public boolean foundDet;					// Returns true if a determiner is within the scope of the phrase
	public boolean foundIN;						// Returns true if a prepositions is within the scope of the phrases
	public boolean foundAdj;					// Returns true if an adjective is infron of the first word in the phrase
	public String window = "";					// The TAGGED window that the phrase is in
	
	public String myPick = ""; 					// Answer that I think is correct

	public String formsListWord1 = "";			// List of alternate word forms for word 1
	public String relatedListWord2 = ""; 		// List of related words for word 2
	
	public String searchResultLinks = ""; 		//Links found in search results
	
	public boolean onWikipediaRedirect;			//Returns true if the phrase has a redirected page on wikipedia
	public boolean onWikipedia;				    //Returns true if the phrase has content on wikipedia
	public boolean onWikiHow;				    //Returns true if the phrase has content on wikipedia
	public boolean hasTie;						//Returns true if the phrase has equal votes
	
	public String wikipediaString = "";	  		//The text from the Wikipedia API
	public String wikiHowString = "";			//The text from the WikiHow page
	
	public String historicalValue = "";			//Returns historically what word 1 is
	public int historicalAdjVote = 0;			//Returns historical number of adj vovtes given to phrase
	public int historicalVerbVote = 0;			//Returns histoial number of verb votes given to phrase
	
	public Phrase(String original){
		o_sentence = original;
	}
	
	public void setTags(String t){t_sentence = t;}
	public void setSearchResults(String flist){f_list = flist;}
	public void setEqualsList(String alist){a_list = alist;}
	public void setContainsList(String clist){c_list = clist;}
	public void setFullList(String fullist){full_list = fullist;}
	public void setWord1Stem(String s){stem = s;}
	public void setWord1(String w1){word1 = w1;}
	public void setWord2(String w2){word2 = w2;}
	public void setWord2Singular(String sw2){s_word2 = sw2;}
	public void setPOS1(String pos1){pos_1 = pos1;}
	public void setPOS2(String pos2){pos_2 = pos2;}
	public void setCorrectResult(String out){correct = out;}
	public void setStemFrequency(int stem){stem_count = stem;}
	public void setPhraseFrequency(int c){freq_count = c;}
	public void setEqualsIndex(int dex ){auto_list_index = dex;}
	public void setContainsIndex(int dex){auto_list_contains=dex;}
	public void setWordCout(int dex){wordCount = dex;}
	public void setWord1Frequency(int dex){fwordCount = dex;}
	public void setWord2Frequency(int dex){swordCount = dex;}
	public void setToFrequency(int dex){towordCount = dex;}
	public void setWord1StemFrequency(int dex){stemCount = dex;}
	public void setNumberOfVerbVotes(int dex){verbVote = dex;}
	public void setNumberOfAdjVotes(int dex){adjVote = dex;}
	public void setFPMI(double dex){FPMI = dex;}
	public void setSPMI(double dex){SPMI = dex;}
	public void setAutoFillScore(double dex){AF = dex;}	
	public void setVerbPhrase1Presence(boolean dex){verbPhrase1 = dex;}
	public void setDetPresence(boolean dex){foundDet = dex;}
	public void setINPresence(boolean dex){foundIN = dex;}
	public void setWindow(String dex){window = dex;}
	public void setMyPick(String dex){myPick = dex;}
	public void setFormsListWord1(String out){formsListWord1 = out;}
	public void setRelatedListWord2(String out){relatedListWord2 = out;}
	public void setSearchResultLinks(String out){searchResultLinks = out;}
	public void setOnWikipedia(boolean dex){onWikipedia = dex;}
	public void setOnWikiHow(boolean dex){onWikiHow = dex;}
	public void setWikipediaText(String out){wikipediaString = out;}
	public void setWikiHowText(String out){wikiHowString = out;}
	public void setOnWikiRedirect(boolean dex){onWikipediaRedirect = dex;}
	public void setHistoricalValue(String out){historicalValue = out;}
	public void setAdjInfront(boolean dex){foundAdj = dex;}
	public void setHistoricalAdjVotes(int dex){historicalAdjVote = dex;}
	public void setHistoricalVerbVotes(int dex){historicalVerbVote = dex;}
	public void setHasTieBreaker(boolean dex){hasTie = dex;}
	
	public String getOriginal(){return o_sentence;}
	public String getTagged(){return t_sentence;}
	public String getSearchResults(){return f_list;}
	public String getEqualsList(){return a_list;}
	public String getContainsList(){return c_list;}
	public String getFullList(){return full_list;}
	public String getWord1Stem(){return stem;}
	public String getWord1(){return word1;}
	public String getWord2(){return word2;}
	public String getWord2Singular(){return s_word2;}
	public String getPOS1(){return pos_1;}
	public String getPOS2(){return pos_2;}
	public String getCorrectResult(){return correct;}
	public int getStemFrequency(){return stem_count;}
	public int getPhraseFrequency(){return freq_count;}
	public int getEqualsIndex(){return auto_list_index;}
	public int getContainsIndex(){return auto_list_contains;}
	public int getWordCount(){return wordCount;}
	public int getWord1Frequency(){return fwordCount;}
	public int getWord2Frequency(){return swordCount;}
	public int getToFrequency(){return towordCount;}
	public int getWord1StemFrequency(){return stemCount;}
	public int getNumberOfVerbVotes(){return verbVote;}
	public int getNumberOfAdjVotes(){return adjVote;}
	public double getFPMI(){return FPMI;}
	public double getSPMI(){return SPMI;}
	public double getAutoFillScore(){return AF;}
	public boolean getVerbPhrase1Presence(){return verbPhrase1;}
	public boolean getDetPresence(){return foundDet;}
	public boolean getINPresence(){return foundIN;}
	public String getWindow(){return window;}
	public String getMyPick(){return myPick;}
	public String getFormsListWord1(){return formsListWord1;}
	public String getRelatedListWord2(){return relatedListWord2;}
	public String getSearchResultLinks(){return searchResultLinks;}
	public boolean getOnWikipedia(){return onWikipedia;}
	public boolean getOnWikiHow(){return onWikiHow;}
	public String getWikipediaText(){return wikipediaString;}
	public String getWikiHowText(){return wikiHowString;}
	public boolean getOnWikipediaRedirect(){return onWikipediaRedirect;}
	public String getHistoricalValue(){return historicalValue;}
	public boolean getAdjInfront(){return foundAdj;}
	public int getHistoricalAdjVotes(){return historicalAdjVote;}
	public int getHistoricalVerbVotes(){return historicalAdjVote;}
	public boolean getHasTieBreaker(){return hasTie;}

}

