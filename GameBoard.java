import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;


public class GameBoard extends JPanel{

    private static final int BoardX = 20;
    private static final int BoardY = 15;
    private static final int SpotSize = 30;
    private static final int DELAY = 200; //200 default
    private static final int DisplayDelay = 3000; // for messages
    private static final int BoosterDelay= 10000; //200 default
    private static final int FlashingDelay= 500; //for wall flashing
    private int[][] board;
    private int[] superFoodPos;
    private int[] boosterPos;
    private Snake s;
    private Color snakeColor;
    private boolean alreadyMoved;
    private boolean menu;
    private boolean wallAccess;
    private boolean wallInvisible;
    private boolean wallFlashing;
    private boolean superFoodVisible;
    private int timesFlashed;
    private Timer t;
    private Timer messageTimer;
    private Timer boosterTimer;
    private Timer flashingTimer;
    private int score;
    private int highScore;
    private int boostCount; // to spawn boosters
    private int pointBoost; // to multiply points
    private int messageID;
    private ImageIcon appleImage;
    private String playerName;
    private String highScorePlayerName;

    public GameBoard(){
        alreadyMoved= false;
        menu = true;

        readHighScoreAndPlayer();

        addKeyBind("RIGHT", 0);
        addKeyBind("UP", 1);
        addKeyBind("LEFT", 2);
        addKeyBind("DOWN", 3);

        addKeySpace();
        addKeyP();
        addKeyL();
        t= new Timer(DELAY, defaultTimerAction);
        messageTimer = new Timer(DisplayDelay, messageTimerAction);
        boosterTimer = new Timer(BoosterDelay, boosterTimerAction);
        flashingTimer = new Timer (FlashingDelay, flashingTimerAction);

        boosterPos = new int[]{0, 0};

        appleImage = new ImageIcon("apple1.png");

        playerName = askForName();
        snakeColor = askForSnakeColor();
    }

    /**
     * creates a JOptionPane to specify name
     * @return to playerName
     */
    public String askForName(){
        String name;
        name = JOptionPane.showInputDialog(this,
                "Username",
                "Insert your username",
                JOptionPane.QUESTION_MESSAGE
        );
        if (null == name || "".equals(name) || name.length()>10)
            return "unknown";
        else
            return name;
    }

    /**
     * asks to specify the snake color
     * @return to snakeColor
     */
    public Color askForSnakeColor(){
        String temp = JOptionPane.showInputDialog(this,
                "1 - gray\n2 - red\n3 - blue\n4 - black\n5 - orange",
                "Choose your snake color",
                JOptionPane.QUESTION_MESSAGE
        );

        if (null == temp)
            temp = "";

        switch (temp){
            case "2":
                return Color.red;
            case "3":
                return Color.blue;
            case "4":
                return Color.black;
            case "5":
                return Color.orange;
            default:
                return Color.gray;
        }
    }

