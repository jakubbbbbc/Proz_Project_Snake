public class SnakeMain {
    public static void main(String[] args){
        /*Tester t = new Tester();
        t.snakeTest();
        t.boardTest();
        */
        GameBoard b = new GameBoard();
        b.getI().iniGUI(b.getBoardX(), b.getBoardY(), b.getSpotSize()).add(b.getEventsManager());

    }
}