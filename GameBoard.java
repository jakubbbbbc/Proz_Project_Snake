import javax.swing.*;
import java.awt.*;


public class GameBoard extends JPanel{

    private static final int BoardX = 20;
    private static final int BoardY = 15;
    private static final int SpotSize =30;
    private enum Direction { RIGHT, UP, LEFT, DOWN };
    private int[][] board;
    private Snake s;

    public GameBoard(){
        board = new int[BoardX][BoardY];
        s = new Snake(new int[] {3,3}, 3);
        board[s.getHeadPos()[0]][s.getHeadPos()[1]] = 2;
        board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
        board[s.getTailPos()[0]][s.getTailPos()[1]] = 1;
    }


    public void game(){

        JFrame f=new JFrame();//creating instance of JFrame
        f.setSize(BoardX * SpotSize, BoardY * SpotSize+20);
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    /*JButton b=new JButton("click");//creating instance of JButton
        b.setBounds(130,100,100, 40);//x axis, y axis, width, height

        f.add(b);//adding button in JFrame
    */
        //game loop
        boolean done = false;
        while (!done) {

            boolean running = true;
            boolean menu = true;

            while (!menu) {
                return;
            }

        }

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //clears display

        /*g.setColor(Color.LIGHT_GRAY); // Paints the grid
		for (int i = 0; i <= numCol; i++) {
			for (int j = 0; j <= numRow; j++) {
				g.drawLine(0, j*cellSize, numCol*cellSize, j*cellSize); // Rows
				g.drawLine(i*cellSize, 0, i*cellSize, numRow*cellSize); // Columns
			}
		}*/

        //updateBoard();
        for (int i = 0; i < BoardX; ++i) {
            for (int j = 0; j < BoardY; ++j) {
                if (board[i][j] == 0)
                    paintSpot(i, j, Color.WHITE, g); // Background
                else if (board[i][j] == 1)
                    paintSpot(i, j, Color.YELLOW, g); // Head
                else if (board[i][j] == 2)
                    paintSpot(i, j, Color.GREEN, g); // Body
                else if (board[i][j] == 3)
                    paintSpot(i, j, Color.RED, g); // Food
            }
        }
    }

    public void updateBoard(){

    }

    public void paintSpot(int x, int y, Color c, Graphics g) {
        g.setColor(c);
        g.fillRect(x* SpotSize, y*SpotSize, SpotSize, SpotSize);
    }

}
