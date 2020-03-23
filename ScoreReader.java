import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.TreeSet;

public class ScoreReader {
	
	private Reader r;
	private Writer w;
	private TreeSet<Score> scores;
	private File file;

	public ScoreReader(File file) throws IOException {
		try {
			this.file = file;
			r = new FileReader(file);
			w = new FileWriter(file, true);
		} catch (IOException e) {
			throw e;
		}
	}
	
	public TreeSet<Score> getSortedScores() {

		try {
			r = new FileReader(file);
			BufferedReader br = new BufferedReader(r);

			String line = br.readLine();
			scores = new TreeSet<Score>();
			

			while (line != null) {
				int colon = line.indexOf(':');
				String name = line.substring(0, colon);
				String scoreS = line.substring(colon + 1);
				int scoreI = Integer.parseInt(scoreS);
				Score score = new Score(scoreI, name);
				scores.add(score);
				line = br.readLine();
			}
			br.close();
			return scores;
		} catch (IOException e) {
			System.err.println(e.getMessage() + "ln 48");
			return scores;
		}
	}
	
	public void addScore(int score, String name) throws IOException {
		TreeSet<Score> top = getSortedScores();
		Score newScore = new Score(score, name);
		
		
		if (top.size() >= 5) {
			
			Score lowest = top.first();
			int least = lowest.getScore();
			
			if (score > least) {
				top.remove(lowest);			
				top.add(newScore);
			}
			else {
				return;
			}
		}
		else {
			top.add(newScore);
		}
		
		
		try {
			w = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(w);
			
			
			for (Score entry : top) {
				int n = entry.getScore();
				String s = entry.getName();
				bw.append(s + ":" + n);
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		} catch(IOException e) {
			throw e;
		} 
		
	}
}
