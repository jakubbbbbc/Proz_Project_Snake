import java.util.ArrayList;

public class Snake {

    private int[] headPos, prevHeadPos, tailPos;
    private int length;
    private boolean hasEaten;
    private ArrayList<int[]> positions; // starting from tail
    private int dir;


    public Snake(int[] initHeadPos, int initLength) {
        headPos = initHeadPos;
        length = initLength;
        hasEaten=false;
        dir = 0;//right
        positions = new ArrayList<int[]>();
        positions.add(headPos);
        if (1 == length) {
            prevHeadPos = null;
            tailPos = new int[]{headPos[0], headPos[1]};
        }
        else for (int i = 1; i < length; ++i) {
            int[] temp = new int[]{headPos[0] - i, headPos[1]};
            positions.add(0, temp);
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

    public void disPos (int i) {
        System.out.println(positions.get(i)[0]);
    }

    public void updatePositions() {
        if (!hasEaten){
            positions.remove(0);
            tailPos= positions.get(0);
        }
        switch (dir){
            case 0://right
                //prevHeadPos[0]=headPos[0];
                headPos[0]+=1;
                break;
            case 1://up
                prevHeadPos[1]=headPos[1];
                --headPos[1];
                break;
            default:
                System.out.println("Wrong direction!");


        }
    }


}
