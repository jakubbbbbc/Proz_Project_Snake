import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventManager extends JPanel{

    private static final int DELAY = 200; //200 default
    private static final int DisplayDelay = 3000; // for messages
    private static final int BoosterDelay= 10000; //200 default
    private static final int FlashingDelay= 500; //for wall flashing
    private Timer t;
    private Timer messageTimer;
    private Timer boosterTimer;
    private Timer flashingTimer;
    private boolean menu;
    private boolean alreadyMoved;
    private boolean wallAccess;
    private GameBoard b;

    public EventManager(){
        b = null;
        menu = true;
        alreadyMoved= false;

        t= new Timer(DELAY, defaultTimerAction);
        messageTimer = new Timer(DisplayDelay, messageTimerAction);
        boosterTimer = new Timer(BoosterDelay, boosterTimerAction);
        flashingTimer = new Timer (FlashingDelay, flashingTimerAction);

        addKeyBind("RIGHT", 0);
        addKeyBind("UP", 1);
        addKeyBind("LEFT", 2);
        addKeyBind("DOWN", 3);

        addKeySpace();
        addKeyP();
        addKeyL();
    }

    /**
     * updates and repaints board and scores during the game
     */
    ActionListener defaultTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (!menu){
                b.updateTail();
                b.getSnake().updatePositions(wallAccess, b.getBoardX(), b.getBoardY());
                if (!b.updateHead()) {
                    menu = true;
                }
                repaint();
                alreadyMoved= false;
            }
        }
    };

    /**
     * for displaying messages
     */
    ActionListener messageTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            messageTimer.stop();
        }
    };

    /**
     * for some boosters working time
     */
    ActionListener boosterTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            boosterTimer.stop();
            b.setPointBoost(1);
            int[] sfp = b.getSuperFoodPos();
            if (sfp !=null)
                b.getBoard()[sfp[0]][sfp[1]] = 0;
            wallAccess = false;
        }
    };

    /**
     * for flashing elements
     */
    ActionListener flashingTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (timesFlashed< BoosterDelay/FlashingDelay){ // times to repeat
                if (wallFlashing)
                    wallInvisible = !wallInvisible;
                superFoodVisible = !superFoodVisible;
                ++timesFlashed;
                flashingTimer.restart();
            }
            else {
                wallInvisible = false;
                superFoodVisible = false;
            }
        }
    };

    /**
     * space initializes game when in menu
     */
    public void addKeySpace() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "keySpace");
        getActionMap().put("keySpace", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                if (menu){
                    b.gameIni();
                    menu = false;
                }
            }
        });
    }

    /**
     * for pause during the game
     */
    public void addKeyP() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"), "keyP");
        getActionMap().put("keyP", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                if (!menu) {
                    if (t.isRunning())
                        t.stop();
                    else
                        t.restart();
                }

            }
        });
    }

    /**
     * special actions for developer use
     */
    public void addKeyL() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("L"), "keyL");
        getActionMap().put("keyL", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                b.newBoost();
            }
        });
    }

    /**
     * connects the given key with changing the snake direction
     * @param key keyboard key
     * @param newDir direction to bind with the key
     */
    public void addKeyBind(String key, int newDir) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                if (!alreadyMoved)
                    b.getSnake().changeDir(newDir);
                alreadyMoved = true;
            }
        });
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.b = gameBoard;
    }

    public void setWallAccess(boolean newValue){
        wallAccess = newValue;
    }

    public Timer getT(){
        return t;
    }

    public Timer getMessageTimer(){
        return messageTimer;
    }

    public Timer getBoosterTimer(){
        return boosterTimer;
    }

    public Timer getFlashingTimer(){
        return flashingTimer;
    }

    public int getDELAY(){
        return DELAY;
    }
}
