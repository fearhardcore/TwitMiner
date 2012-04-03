package badgers.fed.twitminer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import badgers.fed.twitminer.model.Motif;

public class Nettoyage {
	List<List<Motif>> dfs;
	
	//Dictionnaire à remplir
	HashMap<String, String> dic;
	
	ArrayList<String> keywords;

	public static void main(String[] args) {
		new Nettoyage();
	}

	public Nettoyage() {
		dfs = Serializer.deSerializeDF();
		cleanSynonyms();
		cleanNonMax();
		cleanNonMin();
	}

	public void cleanSynonyms() {
		keywords = (ArrayList<String>) Serializer.deSerializeKeywords();
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("./trends.syno")), "UTF-8"));
			dic = new HashMap<String, String>();
			String currLine = new String();
			currLine = is.readLine();
			while (true) {
				String[] s = currLine.split(String.valueOf('"'));
				dic.put(s[1], s[3]);
				currLine = is.readLine();
				if(currLine == null)
					break;
			}
			
			//Etablissement de la liste de keywords à changer
			HashMap<Integer, Integer> synos = new HashMap<Integer, Integer>();
			for(String s : dic.keySet()) {
				int rangKeyToChange = keywords.indexOf(s);
				if(rangKeyToChange != -1) {
//					keywords.remove(rangKeyToChange);
					int rangKeySyno = keywords.indexOf(dic.get(s));
					if (rangKeySyno != -1)
						synos.put(rangKeyToChange, rangKeySyno);
					else {
						keywords.add(dic.get(s));
						synos.put(rangKeyToChange, dic.size() - 1);
					}
				}
			}
			//Remplacement effectif des synonymes
			
			//Pour chaque DF
			for(List<Motif> lm : dfs) {
				//Nettoyage des motifs implicateurs (X)
				
				//List<Integer> l = m.getMotif();
				//Pour chaque mot-clé à remplacer potentiellement
				for(int i : synos.keySet()) {
					int rang = lm.get(0).indexOf(i);
					//Le mot clé doit être remplacé					
					if(rang != -1) {
						System.out.println(keywords.get(i) + " remplacé par " + keywords.get(synos.get((Object)i)));
						lm.get(0).set(rang, synos.get(i));
						//Suppression des autres occurences du mot-clé
						lm.get(0).remove((Object)i);
					}
					
					Motif n = lm.get(1);
					int rangg = n.indexOf(i);
					if(rangg != -1)
						n.set(rangg, synos.get(i));
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void cleanNonMax() {
		List<List<Motif>> dfscopy = new ArrayList<List<Motif>>(dfs);
		for(List<Motif> df : dfs)
			//Chaque df
			for(List<Motif> otherdf : dfs)
				//Cherche une autre DF avec le même X
				if(df.get(0).equals(otherdf.get(0)))
					if(df.get(1).containsAll(otherdf.get(1)))
						if(!df.equals(otherdf))
							dfscopy.remove(otherdf);
		System.out.println(dfs.size());
		System.out.println(dfscopy.size());
	}
	
	public void cleanNonMin() {
		List<List<Motif>> dfscopy = new ArrayList<List<Motif>>(dfs);
		for(List<Motif> df : dfs)
			//Chaque df
			for(List<Motif> otherdf : dfs)
				//Cherche une autre DF avec le même Y
				if(df.get(1).equals(otherdf.get(1)))
					if(!df.equals(otherdf))
						dfscopy.remove(df);
		System.out.println(dfs.size());
		System.out.println(dfscopy.size());
	}
}
