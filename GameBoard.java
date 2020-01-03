import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameBoard extends JPanel{

    private static final int BoardX = 20;
    private static final int BoardY = 15;
    private static final int SpotSize =30;
    private int[][] board;
    private Snake s;
    private boolean done;

    private Timer t;

    public GameBoard(){

        board = new int[BoardX][BoardY];
        s = new Snake(new int[] {2,3}, 3);
        board[s.getHeadPos()[0]][s.getHeadPos()[1]] = 2;
        board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
        board[s.getTailPos()[0]][s.getTailPos()[1]] = 1;

        done = false;

        addKeyBind("RIGHT", 0);
        addKeyBind("UP", 1);
        addKeyBind("LEFT", 2);
        addKeyBind("DOWN", 3);

        t= new Timer(1000, taskPerformer);
        t.start();

    }


    public void game(){

        JFrame f=new JFrame("Snake");//creating instance of JFrame
        //setPreferredSize(new Dimension(800, 800));
        f.setSize(BoardX * SpotSize+15, BoardY * SpotSize+37);
        //f.setSize(800,400);
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameBoard b= new GameBoard();
        f.add(b);

        //game loop
        while (!done) {

            t.restart();
            boolean running = true;
            boolean menu = true;

            while (!menu) {
                return;
            }

            //System.out.println(board[3][3]);
            //s.disPos(0);
        }

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //clears display
        for (int i = 0; i < BoardX; ++i) {
            for (int j = 0; j < BoardY; ++j) {
                if (board[i][j] == 0)
                    paintSpot(i, j, Color.white, g); // Background
                else if (board[i][j] == 1)
                    paintSpot(i, j, Color.gray, g); // Body
                else if (board[i][j] == 2)
                    paintSpot(i, j, Color.darkGray, g); // Head
                else if (board[i][j] == 3)
                    paintSpot(i, j, Color.RED, g); // Food
            }
        }
    }

    public boolean updateHead(){
        int hpx = s.getHeadPos()[0], hpy= s.getHeadPos()[1];
        if (hpx == BoardX || hpx <0 || hpy<0 || hpy == BoardY) {
            done = true;
            t.stop();
            System.out.println("pozycja "+ hpx + " jest slaba");
            return false;
        }
        else{
            board[hpx][hpy] = 2;
            board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
            return true;
        }
    }

    public void updateTail(){
        if(!s.getHasEaten())
            board[s.getTailPos()[0]][s.getTailPos()[1]] = 0;
    }

    public void paintSpot(int x, int y, Color c, Graphics g) {
        g.setColor(c);
        g.fillRect(x* SpotSize, y*SpotSize, SpotSize, SpotSize);
    }

    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            //s.disPos(0);
            //System.out.println(s.getHeadPos()[0]);
            updateTail();
            s.updatePositions();
            if (updateHead())
                repaint();
            //System.out.println(s.getHeadPos()[0]);
        }
    };

    public void addKeyBind(String key, int dir) {
        getInputMap().put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                s.changeDir(dir);
            }

        });
    }

}
