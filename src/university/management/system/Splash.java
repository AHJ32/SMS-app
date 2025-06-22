package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Splash extends JFrame implements Runnable {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private boolean isRunning = true;
    private int progress = 0;
    
    public Splash() {
        // Set up the frame
        setUndecorated(true);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 245, 250));
        
        // Main panel with shadow effect
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 15, 15);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                for (int i = 0; i < 10; i++) {
                    g2d.drawRoundRect(10 - i, 10 - i, getWidth() - 20 + 2 * i, getHeight() - 20 + 2 * i, 15, 15);
                }
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // App logo/title
        JLabel titleLabel = new JLabel("College Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        
        // Loading animation
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setOpaque(false);
        
        // Progress bar with rounded corners
        progressBar = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2d.setColor(new Color(230, 233, 237));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Progress
                int width = (int) (getWidth() * (progress / 100.0));
                g2d.setColor(new Color(52, 152, 219));
                g2d.fillRoundRect(0, 0, width, getHeight(), 10, 10);
                
                // Border
                g2d.setColor(new Color(200, 205, 210));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                
                g2d.dispose();
            }
        };
        progressBar.setPreferredSize(new Dimension(400, 15));
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        progressBar.setForeground(Color.WHITE);
        
        // Status text
        statusLabel = new JLabel("Initializing application...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(100, 110, 120));
        
        // Add components
        loadingPanel.add(progressBar, BorderLayout.CENTER);
        loadingPanel.add(Box.createVerticalStrut(15), BorderLayout.SOUTH);
        loadingPanel.add(statusLabel, BorderLayout.SOUTH);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(loadingPanel, BorderLayout.CENTER);
        
        // Add version info
        JLabel versionLabel = new JLabel("v1.0.0 • © 2025 College Management System", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(150, 160, 170));
        mainPanel.add(versionLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Set rounded corners for the frame
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        
        // Start the loading thread
        new Thread(this).start();
        
        setVisible(true);
    }
    
    @Override
    public void run() {
        String[] loadingMessages = {
            "Loading modules...",
            "Preparing database...",
            "Initializing components...",
            "Almost there..."
        };
        
        try {
            // Simulate loading process
            while (progress < 100) {
                progress += 1 + (Math.random() * 3);
                if (progress > 100) progress = 100;
                
                // Update progress and status
                progressBar.setValue(progress);
                statusLabel.setText(loadingMessages[Math.min(progress / 25, loadingMessages.length - 1)]);
                
                // Random speed for more natural feel
                Thread.sleep(30 + (long)(Math.random() * 20));
            }
            
            // Loading complete
            Thread.sleep(300);
            dispose();
            new Login();
            System.out.println("Splash screen completed. Login screen would open here.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Run on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new Splash());
    }
}