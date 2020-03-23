import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;



public class Snake {
	
	private LinkedList<Tile> snake;
	private Tile head;
	private int width;
	private int height;
	
	
	public Snake(int width, int height) {
		this.height = height;
		this.width = width;
		snake = new LinkedList<Tile>();
		head = new Tile(width / 2, height / 2, Color.green);
		Tile t = new Tile (width / 2 - 1, height / 2, Color.green);
		snake.add(head);
		snake.add(t);
	}
	
	//second constructor sets default height and width for 500 x 500 game court
	public Snake() {
		this.height = 25;
		this.width = 25;
		snake = new LinkedList<Tile>();
		head = new Tile(width / 2, height / 2, Color.green);
		Tile t = new Tile (width / 2 - 1, height / 2, Color.green);
		snake.add(head);
		snake.add(t);
	}
	
	public void eatFood(Direction d) {
		Tile tail = snake.getLast();
		Tile t = new Tile(tail.getPx(), tail.getPy(), Color.green);
		move(d);
		snake.add(t);
	}

	public void move(Direction d) {
		
		if (d == Direction.START) {
			return;
		}

		Iterator<Tile> iter = snake.descendingIterator();

		Tile prev = snake.getLast();

		while (iter.hasNext()) {
			Tile next = iter.next();

			if (next.equals(prev)) {
				continue;
			}

			prev.moveTo(next);
			prev = next;

		}
		head.move(d);
	}
	
	public LinkedList<Tile> getList() {
		return this.snake;
	}
	
	public boolean hitWall(Direction d) {
		if (d.equals(Direction.LEFT)) {
			if (head.getPx() < 0) {
				return true;
			}
		}
		
		if (d.equals(Direction.RIGHT)) {
			if (head.getPx() > width - 1) {
				return true;
			}
		}
		
		if (d.equals(Direction.UP)) {
			if (head.getPy() < 0) {
				return true;
			}
		}
		
		if (d.equals(Direction.DOWN)) {
			if (head.getPy() > height - 1) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hitSelf() {

		for (Tile t : snake) {
			if (t == head) {
				continue;
			}
			else {
				if (head.getPx() == t.getPx() && head.getPy() == t.getPy()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean intersect(Tile t) {
		return (head.getPx() == t.getPx() && head.getPy() == t.getPy());
	}

	public boolean onSnake(Tile t) {

		for (Tile s : snake) {

			if (s.getPx() == t.getPx() && s.getPy() == t.getPy()) {
				return true;
			}
		}
		return false;
	}
	
	public int length() {
		return snake.size();
	}
}
