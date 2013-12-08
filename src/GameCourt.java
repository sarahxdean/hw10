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

	// the state of the game logic
	private Square square;          // the Black Square, keyboard control
	private Circle snitch;          // the Golden Snitch, bounces
	//private Circle snitch2;
	private Brick[][] bricks;          // the Poison Mushroom, doesn't move
	
	public boolean playing = false;  // whether the game is running
	private JLabel status;       // Current status text (i.e. Running...)

	// Game constants
	public static final int COURT_WIDTH = 300;
	public static final int COURT_HEIGHT = 300;
	public static final int COURT_DEPTH = 150;
	public static final int SQUARE_VELOCITY = 8;
	// Update interval for timer in milliseconds 
	public static final int INTERVAL = 35; 
	int n = COURT_HEIGHT / (10+Brick.SIZE/2);
	int m = COURT_WIDTH / (10+Brick.SIZE/2);

	public GameCourt(JLabel status){
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called 
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// this key listener allows the square to move as long
		// as an arrow key is pressed, by changing the square's
		// velocity accordingly. (The tick method below actually 
		// moves the square.)
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					square.v_x = -SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					square.v_x = SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					square.v_y = SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					square.v_y = -SQUARE_VELOCITY;
			}
			public void keyReleased(KeyEvent e){
				square.v_x = 0;
				square.v_y = 0;
			}
		});

		this.status = status;
	}

	/** (Re-)set the state of the game to its initial state.
	 */
	public void reset() {

		square = new Square(COURT_WIDTH, COURT_HEIGHT, COURT_DEPTH);
		snitch = new Circle(170,170,0,COURT_WIDTH, COURT_HEIGHT, COURT_DEPTH);
		//snitch2 = new Circle(170,40,100,COURT_WIDTH, COURT_HEIGHT, COURT_DEPTH);
		

		bricks = new Brick[n+1][m+1];
		
		//for (int k = 0; k < 6; k ++) {
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				bricks[i][j] = new Brick(5+(10+Brick.SIZE/2)*j,5+(10+Brick.SIZE/2)*i, 
						(3+Brick.DEPTH), COURT_WIDTH, COURT_HEIGHT, COURT_DEPTH);
			}
		}
		//}

		playing = true;
		status.setText("Running...");

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

    /**
     * This method is called every time the timer defined
     * in the constructor triggers.
     */
	void tick(){
		if (playing) {
			// advance the square and snitch in their
			// current direction.
			square.move();
			snitch.move();
			//snitch2.move();

			// make the snitch bounce off walls...
			snitch.bounce(snitch.hitWall());
			//snitch2.bounce(snitch2.hitWall());

			// ...and the paddle
			snitch.bounce(snitch.hitObj(square));
			boolean bounced = false;
			//for (int k = 0; k < 6; k ++) {
			for (int i = 0; i <= n-1; i++) {
				for (int j = 0; j <= m-1; j++) {
					bounced = snitch.bounce(snitch.hitObj(bricks[i][j]));
					if (bounced) break;
				}
				if (bounced) break;
			}
			//if (bounced) break;
			//}
			
			//snitch2.bounce(snitch2.hitObj(square));
		
			//check for the game end conditions
			if (snitch.pos_z < 1) { 
				playing = false;
				status.setText("You lose!");
			}
//			} else if (square.intersects(snitch)) {
	//			playing = false;
		//		status.setText("You win!");
			//}
			
			// update the display
			repaint();
		} 
	}

	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//for (int k = 0; k < 6; k ++) {
		for (int i = 0; i <= n-1; i++) {
			for (int j = 0; j <= m-1; j++) {
				bricks[i][j].draw(g);
				if (snitch.pos_z > bricks[i][j].pos_z) snitch.draw(g);
			}
		}
		//}
		if (snitch.pos_z <= 100) snitch.draw(g);
		//snitch2.draw(g);
		square.draw(g);
		
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(COURT_WIDTH,COURT_HEIGHT);
	}
}
