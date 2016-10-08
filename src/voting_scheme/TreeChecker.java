package voting_scheme;

import java.util.ArrayList;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class TreeChecker {

    LexicalizedParser lp; 
    boolean userInputFlag;
    
	public void initialize(){
		lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		
	}
	
	public ArrayList<Sentence> createSentenceList(ArrayList<Phrase> phraseList, boolean uif){
		
		ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();
		ArrayList<Phrase> subSetList = new ArrayList<Phrase>();
		String previousSentence ="";
		String currentSentence ="";
		Phrase currPhrase = null;
		userInputFlag = uif;
		
		for(int i = 0; i < phraseList.size(); i++){
			
			System.out.println("Creating Sentence: " + (i+1));
			currPhrase = phraseList.get(i);
			currentSentence = currPhrase.getOriginal();
			
			if(!currentSentence.equals(previousSentence)){
				if(!subSetList.isEmpty()){
					Sentence newSent = new Sentence(subSetList, lp);
					sentenceList.add(newSent);
				}
				subSetList = new ArrayList<Phrase>();
				subSetList.add(currPhrase);
			}
			else{
				subSetList.add(currPhrase);
			}
			
			previousSentence = currentSentence;
		}
		
		//Take care of last sentence
		Sentence newSent = new Sentence(subSetList, lp);
		sentenceList.add(newSent);
		
		return sentenceList;
		
	}
	
	public Sentence findGrammaticalPhrase(Sentence sent){
				
		String[] splitParseTree = sent.getCleanedParseTree().split(" ");
		int phraseCount = 0;
		for(int i = 0; i < splitParseTree.length - 1;i++){
			
			String word1 = sent.getPhraseList().get(phraseCount).getWord1();
			String word2 = sent.getPhraseList().get(phraseCount).getWord2();
			
			if(splitParseTree[i].contains(word1)){
				int tempI = i+1;
				while(splitParseTree[tempI].equals("NP") || splitParseTree[tempI].equals("VP")){
					tempI++;
					if(tempI == splitParseTree.length){
						tempI--;
						break;
					}
				}
			
				if(splitParseTree[tempI].contains(word2)){
					tempI = i;
					boolean breakFlag = true;
					while(breakFlag == true){
						tempI--;
						if(splitParseTree[tempI].equals("NP") || splitParseTree[tempI].equals("VP")){
							sent.setOriginalGrammaticalList(phraseCount, splitParseTree[tempI]);
							sent.setCorrectGrammaticalList(phraseCount,sent.getPhraseList().get(phraseCount).getCorrectResult());
							sent.setMyGrammaticalList(phraseCount, sent.getPhraseList().get(phraseCount).getMyPick());
							phraseCount++;
							breakFlag = false;
						}
					}
				}
			}
			if(phraseCount == sent.getPhraseList().size())
				break;
		}
		return sent;
	}
	
}
