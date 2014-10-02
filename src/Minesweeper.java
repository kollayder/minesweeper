import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Created by nastya
 */
public class Minesweeper extends JFrame implements ActionListener, Runnable {
    private JButton newGameButton;
    private JLabel mineCounter;
    private JLabel timer;
    private Thread time;
    private boolean started;
    private int row, col;
    private int mineLeft;
    private int allCells;
    private Cell cells[][];
    private int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
    private int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};
    private ImageIcon img_mine = new ImageIcon(Minesweeper.class.getResource("res/mine.png"));
    private ImageIcon img_flag = new ImageIcon(Minesweeper.class.getResource("res/flag.png"));
    private ImageIcon img_redmine = new ImageIcon(Minesweeper.class.getResource("res/redmine.png"));
    private ImageIcon img_strikemine = new ImageIcon(Minesweeper.class.getResource("res/strikemine.png"));
    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JMenuItem simple;
    private JMenuItem medium;
    private JMenuItem hard;
    private JMenuItem custom;
    private JMenuItem exit;
    private boolean isActive;

    /**
     * Конструктор встановлює стандарні параметри ігрового поля, тобто 10х10 і 10 мін
     */
    public Minesweeper() {
        super("Minesweeper");
        row = 10;
        col = 10;
        mineLeft = Cell.allMine;
        isActive = true;
        allCells = (int) row * col;
        initComp();
        setMenu();
        startGame();

    }

    public static void main(String[] args) {
        new Minesweeper();
    }

    /**
     * Починає нову гру, запускає потік таймеру
     */
    private void startGame() {
        reset();
        mineRandom();
        time = new Thread(this);
        topPanel.repaint();
        centerPanel.repaint();

    }

    /**
     * Скидує всі параметри до початкових
     */
    private void reset() {
        started = true;
        isActive = true;
        Cell.openedCells = 0;
        Cell.flags = 0;
        mineLeft = Cell.allMine;
        mineCounter.setText("     " + mineLeft);
        timer.setText("    0");
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j].setOpened(false);
                cells[i][j].setFlag(false);
                cells[i][j].setMine(false);
                cells[i][j].setRed(false);
                cells[i][j].getButton().setForeground(null);
                cells[i][j].getButton().setBackground(null);
                cells[i][j].getButton().setIcon(null);
                cells[i][j].getButton().setText(null);
            }
        }
        repaint();
    }


    /**
     * Відповідає за створення компонентів та відображення вікна на екрані
     */
    private void initComp() {
        getContentPane().setLayout(new BorderLayout());
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopActive();
                timer.setText("    0");
                startGame();
            }
        });

        mineCounter = new JLabel("     " + mineLeft);
        mineCounter.setPreferredSize(new Dimension(100, 40));
        mineCounter.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        mineCounter.setForeground(Color.GREEN);

        timer = new JLabel("    0");
        timer.setPreferredSize(new Dimension(100, 40));
        timer.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        timer.setForeground(Color.GREEN);

        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, false));
        topPanel.add(mineCounter, BorderLayout.EAST);
        topPanel.add(timer, BorderLayout.WEST);
        topPanel.add(newGameButton, BorderLayout.CENTER);
        getContentPane().add(topPanel, BorderLayout.NORTH);


        centerPanel.setLayout(new GridLayout(row, col));
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        cells = new Cell[row][col];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell();
                cells[i][j].getButton().addMouseListener(getListener());
                cells[i][j].getButton().setPreferredSize(new Dimension(30, 30));
                cells[i][j].getButton().setMargin(new Insets(0, 0, 0, 0));
                cells[i][j].getButton().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
                centerPanel.add(cells[i][j].getButton());
            }
        }


        pack();
        setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 3));

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Створює елементи меню
     */
    private void setMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Settings");

        simple = new JMenuItem("Simple");
        medium = new JMenuItem("Medium");
        hard = new JMenuItem("Hard");
        custom = new JMenuItem("Custom");
        exit = new JMenuItem("Exit");

        simple.addActionListener(this);
        medium.addActionListener(this);
        hard.addActionListener(this);
        custom.addActionListener(this);
        exit.addActionListener(this);

        setJMenuBar(menuBar);

        menu.add(simple);
        menu.add(medium);
        menu.add(hard);
        menu.add(custom);
        menu.addSeparator();
        menu.add(exit);

        menuBar.add(menu);
        menuBar.setBackground(Color.BLACK);
        menu.setForeground(Color.LIGHT_GRAY);
        menu.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, Color.DARK_GRAY));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

    }

    /**
     * спрацьовує при виборі одного з пунктів головного меню
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (simple == (JMenuItem) e.getSource()) {
            row = 10;
            col = 10;
            Cell.allMine = 10;
            mineLeft = 10;
            allCells = (int) row * col;
            topPanel.removeAll();
            centerPanel.removeAll();
            getContentPane().removeAll();
            stopActive();
            initComp();
            startGame();
        } else if (medium == (JMenuItem) e.getSource()) {
            row = 16;
            col = 16;
            Cell.allMine = 40;
            mineLeft = 40;
            allCells = (int) row * col;
            topPanel.removeAll();
            centerPanel.removeAll();
            getContentPane().removeAll();
            stopActive();
            initComp();
            startGame();
        } else if (hard == (JMenuItem) e.getSource()) {
            row = 16;
            col = 30;
            Cell.allMine = 99;
            mineLeft = 99;
            allCells = (int) row * col;
            topPanel.removeAll();
            centerPanel.removeAll();
            getContentPane().removeAll();
            stopActive();
            initComp();
            startGame();
        } else if (custom == (JMenuItem) e.getSource()) {
            new InnerFrame();
        } else if (exit == (JMenuItem) e.getSource()) {
            System.exit(0);
        }
    }

    /**
     * Розстановка мін на ігровому полі
     */
    private void mineRandom() {
        int r = 0, c = 0, i = 0;
        Random rand = new Random();
        while (i < Cell.allMine) {
            i++;
            r = rand.nextInt(row);
            c = rand.nextInt(col);
            if (!cells[r][c].isMine()) {
                cells[r][c].setMine(true);
            } else {
                i--;
            }
        }

    }

    /**
     * Встановлення зображення прапорця при натисканні правою кнопкою миші на клітинці
     *
     * @param source - матриця з об"єктів типу Cell(наше ігрове поле)
     */
    private void setFlag(Cell source) {
        if (!source.isOpened()) {
            if (source.isFlag()) {
                source.setFlag(false);
                source.getButton().setIcon(null);
                mineLeft++;
                Cell.flags--;
                mineCounter.setText("     " + mineLeft);
            } else {
                source.setFlag(true);
                source.getButton().setIcon(img_flag);
                mineLeft--;
                Cell.flags++;
                mineCounter.setText("     " + mineLeft);
            }
        }
        lookForWinner();
    }

    /**
     * Підраховує кількість мін навколо обраної клітинки
     *
     * @param i координата стовпця ігрової матриці
     * @param j координата рядка ігрової матриці
     * @return кількість мін навколо обраної клітини
     */
    private int numOfMine(int i, int j) {
        int x, y, numOfMine = 0;
        for (int k = 0; k < dx.length; k++) {
            x = i + dx[k];
            y = j + dy[k];
            if (x >= 0 && x < cells.length && y >= 0 && y < cells[i].length && cells[x][y].isMine()) {
                numOfMine++;
            }
        }
        return numOfMine;
    }

    /**
     * Основна логіка гри. При натисканні на кнопку справцьовує цей метод.
     * Прораховуються різні випадки:
     * якщо кількість мін навколо клітинки більша 0, то відображаємо кількість мін на кнопці.
     * <p/>
     * якщо навколо мін немає, то потрібно відкрити всі клітини навколо обраної,
     * рекурсивно пройшовши всі попередні пункти
     *
     * @param i координата стовпця ігрової матриці
     * @param j координата рядка ігрової матриці
     */
    private void doLogic(int i, int j) {
        if (!cells[i][j].isOpened() && !cells[i][j].isFlag()) {
            Cell.openedCells++;
            cells[i][j].getButton().setBackground(Color.GRAY);
            cells[i][j].getButton().setForeground(Color.WHITE);
            //cells[i][j].getButton().removeMouseListener(getListener());
            //cells[i][j].getButton().setEnabled(false);
            int numOfMine = numOfMine(i, j);
            if (numOfMine > 0) {
                cells[i][j].getButton().setText("" + numOfMine);
                cells[i][j].setOpened(true);
                lookForWinner();
            } else {
                for (int k = 0; k < dx.length; k++) {
                    if (i + dx[k] >= 0 && i + dx[k] < cells.length && j + dy[k] >= 0 && j + dy[k] < cells[i].length) {
//                        System.out.println(i + dx[k]);
//                        System.out.println(" " + (j + dy[k]));
                        cells[i][j].setOpened(true);
                        doLogic(i + dx[k], j + dy[k]);
                    }
                }
                lookForWinner();
            }
        }
    }


    private void endGame() {

        stopActive();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (!cells[i][j].isFlag() && cells[i][j].isMine() && !cells[i][j].isRed()) {
                    cells[i][j].getButton().setIcon(img_mine);
                    cells[i][j].getButton().setBackground(Color.gray);
                } else if (cells[i][j].isFlag() && !cells[i][j].isMine()) {
                    cells[i][j].getButton().setIcon(img_strikemine);
                    cells[i][j].getButton().setBackground(Color.gray);
                }

            }

        }

    }

    private void lookForWinner() {
        if ((Cell.flags == Cell.allMine) && ((allCells - Cell.openedCells) == Cell.allMine)) {
            stopActive();
            JOptionPane.showMessageDialog(this, "YOU ARE WINNER!");
        }
    }

    /**
     * Створює слухач миші, обробляє події, в залежності від кнопки - правої чи лівої.
     * Якщо права, спрацьовує метод встановлення прапорця на клітинці.
     * Якщо ліва, запускає поток таймеру, та відкриває клітинку. Якщо в клітинці міна, викликає
     * метод закінчення гри, інакше - метод doLogic.
     *
     * @return слухач натиску на клітинку
     */
    public MouseListener getListener() {
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < cells.length; i++) {
                    for (int j = 0; j < cells[i].length; j++) {
                        if ((cells[i][j].getButton() == (JButton) e.getSource()) && isActive) {
                            if (e.getButton() == MouseEvent.BUTTON3) {
                                setFlag(cells[i][j]);
                                break;
                            } else {
                                while (started) {
                                    time.start();
                                    started = false;

                                }

                                if (cells[i][j].isMine()) {
                                    if (!cells[i][j].isFlag()) {
                                        cells[i][j].getButton().setBackground(Color.gray);
                                        cells[i][j].getButton().setIcon(img_redmine);
                                        cells[i][j].setRed(true);
                                        endGame();
                                    }
                                } else {
                                    doLogic(i, j);
                                }

                                break;
                            }
                        }

                    }
                }
            }
        };
        return ml;
    }

    /**
     * Допомагає убити потік таймеру у потрібних випадках
     */
    private void stopActive() {
        isActive = false;
        time.interrupt();
    }

    /**
     * Запускає таймер та перериває його за допомогою виключення та метода-провокатора - stopActive()
     */
    @Override
    public void run() {

        int i = 0;
        while (i < 999 && time.isAlive()) {
            i++;
            try {
                Thread.sleep(1000);
                timer.setText("    " + i);
            } catch (InterruptedException e) {
                return;
            }

        }
        return;

    }

    /**
     * Внутрішній клас для створення форми, де користувач вводить свої власні параметри ігрового поля
     */
    class InnerFrame extends JFrame implements ActionListener {
        JTextField tr, tc, tm;
        JLabel lb1, lb2, lb3;
        JButton b;
        JPanel p;
        int c, r, m;

        InnerFrame() {
            super("Custom");

            tr = new JTextField();
            tc = new JTextField();
            tm = new JTextField();

            b = new JButton("OK");
            b.addActionListener(this);
            b.setPreferredSize(new Dimension(200, 30));

            lb1 = new JLabel("Row");
            lb2 = new JLabel("Column");
            lb3 = new JLabel("Mine");

            p = new JPanel(new GridLayout(0, 2));

            getContentPane().setLayout(new BorderLayout());

            p.add(lb1);
            p.add(tr);
            p.add(lb2);
            p.add(tc);
            p.add(lb3);
            p.add(tm);

            getContentPane().add(p, BorderLayout.NORTH);
            getContentPane().add(b, BorderLayout.SOUTH);
            pack();
            setLocation(Minesweeper.this.getLocation());
            setVisible(true);
            setResizable(false);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (Integer.parseInt(tr.getText()) > 40) {
                    row = 40;
                } else if (Integer.parseInt(tr.getText()) < 10) {
                    row = 10;
                } else {
                    row = Integer.parseInt(tr.getText());
                }

                if (Integer.parseInt(tc.getText()) > 40) {
                    col = 40;
                } else if (Integer.parseInt(tc.getText()) < 10) {
                    col = 10;
                } else {
                    col = Integer.parseInt(tc.getText());
                }

                if (Integer.parseInt(tm.getText()) > (int) row * col) {
                    Cell.allMine = (int) row * col;
                } else if (Integer.parseInt(tm.getText()) < 1) {
                    Cell.allMine = 1;
                } else {
                    Cell.allMine = Integer.parseInt(tm.getText());
                }

                mineLeft = Cell.allMine;
                allCells = (int) row * col;
                topPanel.removeAll();
                centerPanel.removeAll();
                getContentPane().removeAll();
                stopActive();
                initComp();
                startGame();
                dispose();
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "Incorrect values!");
                tr.setText("");
                tc.setText("");
                tm.setText("");
            }
        }
    }
}