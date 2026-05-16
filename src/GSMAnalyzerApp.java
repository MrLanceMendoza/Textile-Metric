import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class GSMAnalyzerApp extends JFrame {

    private Image bgImage;

    public GSMAnalyzerApp() {
        setTitle("TextileMetric - GSM Analyzer");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load background image from src/IMAGES folder
        try {
            URL imgUrl = getClass().getResource("/IMAGES/featureBackground.jpg");
            if (imgUrl != null) {
                bgImage = ImageIO.read(imgUrl);
            }
        } catch (Exception e) {
            System.out.println("Background image not found.");
        }

        // Main Panel with Background Painting
        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(214, 203, 190)); // Fallback
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(mainPanel);

        // ==========================================
        // LEFT SIDE (Shadowed & Rearranged)
        // ==========================================
        renderBranding(mainPanel);

        // ==========================================
        // MAIN CARD AREA (Fully Opaque)
        // ==========================================
        JPanel cardPanel = new RoundedPanel(25, new Color(196, 178, 145));
        cardPanel.setBounds(340, 40, 800, 600);
        cardPanel.setLayout(null);
        
        JPanel shadowPanel = new RoundedPanel(25, new Color(40, 40, 40, 150));
        shadowPanel.setBounds(345, 45, 800, 600);
        
        mainPanel.add(cardPanel);
        mainPanel.add(shadowPanel);

        JLabel titleLabel = new JLabel("\"GSM\" (Fabric Weight) Analyzer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(40, 20, 600, 40);
        cardPanel.add(titleLabel);

        // --- Inputs ---
        int startX = 40, startY = 110, gapY = 90;
        
        cardPanel.add(createInputLabel("Length (cm)", startX, startY - 25));
        cardPanel.add(new RulerPanel(startX, startY, 350, 40));
        JTextField lengthField = createTextField("", startX + 380, startY);
        cardPanel.add(lengthField);

        cardPanel.add(createInputLabel("Width (cm)", startX, startY + gapY - 25));
        cardPanel.add(new RulerPanel(startX, startY + gapY, 350, 40));
        JTextField widthField = createTextField("", startX + 380, startY + gapY);
        cardPanel.add(widthField);

        cardPanel.add(createInputLabel("Weight (g)", startX, startY + (gapY * 2) - 25));
        cardPanel.add(new RulerPanel(startX, startY + (gapY * 2), 350, 40));
        JTextField weightField = createTextField("", startX + 380, startY + (gapY * 2));
        cardPanel.add(weightField);

        // --- Outputs ---
        JLabel outLabel = new JLabel("Output:");
        outLabel.setForeground(Color.WHITE);
        outLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        outLabel.setBounds(600, 80, 100, 25);
        cardPanel.add(outLabel);

        JTextField outputField = createTextField("0 GSM", 540, 110);
        outputField.setBounds(540, 110, 200, 50);
        outputField.setFont(new Font("Verdana", Font.BOLD, 22));
        outputField.setEditable(false);
        cardPanel.add(outputField);

        // Scale graphic
        ScalePanel scalePanel = new ScalePanel(); 
        scalePanel.setBounds(565, 180, 150, 150);
        cardPanel.add(scalePanel);

        JButton calcBtn = new RoundedButton("Calculate GSM", new Color(130, 230, 100), 20, 3, Color.WHITE);
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFont(new Font("Verdana", Font.BOLD, 18));
        calcBtn.setBounds(540, 350, 200, 60);
        cardPanel.add(calcBtn);

        // Result Info Box
        ResultPanel infoBox = new ResultPanel();
        infoBox.setBounds(40, 420, 480, 120);
        infoBox.setLayout(null);
        
        JLabel categoryLabel = new JLabel("<html><font color='#888888'><b>Category:</b></font> -- </html>");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        categoryLabel.setBounds(30, 25, 430, 30); 
        
        JLabel suitableLabel = new JLabel("<html><font color='#888888'><b>Best For:</b></font> -- </html>");
        suitableLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        suitableLabel.setBounds(30, 65, 430, 30);
        
        infoBox.add(categoryLabel);
        infoBox.add(suitableLabel);
        cardPanel.add(infoBox);
        // Logic
        calcBtn.addActionListener(e -> {
            try {
                double length = Double.parseDouble(lengthField.getText());
                double width = Double.parseDouble(widthField.getText());
                double weight = Double.parseDouble(weightField.getText());
                if (length <= 0 || width <= 0) return;

                double gsm = (weight * 10000.0) / (length * width);
                int gsmInt = (int) Math.round(gsm);
                outputField.setText(gsmInt + " GSM");
                
                scalePanel.updateGsm(gsmInt);

                // Update text and accent colors based on the weight class
                if (gsmInt < 100) {
                    infoBox.setAccentColor(new Color(100, 180, 220)); // Soft Blue
                    categoryLabel.setText("<html><font color='#7a5c43'><b>Category:</b></font> <font color='#2b6b80'>Lightweight / Sheer</font></html>");
                    suitableLabel.setText("<html><font color='#7a5c43'><b>Best For:</b></font> <font color='#333333'>Lingerie, Blouses, Linings</font></html>");
                } else if (gsmInt <= 200) {
                    infoBox.setAccentColor(new Color(130, 230, 100)); // The Green from your button
                    categoryLabel.setText("<html><font color='#7a5c43'><b>Category:</b></font> <font color='#3c7a26'>Medium-weight</font></html>");
                    suitableLabel.setText("<html><font color='#7a5c43'><b>Best For:</b></font> <font color='#333333'>Shirting, Summer Dresses</font></html>");
                } else {
                    infoBox.setAccentColor(new Color(220, 130, 80)); // Warm Orange/Brown
                    categoryLabel.setText("<html><font color='#7a5c43'><b>Category:</b></font> <font color='#a34914'>Heavyweight</font></html>");
                    suitableLabel.setText("<html><font color='#7a5c43'><b>Best For:</b></font> <font color='#333333'>Denim, Coats, Upholstery</font></html>");
                }
            } catch (Exception ex) {
                // Call the custom dialog instead!
                CustomMessageDialog dialog = new CustomMessageDialog(GSMAnalyzerApp.this, "Invalid Input", "Please enter valid numbers in all fields.");
                dialog.setVisible(true);
            }
        });

        // BACK Button
        JButton backBtn = new RoundedButton("BACK", Color.WHITE, 20, 0, Color.WHITE);
        backBtn.setForeground(new Color(139, 80, 50));
        backBtn.setFont(new Font("Verdana", Font.BOLD, 24));
        backBtn.setBounds(40, 560, 180, 60);
        mainPanel.add(backBtn);
        backBtn.addActionListener(e -> this.dispose());
    }

    private void renderBranding(JPanel p) {
        // Defined constant widths for alignment
        final int LEFT_PANEL_WIDTH = 340; 
        final int LOGO_SIZE = 220; 
        
        // Calculate centered X position
        final int CENTERED_X = (LEFT_PANEL_WIDTH - LOGO_SIZE) / 2;

        JPanel logoPanel = new JPanel() {
            private Image logoImg;
            {
                try {
                    java.net.URL imgUrl = getClass().getResource("/IMAGES/logo.png");
                    if (imgUrl != null) {
                        logoImg = javax.imageio.ImageIO.read(imgUrl);
                    }
                } catch (Exception e) {
                    System.out.println("Logo not found in /src/IMAGES/logo.png");
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (logoImg != null) {
                    g2.drawImage(logoImg, 0, 0, LOGO_SIZE, LOGO_SIZE, this);
                } else {
                    g2.setColor(new Color(45, 110, 160));
                    g2.fillOval(0, 0, LOGO_SIZE, LOGO_SIZE);
                    g2.setColor(Color.WHITE);
                    g2.drawOval(5, 5, LOGO_SIZE - 10, LOGO_SIZE - 10);
                }
            }
        };
        
        int currentY = 50;
        
        logoPanel.setBounds(CENTERED_X, currentY, LOGO_SIZE, LOGO_SIZE); 
        logoPanel.setOpaque(false);
        p.add(logoPanel);
        
        // Spacing between logo and text=
        currentY += (LOGO_SIZE + 30); 

        String[] lines = {"\"Precision", "Behind the", "Pattern\""};
        
        Font quoteFont = new Font("Verdana", Font.BOLD, 38);
        
        for(int i=0; i<3; i++) {
            ShadowLabel l = new ShadowLabel(lines[i], quoteFont, Color.WHITE, Color.BLACK);
            
            // Set bounds. Height reduced to 50 to keep the cluster tight
            l.setBounds(CENTERED_X, currentY + (i*52), LOGO_SIZE + 20, 55); 
            p.add(l);
        }
    }

    private JLabel createInputLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));       
        l.setForeground(Color.WHITE);
        l.setBounds(x, y, 200, 25);
        return l;
    }

    private JTextField createTextField(String text, int x, int y) {
        JTextField field = new JTextField(text);
        field.setBounds(x, y, 80, 40);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Verdana", Font.PLAIN, 18));
        return field;
    }

 // CUSTOM COMPONENT: Label with Drop Shadow
    class ShadowLabel extends JComponent {
        private String text; private Font font; private Color textCol, shadowCol;
        public ShadowLabel(String text, Font font, Color textCol, Color shadowCol) {
            this.text = text; this.font = font; this.textCol = textCol; this.shadowCol = shadowCol;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics();
            int y = fm.getAscent();
            
            // 1. Draw Shadow (2px offset)
            g2.setColor(shadowCol);
            g2.drawString(text, 2, y + 2);
            
            // 2. Draw Main Text
            g2.setColor(textCol);
            g2.drawString(text, 0, y);
        }
    }

    class RoundedPanel extends JPanel {
        private int r; Color c;
        public RoundedPanel(int r, Color c) { this.r = r; this.c = c; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
        }
    }
    
    class ScalePanel extends JPanel {
        private double currentGsm = 0;
        private double targetGsm = 0;
        private Timer animTimer;

        public ScalePanel() {
            setOpaque(false);
            // Timer for smooth needle animation
            animTimer = new Timer(16, e -> {
                if (Math.abs(targetGsm - currentGsm) < 0.5) {
                    currentGsm = targetGsm;
                    animTimer.stop();
                } else {
                    currentGsm += (targetGsm - currentGsm) * 0.1;
                }
                repaint();
            });
        }

        public void updateGsm(double newGsm) {
            this.targetGsm = newGsm;
            animTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.scale(1.5, 1.5);

            // DESIGN: Brown Body
            g2.setColor(new Color(150, 100, 70));
            g2.fillPolygon(new int[]{10, 90, 80, 20}, new int[]{10, 10, 30, 30}, 4);
            g2.fillRoundRect(25, 30, 50, 60, 10, 10);
            
            // ORIGINAL DESIGN: Beige Dial 
            g2.setColor(new Color(220, 210, 190));
            g2.fillOval(30, 40, 40, 40);

            // CALCULATED NEEDLE (HAND)
            g2.setColor(new Color(220, 20, 20)); 
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            int cx = 50; // Center X of dial
            int cy = 60; // Center Y of dial
            int r = 15;  // Length of the original needle

            // Cap the visual max at 500 GSM so it doesn't spin off the dial
            double maxGsmVisually = 500.0; 
            double boundedGsm = Math.min(Math.max(currentGsm, 0), maxGsmVisually);
            
            // Calculate angle
            double angleDeg = -135 + ((boundedGsm / maxGsmVisually) * 90); 
            double angleRad = Math.toRadians(angleDeg);

            // Calculate exact end coordinates for the line
            int nx = (int) (cx + r * Math.cos(angleRad));
            int ny = (int) (cy + r * Math.sin(angleRad));

            g2.drawLine(cx, cy, nx, ny);
        }
    }
    
    class RulerPanel extends JPanel {
        public RulerPanel(int x, int y, int w, int h) { setBounds(x, y, w, h); setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int rulerH = 20, yPos = (getHeight() - rulerH) / 2;
            g2.setColor(new Color(255, 170, 70));
            g2.fillRoundRect(0, yPos, getWidth(), rulerH, 5, 5);
            g2.setColor(Color.BLACK);
            for(int i = 5; i < getWidth() - 5; i += 5) {
                int tickH = (i % 20 == 0) ? 10 : 5;
                g2.drawLine(i, yPos, i, yPos + tickH);
            }
        }
    }
    
    class ResultPanel extends JPanel {
        private Color accentColor = new Color(200, 200, 200); // Default grey awaiting input

        public ResultPanel() {
            setOpaque(false);
        }

        public void setAccentColor(Color c) {
            this.accentColor = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Subtle Drop Shadow
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);

            // Main White Background
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);

            // Colored Accent Bar on the left
            g2.setColor(accentColor);
            g2.fillRoundRect(0, 0, 15, getHeight() - 4, 20, 20);
            
            // Square off the right side of the accent bar so it blends seamlessly
            g2.fillRect(10, 0, 5, getHeight() - 4);
        }
    }
    
    class CustomMessageDialog extends JDialog {
        public CustomMessageDialog(JFrame parent, String title, String message) {
            super(parent, true);
            setUndecorated(true);
            setSize(350, 180);
            setLocationRelativeTo(parent);
            setBackground(new Color(0, 0, 0, 0)); // Transparent background for rounded corners

            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // 1. Soft Drop Shadow
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 25, 25);

                    // 2. Main White Box
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 25, 25);

                    // 3. Top Accent Bar (Red for error/warning)
                    g2.setColor(new Color(220, 80, 80));
                    g2.fillRoundRect(0, 0, getWidth() - 10, 20, 25, 25);
                    g2.fillRect(0, 10, getWidth() - 10, 10); // Square the bottom of the red bar
                }
            };
            mainPanel.setLayout(null);
            mainPanel.setOpaque(false);
            setContentPane(mainPanel);

            // Custom Warning Icon (Drawn using Graphics)
            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(220, 80, 80));
                    g2.fillOval(0, 0, 30, 30);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                    g2.drawString("!", 12, 22);
                }
            };
            iconPanel.setBounds(20, 35, 30, 30);
            iconPanel.setOpaque(false);
            mainPanel.add(iconPanel);

            // Title Label
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(50, 50, 50));
            titleLabel.setBounds(65, 35, 250, 30);
            mainPanel.add(titleLabel);

            // Message Label
            JLabel msgLabel = new JLabel("<html>" + message + "</html>");
            msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            msgLabel.setForeground(new Color(100, 100, 100));
            msgLabel.setBounds(65, 65, 250, 40);
            mainPanel.add(msgLabel);

            // OK Button (Reusing your RoundedButton class)
            JButton okBtn = new RoundedButton("OK", new Color(230, 230, 230), 15, 0, Color.WHITE);
            okBtn.setForeground(new Color(80, 80, 80));
            okBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            okBtn.setBounds(230, 115, 80, 35);
            okBtn.addActionListener(e -> dispose()); // Closes the dialog
            mainPanel.add(okBtn);
        }
    }

    class RoundedButton extends JButton {
        private Color bg, sc; private int r, s;
        public RoundedButton(String t, Color bg, int r, int s, Color sc) {
            super(t); this.bg = bg; this.r = r; this.s = s; this.sc = sc;
            setOpaque(false); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isArmed() ? bg.darker() : bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), r, r));
            if (s > 0) {
                g2.setColor(sc); g2.setStroke(new BasicStroke(s));
                g2.draw(new RoundRectangle2D.Float(s/2f, s/2f, getWidth()-s, getHeight()-s, r, r));
            }
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GSMAnalyzerApp().setVisible(true));
    }
}