    /**
     * paints the menu or game screen
     * @param g graphics to use
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //clears display

        g.setColor(Color.black);
        g.setFont(new Font("Monaco", Font.BOLD, 15));
        g.drawString(playerName + " score: "+score, 0, BoardY * SpotSize+20); //+20 = adjustment
        g.drawString("High score ("+highScorePlayerName + "): "+ highScore, 0, BoardY * SpotSize+45);
        if (messageTimer.isRunning())
            displayMessage(messageID, g);

        if (menu){
            g.setColor(Color.black);
            g.setFont(new Font("Monaco", Font.PLAIN, 40));
            g.drawString("Press Space to Play", 120, BoardY*SpotSize/2+80);
        }
        else {
            for (int i = 0; i < BoardX; ++i) {
                for (int j = 0; j < BoardY; ++j) {
                    switch (board[i][j]) {
                        case 0:
                            paintSpot(i, j, Color.white, g); // Background
                            break;
                        case 1:
                            paintSpot(i, j, snakeColor, g); // Body
                            break;
                        case 2:
                            paintSpot(i, j, Color.darkGray, g); // Head
                            break;
                        case 3:
                            paintImage(i, j, appleImage, g); // Apple
                            break;
                        case 4:
                            paintSpot(i, j, Color.blue, g); // delay+6
                            break;
                        case 5:
                            paintSpot(i, j, Color.ORANGE, g); // length-3
                            break;
                        case 6:
                            paintSpot(i, j, Color.BLACK, g); // bonus 3 points
                            break;
                        case 7:
                            paintSpot(i, j, Color.lightGray, g); // points double for 10 seconds
                            break;
                        case 8:
                            if (superFoodVisible)
                                paintSpot(i, j, Color.red, g); // points double for 10 seconds
                            else
                                paintSpot(i, j, Color.white, g); // Background
                            break;
                        case 9:
                            paintSpot(i, j, Color.GREEN, g); // points double for 10 seconds
                            break;
                        default:
                            break;
                    }
                }
            }
            if (!wallInvisible) {
                g.setColor(Color.black);
                g.drawLine(0, 0, BoardX * SpotSize, 0);
                g.drawLine(0, 0, 0, BoardY * SpotSize);
                g.drawLine(BoardX * SpotSize, 0, BoardX * SpotSize, BoardY * SpotSize);
                g.drawLine(0, BoardY * SpotSize, BoardX * SpotSize, BoardY * SpotSize);
            }

        }
    }

    /**
     * displays appropriate message on the bottom of the screen
     * @param messageID determines message content
     * @param g graphics to use
     */
    public void displayMessage(int messageID, Graphics g){
        AtomicReference<String> message = new AtomicReference<>("");
        switch (messageID){
            case 3:
                //for apple, don't change the message
                break;
            case 4:
                message.set("Snake is now slower!");
                break;
            case 5:
                message.set("Snake is now shorter!");
                break;
            case 6:
                message.set("Bonus 3 points received!");
                break;
            case 7:
                message.set("Points count double for " + BoosterDelay / 1000 + " seconds!");
                break;
            case 8:
                message.set("Super food available for " + BoosterDelay / 1000 + " seconds!");
                break;
            case 9:
                message.set("You can go through walls " + BoosterDelay / 1000 + " seconds!");
                break;
            default:
                message.set("");
                break;
        }
        g.drawString(message.get(), 250, BoardY * SpotSize+20);
    }

    /**
     * paints spot on depending on the value of board[x][y]
     * @param x coordinate
     * @param y coordinate
     * @param c color
     * @param g graphics to use
     */
    public void paintSpot(int x, int y, Color c, Graphics g) {
        g.setColor(c);
        g.fillRect(x * SpotSize, y * SpotSize, SpotSize, SpotSize);
    }

    /**
     * paints the icon on board[x][y]
     * @param x position x on board
     * @param y position y on board
     * @param icon what to draw
     * @param g graphics to use
     */
    public void paintImage(int x, int y, ImageIcon icon, Graphics g){
        icon.paintIcon(this, g, x* SpotSize, y*SpotSize);
    }

    /**
     * sets value of a new place on board to apple value (3)
     */
    public void newApple(){
        int[] applePos= new int[] {(int)(Math.random() * BoardX), (int)(Math.random() * BoardY)};
        boolean isOk=true;
        if (board[applePos[0]][applePos[1]] != 0) {
            isOk = false;
            newApple();
        }
        if (isOk)
            board[applePos[0]][applePos[1]] = 3;
    }

    /**
     * sets value of a new place on board to random booster value (4-9)
     */
    public void newBoost(){
        board[boosterPos[0]][boosterPos[1]] = 0;
        int[] newBoostPos= new int[] {(int)(Math.random() * BoardX), (int)(Math.random() * BoardY)};
        boolean isOk = true;
        if (board[newBoostPos[0]][newBoostPos[1]] != 0) {
            isOk = false;
            newBoost();
        }
        if (isOk)
            board[newBoostPos[0]][newBoostPos[1]] = (int) (Math.random()*6+4); //boost type, default: * NumOfBoosters +4, currently num = 6
        if (8 == board[newBoostPos[0]][newBoostPos[1]]){
            superFoodPos = newBoostPos;
            superFoodVisible = true;
            timesFlashed = 0;
            flashingTimer.restart();
            messageID=8;
            messageTimer.restart();
            boosterTimer.restart();
        }
        else
            boosterPos = newBoostPos;
    }

    /**
     * checks for collision or potential food eaten
     * @return if the snake has't hit something
     */
    public boolean updateHead(){
        int hpx = s.getHeadPos()[0], hpy= s.getHeadPos()[1];

        if (hpx == BoardX || hpx <0 || hpy<0 || hpy == BoardY || board[hpx][hpy] == 1)
            return false;
        else{
            if (board[hpx][hpy] > 2)
                hasEaten();
            board[hpx][hpy] = 2;
            board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
            return true;
        }
    }

