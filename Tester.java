import org.junit.Test;

import javax.swing.*;

public class Tester {
    @Test
    public void snakeTest()
    {
        Snake s  = new Snake(new int[] {2,3});
        assert s != null: "snake not initialized";
        s.removeTail();
        assert s.getLength() == 2: "wrong length after removeTail";
    }
    @Test
    public void boardTest(){
        GameBoard b = new GameBoard();
        assert b != null: "board not initialized";

        b.gameIni();
        assert b.getBoard()[b.getSnake().getHeadPos()[0]][b.getSnake().getHeadPos()[1]] == 2 : "wrong board value at head position";
        assert b.getBoard()[b.getSnake().getPrevHeadPos()[0]][b.getSnake().getPrevHeadPos()[1]] == 1 : "wrong board value at previous head position";
        assert b.getBoard()[b.getSnake().getTailPos()[0]][b.getSnake().getTailPos()[1]] == 1 : "wrong board value at tail position";

        b.makeShorter(1);
        assert b.getSnake().getLength() == 2: "wrong length after makeShorter";

        b.getSnake().setHasEaten(true);
        b.getSnake().updatePositions(false, 20, 15);
        assert b.getSnake().getLength() == 3 : "wrong length after updating positions";
    }
}
