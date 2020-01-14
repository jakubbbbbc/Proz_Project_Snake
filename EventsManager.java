import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventsManager extends JPanel{

    private static final int DELAY = 200; //200 default
    private static final int DisplayDelay = 3000; // for messages
    private static final int BoosterDelay= 10000; //200 default
    private static final int FlashingDelay= 500; //for wall flashing
    private GameBoard b;
    private Interface i;
    private Timer t;
    private Timer messageTimer;
    private Timer boosterTimer;
    private Timer flashingTimer;
    private int timesFlashedWall;
    private int timesFlashedFood;
    private boolean menu;
    private boolean alreadyMoved;
    private boolean wallAccess;

    public EventsManager(){
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

        t.restart();
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
                alreadyMoved= false;
                i.repaint();
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
            if (timesFlashedWall < BoosterDelay/FlashingDelay){ // times to repeat
                if (b.getWallFlashing())
                    b.setWallVisible(!b.getWallVisible());
                ++timesFlashedWall;
                flashingTimer.restart();
            }
            else
                b.setWallVisible(true);

            if (timesFlashedFood < BoosterDelay/FlashingDelay){ // times to repeat
                b.setSuperFoodVisible(!b.getSuperFoodVisible());
                ++timesFlashedFood;
                flashingTimer.restart();
            }
            else
                b.setSuperFoodVisible(false);
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
        b = gameBoard;
    }

    public void setInterface (Interface newIF){
        i = newIF;
    }

    public void setWallAccess(boolean newValue){
        wallAccess = newValue;
    }

    public void resetTimesFlashedWall(){
        timesFlashedWall = 0;
    }

    public void resetTimesFlashedFood(){
        timesFlashedFood = 0;
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

    public boolean getMenu(){
        return menu;
    }


}
