import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;

public class Interface extends JPanel {

    private GameBoard b;
    private EventsManager e;
    private ImageIcon appleImage;
    int messageID;
    private Color snakeColor;
    Timer t1;

    public Interface(){
        b = null;
        appleImage = new ImageIcon("apple1.png");
        t1= new Timer(200, defaultTimerAction);
        t1.start();
    }

    /**
     * creates a GUI
     * @return JFrame
     */
    public JFrame iniGUI(int x, int y, int size){
        JFrame f=new JFrame("Snake");
        f.setSize(x * size+16, y * size+37+60); // 16, 37 - adjustments, 60 - for scores and messages on the bottom
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return f;
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
     */
    public void askForSnakeColor(){
        String temp = JOptionPane.showInputDialog(this,
                "1 - gray\n2 - red\n3 - blue\n4 - black\n5 - orange",
                "Choose your snake color",
                JOptionPane.QUESTION_MESSAGE
        );

        if (null == temp)
            temp = "";

        switch (temp){
            case "2":
                snakeColor = Color.red;
            case "3":
                snakeColor = Color.blue;
            case "4":
                snakeColor = Color.black;
            case "5":
                snakeColor = Color.orange;
            default:
                snakeColor = Color.gray;
        }
    }

    /**
     * paints the menu or game screen
     * @param g graphics to use
     */
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g); //clears display
        System.out.println("paint component");
        g.setColor(Color.black);
        g.setFont(new Font("Monaco", Font.BOLD, 15));
        g.drawString(b.getPlayerName() + " score: "+b.getScore(), 0, b.getBoardY() * b.getSpotSize()+20); //+20 = adjustment
        g.drawString("High score ("+b.getHighScorePlayerName() + "): "+ b.getHighScore(), 0, b.getBoardY() * b.getSpotSize()+45);
        if (e.getMessageTimer().isRunning())
            displayMessage(messageID, g);

        if (e.getMenu()){
            g.setColor(Color.black);
            g.setFont(new Font("Monaco", Font.PLAIN, 40));
            g.drawString("Press Space to Play", 120, b.getBoardY()*b.getSpotSize()/2+80);
        }
        else {
            for (int i = 0; i < b.getBoardX(); ++i) {
                for (int j = 0; j < b.getBoardY(); ++j) {
                    switch (b.getBoard()[i][j]) {
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
                            if (b.getSuperFoodVisible())
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
            if (b.getWallVisible()) {
                int x = b.getBoardX();
                int y = b.getBoardX();
                int size = b.getSpotSize();
                g.setColor(Color.black);
                g.drawLine(0, 0, x * size, 0);
                g.drawLine(0, 0, 0, y * size);
                g.drawLine(x * size, 0, x * size, y * size);
                g.drawLine(0, y * size, x * size, y * size);
            }

        }
    }

    public void paintAll(){
        //repaint();
       // System.out.println("painted");
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
                message.set("Points count double for " + e.getBoosterTimer().getDelay() / 1000 + " seconds!");
                break;
            case 8:
                message.set("Super food available for " + e.getBoosterTimer().getDelay() / 1000 + " seconds!");
                break;
            case 9:
                message.set("You can go through walls " + e.getBoosterTimer().getDelay() / 1000 + " seconds!");
                break;
            default:
                message.set("");
                break;
        }
        g.drawString(message.get(), 250, b.getBoardY() * b.getSpotSize()+20);
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
        int sp = b.getSpotSize();
        g.fillRect(x * sp, y * sp, sp,sp);
    }

    /**
     * paints the icon on board[x][y]
     * @param x position x on board
     * @param y position y on board
     * @param icon what to draw
     * @param g graphics to use
     */
    public void paintImage(int x, int y, ImageIcon icon, Graphics g){
        icon.paintIcon(this, g, x* b.getSpotSize(), y*b.getSpotSize());
    }

    public void setMessageID(int newID){
        messageID = newID;
    }

    public void setGameBoard(GameBoard gameBoard) {
        b = gameBoard;
    }

    public void setEventManager(EventsManager em) {
        e = em;
    }

    ActionListener defaultTimerAction = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            System.out.println("t1");
            //revalidate();
            setIgnoreRepaint(false);
            repaint();
        }
    };
}
