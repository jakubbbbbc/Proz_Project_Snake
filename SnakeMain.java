public class SnakeMain {
    public static void main(String[] args){
        /*Tester t = new Tester();
        t.snakeTest();
        t.boardTest();
        */
        GameBoard b = new GameBoard();
        Interface i = new Interface();
        i.iniGUI(20,15,30).add(b);

    }
}