package voting_scheme;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

public class Sentence {

	public String originalSentence;
	public ArrayList<Phrase> sameSentenceList;
	public String parseTree;
	public String pennParseTree;
	public String cleanedParseTree;
	public String[] myGrammaticalList;
	public String[] correctGrammaticalList;
	public String[] originalGrammaticalList;
	
	public Sentence(ArrayList<Phrase> phraseList, LexicalizedParser lp){
		originalSentence = phraseList.get(0).getOriginal();
		sameSentenceList = phraseList;
		
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(originalSentence));
	    List<CoreLabel> rawWords2 = tok.tokenize();
	    Tree parse = lp.apply(rawWords2);
	    parseTree = parse.toString();
	    cleanedParseTree = cleanTree(parseTree);
	    pennParseTree = parse.pennString().replaceAll("\\[.*\\]", "");
	    
	    myGrammaticalList = new String[phraseList.size()];
	    correctGrammaticalList = new String[phraseList.size()];
	    originalGrammaticalList = new String[phraseList.size()];
	    
	}
	
	public String getOriginalSentenec(){return originalSentence;}
	public ArrayList<Phrase> getPhraseList(){return sameSentenceList;}
	public String getParseTree(){return parseTree;}
	public String getCleanedParseTree(){return cleanedParseTree;}
	public String getOriginalGrammaticalList(int i){return myGrammaticalList[i];}
	public String getMyGrammaticalList(int i){return correctGrammaticalList[i];}
	public String getCorrectGrammaticalList(int i){return originalGrammaticalList[i];}
	public String getPennParseTree(){return pennParseTree;}
	
	public void setOriginalGrammaticalList(int i, String ans){myGrammaticalList[i] = ans;}
	public void setMyGrammaticalList(int i, String ans){correctGrammaticalList[i] = ans;}
	public void setCorrectGrammaticalList(int i, String ans){originalGrammaticalList[i] = ans;}

	public String cleanTree(String PT){
		
		String cleaned = PT;
		
		cleaned = cleaned.replace("ROOT ", "");
		cleaned = cleaned.replace("NX ", "");
		cleaned = cleaned.replace("ADJP ", "");
		cleaned = cleaned.replace("ADVP ", "");
		cleaned = cleaned.replace("WHNP ", "");
		cleaned = cleaned.replace("UCP ", "");
		cleaned = cleaned.replace("WHADVP ", "");
		cleaned = cleaned.replace("FRAG ", "");

		cleaned = cleaned.replace("SBAR ", "");
		cleaned = cleaned.replace("PP ", "");

		
		cleaned = cleaned.replace("NNPS ", "");
		cleaned = cleaned.replace("NNP ", "");
		cleaned = cleaned.replace("NNS ", "");
		cleaned = cleaned.replace("NN ", "");
		
		cleaned = cleaned.replace("JJR ", "");
		cleaned = cleaned.replace("JJS ", "");
		cleaned = cleaned.replace("JJ ", "");
		
		cleaned = cleaned.replace("PRP$ ", "");
		cleaned = cleaned.replace("PDT ", "");
		cleaned = cleaned.replace("POS ", "");
		cleaned = cleaned.replace("PRP ", "");
		
		cleaned = cleaned.replace("VBD ", "");
		cleaned = cleaned.replace("VBG ", "");
		cleaned = cleaned.replace("VBN ", "");
		cleaned = cleaned.replace("VBP ", "");
		cleaned = cleaned.replace("VBZ ", "");
		cleaned = cleaned.replace("VB ", "");
		
		cleaned = cleaned.replace("WP$ ", "");
		cleaned = cleaned.replace("WRB ", "");
		cleaned = cleaned.replace("WDT ", "");
		cleaned = cleaned.replace("WP ", "");
		
		cleaned = cleaned.replace("RBR ", "");
		cleaned = cleaned.replace("RBS ", "");
		cleaned = cleaned.replace("RB ", "");
		cleaned = cleaned.replace("SYM ", "");
		
		cleaned = cleaned.replace("CC ", "");
		cleaned = cleaned.replace("CD ", "");
		cleaned = cleaned.replace("DT ", "");
		cleaned = cleaned.replace("EX ", "");
		cleaned = cleaned.replace("FW ", "");
		cleaned = cleaned.replace("IN ", "");
		cleaned = cleaned.replace("LS ", "");
		cleaned = cleaned.replace("MD ", "");
		cleaned = cleaned.replace("TO ", "");
		cleaned = cleaned.replace("UH ", "");
		cleaned = cleaned.replace("QP ", "");
		cleaned = cleaned.replace("NX ", "");
		cleaned = cleaned.replace("S ", "");
		cleaned = cleaned.replace(", ", "");
		cleaned = cleaned.replace(": ", "");
	
		cleaned = cleaned.replace("`` ``", "");
		cleaned = cleaned.replace("'' ''", "");
		cleaned = cleaned.replace("``",  "");
		cleaned = cleaned.replace("''","");
		cleaned = cleaned.replace("#", "");
		cleaned = cleaned.replace("--", "");
		
		cleaned = cleaned.replace("(", "");
		cleaned = cleaned.replace(")", "");
		cleaned = cleaned.replace(". .", "");
		
		
		return cleaned;
	}
	
}
