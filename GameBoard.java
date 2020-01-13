public class GameBoard {

    private static final int BoardX = 20;
    private static final int BoardY = 15;
    private static final int SpotSize = 30;
    private int[][] board;
    private int[] superFoodPos;
    private int[] boosterPos;
    private Snake s;
    private boolean wallFlashing;
    public boolean wallVisible;
    private boolean superFoodVisible;
    private int score;
    private int highScore;
    private int boostCount; // to spawn boosters
    private int pointBoost; // to multiply points
    private String playerName;
    private String highScorePlayerName;
    private Interface i;
    private EventsManager e;
    private TxtManager m;

    public GameBoard(){
        i = new Interface();
        e = new EventsManager();
        i.setGameBoard(this);
        i.setEventManager(e);
        e.setGameBoard(this);
        e.setInterface(i);

        i.iniGUI(BoardX, BoardY, SpotSize);

        m = new TxtManager();
        m.setGameBoard(this);

        m.readHighScoreAndPlayer();

        boosterPos = new int[]{0, 0};
    }

    /**
     * sets value of a new place on board to apple value (3)
     */
    public void newApple(){
        int[] applePos= new int[] {(int)(Math.random() * BoardX), (int)(Math.random() * BoardY)};
        boolean isOk=true;
        if (board[applePos[0]][applePos[1]] != 0) {
            isOk = false;
            newApple();
        }
        if (isOk)
            board[applePos[0]][applePos[1]] = 3;
    }

    /**
     * sets value of a new place on board to random booster value (4-9)
     */
    public void newBoost(){
        board[boosterPos[0]][boosterPos[1]] = 0;
        int[] newBoostPos= new int[] {(int)(Math.random() * BoardX), (int)(Math.random() * BoardY)};
        boolean isOk = true;
        if (board[newBoostPos[0]][newBoostPos[1]] != 0) {
            isOk = false;
            newBoost();
        }
        if (isOk)
            board[newBoostPos[0]][newBoostPos[1]] = (int) (Math.random()*6+4); //boost type, default: * NumOfBoosters +4, currently num = 6
        if (8 == board[newBoostPos[0]][newBoostPos[1]]){
            superFoodPos = newBoostPos;
            superFoodVisible = true;
            e.resetTimesFlashed();
            e.getFlashingTimer().restart();
            i.setMessageID(8);
            e.getMessageTimer().restart();
            e.getBoosterTimer().restart();
        }
        else
            boosterPos = newBoostPos;
    }

    /**
     * checks for collision or potential food eaten
     * @return if the snake has't hit something
     */
    public boolean updateHead(){
        int hpx = s.getHeadPos()[0], hpy= s.getHeadPos()[1];

        if (hpx == BoardX || hpx <0 || hpy<0 || hpy == BoardY || board[hpx][hpy] == 1) {
            return false;
        }
        else{
            if (board[hpx][hpy] > 2)
                hasEaten();
            board[hpx][hpy] = 2;
            board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
            return true;
        }
    }

    /**
     * performs action depending on what time of food was consumed
     */
    public void hasEaten() {
        //what food?
        int atHeadPos = board[s.getHeadPos()[0]][s.getHeadPos()[1]];
        i.setMessageID(atHeadPos);
        switch (atHeadPos){
            case 3: //apple
                s.setHasEaten(true);
                score+= pointBoost;
                newApple();
                if (e.getT().getDelay()>50)
                    e.getT().setDelay(e.getT().getDelay()-2);
                break;
            case 4: // delay+10
                e.getT().setDelay(e.getT().getDelay()+6);
                e.getMessageTimer().restart();
                break;
            case 5: // shorter by 3
                makeShorter(3);
                e.getMessageTimer().restart();
                break;
            case 6: // bonus points
                score+=3;
                e.getMessageTimer().restart();
                break;
            case 7: // points *2
                pointBoost = 2;
                e.getMessageTimer().restart();
                e.getBoosterTimer().restart();
                break;
            case 8: // superFood (10 pts)
                score += 10;
                boostCount += 2; // to not display new booster
                break;
            case 9: // going through walls for 10s
                e.setWallAccess(true);
                wallFlashing = true;
                e.getMessageTimer().restart();
                e.getBoosterTimer().restart();
                e.resetTimesFlashed();
                e.getFlashingTimer().restart();
                break;
            default:
                break;
        }
        if (score/5 > boostCount){ // new booster every 5 points;
            newBoost();
            ++boostCount;
        }

        if (score>highScore) {
            highScore = score;
            highScorePlayerName = playerName;
            m.saveHighScoreAndPlayer();
        }
    }

    /**
     * removes tail of snake n times
     * @param n how much shorter the snake will be
     */
    public void makeShorter(int n){
        for (int i=0; i<n; ++i){
            board[s.getTailPos()[0]][s.getTailPos()[1]] = 0;
            s.removeTail();
        }
    }

    /**
     * if the snake didn't eat, sets board at tail position to 0
     */
    public void updateTail() {
        if (!s.getHasEaten())
            board[s.getTailPos()[0]][s.getTailPos()[1]] = 0;
    }

    /**
     * initialize everything for a new game and start it
     */
    public void gameIni(){
        playerName = i.askForName();
        i.askForSnakeColor();
        board = new int[BoardX][BoardY];

        newApple();
        score = 0;
        boostCount = 0;
        pointBoost = 1;
        i.setMessageID(0);

        e.setWallAccess(false);
        wallVisible = true;
        wallFlashing = false;
        e.resetTimesFlashed();

        s = new Snake(new int[] {2,3});
        board[s.getHeadPos()[0]][s.getHeadPos()[1]] = 2;
        board[s.getPrevHeadPos()[0]][s.getPrevHeadPos()[1]] = 1;
        board[s.getTailPos()[0]][s.getTailPos()[1]] = 1;
        e.getT().setDelay(e.getDELAY());
        e.getT().start();
    }

    public int getBoardX(){
        return BoardX;
    }

    public int getBoardY(){
        return BoardY;
    }

    public int getSpotSize(){
        return SpotSize;
    }

    public int getScore() {
        return score;
    }

    public String getPlayerName(){
        return playerName;
    }

    public int getHighScore(){
        return highScore;
    }

    public String getHighScorePlayerName(){
        return highScorePlayerName;
    }

    public int[][] getBoard(){
        return board;
    }

    public Snake getSnake(){
        return s;
    }

    public int[] getSuperFoodPos(){
        return superFoodPos;
    }

    public boolean getWallFlashing(){
        return wallFlashing;
    }

    public boolean getWallVisible(){
        return wallVisible;
    }

    public boolean getSuperFoodVisible(){
        return superFoodVisible;
    }

    public void setWallVisible(boolean newV){
        wallVisible = newV;
    }

    public void setSuperFoodVisible(boolean newV){
        superFoodVisible = newV;
    }

    public void setPointBoost (int newPB){
        pointBoost = newPB;
    }

    public void setHighScore(int newHS){
        highScore = newHS;
    }

    public void setHighScorePlayerName(String newPN){
        highScorePlayerName = newPN;
    }
}
