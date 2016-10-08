package voting_scheme;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class driver {

	public static void main (String args[]) throws IOException{

		VotingScheme vs = new VotingScheme();
		TreeChecker tc = new TreeChecker();
		
		vs.initialize();
		tc.initialize();
		
		ArrayList<Phrase> phraseList = vs.assignCategorization();
		ArrayList<Sentence> sentenceList = tc.createSentenceList(phraseList, vs.userInputFlag);
		
		
		for(Sentence sent : sentenceList){	
			sent =  tc.findGrammaticalPhrase(sent);
		}
		
		//Corpus Input
		if(tc.userInputFlag == false){
			
			PrintWriter grammarWriter = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Statistics/grammar_results.txt");
			for(Sentence sent: sentenceList){
				for(int i = 0; i < sent.originalGrammaticalList.length; i++){
					if(sent.getOriginalGrammaticalList(i) == null){
						System.out.println(sent.getOriginalSentenec());
						System.out.println(sent.getCleanedParseTree());
					}
					
					if(!sent.getPhraseList().get(i).getCorrectResult().equals("other"))
						grammarWriter.println(sent.getOriginalGrammaticalList(i));
				}
			}
			grammarWriter.close();
		}
		//User Input
		else{
			
			for(Sentence sent: sentenceList){
				
				System.out.println("Original Sentence: " + sent.getOriginalSentenec() + "\n");
				
				int index = 0; 
				for(Phrase phrase: sent.getPhraseList()){
					
					String gramPhrase = sent.getOriginalGrammaticalList(index);
					String cPhrase = phrase.getWord1() + " " + phrase.getWord2();
					
					System.out.println(" \"" + cPhrase + "\" was classified as " + phrase.getMyPick());
					if(phrase.getHasTieBreaker() == true)
						System.out.println("--- Vote Tie: Historical votes where used");
					System.out.println("--- Verb Votse: " + phrase.getNumberOfVerbVotes());
					System.out.println("--- Adj Votes : " + phrase.getNumberOfAdjVotes());
					
					System.out.println(" \"" + cPhrase + "\" was found in a " + gramPhrase);
					
					if(phrase.getMyPick().equals("adj") && gramPhrase.equals("NP")){
						System.out.println("System agrees with the result: NP\n");	
					}
					else if(phrase.getMyPick().equals("verb") && gramPhrase.equals("VP")){
						System.out.println("System agrees with the result: VP\n");			
					}
					else if(phrase.getMyPick().equals("adj") && gramPhrase.equals("VP")){
						System.out.println("System disagrees with result, change to: NP\n");					
					}
					else if(phrase.getMyPick().equals("verb") && gramPhrase.equals("NP")){
						System.out.println("System disagrees with result, change to: VP\n");
					}
					else
						System.out.println("Error: End of Result Comparison");
					
					index++;
				}
				
				System.out.println("\n" + sent.getPennParseTree());
				
			}
			
			System.out.println();
		}
	}
}