import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class FitEaseStandardizerApp extends JFrame {

    private Image bgImage;

    public FitEaseStandardizerApp() {
        setTitle("TextileMetric - Fit Ease Standardizer");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load background image
        try {
            URL imgUrl = getClass().getResource("/IMAGES/featureBackground.jpg");
            if (imgUrl != null) {
                bgImage = ImageIO.read(imgUrl);
            }
        } catch (Exception e) {
            System.out.println("Background image not found.");
        }

        // Main background panel
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

        renderBranding(mainPanel);

        // ==========================================
        // MAIN CARD AREA
        // ==========================================
        JPanel cardPanel = new RoundedPanel(25, new Color(196, 178, 145));
        cardPanel.setBounds(340, 40, 800, 600);
        cardPanel.setLayout(null);
        
        JPanel shadowPanel = new RoundedPanel(25, new Color(40, 40, 40, 150));
        shadowPanel.setBounds(345, 45, 800, 600);
        
        mainPanel.add(cardPanel);
        mainPanel.add(shadowPanel);

        JLabel titleLabel = new JLabel("\"Fit Ease\" Standardizer");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(40, 20, 500, 40);
        cardPanel.add(titleLabel);

        // ==========================================
        // LEFT SIDE: FULL BODY INPUT FORM
        // ==========================================
        int leftX = 40;
        int inputW = 300;
        
        JLabel measureLabel = new JLabel("Raw Body Measurements (in):");
        measureLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        measureLabel.setForeground(Color.WHITE);
        measureLabel.setBounds(leftX, 80, 320, 30);
        cardPanel.add(measureLabel);

        // Bust Input
        JLabel bustLabel = new JLabel("Bust:");
        bustLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        bustLabel.setForeground(new Color(60, 60, 60));
        bustLabel.setBounds(leftX, 115, 60, 40);
        cardPanel.add(bustLabel);
        
        JTextField bustField = createTextField(leftX + 60, 115, 240, 40);
        cardPanel.add(bustField);

        // Waist Input
        JLabel waistLabel = new JLabel("Waist:");
        waistLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        waistLabel.setForeground(new Color(60, 60, 60));
        waistLabel.setBounds(leftX, 170, 60, 40);
        cardPanel.add(waistLabel);
        
        JTextField waistField = createTextField(leftX + 60, 170, 240, 40);
        cardPanel.add(waistField);

        // Hips Input
        JLabel hipsLabel = new JLabel("Hips:");
        hipsLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        hipsLabel.setForeground(new Color(60, 60, 60));
        hipsLabel.setBounds(leftX, 225, 60, 40);
        cardPanel.add(hipsLabel);
        
        JTextField hipsField = createTextField(leftX + 60, 225, 240, 40);
        cardPanel.add(hipsField);

        // Silhouette Dropdown
        JLabel silLabel = new JLabel("Desired Silhouette:");
        silLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        silLabel.setForeground(Color.WHITE);
        silLabel.setBounds(leftX, 290, 300, 30);
        cardPanel.add(silLabel);

        String[] silhouettes = {"Skin-tight", "Fitted", "Semi-Fitted", "Loose"};
        JComboBox<String> silhouetteCombo = createStyledCombo(silhouettes);
        silhouetteCombo.setBounds(leftX, 325, inputW, 45);
        cardPanel.add(silhouetteCombo);

        JButton applyBtn = new RoundedButton("Generate Spec", new Color(130, 230, 100), 20, 3, Color.WHITE);
        applyBtn.setFont(new Font("Verdana", Font.BOLD, 18));
        applyBtn.setForeground(new Color(40, 60, 40));
        applyBtn.setBounds(leftX, 410, inputW, 60);
        cardPanel.add(applyBtn);

        // Vertical Divider
        JPanel divider = new JPanel();
        divider.setBackground(Color.WHITE);
        divider.setBounds(380, 80, 2, 480);
        cardPanel.add(divider);

        // ==========================================
        // RIGHT SIDE: DYNAMIC SPEC DASHBOARD
        // ==========================================
        JPanel resultsPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(45, 55, 65)); // Dark slate background
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(80, 95, 110)); // Inner border
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
            }
        };
        resultsPanel.setBounds(410, 80, 350, 480);
        cardPanel.add(resultsPanel);

        // Dashboard Headers
        JLabel outputTitle = new JLabel("FINISHED GARMENT SPEC", SwingConstants.CENTER);
        outputTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        outputTitle.setForeground(Color.WHITE);
        outputTitle.setBounds(0, 20, 350, 25);
        resultsPanel.add(outputTitle);

        JLabel currentSilLabel = new JLabel("Silhouette: Pending...", SwingConstants.CENTER);
        currentSilLabel.setFont(new Font("Verdana", Font.ITALIC, 14));
        currentSilLabel.setForeground(new Color(150, 165, 180));
        currentSilLabel.setBounds(0, 45, 350, 20);
        resultsPanel.add(currentSilLabel);

        // Separator Line
        JPanel line = new JPanel();
        line.setBackground(new Color(80, 95, 110));
        line.setBounds(20, 80, 310, 1);
        resultsPanel.add(line);

        // Result Labels - Initialized empty
        JLabel outBust = createResultLabel("BUST:", 95, resultsPanel);
        JLabel outWaist = createResultLabel("WAIST:", 185, resultsPanel);
        JLabel outHips = createResultLabel("HIPS:", 275, resultsPanel);

        // --- Dynamic Description Label ---
        JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; color: #96A5B4;'>Select a silhouette to generate fit profile.</div></html>", SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        descriptionLabel.setBounds(20, 370, 310, 90);
        resultsPanel.add(descriptionLabel);

        // ==========================================
        // LOGIC ENGINE (Math & UI Update)
        // ==========================================
        applyBtn.addActionListener(e -> {
            try {
                // Parse inputs
                double rawBust = Double.parseDouble(bustField.getText());
                double rawWaist = Double.parseDouble(waistField.getText());
                double rawHips = Double.parseDouble(hipsField.getText());
                String sil = (String) silhouetteCombo.getSelectedItem();

                // Ease Variables & Description
                double easeBust = 0, easeWaist = 0, easeHips = 0;
                String descText = "";

                // Determine Ease & Description based on Silhouette choice
                if (sil.equals("Skin-tight")) {
                    easeBust = 0.0; easeWaist = 0.0; easeHips = 0.0;
                    descText = "Garment sits flush against the body with zero added ease. Ideal for stretch fabrics, activewear, and swimwear.";
                } else if (sil.equals("Fitted")) {
                    easeBust = 1.5; easeWaist = 1.0; easeHips = 1.5;
                    descText = "Closely follows natural curves with minimal ease for comfortable movement. Ideal for tailored blouses and sleek dresses.";
                } else if (sil.equals("Semi-Fitted")) {
                    easeBust = 3.0; easeWaist = 2.0; easeHips = 2.5;
                    descText = "Skims the body without clinging, providing a flattering shape with room to breathe. Ideal for everyday wear.";
                } else if (sil.equals("Loose")) {
                    easeBust = 5.0; easeWaist = 4.0; easeHips = 4.5;
                    descText = "Generous fit that drapes away from the body, prioritizing comfort and a relaxed aesthetic. Ideal for oversized garments.";
                }
                
                // Update the new description label
                descriptionLabel.setText(String.format("<html><div style='text-align: center; color: #A0B0C0;'><b style='color:#E6C896;'>FIT PROFILE:</b><br><br>%s</div></html>", descText));

                // Update Dashboard Headers
                currentSilLabel.setText("Silhouette: " + sil);

                // Update Output Labels with formatted HTML for beautiful layout
                outBust.setText(String.format("<html><body><span style='font-size:12px; color:#96A5B4;'>BUST:</span><br><span style='font-size:26px; color:#E6C896;'>%.1f\"</span> <span style='font-size:11px; color:#96A5B4;'>(Raw %.1f\" + %.1f\" ease)</span></body></html>", 
                        (rawBust + easeBust), rawBust, easeBust));

                outWaist.setText(String.format("<html><body><span style='font-size:12px; color:#96A5B4;'>WAIST:</span><br><span style='font-size:26px; color:#E6C896;'>%.1f\"</span> <span style='font-size:11px; color:#96A5B4;'>(Raw %.1f\" + %.1f\" ease)</span></body></html>", 
                        (rawWaist + easeWaist), rawWaist, easeWaist));

                outHips.setText(String.format("<html><body><span style='font-size:12px; color:#96A5B4;'>HIPS:</span><br><span style='font-size:26px; color:#E6C896;'>%.1f\"</span> <span style='font-size:11px; color:#96A5B4;'>(Raw %.1f\" + %.1f\" ease)</span></body></html>", 
                        (rawHips + easeHips), rawHips, easeHips));

            } catch (NumberFormatException ex) {
                new NotificationDialog(
                    FitEaseStandardizerApp.this, 
                    "Input Error", 
                    "Please enter valid numeric measurements for all three fields."
                ).setVisible(true);
            }
        });

        // BACK Button
        JButton backBtn = new RoundedButton("BACK", Color.WHITE, 20, 0, Color.WHITE);
        backBtn.setForeground(new Color(139, 80, 50));
        backBtn.setFont(new Font("Verdana", Font.BOLD, 24));
        backBtn.setBounds(40, 560, 180, 60);
        mainPanel.add(backBtn);
        backBtn.addActionListener(ev -> this.dispose());
    }

    // --- Helper Methods ---

    private JLabel createResultLabel(String labelText, int yPos, JPanel parent) {
        JLabel label = new JLabel(String.format("<html><body style='padding-top: 5px;'><span style='font-size:12px; color:#96A5B4;'>%s</span><br><span style='font-size:26px; color:#E6C896;'>--</span></body></html>", labelText));
        label.setFont(new Font("Verdana", Font.BOLD, 14));
        
        label.setBounds(15, yPos, 320, 80); 
        
        parent.add(label);
        return label;
    }

    private JTextField createTextField(int x, int y, int w, int h) {
        JTextField f = new JTextField();
        f.setBounds(x, y, w, h);
        f.setFont(new Font("Verdana", Font.BOLD, 18));
        f.setForeground(new Color(40, 50, 60));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        return f;
    }

    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Verdana", Font.BOLD, 16));
        combo.setForeground(new Color(60, 60, 60));
        combo.setBackground(Color.WHITE);
        combo.setFocusable(false);
        
        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("\u25BC");
                button.setFont(new Font("SansSerif", Font.PLAIN, 12));
                button.setBackground(new Color(235, 235, 240));
                button.setForeground(new Color(100, 100, 100));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(false);
                return button;
            }
        });
        
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
                if (isSelected) {
                    label.setBackground(new Color(85, 105, 125)); 
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(60, 60, 60));
                }
                return label;
            }
        });
        combo.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        return combo;
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
        
        for(int i=0; i<3; i++) {
            ShadowLabel l = new ShadowLabel(lines[i], quoteFont, Color.WHITE, Color.BLACK);
            l.setBounds(CENTERED_X, currentY + (i*52), LOGO_SIZE + 20, 55); 
            p.add(l);
        }
    }

    // Custom UI Component Classes
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
            int y = fm.getAscent();
            g2.setColor(shadowCol);
            g2.drawString(text, 2, y + 2);
            g2.setColor(textCol);
            g2.drawString(text, 0, y);
        }
    }

    class RoundedPanel extends JPanel {
        private int radius; private Color color;
        public RoundedPanel(int radius, Color color) {
            this.radius = radius; this.color = color; setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    class RoundedButton extends JButton {
        private Color bgColor, sColor; private int radius, stroke;
        public RoundedButton(String text, Color bg, int rad, int str, Color sc) {
            super(text); this.bgColor = bg; this.radius = rad; this.stroke = str; this.sColor = sc;
            setOpaque(false); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isArmed() ? bgColor.darker() : bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            if (stroke > 0) {
                g2.setColor(sColor);
                g2.setStroke(new BasicStroke(stroke));
                g2.draw(new RoundRectangle2D.Float(stroke/2f, stroke/2f, getWidth()-stroke, getHeight()-stroke, radius, radius));
            }
            super.paintComponent(g);
        }
    }

    // CUSTOM COMPONENT: Modern Error Notification
    class NotificationDialog extends JDialog {
        public NotificationDialog(JFrame parent, String title, String message) {
            super(parent, true); 
            setUndecorated(true);
            setBackground(new Color(0, 0, 0, 0)); // Transparent to allow rounded corners
            setSize(420, 200);
            setLocationRelativeTo(parent); // Centers exactly over the app window

            JPanel dialogPanel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Main Slate Background
                    g2.setColor(new Color(45, 55, 65));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    
                    // Warning Accent Banner (Soft Red)
                    g2.setColor(new Color(220, 80, 80));
                    g2.fillRoundRect(0, 0, getWidth(), 15, 20, 20);
                    g2.fillRect(0, 10, getWidth(), 5); // Squares off the bottom of the red banner
                    
                    // Inner Border
                    g2.setColor(new Color(80, 95, 110));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                }
            };
            dialogPanel.setOpaque(false);
            setContentPane(dialogPanel);

            // Title Label
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Verdana", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBounds(20, 30, 380, 30);
            dialogPanel.add(titleLabel);

            // Message Label
            JLabel msgLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>", SwingConstants.CENTER);
            msgLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
            msgLabel.setForeground(new Color(220, 220, 220));
            msgLabel.setBounds(20, 65, 380, 50);
            dialogPanel.add(msgLabel);

            // OK Button (Reusing your rounded button)
            JButton okBtn = new RoundedButton("OK", new Color(130, 135, 140), 20, 2, Color.WHITE);
            okBtn.setForeground(Color.WHITE);
            okBtn.setFont(new Font("Verdana", Font.BOLD, 16));
            okBtn.setBounds(150, 130, 120, 45);
            okBtn.addActionListener(e -> dispose()); // Closes the window when clicked
            dialogPanel.add(okBtn);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FitEaseStandardizerApp().setVisible(true));
    }
}