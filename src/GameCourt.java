/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic of how different objects 
 * interact with one another.  Take time to understand how the timer 
 * interacts with the different methods and how it repaints the GUI 
 * on every tick().
 *
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// game objects
	private Paddle paddle1;          // the Black Square, keyboard control
	private Ball ball;          // the Golden Snitch, bounces
	private Brick[][][] bricks;          // the Poison Mushroom, doesn't move
	
	//state of game
	public boolean playing = false;  // whether the game is running
	public boolean won = false;
	public boolean instr_open = false;
	public boolean paused = false;
	private int scr;
	private int lvs;

	//labels to be updated
	private JLabel status;       // Current status text (i.e. Running...)
	private JLabel score;
	private JLabel lives;
	
	//instructions
	String instr_text = "Welcome to this game!";
	JLabel instr =
		      new JLabel(instr_text, JLabel.CENTER);


	// Game constants
	public static final int COURT_SIDE = 350;
	public static final int COURT_DEPTH = 150;
	public static final int SQUARE_VELOCITY = 8;
	
	Map map = new Map(200.0, COURT_SIDE/2);
	
	int mapped_o = map.point(0, COURT_DEPTH);
	int mapped_s = map.dimension(COURT_SIDE, COURT_DEPTH);
	public int n = 4;
	public int m = 4;
	public int h = 3;
	
	// Update interval for timer in milliseconds 
	public static final int INTERVAL = 35; 

	public GameCourt(JLabel status, JLabel lives, JLabel score){
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
		Timer timer = new Timer(INTERVAL, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tick();
			}
		});
		timer.start();

		// Enable keyboard focus on the court area
		setFocusable(true);

		// moves square
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					paddle1.v_x = -SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					paddle1.v_x = SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					paddle1.v_y = SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					paddle1.v_y = -SQUARE_VELOCITY;
			}
			public void keyReleased(KeyEvent e){
				paddle1.v_x = 0;
				paddle1.v_y = 0;
			}
		});
		
		// add pause functionality to space bar
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					pause();
			}
		});

		this.status = status;
		this.score = score;
		this.lives = lives;
		scr = 0;
		lvs = 3;
	}

	/** (Re-)set the state of the game to its initial state.
	 */
	public void reset() {

		paddle1 = new Paddle(COURT_SIDE, COURT_SIDE, COURT_DEPTH, map);
		ball = new Ball(COURT_SIDE, COURT_SIDE, COURT_DEPTH, map);
		bricks = new Brick[n][m][h];
		
		// creates bricks in 3d layout
		for (int k = 0; k < h; k ++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					bricks[i][j][k] = new Brick((10+Brick.SIZE)*j,(10+Brick.SIZE)*i, 
							(3+Brick.DEPTH)*k, COURT_SIDE, COURT_SIDE, COURT_DEPTH, map);
				}
			}
		}

		//resets states
		playing = true;
		paused = false;
		won = false;
		instr_open = false;
		lvs = 3;
		scr = 0;
		
		//resets labels
		try {
			this.remove(instr);
		} catch (NullPointerException e) {	}
		status.setText("Playing...");
		lives.setText("Lives: " + lvs);
		score.setText("Bricks Left: " + (h*n*m - scr));

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

    /**
     * This method is called every time the timer defined
     * in the constructor triggers.
     */
	void tick(){
		if (playing) {
			// move ball and paddle
			paddle1.move();
			ball.move();
			
			// make the ball bounce off walls...
			ball.bounce(ball.hitWall());

			// ...and the paddle
			ball.bounce(ball.hitObj(paddle1));
			
			//...and the bricks
			boolean bounced = false;
			for (int k = 0; k < h; k ++) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						if (bricks[i][j][k].isAlive){
							bounced = ball.bounce(ball.hitObj(bricks[i][j][k]));
							if (bounced) {
								bricks[i][j][k].isAlive = false;
								scr += 1;
								score.setText("Bricks Left: " + (h*m*n - scr));
							}
						}
					}
					if (bounced) break; //no need to continue searching
				}
				if (bounced) break;
			}
		
			// check for the game end conditions
			/*if (ball.pos_z <= 0) { 
				lvs += -1;
				lives.setText("Lives: " + lvs);
				if (lvs < 1) {
					playing = false;
					status.setText("You lose!");
				}
			} else if (scr >= h*n*m) {
				playing = false;
				won = true;
				status.setText("You win!");
			}*/
			
			// update the display
			repaint();
		} 
	}

	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(!instr_open)
		{		
			// drawing box to show depth
			Color oldColor = g.getColor();
			g.setColor(Color.GRAY);
			g.drawRect(mapped_o, mapped_o, mapped_s, mapped_s);
			g.drawLine(0, 0, mapped_o, mapped_o);
			g.drawLine(0, COURT_SIDE, mapped_o, mapped_o+mapped_s);
			g.drawLine(COURT_SIDE, 0, mapped_o+mapped_s, mapped_o);
			g.drawLine(COURT_SIDE, COURT_SIDE, mapped_o+mapped_s, mapped_o+mapped_s);
			g.setColor(oldColor);
			
			//draw all alive bricks
			for (int k = 0; k < h; k ++) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						bricks[i][j][k].draw(g);
						if (ball.pos_z > bricks[i][j][k].pos_z) ball.draw(g);
					}
				}
			}
			
			//if (ball.pos_z <= 110) 
			ball.draw(g);
			paddle1.draw(g);
		}
		
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(COURT_SIDE,COURT_SIDE);
	}
	
	public void instructions(){
		
		if (instr_open){
			this.remove(instr);
			
			if (lvs > 0) {
				playing = true;
				status.setText("Playing...");
			} else {
				status.setText("You lose!");
			}
			
			paused = false;
			repaint();
			instr_open = false;
			requestFocusInWindow();
		} else {
			playing = false;
			status.setText("Instructions open, game paused");
        	this.add(instr, BorderLayout.CENTER);
			instr_open=true;
			repaint();
		}
	}
	
public void pause(){
		
		if (paused){
			if (lvs > 0) {
				playing = true;
				status.setText("Playing...");
			} else {
				status.setText("You lose!");
			}
			paused = false;
			requestFocusInWindow();
		} else {
			playing = false;
			status.setText("Paused");
			paused=true;
		}
	}
}


