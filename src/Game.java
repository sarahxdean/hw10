/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** 
 * Game
 * Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run(){

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("3D Brick Breaker");
        frame.setLocation(300,300);

        
		// Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        
        status_panel.setLayout(new BorderLayout());

        final JPanel text_panel = new JPanel();
        final JPanel lives_panel = new JPanel();
        final JPanel score_panel = new JPanel();
        
        final JLabel status = new JLabel("Playing...");
        text_panel.add(status);
        final JLabel lives = new JLabel("Lives: 3");
        lives_panel.add(lives);
        final JLabel score = new JLabel("Bricks Left: 48");
        score_panel.add(score);
        
        status_panel.add(BorderLayout.LINE_START, lives_panel);
        status_panel.add(BorderLayout.PAGE_START, text_panel);
        status_panel.add(BorderLayout.LINE_END,score_panel);
        

        // Main playing area
        final GameCourt court = new GameCourt(status, lives, score);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Restart");
        reset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    court.reset();
                }
            });
        control_panel.add(reset);
        
        // Instructions button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	court.instructions();
                	if (court.instr_open) {
                		instructions.setText("Return to game");
                	} else {
                		instructions.setText("Instructions");
                	}
                }
            });
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /*
     * Main method run to start and run the game
     * Initializes the GUI elements specified in Game and runs it
     * NOTE: Do NOT delete! You MUST include this in the final submission of your game.
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Game());
    }
}
