import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tugas3 {
    private JFrame frame;
    private JTextArea textArea;
    private JButton startButton, stopButton;
    private ExecutorService executor;
    private boolean isRunning = false;
    
    public Tugas3() {
        frame = new JFrame("Simulasi Antrean Bank");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        startButton = new JButton("Mulai Layanan");
        stopButton = new JButton("Hentikan Layanan");
        stopButton.setEnabled(false);
        
        panel.add(startButton);
        panel.add(stopButton);
        frame.add(panel, BorderLayout.SOUTH);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startLayanan();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopLayanan();
            }
        });

        frame.setVisible(true);
    }
    
    private void startLayanan() {
        if (!isRunning) {
            isRunning = true;
            executor = Executors.newFixedThreadPool(3);
            textArea.append("Memulai Layanan Bank...\n");
            
            for (int i = 1; i <= 3; i++) {
                int pelangganId = i;
                executor.execute(() -> layaniPelanggan(pelangganId));
            }
            
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
    }
    
    private void layaniPelanggan(int pelangganId) {
        try {
            for (int i = 1; i <= 5 && isRunning; i++) {
                Thread.sleep(1000);
                appendText("Pelanggan " + pelangganId + " sedang dilayani: langkah " + i);
            }
            appendText("Pelanggan " + pelangganId + " telah selesai dilayani.");
        } catch (InterruptedException e) {
            appendText("Layanan pelanggan " + pelangganId + " terganggu.");
        }
    }
    
    private void stopLayanan() {
        if (isRunning) {
            isRunning = false;
            executor.shutdownNow();
            textArea.append("Semua layanan dihentikan.\n");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }
    
    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Tugas3::new);
    }
}
