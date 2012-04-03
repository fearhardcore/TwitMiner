package badgers.fed.twitminer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import badgers.fed.twitminer.Serializer;
import badgers.fed.twitminer.model.Motif;

public class DFWriter {
	BufferedWriter o;
	List<List<Motif>> dfs;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DFWriter();

	}

	/**
	 * Generate an human-readable list of functional dependencies, and output it
	 * to the file "dfs_human_readable"
	 */
	public DFWriter() {
		try {
			o = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					new File("./dfs_human_readable"))));
			List<String> keywords = Serializer.deSerializeKeywords();
			dfs = Serializer.deSerializeDF();
			for (List<Motif> m : dfs) {
				String s = m.get(0).toString(keywords) + "("
						+ m.get(0).getFreq() + ")" + " Implique "
						+ m.get(0).toString(keywords) + '('
						+ m.get(1).getFreq() + ')' + " Confiance : "
						+ m.get(1).getFreq() / m.get(0).getFreq() + '\n';
				o.write(s);
			}
			o.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
