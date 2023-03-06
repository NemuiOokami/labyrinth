package the.labyrinth;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

enum Block {
    NOTHING, WALL, PLAYER, DRAGON, FINISH, VICTORY
}

public class GameArea extends JPanel implements KeyListener {

    private JFrame frame;
    private GameMenu menu;
    private final ImageIcon PLAYER, DRAGON, DIRT, WALL, FINISH, UNKNOWN;
    private Block[][] gameTable;
    private JLabel[][] imageTable;
    private Player player;
    private Dragon dragon;
    private Timer dragonTimer;
    private TimerTask dragonMoving;
    private boolean startedYet;
    private Random dragonDirectionGenerator;
    private boolean isValidDir;
    private int dragonDirection;
    private static int levelsCompleted = 0;
    private static final int availableLevels = 2;

    /**
     * 
     * @param frame
     * @param menu
     * @param mapnum 
     * Itt szerepel a sárkány mozgását kezelő TimerTask.
     */
    public GameArea(JFrame frame, GameMenu menu, int mapnum) {
        this.menu = menu;
        this.frame = frame;
        this.startedYet = false;
        gameTable = new Block[21][21];
        imageTable = new JLabel[21][21];
        dragonDirectionGenerator = new Random();
        isValidDir = false;
        dragonDirection = 4;
        dragonTimer = new Timer();
        dragonMoving = new TimerTask() {
            @Override
            public void run() {
                do {
                    if (!isValidDir) {
                        dragonDirection = dragonDirectionGenerator.nextInt(4);
                    }
                    switch (dragonDirection) {
                        case 0:
                            isValidDir = move(-1, 0, dragon);
                            break;
                        case 1:
                            isValidDir = move(1, 0, dragon);
                            break;
                        case 2:
                            isValidDir = move(0, -1, dragon);
                            break;
                        case 3:
                            isValidDir = move(0, 1, dragon);
                            break;
                    }
                } while (!isValidDir);
            }
        };
        PLAYER = new ImageIcon(new ImageIcon("resources/player.jpg").getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        DRAGON = new ImageIcon(new ImageIcon("resources/dragon.jpg").getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        DIRT = new ImageIcon(new ImageIcon("resources/dirt.jpg").getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        WALL = new ImageIcon(new ImageIcon("resources/wall.jpg").getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        FINISH = new ImageIcon(new ImageIcon("resources/finish.jpg").getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        UNKNOWN = new ImageIcon(new ImageIcon("resources/unknown.jpg").getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        GridLayout layout = new GridLayout(21, 21);
        this.setLayout(layout);
        frame.addKeyListener(this);
        try {
            mapmaker(mapnum);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        };

    }

    protected void backToMenu() {
        frame.removeKeyListener(this);
        frame.remove(this);
        frame.getContentPane().add(menu);
        frame.setPreferredSize(new Dimension(800, 800));
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    /**
     * 
     * @param r
     * @param c
     * @param character
     * @return 
     * Megkapja a mozgatandó karaktert. Falra nem lép, sárkánnyal nem lép kijáratra. Frissíti a képeket lépés esetén. 
     * Megszakítja a játékot vesztés/nyerés esetén.
     */
    public boolean move(int r, int c, Character character) {
        if (gameTable[character.getPosR() + r][character.getPosC() + c] != Block.WALL) {
            if (character.characterType() == 'd' && gameTable[character.getPosR() + r][character.getPosC() + c] == Block.FINISH) {
                return false;
            }
            ImageIcon temp;
            if (gameTable[character.getPosR() + r][character.getPosC() + c] == Block.FINISH) {
                temp = new ImageIcon(new ImageIcon("resources/victory.jpg").getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
            } else {
                temp = (ImageIcon) imageTable[character.getPosR()][character.getPosC()].getIcon();
            }

            imageTable[character.getPosR()][character.getPosC()].setIcon(imageTable[character.getPosR() + r][character.getPosC() + c].getIcon());
            imageTable[character.getPosR() + r][character.getPosC() + c].setIcon(temp);
            gameTable[character.getPosR()][character.getPosC()] = Block.NOTHING;
            if (character.characterType() == 'p') {
                if (gameTable[character.getPosR() + r][character.getPosC() + c] == Block.FINISH) {
                    gameTable[character.getPosR() + r][character.getPosC() + c] = Block.VICTORY;
                } else {
                    gameTable[character.getPosR() + r][character.getPosC() + c] = Block.PLAYER;
                }
                if (!startedYet) {
                    startedYet = true;
                    dragonTimer.scheduleAtFixedRate(dragonMoving, 0, 800);
                }
            } else {
                gameTable[character.getPosR() + r][character.getPosC() + c] = Block.DRAGON;
            }
            character.setPosR(character.getPosR() + r);
            character.setPosC(character.getPosC() + c);
            if (gameTable[character.getPosR()][character.getPosC()] == Block.VICTORY) {
                dragonTimer.cancel();
                dragonTimer.purge();
                levelsCompleted++;
                Object[] options = {"OK"};
                int input;
                if (levelsCompleted != availableLevels) {
                    input = JOptionPane.showOptionDialog(frame,
                            "You won!", "Congragulations!",
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]);
                } else {
                    input = JOptionPane.showOptionDialog(frame,
                            "You've completed all the available levels!", "Congragulations!",
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]);
                }

                if (input == JOptionPane.OK_OPTION) {
                    frame.getContentPane().remove(this);
                    if (levelsCompleted != availableLevels) {
                        frame.getContentPane().add(new GameArea(frame, menu, levelsCompleted + 1));
                    } else {
                        backToMenu();
                    }
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                }
            }

            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {
                    if (gameTable[player.getPosR() + i][player.getPosC() + j] == Block.DRAGON) {
                        dragonTimer.cancel();
                        dragonTimer.purge();
                        Object[] options = {"OK"};
                        int input = JOptionPane.showOptionDialog(frame,
                                "You died!", "Game over!",
                                JOptionPane.PLAIN_MESSAGE,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                options,
                                options[0]);

                        if (input == JOptionPane.OK_OPTION) {
                            backToMenu();
                        }
                    }
                }
            }
            visionUpdate(r, c);
            return true;
        }
        return false;
    }
    /**
     * 
     * @param r
     * @param c 
     * Minden lépés után meghívódik. Frissíti a 3 sugarú látókört.
     */
    public void visionUpdate(int r, int c) {
        imageTable[dragon.getPosR()][dragon.getPosC()].setIcon(UNKNOWN);
        int pr = player.getPosR();
        int pc = player.getPosC();
        for (int i = -3; i < 4; ++i) {
            for (int j = -3; j < 4; ++j) {
                if (pr + i >= 0 && pr + i < 21 && pc + j >= 0 && pc + j < 21) {
                    switch (gameTable[pr + i][pc + j]) {
                        case NOTHING:
                            imageTable[pr + i][pc + j].setIcon(DIRT);
                            break;
                        case WALL:
                            imageTable[pr + i][pc + j].setIcon(WALL);
                            break;
                        case PLAYER:
                            imageTable[pr + i][pc + j].setIcon(PLAYER);
                            break;
                        case DRAGON:
                            imageTable[pr + i][pc + j].setIcon(DRAGON);
                            break;
                        case FINISH:
                            imageTable[pr + i][pc + j].setIcon(FINISH);
                            break;
                    }
                }
            }
        }
        if (r == -1 && c == 0) {
            for (int i = -3; i < 4; ++i) {
                if (pr + 4 < 21 & pc + i >= 0 && pc + i < 21) {
                    imageTable[pr + 4][pc + i].setIcon(UNKNOWN);
                }
            }
        } else if (r == 1 && c == 0) {
            for (int i = -3; i < 4; ++i) {
                if (pr - 4 >= 0 & pc + i >= 0 && pc + i < 21) {
                    imageTable[pr - 4][pc + i].setIcon(UNKNOWN);
                }
            }
        } else if (r == 0 && c == -1) {
            for (int i = -3; i < 4; ++i) {
                if (pr + i >= 0 & pr + i < 21 && pc + 4 < 21) {
                    imageTable[pr + i][pc + 4].setIcon(UNKNOWN);
                }
            }
        } else if (r == 0 && c == 1) {
            for (int i = -3; i < 4; ++i) {
                if (pr + i >= 0 & pr + i < 21 && pc - 4 >= 0) {
                    imageTable[pr + i][pc - 4].setIcon(UNKNOWN);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e
    ) {

    }

    @Override
    public void keyPressed(KeyEvent e
    ) {
    }

    /**
     * @param e
     * A négy nyílbillentyűt kezeli, és a move segítségével mozgatja a karaktereket.
     */
    @Override
    public void keyReleased(KeyEvent e
    ) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                System.out.println(keyCode);
                move(-1, 0, player);
                break;
            case KeyEvent.VK_DOWN:
                System.out.println(keyCode);
                move(1, 0, player);
                break;
            case KeyEvent.VK_LEFT:
                System.out.println(keyCode);
                move(0, -1, player);
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println(keyCode);
                move(0, 1, player);
                break;
        }
    }

    /**
     * 
     * @param mapnum
     * @throws FileNotFoundException
     * @throws IOException 
     * A txt fájlból felépíti a maonum számú pályát.
     */
    public void mapmaker(int mapnum) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/map" + mapnum + ".txt"));

        for (int i = 0; i < 21; ++i) {
            String line = reader.readLine();
            for (int j = 0; j < 21; ++j) {
                JLabel block;
                switch (line.charAt(j)) {
                    case ' ':
                        block = new JLabel(UNKNOWN);
                        imageTable[i][j] = block;
                        this.add(block);
                        gameTable[i][j] = Block.NOTHING;
                        break;
                    case 'w':
                        block = new JLabel(UNKNOWN);
                        imageTable[i][j] = block;
                        this.add(block);
                        gameTable[i][j] = Block.WALL;
                        break;
                    case 'f':
                        block = new JLabel(UNKNOWN);
                        imageTable[i][j] = block;
                        this.add(block);
                        gameTable[i][j] = Block.FINISH;
                        break;
                    case 'p':
                        block = new JLabel(PLAYER);
                        imageTable[i][j] = block;
                        this.add(block);
                        gameTable[i][j] = Block.PLAYER;
                        player = new Player(i, j);
                        break;
                    case 'd':
                        block = new JLabel(UNKNOWN);
                        imageTable[i][j] = block;
                        this.add(block);
                        gameTable[i][j] = Block.DRAGON;
                        dragon = new Dragon(i, j);
                        break;
                }
            }
        }
    }
}
