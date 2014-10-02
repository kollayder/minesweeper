import javax.swing.*;

/**
 * Created by nastya
 */
public class Cell {
    private JButton button = new JButton("");
    private boolean flag = false;
    private boolean mine = false;
    private boolean opened = false;
    private boolean red = false;
    static int openedCells = 0;
    static int flags = 0;
    static int allMine = 10;

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean isMine() {
        return mine;
    }

    public void setFlag(boolean b) {
        this.flag = b;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }


}
