
package the.labyrinth;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author szabi
 */
public class GameMenu extends JPanel {

    private final JButton start;
    private final JButton highScores;
    private final JButton exit;
    private final JLabel title;
    private final ImageIcon titleImage;
    private final JFrame frame;
    private GameArea gameArea;

    /**
     * 
     * @param frame
     * @param gameArea
     * Felépíti a menüt actionListenerekkel együtt.
     * 
     */
    public GameMenu(JFrame frame) {
        super();
        this.frame = frame;
        titleImage = new ImageIcon("resources/title.png");
        title = new JLabel(titleImage);
        start = new JButton("Start");
        highScores = new JButton("High Scores");
        exit = new JButton("Exit");
        start.addActionListener(new startActionListener());
        highScores.addActionListener(new HighScoresActionListener());
        exit.addActionListener(new exitActionListener());
        this.add(Box.createRigidArea(new Dimension(0, 120)));
        this.add(title);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(Box.createRigidArea(new Dimension(0, 120)));

        this.add(start);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(0, 50)));

        this.add(highScores);
        highScores.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(0, 50)));

        this.add(exit);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    class startActionListener implements ActionListener {

        /**
         * 
         * @param ae 
         * Létrehozza és átadja a frame-nek a GameArea-t.
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            gameArea = new GameArea(frame, GameMenu.this, 1);
            frame.getContentPane().remove(GameMenu.this);
            frame.getContentPane().add(gameArea);
            frame.setPreferredSize(new Dimension(1000, 1000));
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }

    class HighScoresActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
        }
    }

    class exitActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            System.exit(0);
        }
    }
}
