package annotator;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;

import org.apache.commons.io.FileUtils;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.Color;


public class annotator extends JFrame {

	private static final long serialVersionUID = 1L;
	public annotator(){
		getContentPane().setLayout(null);
		
		JButton btnAdj = new JButton("Adjective");
		btnAdj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				input = 1;
				System.out.println(input);
			}
		});
		
		btnAdj.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAdj.setBounds(10, 184, 119, 66);
		getContentPane().add(btnAdj);
		
		JButton btnVerb = new JButton("Verb");
		btnVerb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = 2;
			}
		});
		
		btnVerb.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnVerb.setBounds(139, 184, 119, 66);
		getContentPane().add(btnVerb);
		
		JButton btnOther = new JButton("Other");
		btnOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = 3;
			}
		});
		
		btnOther.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnOther.setBounds(268, 184, 119, 66);
		getContentPane().add(btnOther);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = 4;
			}
		});
		
		btnQuit.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnQuit.setBounds(397, 184, 119, 66);
		getContentPane().add(btnQuit);
		
		SentencePane = new JTextPane();
		SentencePane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		SentencePane.setBounds(10, 11, 509, 104);
		getContentPane().add(SentencePane);
		
		phrasePane = new JTextPane();
		phrasePane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		phrasePane.setBounds(155, 126, 218, 50);
		phrasePane.setForeground(Color.blue);
		getContentPane().add(phrasePane);
		
		numberPane = new JTextPane();
		numberPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		numberPane.setBounds(400, 126,100 ,50);
		getContentPane().add(numberPane);
		
	}
	
	static JTextPane numberPane;
	static JTextPane SentencePane;
	static JTextPane phrasePane;
	static int input;
	public static void main (String[] args) throws IOException{
		
		annotator frame = new annotator();
		frame.setSize(550, 320);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		File taggedCorpusFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Corpus/tagged_corpus.txt");
		File annotationsFile = new File("C:/Users/Tyler/Desktop/POS Corrector/Annotation/annotations.txt");
		File lastSentenceFile = new File("C:/USers/Tyler/Desktop/POS Corrector/Annotation/LastSentence.txt");
		
		String taggedCorpusList = FileUtils.readFileToString(taggedCorpusFile, "UTF-8");
		String annotations = FileUtils.readFileToString(annotationsFile, "UTF-8");
		String lastSentenceString = FileUtils.readFileToString(lastSentenceFile, "UTF-8");
		
		String[] taggedCorpus = taggedCorpusList.split("[\\\n]");
		
		int lastSentence = Integer.parseInt(lastSentenceString);

		boolean leave = false;
		
		System.setProperty("wordnet.database.dir", "C:/Program Files/eclipse/WordNet-3.0/dict/");
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		
		while(!leave){
			
			// Find each phrase phrase in the sentence from the corpus
			String taggedSentence = taggedCorpus[lastSentence];
			String taggedWords[] = taggedSentence.split(" ");
			
			String cleaned = cleanSentence(taggedSentence);
		
			System.out.print(cleanSentence(taggedSentence));
			System.out.println("1:Adj -- 2:Verb -- 3:Other -- 4:Quit");
			
			for(int i = 0; i < taggedWords.length; i++){
				if(i < taggedWords.length - 3){
					if(taggedWords[i].contains("ing_") && taggedWords[i+1].contains("_N") &&
					   Character.isLowerCase(taggedWords[i].charAt(0)) && Character.isLowerCase(taggedWords[i+1].charAt(0))){
						
						String word1 = taggedWords[i].substring(0, taggedWords[i].indexOf('_'));
						String word2 = taggedWords[i+1].substring(0, taggedWords[i+1].indexOf('_'));
						
						Synset[] synsets = database.getSynsets(word1, SynsetType.VERB);
						
						if(synsets.length > 0 && word2.length() > 2){
							System.out.println("PHRASE: " + word1 + " " + word2);
					
							String firstPart = cleaned.substring(0, cleaned.indexOf(word1 + " " + word2));
							String secondPart = cleaned.substring(cleaned.indexOf(word1 + " " + word2) + word1.length() + 1 + word2.length());
							
							System.out.println(firstPart);
							System.out.println(word1);
							System.out.println(word2);
							System.out.println(secondPart);
							
							SentencePane.setText("");
							
					        StyledDocument doc = SentencePane.getStyledDocument();
		
					        MutableAttributeSet style = SentencePane.addStyle("I'm a Style", null);
					        
					        StyleConstants.setForeground(style,  Color.black);
					        
					        try{ doc.insertString(doc.getLength(), firstPart, style);}
					        catch (BadLocationException e){}
		
					        StyleConstants.setForeground( style, Color.blue);
		
					        try { doc.insertString(doc.getLength(), word1 + " " + word2,style); }
					        catch (BadLocationException e){}
					        
					        StyleConstants.setForeground(style,  Color.black);
					        
					        try{ doc.insertString(doc.getLength(), secondPart, style);}
					        catch (BadLocationException e){}
							
							phrasePane.setText(word1 + " " + word2);
							numberPane.setText("" + lastSentence);
							input = 0;
							while(input == 0){
								System.out.println(input);
							}
							
							//input = reader.nextInt();
							
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
								frame.setVisible(false);
								break;
							}					
						}
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
