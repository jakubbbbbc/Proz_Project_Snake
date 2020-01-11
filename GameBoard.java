import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


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
   // private int[] applePos;
    private Snake s;
    private Color snakeColor;
   // private boolean done;
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
    String playerName;
    String highScorePlayerName;


    public GameBoard(){
        /*board = new int[BoardX][BoardY];
        newApple();
        s = new Snake(new int[] {2,3}, 3);
        board[s.getHeadPos()[0]][s.getHeadPos()[1]] = 2;
        board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
        board[s.getTailPos()[0]][s.getTailPos()[1]] = 1;*/
        //done = false;
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

        //t.start();

        appleImage = new ImageIcon("apple1.png");

        //playerName = "default";
        playerName = askForName();
        snakeColor = askForSnakeColor();

    }

    public JFrame iniGUI(){
        JFrame f=new JFrame("Snake");//creating instance of JFrame
        //setPreferredSize(new Dimension(800, 800));
        f.setSize(BoardX * SpotSize+16, BoardY * SpotSize+37+60); // 15 = poprawka, 37 = poprawka, 60 = na scorsy
        //f.setSize(800,400);
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return f;
    }

    public String askForName(){
        String name;
        name = JOptionPane.showInputDialog(this,
                "Username",
                "Insert your username",
                JOptionPane.QUESTION_MESSAGE
        );
        if (null == name) {

            name = "unknown";
        }

        //name = name.trim();
        if ("".equals(name) || name.length()>10) {
            return "unknown";
        } else {
           return name;
        }
    }

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

    public void game(){
        //GameBoard b= new GameBoard();
        iniGUI().add(this);

        //game loop
        /*while (true) {
            //System.out.println(s.getHeadPos()[0]);
            if (menu){
                System.out.println("menu");
            }
            //t.restart(); żeby nie pisało dwa razy??
            //System.out.println(s.getHeadPos()[0]);
        }*/
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //clears display

        g.setColor(Color.black);
        g.setFont(new Font("Monaco", Font.BOLD, 15));
        g.drawString(playerName + " score: "+score, 0, BoardY * SpotSize+20); //+20 = poprawka
        g.drawString("High score ("+highScorePlayerName + "): "+ highScore, 0, BoardY * SpotSize+45);
        //g.drawString(playerName, 300, BoardY * SpotSize+20);
        //System.out.println(playerName);
        if (messageTimer.isRunning())
            displayMessage(messageID, g);

        if (menu){
            //TODO display highScore and player
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

    public void displayMessage(int messageID, Graphics g){
        String message = "";
        switch (messageID){
            case 3:
                //for apple, don't change the message
                break;
            case 4:
                message = "Snake is now slower!";
                break;
            case 5:
                message = "Snake is now shorter!";
                break;
            case 6:
                message = "Bonus 3 points received!";
                break;
            case 7:
                message = "Points count double for " + BoosterDelay/1000 + " seconds!";
                break;
            case 8:
                message = "Super food available for " + BoosterDelay/1000 + " seconds!";
                break;
            case 9:
                message = "You can go through walls " + BoosterDelay/1000 + " seconds!";
                break;
            default:
                message = "";
                System.out.println("Wrong message ID, go to displayMessage()");
                break;
        }
        g.drawString(message, 250, BoardY * SpotSize+20);
    }

    public void paintSpot(int x, int y, Color c, Graphics g) {
        g.setColor(c);
        //appleImage.paintIcon(this, g, x* SpotSize, y*SpotSize);
        g.fillRect(x* SpotSize, y*SpotSize, SpotSize, SpotSize);
    }

    public void paintImage(int x, int y, ImageIcon icon, Graphics g){
        icon.paintIcon(this, g, x* SpotSize, y*SpotSize);
    }

    public void newApple(){
        int[] applePos= new int[] {(int)(Math.random() * BoardX), (int)(Math.random() * BoardY)};
        boolean isOk=true;
        if (board[applePos[0]][applePos[1]] != 0) {
            isOk = false;
            newApple();
        }
        if (isOk)
            board[applePos[0]][applePos[1]] = 3;
       //m.out.println("apple pos: " + applePos[0] + ", " + applePos[1]);
    }

    public void newBoost(){
        board[boosterPos[0]][boosterPos[1]] = 0;
        int[] newBoostPos= new int[] {(int)(Math.random() * BoardX), (int)(Math.random() * BoardY)};
        boolean isOk = true;
        if (board[newBoostPos[0]][newBoostPos[1]] != 0) {
            isOk = false;
            newBoost();
        }
        if (isOk)
            board[newBoostPos[0]][newBoostPos[1]] = (int) (Math.random()*4+6); //boost type, default: * NumOfBoosters +4, currently num = 6
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
        //System.out.println("boost pos: " + newBoostPos[0] + ", " + newBoostPos[1]);
        //System.out.println(board[newBoostPos[0]][newBoostPos[1]]);
    }

    public boolean updateHead(){
        int hpx = s.getHeadPos()[0], hpy= s.getHeadPos()[1];

        if (hpx == BoardX || hpx <0 || hpy<0 || hpy == BoardY || board[hpx][hpy] == 1) {
            //t.stop();
            System.out.println("pozycja "+ hpx + ", " + hpy + " jest słaba");
            return false;
        }
        else{
            if (board[hpx][hpy] > 2)
                hasEaten();
            board[hpx][hpy] = 2;
            board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
            return true;
        }
    }

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
                //System.out.println("length "+s.getLength()+" pos.size: "+s.getPos().size());
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
        //messageTimer.restart();
        //System.out.println("Delay: "+t.getDelay());
        if (score/5 > boostCount){ // new booster every 5 points;
            newBoost();
            ++boostCount;
        }

        if (score>highScore) {
            highScore = score;
            highScorePlayerName = playerName;
            saveHighScoreAndPlayer();
        }
        //System.out.println(score+"\n"+highScore+"\n");
    }

    public void makeShorter(int n){
        for (int i=0; i<n; ++i){
            board[s.getTailPos()[0]][s.getTailPos()[1]] = 0;
            s.removeTail();
        }
        //board[s.getTailPos()[0]][s.getTailPos()[1]] = 0;
    }

    public void updateTail(){
        if(!s.getHasEaten())
            board[s.getTailPos()[0]][s.getTailPos()[1]] = 0;
    }

    ActionListener defaultTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (!menu){
                //s.disPos(0);
                //System.out.println(pointBoost);
                updateTail();
                s.updatePositions(wallAccess, BoardX, BoardY);
                if (!updateHead()) {
                    menu = true;
                }
                repaint();
                alreadyMoved= false;
                //System.out.println(s.getLength());
            }
        }
    };

    ActionListener messageTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            //messageID = 0;
            messageTimer.stop();
        }
    };

    ActionListener boosterTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            boosterTimer.stop();
            pointBoost = 1;
            if (superFoodPos !=null)
                board[superFoodPos[0]][superFoodPos[1]] = 0;
            wallAccess = false;
        }
    };

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

    public void addKeySpace() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "keySpace");
        getActionMap().put("keySpace", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                //System.out.print("lalalal");
                if (menu){
                    gameIni();
                    menu = false;
                }
            }
        });
    }

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

        s = new Snake(new int[] {2,3}, 3);
        board[s.getHeadPos()[0]][s.getHeadPos()[1]] = 2;
        board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
        board[s.getTailPos()[0]][s.getTailPos()[1]] = 1;
        t.setDelay(DELAY);
        t.start();
    }

    public void addKeyP() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"), "keyP");
        getActionMap().put("keyP", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                if (!menu) {
                    System.out.println("P pressed");
                    if (t.isRunning())
                        t.stop();
                    else
                        t.restart();
                }

            }
        });
    }

    public void addKeyL() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("L"), "keyL");
        getActionMap().put("keyL", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("L pressed");
                newBoost();
            }
        });
    }

    public void addKeyBind(String key, int newDir) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                if (!alreadyMoved)
                    s.changeDir(newDir);
                alreadyMoved = true;
               // done= true;
            }
        });
    }

    public void saveHighScoreAndPlayer(){
        try {
            PrintWriter out = new PrintWriter("highScore.txt");
            out.println(highScore);
            out.println(highScorePlayerName);
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Niestety, nie mogę utworzyć pliku!");
        }
    }

    public void readHighScoreAndPlayer(){
        File reader = new File("highScore.txt");
       // String odczyt = "";
        try {
            // Utworzenie obiektu typu String
            Scanner sc = new Scanner(reader);
            highScore=Integer.parseInt(sc.next());
            highScorePlayerName=sc.next();
            // Odczytywanie kolejnych linii pliku dopóki są kolejne linie
            /*while (sc.hasNextLine()) {
                // Do łańcucha znaków dodawana jest zawartość kolejnej linii
                // oraz znak \n oznaczający następną linię
                odczyt = odczyt + skaner.nextLine() + "\n";
            }*/
        } catch (FileNotFoundException e) {
            System.out.println("Brak Pliku do odczytania!");
        }
    }


}
