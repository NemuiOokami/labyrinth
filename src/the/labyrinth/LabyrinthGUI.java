
package the.labyrinth;


import java.awt.Dimension;
import javax.swing.JFrame;


public class LabyrinthGUI {

    private final JFrame frame;
    private final GameMenu menu;

    public LabyrinthGUI() {
        frame = new JFrame("The Labyrinth");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu = new GameMenu(frame);
        frame.getContentPane().add(menu);
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
