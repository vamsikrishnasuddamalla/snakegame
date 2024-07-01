import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JFrame {

    private static final int BOARD_WIDTH = 300;
    private static final int BOARD_HEIGHT = 300;
    private static final int UNIT_SIZE = 10;
    private static final int ALL_UNITS = (BOARD_WIDTH * BOARD_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 140;

    private final int[] x = new int[ALL_UNITS];
    private final int[] y = new int[ALL_UNITS];

    private int snakeLength;
    private int foodX;
    private int foodY;
    private int score;

    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        add(new GameBoard());
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class GameBoard extends JPanel implements ActionListener {

        public GameBoard() {
            setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
            setBackground(Color.BLACK);
            setFocusable(true);
            addKeyListener(new TAdapter());
            startGame();
        }

        private void startGame() {
            snakeLength = 3;
            for (int i = 0; i < snakeLength; i++) {
                x[i] = 50 - i * UNIT_SIZE;
                y[i] = 50;
            }
            placeFood();
            timer = new Timer(DELAY, this);
            timer.start();
            running = true;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        private void draw(Graphics g) {
            if (running) {
                g.setColor(Color.RED);
                g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

                for (int i = 0; i < snakeLength; i++) {
                    if (i == 0) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.YELLOW);
                    }
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

                Toolkit.getDefaultToolkit().sync();
            } else {
                gameOver(g);
            }
        }

        private void placeFood() {
            Random random = new Random();
            foodX = random.nextInt(BOARD_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            foodY = random.nextInt(BOARD_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        }

        private void move() {
            for (int i = snakeLength; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }

            switch (direction) {
                case 'U':
                    y[0] -= UNIT_SIZE;
                    break;
                case 'D':
                    y[0] += UNIT_SIZE;
                    break;
                case 'L':
                    x[0] -= UNIT_SIZE;
                    break;
                case 'R':
                    x[0] += UNIT_SIZE;
                    break;
            }
        }

        private void checkFood() {
            if ((x[0] == foodX) && (y[0] == foodY)) {
                snakeLength++;
                score++;
                placeFood();
            }
        }

        private void checkCollision() {
            for (int i = snakeLength; i > 0; i--) {
                if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                    running = false;
                }
            }

            if (x[0] < 0 || x[0] >= BOARD_WIDTH || y[0] < 0 || y[0] >= BOARD_HEIGHT) {
                running = false;
            }

            if (!running) {
                timer.stop();
            }
        }

        private void gameOver(Graphics g) {
            String msg = "Game Over";
            String scoreMsg = "Score: " + score;
            Font font = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metrics = getFontMetrics(font);

            g.setColor(Color.RED);
            g.setFont(font);
            g.drawString(msg, (BOARD_WIDTH - metrics.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
            g.drawString(scoreMsg, (BOARD_WIDTH - metrics.stringWidth(scoreMsg)) / 2, BOARD_HEIGHT / 2 + 20);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                checkFood();
                checkCollision();
                move();
            }
            repaint();
        }

        private class TAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_LEFT) && (direction != 'R')) {
                    direction = 'L';
                }

                if ((key == KeyEvent.VK_RIGHT) && (direction != 'L')) {
                    direction = 'R';
                }

                if ((key == KeyEvent.VK_UP) && (direction != 'D')) {
                    direction = 'U';
                }

                if ((key == KeyEvent.VK_DOWN) && (direction != 'U')) {
                    direction = 'D';
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}
