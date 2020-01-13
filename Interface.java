import javax.swing.*;

public class Interface {


    /**
     * creates a GUI
     * @return JFrame
     */
    public JFrame iniGUI(int x, int y, int size){
        JFrame f=new JFrame("Snake");
        f.setSize(x * size+16, y * size+37+60); // 16, 37 - adjustments, 60 - for scores and messages on the bottom
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return f;
    }
}
