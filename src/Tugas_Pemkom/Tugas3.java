import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Tugas3 extends JFrame {
    private JLabel timerLabel;
    private int timeLeft = 30;
    private Timer timer;
    private char[][] board;
    private boolean playerTurn = true;
    private Random random = new Random();
    private static final int SIZE = 9;

    public Tugas3() {
        setTitle("SOS Game with AI");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        timerLabel = new JLabel("Waktu: " + timeLeft + " detik", SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        board = new char[SIZE][SIZE];
        add(new SOSBoard(), BorderLayout.CENTER);

        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    SwingUtilities.invokeLater(() -> timerLabel.setText("Waktu: " + timeLeft + " detik"));
                } else {
                    timer.cancel();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Waktu Habis!"));
                }
            }
        }, 0, 1000);
    }

    private void aiMove() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                int row, col;
                do {
                    row = random.nextInt(SIZE);
                    col = random.nextInt(SIZE);
                } while (board[row][col] != '\0');

                board[row][col] = 'O';
                removeSOS();
                playerTurn = true;
                repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void removeSOS() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE - 2; j++) {
                if (board[i][j] == 'S' && board[i][j + 1] == 'O' && board[i][j + 2] == 'S') {
                    board[i][j] = board[i][j + 1] = board[i][j + 2] = '\0';
                }
            }
        }
        for (int i = 0; i < SIZE - 2; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 'S' && board[i + 1][j] == 'O' && board[i + 2][j] == 'S') {
                    board[i][j] = board[i + 1][j] = board[i + 2][j] = '\0';
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Tugas3().setVisible(true));
    }

    class SOSBoard extends JPanel {
        public SOSBoard() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!playerTurn) return;
                    int tileSize = getWidth() / SIZE;
                    int row = e.getY() / tileSize;
                    int col = e.getX() / tileSize;
                    if (row < SIZE && col < SIZE && board[row][col] == '\0') {
                        board[row][col] = 'S';
                        removeSOS();
                        playerTurn = false;
                        repaint();
                        aiMove();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int tileSize = getWidth() / SIZE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
                    if (board[i][j] != '\0') {
                        g.setFont(new Font("Arial", Font.BOLD, tileSize / 2));
                        g.drawString(String.valueOf(board[i][j]), j * tileSize + tileSize / 3, i * tileSize + tileSize / 2);
                    }
                }
            }
        }
    }
}
