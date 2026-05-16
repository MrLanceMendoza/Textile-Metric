import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class FabricComparatorApp extends JFrame {

    private SwatchPanel mainFabric, liningFabric, hardwareSwatch;
    private JTextPane resultArea;
    private Image bgImage;

    public FabricComparatorApp() {
        setTitle("TextileMetric - Visual Fabric Comparator");
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

        // Main Panel with Background
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
        cardPanel.setBounds(340, 40, 800, 620);
        cardPanel.setLayout(null);
        
        JPanel shadowPanel = new RoundedPanel(25, new Color(40, 40, 40, 150));
        shadowPanel.setBounds(345, 45, 800, 620);
        
        mainPanel.add(cardPanel);
        mainPanel.add(shadowPanel);

        JLabel titleLabel = new JLabel("Visual Fabric Comparator");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 20, 500, 40);
        cardPanel.add(titleLabel);

        // ==========================================
        // SWATCH SLOTS WITH CUSTOM DROPDOWNS
        // ==========================================
        String[] fabricWeights = {"Lightweight (Chiffon/Silk)", "Medium (Cotton/Linen)", "Heavyweight (Denim/Wool)", "Stretch/Knit"};
        String[] fabricTextures = {"Matte/Flat", "Shiny/Satin", "Textured/Woven", "Sheer"};
        
        String[] hardMaterials = {"Metal", "Plastic/Resin", "Wood", "Fabric-Covered"};
        String[] hardFinishes = {"Polished/Shiny", "Brushed/Matte", "Antique/Oxidized"};

        mainFabric = new SwatchPanel("Main Fabric", fabricWeights, fabricTextures);
        mainFabric.setBounds(30, 70, 220, 280); 
        cardPanel.add(mainFabric);

        liningFabric = new SwatchPanel("Secondary/Lining", fabricWeights, fabricTextures);
        liningFabric.setBounds(290, 70, 220, 280);
        cardPanel.add(liningFabric);

        hardwareSwatch = new SwatchPanel("Hardware/Buttons", hardMaterials, hardFinishes);
        hardwareSwatch.setBounds(550, 70, 220, 280);
        cardPanel.add(hardwareSwatch);  

        // ==========================================
        // ACTION BUTTON
        // ==========================================
        JButton analyzeBtn = new RoundedButton("Run Coordination Analysis", new Color(55, 70, 85), 20, 2, Color.WHITE);
        analyzeBtn.setFont(new Font("Verdana", Font.BOLD, 16));
        analyzeBtn.setForeground(Color.WHITE);
        analyzeBtn.setBounds(250, 370, 300, 45); 
        cardPanel.add(analyzeBtn);

        // ==========================================
        // PREMIUM HTML ANALYSIS DASHBOARD
        // ==========================================
        resultArea = new JTextPane();
        resultArea.setContentType("text/html");
        resultArea.setBackground(new Color(45, 55, 65)); 
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        
        JScrollPane scrollResults = new JScrollPane(resultArea);
        scrollResults.setBounds(30, 425, 740, 175);
        scrollResults.setBorder(BorderFactory.createLineBorder(new Color(80, 95, 110), 2)); 
        
        // Custom Modern Scrollbar
        scrollResults.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 110, 120); 
                this.trackColor = new Color(45, 55, 65);    
            }
            @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                return jbutton;
            }
        });
        cardPanel.add(scrollResults);

        analyzeBtn.addActionListener(e -> performAnalysis());
        
        // BACK Button
        JButton backBtn = new RoundedButton("BACK", Color.WHITE, 20, 0, Color.WHITE);
        backBtn.setForeground(new Color(139, 80, 50));
        backBtn.setFont(new Font("Verdana", Font.BOLD, 24));
        backBtn.setBounds(40, 560, 180, 60);
        mainPanel.add(backBtn);
        backBtn.addActionListener(ev -> this.dispose());
    }

    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Verdana", Font.BOLD, 12));
        combo.setForeground(new Color(60, 60, 60));
        combo.setBackground(Color.WHITE);
        combo.setFocusable(false);
        
        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("\u25BC"); 
                button.setFont(new Font("SansSerif", Font.PLAIN, 12));
                button.setBackground(new Color(230, 230, 235));
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
                label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10)); 
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

    // ==========================================
    // THE SMART ANALYSIS ENGINE (HTML VERSION)
    // ==========================================
    private void performAnalysis() {
        if (!mainFabric.hasImage() || !liningFabric.hasImage() || !hardwareSwatch.hasImage()) {
            resultArea.setText("<html><body style='font-family: Verdana; font-size: 13px; color: #FF8C8C; text-align: center; padding-top: 15px;'>"
                    + "<b>AWAITING INPUT:</b><br>Please upload images for all three swatches to run the analysis.</body></html>");
            return;
        }

        StringBuilder report = new StringBuilder();
        
        // Basic HTML setup for the document
        report.append("<html><body style='font-family: Verdana; font-size: 12px; color: #E0E0E5; margin: 0; line-height: 1.4;'>");
        report.append("<h2 style='color: #E6C896; margin-top: 0; margin-bottom: 8px; font-size: 15px; border-bottom: 1px solid #E6C896; padding-bottom: 4px;'>COORDINATION DIAGNOSTICS</h2>");
        
        // Data Extraction
        Color mainColor = mainFabric.getAverageColor();
        Color liningColor = liningFabric.getAverageColor();
        Color hardColor = hardwareSwatch.getAverageColor();

        String mainWeight = (String) mainFabric.typeCombo.getSelectedItem();
        String liningWeight = (String) liningFabric.typeCombo.getSelectedItem();
        
        String mainTex = (String) mainFabric.finishCombo.getSelectedItem();
        String liningTex = (String) liningFabric.finishCombo.getSelectedItem();
        String hardFin = (String) hardwareSwatch.finishCombo.getSelectedItem();

        // Mathematical Distances
        double mainToLining = getColorDistance(mainColor, liningColor);
        double mainToHard = getColorDistance(mainColor, hardColor);

        // ---------------------------------------------------------
        // 1. COLOR & CONTRAST ANALYSIS
        // ---------------------------------------------------------
        report.append("<h3 style='color: #A0B4E6; margin-bottom: 3px; font-size: 13px;'>1. Color & Contrast</h3>");
        
        if (mainToLining < 40) {
            report.append("<p style='margin: 0 0 5px 0;'><b>• Monochromatic Palette:</b> Main fabric and lining blend seamlessly. Creates a highly cohesive, unified aesthetic.</p>");
        } else {
            report.append("<p style='margin: 0 0 5px 0;'><b>• High-Contrast Palette:</b> Strong color blocking detected between exterior and lining. Excellent for statement pieces.</p>");
        }

        if (mainToHard < 60) {
            report.append("<p style='margin: 0 0 5px 0;'><b>• Hardware Blend:</b> Hardware color blends with the main fabric. <span style='color:#FFD166;'>Note:</span> Ideal for concealed closures, but lacks visual pop for decorative accents.</p>");
        } else {
            report.append("<p style='margin: 0 0 5px 0;'><b>• Hardware Pop:</b> <span style='color:#A0E6B4;'>Optimal Contrast.</span> Hardware will stand out distinctively against the garment.</p>");
        }

        // ---------------------------------------------------------
        // 2. STRUCTURAL DRAPE (WEIGHT) ANALYSIS
        // ---------------------------------------------------------
        report.append("<h3 style='color: #A0B4E6; margin-top: 10px; margin-bottom: 3px; font-size: 13px;'>2. Structural Drape</h3>");
        
        if (mainWeight.contains("Heavyweight") && liningWeight.contains("Lightweight")) {
            report.append("<p style='margin: 0 0 5px 0;'><b style='color:#FF8C8C;'>• SEAM WARNING:</b> Heavy exterior paired with delicate lining. Risk of structural pull, bagging, or tearing at internal seams.</p>");
        } else if (mainWeight.contains("Lightweight") && (liningWeight.contains("Heavyweight") || liningWeight.contains("Medium"))) {
            report.append("<p style='margin: 0 0 5px 0;'><b style='color:#FF8C8C;'>• DRAPE WARNING:</b> Lining is heavier than the main fabric. This will distort the exterior drape and cause stiff bulk.</p>");
        } else {
            report.append("<p style='margin: 0 0 5px 0;'><b>• Structural Harmony:</b> <span style='color:#A0E6B4;'>Passed.</span> Fabric weights are compatible and will drape naturally together.</p>");
        }

        // ---------------------------------------------------------
        // 3. TEXTURE & HARDWARE HARMONY
        // ---------------------------------------------------------
        report.append("<h3 style='color: #A0B4E6; margin-top: 10px; margin-bottom: 3px; font-size: 13px;'>3. Texture Mixing</h3>");
        
        if (mainTex.contains("Shiny") && liningTex.contains("Shiny")) {
            report.append("<p style='margin: 0 0 5px 0;'><b style='color:#FFD166;'>• Visual Overload:</b> Two shiny fabrics lack textural contrast. Consider a matte lining to anchor the look.</p>");
        } else if ((mainTex.contains("Matte") && liningTex.contains("Shiny")) || (mainTex.contains("Shiny") && liningTex.contains("Matte"))) {
            report.append("<p style='margin: 0 0 5px 0;'><b>• Textural Depth:</b> <span style='color:#A0E6B4;'>Excellent balance.</span> Mixing matte and shiny fabrics creates elegant depth and tactile interest.</p>");
        } else {
            report.append("<p style='margin: 0 0 5px 0;'><b>• Standard Texture:</b> Textures are safe and consistent.</p>");
        }

        if (mainTex.contains("Shiny") && hardFin.contains("Polished")) {
            report.append("<p style='margin: 0 0 5px 0;'><b style='color:#FFD166;'>• Hardware Clash:</b> High-shine fabric with polished hardware competes for attention. Brushed/Matte hardware is recommended to tone it down.</p>");
        } else if (mainTex.contains("Matte") && hardFin.contains("Polished")) {
            report.append("<p style='margin: 0 0 5px 0;'><b>• Hardware Harmony:</b> <span style='color:#A0E6B4;'>Optimal.</span> Polished hardware acts as a brilliant focal point against the flat matte fabric.</p>");
        }

        report.append("</body></html>");
        resultArea.setText(report.toString());
        resultArea.setCaretPosition(0); 
    }

    private double getColorDistance(Color c1, Color c2) {
        // Euclidean distance in RGB color space
        return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2));
    }

    private void renderBranding(JPanel p) {
        final int LEFT_PANEL_WIDTH = 340; final int LOGO_SIZE = 220; final int CENTERED_X = (LEFT_PANEL_WIDTH - LOGO_SIZE) / 2;
        JPanel logoPanel = new JPanel() {
            private Image logoImg;
            { try { URL imgUrl = getClass().getResource("/IMAGES/logo.png"); if (imgUrl != null) logoImg = ImageIO.read(imgUrl); } catch (Exception e) {} }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g); Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (logoImg != null) g2.drawImage(logoImg, 0, 0, LOGO_SIZE, LOGO_SIZE, this);
            }
        };
        int currentY = 50; logoPanel.setBounds(CENTERED_X, currentY, LOGO_SIZE, LOGO_SIZE); logoPanel.setOpaque(false); p.add(logoPanel);
        currentY += (LOGO_SIZE + 30); String[] lines = {"\"Precision", "Behind the", "Pattern\""}; Font quoteFont = new Font("Verdana", Font.BOLD, 38); 
        for(int i=0; i<3; i++) {
            ShadowLabel l = new ShadowLabel(lines[i], quoteFont, Color.WHITE, Color.BLACK);
            l.setBounds(CENTERED_X, currentY + (i*52), LOGO_SIZE + 20, 55); p.add(l);
        }
    }

    // ==========================================
    // UI COMPONENT CLASSES
    // ==========================================
    class ShadowLabel extends JComponent {
        private String text; private Font font; private Color textCol, shadowCol;
        public ShadowLabel(String text, Font font, Color textCol, Color shadowCol) { this.text = text; this.font = font; this.textCol = textCol; this.shadowCol = shadowCol; }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(font); FontMetrics fm = g2.getFontMetrics(); int y = fm.getAscent();
            g2.setColor(shadowCol); g2.drawString(text, 2, y + 2); g2.setColor(textCol); g2.drawString(text, 0, y);
        }
    }

    class SwatchPanel extends JPanel {
        private BufferedImage img = null;
        public JComboBox<String> typeCombo, finishCombo;
        
        public SwatchPanel(String titleText, String[] types, String[] finishes) {
            setLayout(null);
            setOpaque(false); 
            
            JButton imageClickZone = new JButton();
            imageClickZone.setBounds(10, 10, 200, 160); 
            imageClickZone.setOpaque(false);
            imageClickZone.setContentAreaFilled(false);
            imageClickZone.setBorderPainted(false);
            imageClickZone.setCursor(new Cursor(Cursor.HAND_CURSOR));
            imageClickZone.addActionListener(e -> uploadImage());
            add(imageClickZone);

            JLabel title = new JLabel(titleText, SwingConstants.CENTER);
            title.setBounds(0, 185, 220, 20); 
            title.setFont(new Font("Verdana", Font.BOLD, 14));
            title.setForeground(Color.WHITE);
            add(title);

            typeCombo = createStyledCombo(types);
            typeCombo.setBounds(5, 210, 210, 30); 
            add(typeCombo);

            finishCombo = createStyledCombo(finishes);
            finishCombo.setBounds(5, 245, 210, 30); 
            add(finishCombo);
        }

        private void uploadImage() {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try { img = ImageIO.read(chooser.getSelectedFile()); repaint(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
        
        public boolean hasImage() { return img != null; }
        
        public Color getAverageColor() {
            if (img == null) return Color.GRAY;
            int r=0, g=0, b=0, count=0, centerX = img.getWidth()/2, centerY = img.getHeight()/2;
            for(int x=centerX-5; x<=centerX+5; x++) {
                for(int y=centerY-5; y<=centerY+5; y++) {
                    if(x>=0 && x<img.getWidth() && y>=0 && y<img.getHeight()) {
                        Color c = new Color(img.getRGB(x,y));
                        r+=c.getRed(); g+=c.getGreen(); b+=c.getBlue(); count++;
                    }
                }
            }
            return new Color(r/count, g/count, b/count);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(139, 115, 80)); 
            g2.fillRect(0, 0, 220, 180); 
            
            g2.setColor(new Color(220, 210, 180)); 
            g2.fillRect(10, 10, 200, 160); 
            
            if (img != null) {
                g2.drawImage(img, 15, 15, 190, 150, null);
            } else { 
                g2.setColor(Color.DARK_GRAY); 
                g2.setFont(new Font("Verdana", Font.BOLD, 40)); 
                g2.drawString("+", 95, 105); 
            }
        }
    }

    class RoundedPanel extends JPanel {
        private int r; Color c;
        public RoundedPanel(int r, Color c) { this.r = r; this.c = c; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
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
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),r,r));
            if(s > 0) { 
                g2.setColor(sc); g2.setStroke(new BasicStroke(s)); 
                g2.draw(new RoundRectangle2D.Float(s/2f,s/2f,getWidth()-s,getHeight()-s,r,r)); 
            }
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FabricComparatorApp().setVisible(true));
    }
}