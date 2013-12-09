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
        // NOTE : recall that the 'final' keyword notes inmutability
		  // even for local variables. 

        // Top-level frame in which game components live
		  // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300,300);

		  // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Playing...");
        status_panel.add(status, BorderLayout.CENTER);
        final JLabel lives = new JLabel("Lives: 3");
        status_panel.add(lives, BorderLayout.WEST);
        final JLabel points = new JLabel("Score: 0");
        status_panel.add(points, BorderLayout.EAST);

        // Main playing area
        final GameCourt court = new GameCourt(status, lives, points);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset
        // button, we define it as an anonymous inner class that is 
        // an instance of ActionListener with its actionPerformed() 
        // method overridden. When the button is pressed,
        // actionPerformed() will be called.
        final JButton reset = new JButton("Restart");
        reset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    court.reset();
                }
            });
        control_panel.add(reset);
        
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
