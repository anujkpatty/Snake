import java.awt.Color;
import java.awt.Graphics;

public class Tile {
	
	private int px;
	private int py;
	private Color color;
	public static final int SIZE = 16;
	
	public Tile(int px, int py, Color color) {
		this.px = px;
		this.py = py;
		this.color = color;
	}

	public int getPx() {
		return this.px;
	}

	public int getPy() {
		return this.py;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	
	public void move(Direction d) {
		
		if (d.equals(Direction.LEFT)) {
			this.px -= 1;
		}
		
		if (d.equals(Direction.RIGHT)) {
			this.px += 1;
		}
		
		if (d.equals(Direction.UP)) {
			this.py -= 1;
		}
		
		if (d.equals(Direction.DOWN)) {
			this.py += 1;
		}
	}
	
	public void moveTo(Tile t) {
		this.px = t.px;
		this.py = t.py;
	}
	
	public void moveTo(int px, int py) {
		this.px = px;
		this.py = py;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) { 
	        return true; 
	    }
	    if (o == null) { 
	        return false; 
	    }
	    if (!(o instanceof Tile)) {
	        return false;
	    }
	    
	    Tile that = (Tile) o;
	    
	    if (!(this.getPx() == that.getPx())) {
	    	return false;
	    }
	    
	    if (!(this.getPy() == that.getPy())) {
	    	return false;
	    }
	    
	    if (!(this.getColor() == that.getColor())) {
	    	return false;
	    }

	    return true;
	}
	
	public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx() * 20 + 2, this.getPy() * 20 + 2, SIZE, SIZE);
    }

}
