import java.util.ArrayList;

public class Snake<headPos> {

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
        //positions.add(headPos);
        /*if (1 == length) {
            prevHeadPos = null;
            tailPos = new int[]{headPos[0], headPos[1]};
        }
        else for (int i = 1; i < length; ++i) {
            int[] temp = new int[]{headPos[0] - i, headPos[1]};
            positions.add(0, temp);
            if (1 == i) prevHeadPos = temp;
            if (length - 1 == i) tailPos = temp;
        }*/
        prevHeadPos = new int[] {headPos[0]-1, headPos[1] };
        tailPos = new int[] {headPos[0]-2, headPos[1] };
        positions.add(tailPos);
        positions.add(prevHeadPos);
        positions.add(headPos);
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

    public boolean getHasEaten(){
        return hasEaten;
    }

    public void setHasEaten (boolean he){
        hasEaten = he;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<int[]> getPos () {
        return positions;
    }

    public void updatePositions() {
        if (!hasEaten){
            positions.remove(tailPos);
            tailPos= positions.get(0);
        }
        else{
            hasEaten = false;
            ++length;
        }
        prevHeadPos=new int[] {headPos[0], headPos[1]};
        switch (dir){
            case 0://right
                headPos= new int[] {headPos[0]+1, headPos[1]};
                break;
            case 1://up
                headPos= new int[] {headPos[0], headPos[1]-1};
                break;
            case 2://left
                headPos= new int[] {headPos[0]-1, headPos[1]};
                break;
            case 3://down
                headPos= new int[] {headPos[0], headPos[1]+1};
                break;
            default:
                System.out.println("Wrong direction!");
        }
        positions.add(headPos);
    }

    public void changeDir(int newDir){
        if (newDir%2 != dir%2)
            dir=newDir;
    }

    public void removeTail(){
        positions.remove(tailPos);
        tailPos= positions.get(0);
        --length;
    }


}
