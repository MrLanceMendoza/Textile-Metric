

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class StretchAnalyzerApp extends JFrame {

    private Image bgImage;

    public StretchAnalyzerApp() {
        setTitle("TextileMetric - Stretch Ratio & Negative Ease");
        setSize(1200, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        // Load background image
        try {
            URL imgUrl = getClass().getResource("/IMAGES/featureBackground.jpg");
            if (imgUrl != null) bgImage = ImageIO.read(imgUrl);
        } catch (Exception e) {
            System.out.println("Background image not found.");
        }

        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(214, 203, 190));
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
        // RIGHT SIDE (Main Card Wrapper)
        // ==========================================
        JPanel cardPanel = new RoundedPanel(25, new Color(196, 178, 145));
        cardPanel.setBounds(340, 40, 800, 600);
        cardPanel.setLayout(null);
        
        JPanel shadowPanel = new RoundedPanel(25, new Color(40, 40, 40, 150));
        shadowPanel.setBounds(345, 45, 800, 600);
        
        mainPanel.add(cardPanel);
        mainPanel.add(shadowPanel);

        JLabel titleLabel = new JLabel("Stretch Ratio Analyzer");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(40, 20, 700, 40);
        cardPanel.add(titleLabel);

        // BLUE FORM PANEL (Plaque) 
        JPanel formPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                
                // Dark background shadow
                g2.setColor(new Color(55, 70, 85));
                g2.fillRoundRect(0, 0, w, h, 15, 15);
                
                // Cut-corner polygon
                int pad = 12, snip = 15;
                Polygon p = new Polygon();
                p.addPoint(pad + snip, pad); p.addPoint(w - pad - snip, pad);
                p.addPoint(w - pad, pad + snip); p.addPoint(w - pad, h - pad - snip);
                p.addPoint(w - pad - snip, h - pad); p.addPoint(pad + snip, h - pad);
                p.addPoint(pad, h - pad - snip); p.addPoint(pad, pad + snip);
                
                // Vertical Blue Gradient instead of flat color
                GradientPaint blueGrad = new GradientPaint(0, pad, new Color(85, 135, 185), 0, h - pad, new Color(55, 95, 145));
                g2.setPaint(blueGrad);
                g2.fillPolygon(p);
                
                // Dividing line
                g2.setColor(new Color(255, 255, 255, 150)); // Slightly transparent white
                int lineX = (int)(w * 0.58);
                g2.drawLine(lineX, 60, lineX, h - 30);
            }
        };
        formPanel.setBounds(30, 80, 740, 320);
        cardPanel.add(formPanel);

        JLabel formTitle = new JLabel("Measurement Specification Form", SwingConstants.CENTER);
        formTitle.setFont(new Font("Verdana", Font.BOLD, 24));
        formTitle.setForeground(Color.WHITE);
        formTitle.setBounds(0, 15, 740, 35);
        formPanel.add(formTitle);

        int inputX = 65, inputW = 300, fieldH = 35;
        formPanel.add(createInputLabel("Relaxed Fabric Length (cm):", inputX, 70));
        JTextField relaxedField = createTextField(inputX, 100, inputW, fieldH);
        formPanel.add(relaxedField);

        formPanel.add(createInputLabel("Stretched Fabric Length (cm):", inputX, 150));
        JTextField stretchedField = createTextField(inputX, 180, inputW, fieldH);
        formPanel.add(stretchedField);

        formPanel.add(createInputLabel("Target Body Measurement (inches):", inputX, 230));
        JTextField targetField = createTextField(inputX, 260, inputW, fieldH);
        formPanel.add(targetField);

        JButton analyzeBtn = new RoundedButton("Analyze Stretch", new Color(130, 135, 140), 25, 3, Color.WHITE);
        analyzeBtn.setForeground(Color.WHITE);
        analyzeBtn.setFont(new Font("Verdana", Font.BOLD, 20)); 
        analyzeBtn.setBounds(460, 120, 240, 80); 
        formPanel.add(analyzeBtn);

        JButton clearBtn = new RoundedButton("Clear", new Color(180, 60, 60), 25, 0, Color.WHITE);
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Verdana", Font.BOLD, 18));
        clearBtn.setBounds(460, 215, 240, 50);
        formPanel.add(clearBtn);

        // ==========================================
        // ENHANCED RESULTS DASHBOARD
        // ==========================================
        JPanel outputBox = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Rich dark slate background for high contrast
                g2.setColor(new Color(45, 55, 65));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Subtle inner border
                g2.setColor(new Color(80, 95, 110));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                
                // Centered vertical divider line
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(80, 95, 110));
                g2.drawLine(getWidth() / 2, 25, getWidth() / 2, getHeight() - 25);
            }
        };
        outputBox.setBounds(30, 420, 740, 150);
        cardPanel.add(outputBox);

        // --- LEFT METRIC: Percentage Stretch ---
        JLabel stretchTitle = new JLabel("PERCENTAGE STRETCH", SwingConstants.CENTER);
        stretchTitle.setFont(new Font("Verdana", Font.BOLD, 14));
        stretchTitle.setForeground(new Color(150, 165, 180)); // Soft grey-blue
        stretchTitle.setBounds(0, 30, 370, 30);
        outputBox.add(stretchTitle);

        JLabel stretchValue = new JLabel("-- %", SwingConstants.CENTER);
        stretchValue.setFont(new Font("Verdana", Font.BOLD, 46));
        stretchValue.setForeground(new Color(230, 200, 150)); // Warm gold/tan accent
        stretchValue.setBounds(0, 65, 370, 55);
        outputBox.add(stretchValue);

        // --- RIGHT METRIC: Recommended Cut ---
        JLabel cutTitle = new JLabel("RECOMMENDED CUT", SwingConstants.CENTER);
        cutTitle.setFont(new Font("Verdana", Font.BOLD, 14));
        cutTitle.setForeground(new Color(150, 165, 180)); // Soft grey-blue
        cutTitle.setBounds(370, 30, 370, 30);
        outputBox.add(cutTitle);

        JLabel cutValue = new JLabel("-- in", SwingConstants.CENTER);
        cutValue.setFont(new Font("Verdana", Font.BOLD, 46));
        cutValue.setForeground(new Color(230, 200, 150)); // Warm gold/tan accent
        cutValue.setBounds(370, 70, 370, 55);
        outputBox.add(cutValue);

        analyzeBtn.addActionListener(e -> {
            try {
                double relaxed = Double.parseDouble(relaxedField.getText());
                double stretched = Double.parseDouble(stretchedField.getText());
                double target = Double.parseDouble(targetField.getText());
                
                if (relaxed <= 0) return;
                
                double stretchFraction = (stretched - relaxed) / relaxed;
                double patternMeasurement = target / (1.0 + stretchFraction);
                
                // Update the new styled labels
                stretchValue.setText(String.format("%.0f %%", stretchFraction * 100.0));
                cutValue.setText(String.format("%.1f in", patternMeasurement));
            } catch (Exception ex) {
                new NotificationDialog(
                    StretchAnalyzerApp.this, 
                    "Invalid Input", 
                    "Please enter valid numeric values."
                ).setVisible(true);
            }
        });

        clearBtn.addActionListener(e -> {
            relaxedField.setText("");
            stretchedField.setText("");
            targetField.setText("");
            stretchValue.setText("-- %");
            cutValue.setText("-- in");
        });

        JButton backBtn = new RoundedButton("BACK", Color.WHITE, 20, 0, Color.WHITE);
        backBtn.setForeground(new Color(139, 80, 50));
        backBtn.setFont(new Font("Verdana", Font.BOLD, 24));
        backBtn.setBounds(40, 560, 180, 80);
        mainPanel.add(backBtn);
        backBtn.addActionListener(e -> this.dispose());
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
        
        int currentY = 50; // Moved logo UP
        logoPanel.setBounds(CENTERED_X, currentY, LOGO_SIZE, LOGO_SIZE); 
        logoPanel.setOpaque(false);
        p.add(logoPanel);
        
        currentY += (LOGO_SIZE + 30); 
        String[] lines = {"\"Precision", "Behind the", "Pattern\""};
        Font quoteFont = new Font("Verdana", Font.BOLD, 38); // Reduced text size
        
        for(int i=0; i<3; i++) {
            ShadowLabel l = new ShadowLabel(lines[i], quoteFont, Color.WHITE, Color.BLACK);
            l.setBounds(CENTERED_X, currentY + (i*52), LOGO_SIZE + 20, 55); 
            p.add(l);
        }
    }

    private JLabel createInputLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Verdana", Font.PLAIN, 16));
        l.setForeground(Color.WHITE);
        l.setBounds(x, y, 300, 25);
        return l;
    }

    private JTextField createTextField(int x, int y, int w, int h) {
        JTextField f = new JTextField();
        f.setBounds(x, y, w, h);
        f.setFont(new Font("Verdana", Font.BOLD, 18));
        f.setForeground(new Color(40, 50, 60)); // Darker, softer text
        f.setBackground(new Color(248, 248, 252)); // Very soft off-white
        
        // Custom compound border for padding and outline
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(110, 130, 160), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Internal padding
        ));
        
        f.setCaretColor(new Color(220, 100, 100)); // Red cursor for a pop of color
        return f;
    }

    

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
            Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
        }
    }

    class RoundedButton extends JButton {
        private Color bg, sc; private int r, s;
        public RoundedButton(String t, Color bg, int r, int s, Color sc) {
            super(t); this.bg = bg; this.r = r; this.s = s; this.sc = sc;
            setOpaque(false); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isArmed() ? bg.darker() : bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), r, r));
            if (s > 0) { g2.setColor(sc); g2.setStroke(new BasicStroke(s)); 
                g2.draw(new RoundRectangle2D.Float(s/2f, s/2f, getWidth()-s, getHeight()-s, r, r)); }
            super.paintComponent(g);
        }
    }

    class NotificationDialog extends JDialog {
        public NotificationDialog(JFrame parent, String title, String message) {
            super(parent, true); // True makes it modal (blocks background clicks)
            setUndecorated(true);
            setBackground(new Color(0, 0, 0, 0)); // Transparent to show rounded corners
            setSize(400, 200);
            setLocationRelativeTo(parent);

            JPanel dialogPanel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Main Background (using your form's slate blue)
                    g2.setColor(new Color(55, 70, 85));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    
                    // Warning Accent Banner (Soft Red)
                    g2.setColor(new Color(220, 80, 80));
                    g2.fillRoundRect(0, 0, getWidth(), 20, 25, 25);
                    g2.fillRect(0, 10, getWidth(), 10); // Squares off the bottom of the banner
                    
                    // Optional Border
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);
                }
            };
            dialogPanel.setOpaque(false);
            setContentPane(dialogPanel);

            // Title Label
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBounds(20, 35, 360, 30);
            dialogPanel.add(titleLabel);

            // Message Label
            JLabel msgLabel = new JLabel(message, SwingConstants.CENTER);
            msgLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
            msgLabel.setForeground(new Color(220, 220, 220));
            msgLabel.setBounds(20, 75, 360, 30);
            dialogPanel.add(msgLabel);

            // OK Button (Reusing your RoundedButton class)
            JButton okBtn = new RoundedButton("OK", new Color(130, 135, 140), 20, 2, Color.WHITE);
            okBtn.setForeground(Color.WHITE);
            okBtn.setFont(new Font("Verdana", Font.BOLD, 16));
            okBtn.setBounds(140, 130, 120, 45);
            okBtn.addActionListener(e -> dispose()); // Closes the dialog
            dialogPanel.add(okBtn);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StretchAnalyzerApp().setVisible(true));
    }
}