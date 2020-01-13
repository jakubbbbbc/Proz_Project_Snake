import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TxtManager {

    GameBoard b;

    public void setGameBoard(GameBoard gameboard){
        b = gameboard;
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
            out.println(b.getHighScore());
            out.println(b.getHighScorePlayerName());
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
            b.setHighScore(Integer.parseInt(sc.next()));
            b.setHighScorePlayerName(sc.next());
            sc.close();
        }
    }

}