    /**
     * performs action depending on what time of food was consumed
     */
    public void hasEaten() {
        //what food?
        int atHeadPos = board[s.getHeadPos()[0]][s.getHeadPos()[1]];
        messageID = atHeadPos;
        switch (atHeadPos){
            case 3: //apple
                s.setHasEaten(true);
                score+= pointBoost;
                newApple();
                if (t.getDelay()>50)
                    t.setDelay(t.getDelay()-2);
                break;
            case 4: // delay+10
                t.setDelay(t.getDelay()+6);
                messageTimer.restart();
                break;
            case 5: // shorter by 3
                makeShorter(3);
                messageTimer.restart();
                break;
            case 6: // bonus points
                score+=3;
                messageTimer.restart();
                break;
            case 7: // points *2
                pointBoost = 2;
                messageTimer.restart();
                boosterTimer.restart();
                break;
            case 8: // superFood (10 pts)
                score += 10;
                boostCount += 2; // to not display new booster
                break;
            case 9: // going through walls for 10s
                wallAccess = true;
                wallFlashing = true;
                messageTimer.restart();
                boosterTimer.restart();
                timesFlashed = 0;
                flashingTimer.restart();
                break;
            default:
                break;
        }
        if (score/5 > boostCount){ // new booster every 5 points;
            newBoost();
            ++boostCount;
        }

        if (score>highScore) {
            highScore = score;
            highScorePlayerName = playerName;
            saveHighScoreAndPlayer();
        }
    }

    /**
     * removes tail of snake n times
     * @param n how much shorter the snake will be
     */
    public void makeShorter(int n){
        for (int i=0; i<n; ++i){
            board[s.getTailPos()[0]][s.getTailPos()[1]] = 0;
            s.removeTail();
        }
    }

    /**
     * if the snake didn't eat, sets board at tail position to 0
     */
    public void updateTail() {
        if (!s.getHasEaten())
            board[s.getTailPos()[0]][s.getTailPos()[1]] = 0;
    }

    /**
     * updates and repaints board and scores during the game
     */
    ActionListener defaultTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (!menu){
                updateTail();
                s.updatePositions(wallAccess, BoardX, BoardY);
                if (!updateHead()) {
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
            pointBoost = 1;
            if (superFoodPos !=null)
                board[superFoodPos[0]][superFoodPos[1]] = 0;
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
                    gameIni();
                    menu = false;
                }
            }
        });
    }

    /**
     * initialize everything for a new game and start it
     */
    public void gameIni(){
        board = new int[BoardX][BoardY];

        newApple();
        score = 0;
        boostCount = 0;
        pointBoost = 1;
        messageID = 0;

        wallAccess = false;
        wallInvisible = false;
        wallFlashing = false;
        timesFlashed = 0;

        s = new Snake(new int[] {2,3});
        board[s.getHeadPos()[0]][s.getHeadPos()[1]] = 2;
        board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
        board[s.getTailPos()[0]][s.getTailPos()[1]] = 1;
        t.setDelay(DELAY);
        t.start();
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
                newBoost();
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
                    s.changeDir(newDir);
                alreadyMoved = true;
            }
        });
    }

    /**
     * saves high score and player with high score to a txt file
     */
    public void saveHighScoreAndPlayer() {
        PrintWriter out = null;
        try {
            out = new PrintWriter("highScore.txt");
        } catch (FileNotFoundException e) {
            //no file -> can't save highScore. Nothing we can do
        }
        if (out != null) {
            out.println(highScore);
            out.println(highScorePlayerName);
            out.close();
        }
    }

    /**
     * reads high score and player with high score from a txt file
     */
    public void readHighScoreAndPlayer(){
        File reader = new File("highScore.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(reader);
        } catch (FileNotFoundException e) {
            //no file -> no highScore yet. Everything ok
        }
        if (sc != null) {
        highScore = Integer.parseInt(sc.next());
        highScorePlayerName = sc.next();
            sc.close();
        }
    }

    public int[][] getBoard(){
        return board;
    }

    public Snake getSnake(){
        return s;
     }

}
