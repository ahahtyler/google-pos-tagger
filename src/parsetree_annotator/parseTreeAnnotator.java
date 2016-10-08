package parsetree_annotator;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.io.FileUtils;

import annotator.annotator;
import voting_scheme.Phrase;
import voting_scheme.Sentence;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.Color;


public class parseTreeAnnotator extends JFrame {

	private static final long serialVersionUID = 1L;
	public parseTreeAnnotator(){
		getContentPane().setLayout(null);
		
		JButton btnAdj = new JButton("Noun Phrase");
		btnAdj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				input = 1;
				System.out.println(input);
			}
		});
		
		btnAdj.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAdj.setBounds(10, 220, 140, 66);
		getContentPane().add(btnAdj);
		
		JButton btnVerb = new JButton("Verb Phrase");
		btnVerb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = 2;
			}
		});
		
		btnVerb.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnVerb.setBounds(153, 220, 140, 66);
		getContentPane().add(btnVerb);
		
		JButton btnOther = new JButton("Other");
		btnOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = 3;
			}
		});
		
		btnOther.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnOther.setBounds(296, 220, 140, 66);
		getContentPane().add(btnOther);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = 4;
			}
		});
		
		btnQuit.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnQuit.setBounds(439, 220, 140, 66);
		getContentPane().add(btnQuit);
		
		parseTreePane = new JTextPane();
		parseTreePane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		parseTreePane.setBounds(10, 11, 660, 200);
		getContentPane().add(parseTreePane);
		
		phrasePane = new JTextPane();
		phrasePane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		phrasePane.setBounds(155, 126, 218, 50);
		phrasePane.setForeground(Color.blue);
		//getContentPane().add(phrasePane);
		
		numberPane = new JTextPane();
		numberPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		numberPane.setBounds(582, 220,87 ,66);
		getContentPane().add(numberPane);
		
		annotationResult = new JTextPane();
		annotationResult.setFont(new Font("Tahoma", Font.PLAIN, 15));
		annotationResult.setBounds(10, 290,180 ,50);
		getContentPane().add(annotationResult);
		
		pennTreePane = new JTextPane();
		pennTreePane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pennTreePane.setBounds(677, 11, 300, 490);
		slider = new JScrollPane(pennTreePane);
		slider.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		slider.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		slider.setBounds(677, 11, 400, 490);
		getContentPane().add(slider);

	}
	
	static JScrollPane slider;
	static JTextPane numberPane;
	static JTextPane phrasePane;
	static JTextPane parseTreePane;
	static JTextPane annotationResult;
	static JTextPane pennTreePane;
	static int input;
	
	public static void main (String[] args) throws IOException{
		
		parseTreeAnnotator frame = new parseTreeAnnotator();
		frame.setSize(1100, 550);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		File taggedCorpusFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Corpus/tagged_corpus.txt");
		File phraseAnnotationsFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Annotation/phrase_tree_annotations.txt");
		File lastSentenceFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Annotation/LastSentence.txt");
		File untaggedCorpusFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Corpus/untagged_corpus.txt");
		File annotationsFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Annotation/annotations.txt");
		File lastAnnotationFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Annotation/LastAnnotation.txt");
		
		String taggedCorpusList = FileUtils.readFileToString(taggedCorpusFile, "UTF-8");
		String phraseAnnotations = FileUtils.readFileToString(phraseAnnotationsFile, "UTF-8");
		String lastSentenceString = FileUtils.readFileToString(lastSentenceFile, "UTF-8");
		String untaggedCorpusList = FileUtils.readFileToString(untaggedCorpusFile, "UTF-8");
		String annotationsList = FileUtils.readFileToString(annotationsFile, "UTF-8");
		String lastAnnotationList = FileUtils.readFileToString(lastAnnotationFile, "UTF-8");
		
		int lastSentence = Integer.parseInt(lastSentenceString);
		int lastAnnotation = Integer.parseInt(lastAnnotationList);
		
		String[] taggedCorpus = taggedCorpusList.split("[\\\n]");
		String[] untaggedCorpus = untaggedCorpusList.split("[\\n]");
		String[] annotations = annotationsList.split("[\\n]");
		
		System.setProperty("wordnet.database.dir", "C:/Program Files/eclipse/WordNet-3.0/dict/");
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");

		int cLength = taggedCorpus.length;
		
		boolean leave = false;
		int globalAnnotationCounter = lastAnnotation;
		int globaldisplay = lastAnnotation;
		int currentRun = 0;
		while(!leave){
			for(int i = lastSentence; i < cLength; i++){
				ArrayList<Phrase> phraseList = new ArrayList<Phrase>();
				
				String taggedSentence = taggedCorpus[i];
				String taggedWords[] = taggedSentence.split(" ");
				
				String taggedSentenceNoPunct = taggedSentence;
				taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(",_,", "");
				taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(";_;", "");
				taggedSentenceNoPunct = taggedSentenceNoPunct.replaceAll(":_:", "");
					
				currentRun = 0;
				
				for(int j = 0; j < taggedWords.length; j++){
					
					if(j < taggedWords.length -3){
						if(taggedWords[j].contains("ing_") && taggedWords[j+1].contains("_N")&&
						   Character.isLowerCase(taggedWords[j].charAt(0)) && Character.isLowerCase(taggedWords[j+1].charAt(0))){
							
							int dex1 = taggedWords[j].indexOf('_');
							int dex2 = taggedWords[j+1].indexOf('_');
							
							String first = taggedWords[j].substring(0, dex1);
							String second = taggedWords[j+1].substring(0, dex2);
							
							first = first.trim();
							second = second.trim();
							
							Synset[] synsets = database.getSynsets(first,SynsetType.VERB);
							
							if(synsets.length > 0 && second.length() > 2){
								Phrase p = new Phrase(untaggedCorpus[i]);
								p.setWord1(first);
								p.setWord2(second);

								String correctPhrase = annotations[globalAnnotationCounter].substring(0, annotations[globalAnnotationCounter].indexOf('-')-1).trim();
								
								if(!correctPhrase.equals(first + " " + second)){
									
									parseTreePane.setText(first + " " + second + "\n" + correctPhrase);
									stall(10);
								}
								
								String correctResult = annotations[globalAnnotationCounter].substring(annotations[globalAnnotationCounter].indexOf('-')+1).trim();
								
								p.setCorrectResult(correctResult);
								globalAnnotationCounter++;
								currentRun++;
								phraseList.add(p);
							}
						}
					}
				}
				
				Sentence sent = new Sentence(phraseList, lp);
				pennTreePane.setText(sent.getPennParseTree());
				String[] splitParseTree = sent.getCleanedParseTree().split(" ");
				int phraseCount = 0;
				for(int k = 0; k < splitParseTree.length - 1; k++){
					String word1 = sent.getPhraseList().get(phraseCount).getWord1();
					String word2 = sent.getPhraseList().get(phraseCount).getWord2();
					
					if(splitParseTree[k].contains(word1)){
						
						int tempK = k+1;
						while(splitParseTree[tempK].equals("NP") || splitParseTree[tempK].equals("VP")){
							tempK++;
							if(tempK == splitParseTree.length){
								tempK--;
								break;
							}
						}
						
						if(splitParseTree[tempK].contains(word2)){
							phraseCount++;
							//HERE
							annotationResult.setText(annotations[globaldisplay]);
							globaldisplay++;
							int tempI = k;
							boolean breakFlag = true;
							
							while(breakFlag == true){
								tempI--;
								if(splitParseTree[tempI].equals("NP") || splitParseTree[tempI].equals("VP")){
									breakFlag = false;
								}
							}
							
							String firstPartTree = "";
							String firstPartTree2 = "";
							String middlePartTree = "";
							String endPartTree = "";
							
							String word11 = splitParseTree[k];
							String word21 = splitParseTree[tempK];
							String grammarPhrase = splitParseTree[tempI];
							
							for(int z = 0; z < tempI-1; z++)
								firstPartTree = firstPartTree + " " + splitParseTree[z];
							
							for(int z = tempI + 1; z < k; z++)
								firstPartTree2 = firstPartTree2 + " " + splitParseTree[z];
							
							for(int z = k + 1; z < tempK; z++)
								middlePartTree = middlePartTree + " " + splitParseTree[z];
							
							for(int z = tempK + 1; z < splitParseTree.length; z++)
								endPartTree = endPartTree + " " + splitParseTree[z]; 
							
							System.out.println(sent.cleanedParseTree);
							System.out.println(firstPartTree);
							System.out.println(middlePartTree);
							System.out.println(endPartTree);
							System.out.println(word11 + " " + word21);
							parseTreePane.setText("");
							StyledDocument doc = parseTreePane.getStyledDocument();
							MutableAttributeSet style = parseTreePane.addStyle("I'm a Style", null);
							StyleConstants.setForeground(style,  Color.black);
							
							try{doc.insertString(doc.getLength(),  firstPartTree,  style);}
							catch(BadLocationException e){}
							
							StyleConstants.setForeground(style, Color.magenta);
							
							try{doc.insertString(doc.getLength(),  " " + grammarPhrase,  style);}
							catch(BadLocationException e){}
							
							StyleConstants.setForeground(style, Color.black);
							
							try{doc.insertString(doc.getLength(),  firstPartTree2,  style);}
							catch(BadLocationException e){}
							
							StyleConstants.setForeground(style, Color.blue);
							
							try{doc.insertString(doc.getLength(),  " " + word11 + " ",  style);}
							catch(BadLocationException e){}
							
							StyleConstants.setForeground(style, Color.black);
							
							try{doc.insertString(doc.getLength(),  middlePartTree,  style);}
							catch(BadLocationException e){}
							
							StyleConstants.setForeground(style, Color.blue);
							
							try{doc.insertString(doc.getLength(), " " + word21,  style);}
							catch(BadLocationException e){}
							
							StyleConstants.setForeground(style, Color.black);
							
							try{doc.insertString(doc.getLength(),  endPartTree,  style);}
							catch(BadLocationException e){}
							
							phrasePane.setText(word11 + " " + word21);
							numberPane.setText("" + lastSentence);
							input = 0;
							
							while(input == 0){
								System.out.println(input);
							}
							
							if(input == 1){
								phraseAnnotations = phraseAnnotations + "\r\n" + word11 + " " + word21 + " - adj phrase";
							}
							else if(input == 2){
								phraseAnnotations = phraseAnnotations + "\r\n" + word11 + " " + word21 + " - verb phrase";
							}
							else if(input == 3){
								phraseAnnotations = phraseAnnotations + "\r\n" + word11 + " " + word21 + " - other phrase";
							}
							else{
								PrintWriter writer = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Annotation/phrase_tree_annotations.txt");
								writer.print(phraseAnnotations);
								writer.close();
								
								PrintWriter writer1 = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Annotation/LastSentence.txt");
								writer1.print(lastSentence);
								writer1.close();
							
								PrintWriter writer2 = new PrintWriter("C:/Users/Tyler/Desktop/POS Corrector/Annotation/LastAnnotation.txt");
								writer2.print(globalAnnotationCounter-currentRun);
								writer2.close();
								
								leave = true;
								frame.setVisible(false);
								System.exit(0);
								break;
							}
						}
					}
					
					if(phraseCount == sent.getPhraseList().size())
						break;
				}
				lastSentence++;
			}
		}
	}
	public static void stall(int seconds){
		
		try{
			Thread.sleep(seconds * 1000);
		}catch(InterruptedException ex){
			Thread.currentThread().interrupt();
		}
	}
}
