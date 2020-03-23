import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Snake");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        frame.add(status_panel, BorderLayout.SOUTH);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("New Game");
        final JButton instructions = new JButton("Show Instructions");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.showInstructions();
                court.reset();
            }
        });
        control_panel.add(reset);
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        // Start game
        court.reset();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}