import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another.
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	private Tile food;
	private Snake snake;

	public boolean playing = false; // whether the game is running
	private boolean ate; // keeps track if the snake just ate
	private boolean showInstructions = false;
	private JLabel status; // Current status text shows current score

	// Game constants
	// height and width must be evenly divisible by tile size (500 default)
	private static final int COURT_WIDTH = 500;
	private static final int COURT_HEIGHT = 500;
	private static final int TILE_SIZE = 20;
	private static final int WRatio = COURT_WIDTH / TILE_SIZE;
	private static final int HRatio = COURT_HEIGHT / TILE_SIZE;

	private Tile[][] board;

	private Timer timer;

	private ScoreReader highScore;

	private Direction direction = Direction.START;

	private String user;

	// Text file to read and write high scores.
	private File highScoreFile = new File("files/HighScores.txt/");

	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 110;

	public GameCourt(JLabel status) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key events are handled by its key
		// listener.
		setFocusable(true);

		// Key listeners change direction of snake depending on which key is pressed
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT && !(direction.equals(Direction.RIGHT))) {
					direction = Direction.LEFT;
				}

				else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !(direction.equals(Direction.LEFT))) {
					direction = Direction.RIGHT;
				}

				else if (e.getKeyCode() == KeyEvent.VK_DOWN && !(direction.equals(Direction.UP))) {
					direction = Direction.DOWN;
				}

				else if (e.getKeyCode() == KeyEvent.VK_UP && !(direction.equals(Direction.DOWN))) {
					direction = Direction.UP;
				}

			}
		});

		this.status = status;
	}

	// randomly place food and make sure it's not placed on top of snake
	public void locateFood() {
		food = new Tile((int) (Math.random() * (COURT_WIDTH / TILE_SIZE - 1)),
				(int) (Math.random() * (COURT_HEIGHT / TILE_SIZE - 1)), Color.yellow);
		while (snake.onSnake(food)) {
			food.moveTo((int) (Math.random() * (COURT_WIDTH / TILE_SIZE - 1)),
					(int) (Math.random() * (COURT_HEIGHT / TILE_SIZE - 1)));
		}
	}

	// inserting snake into 2d array so the coordinates of each tile can easily be
	// managed by
	// GameCourt and converted to graphics coordinates using the width and height
	// constants
	// specified.
	public void insertSnakeAndFood() {
		board = new Tile[COURT_WIDTH / TILE_SIZE][COURT_HEIGHT / TILE_SIZE];
		for (Tile t : snake.getList()) {
			board[t.getPx()][t.getPy()] = t;
		}
		board[food.getPx()][food.getPy()] = food;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
		timer.restart();
		snake = new Snake(WRatio, HRatio);
		locateFood();
		direction = Direction.START;

		playing = true;
		status.setText("Your score: " + snake.length());

		try {
			highScore = new ScoreReader(highScoreFile);
		} catch (IOException e) {
			System.err.println("Invalid Score File");
		}

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	public void showInstructions() {
		showInstructions = !showInstructions;
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		
		if (playing) {

			if (ate) {
				ate = false;
				snake.eatFood(direction);
			}

			else {
				
				snake.move(direction);

				if (snake.hitWall(direction)) {
					playing = false;
					showInstructions = false;
					status.setText("You lose!");

				}

				if (snake.hitSelf()) {
					playing = false;
					showInstructions = false;
					status.setText("You lose!");
				}

				if (snake.intersect(food)) {
					ate = true;
					locateFood();
				}
			}

			status.setText("Your score: " + snake.length());
			repaint();
		}

		else {
			
			timer.stop();
			status.setText("You Lose!");
			user = JOptionPane.showInputDialog(this, "Enter name here to record score or press" + " cancel to skip.");
			if (user != null) {
				user = user.toLowerCase();
				user = user.trim();
				if (user.length() > 15) {
					user = user.substring(0, 15);
				}

				try {
					highScore.addScore(snake.length(), user);
				} catch (IOException e) {
					System.err.println("Invalid Score File");
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (playing) {
			
			if (showInstructions) {
				
				g.setColor(Color.GREEN);
				Font f = new Font("sansserif", Font.BOLD, 45);
				g.setFont(f);
				g.drawString("Snake Instructions:", 20, 100);
				f = new Font("sansserif", Font.PLAIN, 18);
				g.setFont(f);
				g.setColor(Color.black);
				g.drawString("Snake is a classic game where the player must guide ", 15, 150);
				g.drawString("the snake and eat food to increase the score. The goal", 15, 170);
				g.drawString("is to survive as long as possible while eating as much", 15, 190);
				g.drawString("food as you can to increase your score.", 15, 210);
				g.drawString("To play Snake, simply press any direction arrow key ", 15, 250);
				g.drawString("to direct the snake. Collect food by moving the head", 15, 270);
				g.drawString("of the snake over it. The game is over once the snake", 15, 290);
				g.drawString("collides with the wall or itself.", 15, 310);
			} 
			
			else {
				
				insertSnakeAndFood();
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[0].length; j++) {
						if (board[i][j] == null) {
							continue;
						}
						g.setColor(board[i][j].getColor());
						g.fillRect(board[i][j].getPx() * TILE_SIZE + 2, board[i][j].getPy() * TILE_SIZE + 2,
								TILE_SIZE - 4, TILE_SIZE - 4);
					}
				}
			}
		}

		else if (!playing) {

			if (showInstructions) {
				
				g.setColor(Color.GREEN);
				Font f = new Font("sansserif", Font.BOLD, 45);
				g.setFont(f);
				g.drawString("Snake Instructions:", 20, 100);
				f = new Font("sansserif", Font.PLAIN, 18);
				g.setFont(f);
				g.setColor(Color.black);
				g.drawString("Snake is a classic game where the player must guide ", 15, 150);
				g.drawString("the snake and eat food to increase the score. The goal", 15, 170);
				g.drawString("is to survive as long as possible while eating as much", 15, 190);
				g.drawString("food as you can to increase your score.", 15, 210);
				g.drawString("To play Snake, simply press any direction arrow key ", 15, 250);
				g.drawString("to direct the snake. Collect food by moving the head", 15, 270);
				g.drawString("of the snake over it. The game is over once the snake", 15, 290);
				g.drawString("collides with the wall or itself.", 15, 310);
			}

			else {
				
				TreeSet<Score> top = highScore.getSortedScores();
				g.setColor(Color.GREEN);
				Font f = new Font("sansserif", Font.BOLD, 52);
				g.setFont(f);
				g.drawString("Your Score: " + snake.length(), 70, 100);
				g.setColor(Color.MAGENTA);
				g.drawString("High Scores:", 90, 200);
				f = new Font("sansserif", Font.PLAIN, 32);
				g.setFont(f);
				g.setColor(Color.BLACK);
				int i = 1;
				int y = 250;
				for (Score s : top.descendingSet()) {
					g.drawString(i + ". " + s.getScore() + " - " + s.getName().toUpperCase(), 100, y += 32);
					i++;
				}
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}