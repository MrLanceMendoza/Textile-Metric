import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class TextileMetricHome extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel containerPanel = new JPanel(cardLayout);
    private Image featureBg;

    public TextileMetricHome() {
        setTitle("TextileMetric - Material & Fit Logic Engine");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load background for the dashboard screen
        try {
            URL imgUrl = getClass().getResource("/IMAGES/featureBackground.jpg");
            if (imgUrl != null) featureBg = ImageIO.read(imgUrl);
        } catch (Exception e) {
            System.out.println("Feature background not found.");
        }

        // Add the two main screens to the CardLayout
        containerPanel.add(createHomeScreen(), "HOME");
        containerPanel.add(createDashboardScreen(), "DASHBOARD");
        
        setContentPane(containerPanel);
        showView("HOME");
    }

    // Switch between Home and Dashboard
    public void showView(String name) {
        cardLayout.show(containerPanel, name);
    }

    // ==========================================
    // VIEW 1: HOME PAGE (Splash Screen)
    // ==========================================
    private JPanel createHomeScreen() {
        JPanel homePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL imgURL = getClass().getResource("/IMAGES/background.png");
                if (imgURL != null) {
                    ImageIcon bg = new ImageIcon(imgURL);
                    g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Logo placement
        URL logoURL = getClass().getResource("/IMAGES/logo.png");
        if (logoURL != null) {
            ImageIcon logoIcon = new ImageIcon(logoURL);
            Image scaledLogo = logoIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setBounds(65, 40, 180, 180);
            homePanel.add(logoLabel);
        }

        // Main Menu Buttons
        int btnWidth = 320, btnHeight = 100, xPos = 800, gap = 10, yStart = 85;
        
        JButton btnStart = createImageButton("/IMAGES/start.png", xPos, yStart, btnWidth, btnHeight);
        JButton btnInstructions = createImageButton("/IMAGES/instructions.png", xPos, yStart + (btnHeight + gap), btnWidth, btnHeight);
        JButton btnCredits = createImageButton("/IMAGES/credits.png", xPos, yStart + 2 * (btnHeight + gap), btnWidth, btnHeight);
        JButton btnExit = createImageButton("/IMAGES/exit.png", xPos, yStart + 3 * (btnHeight + gap), btnWidth, btnHeight);

        // Navigation Logic
        btnStart.addActionListener(e -> showView("DASHBOARD"));
        btnInstructions.addActionListener(e -> openApp(new InstructionsPage(this)));
        // 'this' passes the main app so the Back button works flawlessly
        btnCredits.addActionListener(e -> {
            new CreditsPage(this).setVisible(true);
            this.setVisible(false); // Hide the splash screen while CV is open
        });
        btnExit.addActionListener(e -> System.exit(0));

        homePanel.add(btnStart); 
        homePanel.add(btnInstructions);
        homePanel.add(btnCredits); 
        homePanel.add(btnExit);

        return homePanel;
    }

    // ==========================================
    // VIEW 2: MAIN DASHBOARD (Module Selection)
    // ==========================================
    private JPanel createDashboardScreen() {
        // Custom panel that draws the featureBackground.jpg
        JPanel dashPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (featureBg != null) {
                    g.drawImage(featureBg, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(214, 203, 190));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        renderBranding(dashPanel);

        // BACK Button (Returns to Home Splash Screen)
        JButton backBtn = new RoundedButton("BACK", Color.WHITE, 20, 0, Color.WHITE);
        backBtn.setForeground(new Color(139, 80, 50));
        backBtn.setFont(new Font("Verdana", Font.BOLD, 24));
        backBtn.setBounds(40, 560, 180, 60);
        backBtn.addActionListener(e -> showView("HOME"));
        dashPanel.add(backBtn);

        // Navigation Card
        JPanel navCard = new RoundedPanel(25, new Color(196, 178, 145, 230)); 
        navCard.setBounds(340, 40, 800, 600);
        navCard.setLayout(null);
        
        JPanel shadowPanel = new RoundedPanel(25, new Color(40, 40, 40, 150));
        shadowPanel.setBounds(345, 45, 800, 600);
        
        dashPanel.add(navCard);
        dashPanel.add(shadowPanel);

        JLabel menuTitle = new JLabel("Module Selection");
        menuTitle.setFont(new Font("Verdana", Font.BOLD, 32));
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setBounds(40, 30, 400, 50);
        navCard.add(menuTitle);

        // --- Feature Navigation Buttons ---
        int btnW = 340, btnH = 120, startX = 40, startY = 110, gap = 20;

        navCard.add(createNavButton("Stretch Ratio Analyzer", 
                    "Calculate precise pattern cuts for stretch and fit.", 
                    startX, startY, btnW, btnH, e -> openApp(new StretchAnalyzerApp())));

        navCard.add(createNavButton("GSM (Fabric Weight) Analyzer", 
                    "Measure fabric density for optimal garment structure.", 
                    startX + btnW + gap, startY, btnW, btnH, e -> openApp(new GSMAnalyzerApp())));

        navCard.add(createNavButton("Fabric Recommender", 
                    "Find smart material alternatives by drape and finish.", 
                    startX, startY + btnH + gap, btnW, btnH, e -> openApp(new FabricRecommenderApp())));

        navCard.add(createNavButton("Fit Ease Standardizer", 
                    "Generate full-body garment specs using standardized ease.", 
                    startX + btnW + gap, startY + btnH + gap, btnW, btnH, e -> openApp(new FitEaseStandardizerApp())));

        navCard.add(createNavButton("Visual Fabric Comparator", 
                    "Analyze color and texture coordination across fabrics.", 
                    startX, startY + (btnH + gap) * 2, btnW * 2 + gap, btnH - 20, e -> openApp(new FabricComparatorApp())));

        return dashPanel;
    }

    // ==========================================
    // THE NAVIGATION ENGINE (Magic happens here)
    // ==========================================
    private void openApp(JFrame app) {
        // 1. Force the child app to DISPOSE instead of EXIT so it doesn't kill the whole program
        app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // 2. Add a listener to detect when the child app closes (via the 'X' or its Back button)
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // When the feature closes, reappear!
                TextileMetricHome.this.setVisible(true);
            }
        });
        
        // 3. Show the requested feature and hide the main menu
        app.setVisible(true);
        this.setVisible(false);
    }

    // ==========================================
    // UI HELPER METHODS
    // ==========================================
    private JButton createImageButton(String imagePath, int x, int y, int width, int height) {
        URL imgURL = getClass().getResource(imagePath);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            JButton button = new JButton(new ImageIcon(scaledImage));
            button.setBounds(x, y, width, height);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }
        return new JButton("Missing");
    }

    private JPanel createNavButton(String title, String desc, int x, int y, int w, int h, java.awt.event.ActionListener action) {
        JPanel container = new JPanel(null);
        container.setBounds(x, y, w, h);
        container.setOpaque(false);

        JButton btn = new RoundedButton(title, new Color(55, 70, 85), 15, 2, Color.WHITE);
        btn.setBounds(0, 0, w, h - 30);
        btn.setFont(new Font("Verdana", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.addActionListener(action);

        JLabel subText = new JLabel(desc);
        subText.setFont(new Font("Verdana", Font.ITALIC, 12));
        subText.setForeground(new Color(60, 60, 60));
        subText.setBounds(5, h - 28, w, 20);

        container.add(btn);
        container.add(subText);
        return container;
    }

    private void renderBranding(JPanel p) {
        final int LEFT_PANEL_WIDTH = 340; 
        final int LOGO_SIZE = 220; 
        final int CENTERED_X = (LEFT_PANEL_WIDTH - LOGO_SIZE) / 2;

        JPanel logoPanel = new JPanel() {
            private Image logoImg;
            {
                try {
                    URL imgUrl = getClass().getResource("/IMAGES/logo.png");
                    if (imgUrl != null) logoImg = ImageIO.read(imgUrl);
                } catch (Exception e) {}
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (logoImg != null) g2.drawImage(logoImg, 0, 0, LOGO_SIZE, LOGO_SIZE, this);
            }
        };
        
        int currentY = 50; 
        logoPanel.setBounds(CENTERED_X, currentY, LOGO_SIZE, LOGO_SIZE); 
        logoPanel.setOpaque(false);
        p.add(logoPanel);
        
        currentY += (LOGO_SIZE + 30); 
        String[] lines = {"\"Precision", "Behind the", "Pattern\""};
        Font quoteFont = new Font("Verdana", Font.BOLD, 38); 
        
        for(int i = 0; i < 3; i++) {
            ShadowLabel l = new ShadowLabel(lines[i], quoteFont, Color.WHITE, Color.BLACK);
            l.setBounds(CENTERED_X, currentY + (i * 52), LOGO_SIZE + 20, 55); 
            p.add(l);
        }
    }

    // ==========================================
    // CUSTOM REUSABLE COMPONENTS
    // ==========================================
    class ShadowLabel extends JComponent {
        private String text; private Font font; private Color textCol, shadowCol;
        public ShadowLabel(String text, Font font, Color textCol, Color shadowCol) {
            this.text = text; this.font = font; this.textCol = textCol; this.shadowCol = shadowCol;
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(shadowCol);
            g2.drawString(text, 2, fm.getAscent() + 2);
            g2.setColor(textCol);
            g2.drawString(text, 0, fm.getAscent());
        }
    }

    class RoundedPanel extends JPanel {
        private int r; Color c;
        public RoundedPanel(int r, Color c) { this.r = r; this.c = c; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
        }
    }

    class RoundedButton extends JButton {
        private Color bg, sc; private int r, s;
        public RoundedButton(String t, Color bg, int r, int s, Color sc) {
            super(t); this.bg = bg; this.r = r; this.s = s; this.sc = sc;
            setOpaque(false); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isArmed() ? bg.darker() : bg);
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),r,r));
            if(s > 0) { 
                g2.setColor(sc); 
                g2.setStroke(new BasicStroke(s)); 
                g2.draw(new RoundRectangle2D.Float(s/2f,s/2f,getWidth()-s,getHeight()-s,r,r)); 
            }
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextileMetricHome().setVisible(true));
    }
}