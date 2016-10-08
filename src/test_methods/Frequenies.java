package test_methods;

public class Frequenies {

	public static void main (String args []){
		
		String a = "activity  | aptitude  | competence  | energy  | force  | genius  | qualification  | skill  | strength  | vigor  | virtue |  carte blanche  | clout  | domination  | government  | influence  | prerogative  | supremacy  | sway |  arm  | command  | establishment  | management  | power structure  | powerhouse |  control |  air power  | arm  | firepower  | ordnance  | sea power |  horsepower  | impetus  | momentum |  consequence  | emotion  | force  | significance |  divinity |";
		String b = "ruled | rules | ruling | rule";
		
		a = a.replace(" | ", "|");
		String[] aa = a.split("[\\|]");
		
		b = b.replace(" | ",  "|");
		String[] bb = b.split("[\\|]");
		
		for(int i = 0; i < aa.length; i++){	
			for(int j = 0; j < bb.length; j++){
				System.out.println(bb[j].trim() + " " + aa[i].trim());
			}
		}

		
		
	}
}
