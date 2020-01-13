import java.util.ArrayList;

public class Snake {

    private int[] headPos, prevHeadPos, tailPos;
    private int length;
    private boolean hasEaten;
    private ArrayList<int[]> positions; // starting from tail
    private int dir;


    /**
     * creates a new snake, length =3, moving right with a head at a specified position
     * @param initHeadPos
     */
    public Snake(int[] initHeadPos) {
        headPos = initHeadPos;
        length = 3;
        hasEaten=false;
        dir = 0;//right
        positions = new ArrayList<>();
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

    /**
     * moves the sake depending on the direction
     * @param wallAccess
     * @param boardX
     * @param boardY
     */
    public void updatePositions(boolean wallAccess, int boardX, int boardY) {
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
                if (wallAccess && headPos[0]+1 == boardX)
                    headPos= new int[] {0, headPos[1]};
                else
                    headPos= new int[] {headPos[0]+1, headPos[1]};
                break;
            case 1://up
                if (wallAccess && headPos[1]-1 == -1)
                    headPos= new int[] {headPos[0], boardY-1};
                else
                    headPos= new int[] {headPos[0], headPos[1]-1};
                break;
            case 2://left
                if (wallAccess && headPos[0]-1 == -1)
                    headPos= new int[] {boardX-1, headPos[1]};
                else
                    headPos= new int[] {headPos[0]-1, headPos[1]};
                break;
            case 3://down
                if (wallAccess && headPos[1]+1 == boardY)
                    headPos= new int[] {headPos[0], 0};
                else
                    headPos= new int[] {headPos[0], headPos[1]+1};
                break;
            default:
                System.out.println("Wrong direction!");
        }
        positions.add(headPos);
    }

    /**
     * changes the direction the snake is moving in
     * @param newDir
     */
    public void changeDir(int newDir){
        if (newDir%2 != dir%2) //no turning around
            dir=newDir;
    }

    /**
     * removes the last position of the snake
     */
    public void removeTail(){
        positions.remove(tailPos);
        tailPos= positions.get(0);
        --length;
    }

}
