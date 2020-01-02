import java.util.ArrayList;

public class Snake {

    private int[] headPos, prevHeadPos, tailPos;
    private int length;
    private ArrayList<int[]> positions; // starting from tail
    private int dir;


    public Snake(int[] initHeadPos, int initLength) {
        headPos = initHeadPos;
        length = initLength;
        dir = 0;
        positions = new ArrayList<int[]>();
        positions.add(headPos);
        if (1 == length) {
            prevHeadPos = null;
            tailPos = new int[]{headPos[0], headPos[1]};
        }
        else for (int i = 1; i < length; ++i) {
            int[] temp = new int[]{headPos[0] - i, headPos[1]};
            positions.add(temp);
            if (1 == i) prevHeadPos = temp;
            if (length - 1 == i) tailPos = temp;
        }
    }

    public int[] getHeadPos() {
        return headPos;
    }

    public int[] getPrevHeadPos() {
        return prevHeadPos;
    }

    public int[] getTailPos() {
        return tailPos;
    }

    public int getLength() {
        return length;
    }





}
