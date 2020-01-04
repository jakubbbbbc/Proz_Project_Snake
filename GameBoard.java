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
    private static final int DELAY = 100;
    private int[][] board;
   // private int[] applePos;
    private Snake s;
   // private boolean done;
    private boolean alreadyMoved;
    private boolean menu;
    private Timer t;
    private int score;
    private int highScore;
    private int boostCount;

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

        readHighScore();

        addKeyBind("RIGHT", 0);
        addKeyBind("UP", 1);
        addKeyBind("LEFT", 2);
        addKeyBind("DOWN", 3);

        addKeySpace();
        addKeyP();
        addKeyL();
        t= new Timer(DELAY, taskPerformer);
        //t.start();

    }

    public JFrame iniGUI(){
        JFrame f=new JFrame("Snake");//creating instance of JFrame
        //setPreferredSize(new Dimension(800, 800));
        f.setSize(BoardX * SpotSize+15, BoardY * SpotSize+37+30); // 15 = poprawka, 37 = poprawka, 30 = na scorsy
        //f.setSize(800,400);
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return f;
    }

    public void game(){

        GameBoard b= new GameBoard();
        iniGUI().add(b);
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
        g.drawString("Score: "+score, 0, BoardY * SpotSize+20); //+20 = poprawka
        g.drawString("High score: "+highScore, 100, BoardY * SpotSize+20);

        if (menu){
            //TODO display highScore and player
            g.setColor(Color.black);
            g.setFont(new Font("Monaco", Font.PLAIN, 40));
            g.drawString("Press Space to Play", 120, BoardY*SpotSize/2+80);
        }
        else for (int i = 0; i < BoardX; ++i) {
            for (int j = 0; j < BoardY; ++j) {
                switch (board[i][j]){
                    case 0:
                        paintSpot(i, j, Color.white, g); // Background
                        break;
                    case 1:
                        paintSpot(i, j, Color.gray, g); // Body
                        break;
                    case 2:
                        paintSpot(i, j, Color.darkGray, g); // Head
                        break;
                    case 3:
                        paintSpot(i, j, Color.RED, g); // Apple
                        break;
                    case 4:
                        paintSpot(i, j, Color.blue, g); // delay+10
                        break;
                    case 5:
                        paintSpot(i, j, Color.ORANGE, g); // length-5
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void paintSpot(int x, int y, Color c, Graphics g) {
        g.setColor(c);
        g.fillRect(x* SpotSize, y*SpotSize, SpotSize, SpotSize);
    }

    public void newApple(){
        int[] applePos= new int[] {(int)(Math.random() * BoardX), (int)(Math.random() * BoardY)};
        if (board[applePos[0]][applePos[1]] != 0)
            newApple();
        board[applePos[0]][applePos[1]] = 3;
       //m.out.println("apple pos: " + applePos[0] + ", " + applePos[1]);
    }

    public void newBoost(){
        int[] boostPos= new int[] {(int)(Math.random() * BoardX), (int)(Math.random() * BoardY)};
        if (board[boostPos[0]][boostPos[1]] != 0)
            newApple();
        board[boostPos[0]][boostPos[1]] = (int) (Math.random()*1+5); //boost type
        //System.out.println("boost pos: " + boostPos[0] + ", " + boostPos[1]);
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
        switch (board[s.getHeadPos()[0]][s.getHeadPos()[1]]){
            case 3: //apple
                s.setHasEaten(true);
                ++score;
                newApple();
                if (t.getDelay()>50)
                    t.setDelay(t.getDelay()-2);
                break;
            case 4: // delay+10
                t.setDelay(t.getDelay()+10);
                break;
            case 5:
                shortLength(5);
                //System.out.println("length "+s.getLength()+" pos.size: "+s.getPos().size());
                break;
            default:
                break;
        }
        //System.out.println("Delay: "+t.getDelay());
        if (score/5 > boostCount){ // new booster every 5 points;
            newBoost();
            ++boostCount;
        }

        if (score>highScore) {
            highScore = score;
            saveHighScore();
        }
        //System.out.println(score+"\n"+highScore+"\n");
    }

    public void shortLength(int n){
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

    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (!menu){
                //s.disPos(0);
                //System.out.println(menu);
                updateTail();
                s.updatePositions();
                if (updateHead())
                    repaint();
                else {
                    menu = true;
                    repaint();
                }
                alreadyMoved= false;
                //System.out.println(s.getLength());
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
        boostCount=0;

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
                if (!menu) {
                    System.out.println("L pressed");

                }

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

    public void saveHighScore(){
        try {
            PrintWriter out = new PrintWriter("highScore.txt");
            out.println(highScore);
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Niestety, nie mogę utworzyć pliku!");
        }
    }

    public void readHighScore(){
        File reader = new File("highScore.txt");
       // String odczyt = "";
        try {
            // Utworzenie obiektu typu String
            Scanner sc = new Scanner(reader);
            highScore=Integer.parseInt(sc.next());
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
