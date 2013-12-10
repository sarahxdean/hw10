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
 * interact with one another.
 *
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// game objects
	private Paddle paddle1;          
	private Paddle paddle2;
	private BouncingBall ball;  
	private Ball deathball;
	private Brick[][][] bricks;        
	
	//state of game
	public boolean playing = false; 
	public boolean won = false;
	public boolean instr_open = false;
	public boolean paused = false;
	private int scr;
	private int lvs;

	//labels to be updated
	private JLabel status;      
	private JLabel score;
	private JLabel lives;
	
	//instructions
	String instr_text = 
		"<html><div WIDTH=300><center>Welcome to Brick Breaker 3D</center>"
		+ "<br>&emsp;The goal of this game is to eliminate all of the bricks "
		+ "before losing all 3 lives. Bricks are eliminated when the ball hits "
		+ "them. You lose a life when you miss hitting the ball with the paddles"
		+ " and it falls towards you. "
		+ "<br>&emsp;There are two paddles that you can move to hit the ball, "
		+ "(arrow keys or awsd) so this"
		+ " game can either be a test of your multitasking skills or an "
		+ "opportunity for teamwork."
		+ "<br>&emsp;Press the space bar to pause and unpause. Note that "
		+ "exiting the instructions will cause the game to unpause! "
		+ "<br>&emsp;Watch out for the red ball - if it hits either paddle you"
		+ " will lose a life!"
		+ "<br><br> Cool features of this game include (1) the 3D bouncing, (2) "
		+ "the 3D visual projection, (3) the two paddles, (4) the sound effects,"
		+ " and (5) the killer ball."
		+ "</div></html>";
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

		// moves paddles
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
		
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_A)
					paddle2.v_x = -SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_D)
					paddle2.v_x = SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_S)
					paddle2.v_y = SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_W)
					paddle2.v_y = -SQUARE_VELOCITY;
			}
			public void keyReleased(KeyEvent e){
				paddle2.v_x = 0;
				paddle2.v_y = 0;
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

		paddle1 = new Paddle(0, COURT_SIDE, COURT_SIDE, COURT_DEPTH, map, 
				new Color(0, 0,0, 200 ));
		paddle2 = new Paddle(-3, COURT_SIDE, COURT_SIDE, COURT_DEPTH, map, 
				new Color(0, 50,0, 200 ));
		ball = new BouncingBall(COURT_SIDE, COURT_SIDE, COURT_DEPTH, map, 
				(new Color(51, 0, 102)));
		deathball = new Ball(7, COURT_SIDE, COURT_SIDE, COURT_DEPTH, map, 
				Color.RED);
		bricks = new Brick[n][m][h];
		
		// creates bricks in 3d layout
		for (int k = 0; k < h; k ++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					bricks[i][j][k] = new Brick((10+Brick.SIZE)*j,
							(10+Brick.SIZE)*i,(3+Brick.DEPTH)*k, COURT_SIDE,
							COURT_SIDE, COURT_DEPTH, map);
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
			// move balls and paddle
			paddle1.move();
			paddle2.move();
			ball.move();
			deathball.move();
			
			// make the balls bounce off walls...
			if(ball.bounce(ball.hitWall())) {
				playSound.WALL.play();
			}
			deathball.bounce(deathball.hitWall());

			// ...and the paddle
			if (ball.bounce(ball.hitObj(paddle1)) || 
					ball.bounce(ball.hitObj(paddle2))) {
				playSound.WALL.play();
			}
			
			if (deathball.bounce(deathball.hitObj(paddle1)) || 
				deathball.bounce(deathball.hitObj(paddle2))) {
				playSound.DEATH.play();
				lvs += -1;
				lives.setText("Lives: " + lvs);
				if (lvs < 1) {
					playing = false;
					status.setText("You lose!");
				}
			}
			
			
			//...and the bricks
			boolean bounced = false;
			for (int k = 0; k < h; k ++) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						if (bricks[i][j][k].isAlive){
							deathball.bounce(deathball.hitObj(bricks[i][j][k]));
							bounced = ball.bounce(ball.hitObj(bricks[i][j][k]));
							if (bounced) {
								playSound.BRICK.play();
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
			if (ball.pos_z <= -4) { 
				playSound.DEATH.play();
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
			}
			
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
						//draw balls under bricks if necessary
						if (ball.pos_z > bricks[i][j][k].pos_z) ball.draw(g);
						if (deathball.pos_z > bricks[i][j][k].pos_z) 
							deathball.draw(g);
					}
				}
			}
			
			if (ball.pos_z <= 100) ball.draw(g);
			if (deathball.pos_z <= 100) deathball.draw(g);

			paddle1.draw(g);
			paddle2.draw(g);
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


