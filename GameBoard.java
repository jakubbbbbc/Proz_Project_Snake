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

    private Timer t;

    public GameBoard(){

        board = new int[BoardX][BoardY];
        s = new Snake(new int[] {3,3}, 3);
        board[s.getHeadPos()[0]][s.getHeadPos()[1]] = 2;
        board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
        board[s.getTailPos()[0]][s.getTailPos()[1]] = 1;

        t= new Timer(1000, taskPerformer);
        t.start();

    }


    public void game(){

        JFrame f=new JFrame("Snake");//creating instance of JFrame
        f.setSize(BoardX * SpotSize, BoardY * SpotSize+20);
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameBoard b= new GameBoard();
        f.add(b);

        //game loop
        boolean done = false;
        while (!done) {

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
        updateBoard();
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

    public void updateBoard(){
        board[s.getHeadPos()[0]][s.getHeadPos()[1]] = 2;
        board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
    }

    public void paintSpot(int x, int y, Color c, Graphics g) {
        g.setColor(c);
        g.fillRect(x* SpotSize, y*SpotSize, SpotSize, SpotSize);
    }

    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            //System.out.println(s.getTailPos()[0]);
            s.disPos(2);
            s.updatePositions();
            repaint();


        }
    };

}
