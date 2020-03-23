
public class Score implements Comparable<Score> {
	private int score;
	private String name;
	
	public Score(int score, String name) {
		this.score = score;
		this.name = name;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) { 
	        return true; 
	    }
	    if (o == null) { 
	        return false; 
	    }
	    if (!(o instanceof Score)) {
	        return false;
	    }
	    
	    Score that = (Score) o;
	    
	    if (!(this.name == that.name)) {
	    	return false;
	    }
	    
	    if (!(this.score == that.score)) {
	    	return false;
	    }
	    
	    return true;
	}
	
	@Override
	public int compareTo(Score o) {
		if (this.score < o.score) {
			return -1;
		}
		
		if (this.score > o.score) {
			return 1;
		}
		
		else {
			return this.name.compareTo(o.name);
		}
	}
}
