import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class InstructionsPage extends JFrame {

    private Image bgImage;
    private CardLayout cardLayout;
    private JPanel contentCards;
    private JButton[] navButtons;
    private TextileMetricHome parentApp;

    public InstructionsPage(TextileMetricHome parentApp) {
        this.parentApp = parentApp;
        
        setTitle("TextileMetric - Interactive User Guide");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load Background
        try {
            URL imgUrl = getClass().getResource("/IMAGES/featureBackground.jpg");
            if (imgUrl != null) bgImage = ImageIO.read(imgUrl);
        } catch (Exception e) {
            System.out.println("Background image not found.");
        }

        // Main Panel
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
        // LEFT SIDEBAR (Navigation)
        // ==========================================
        JPanel sidebar = new RoundedPanel(20, new Color(45, 55, 65, 245));
        sidebar.setBounds(30, 40, 320, 580);
        sidebar.setLayout(null);
        mainPanel.add(sidebar);

        JLabel guideTitle = new JLabel("USER GUIDE", SwingConstants.CENTER);
        guideTitle.setFont(new Font("Verdana", Font.BOLD, 26));
        guideTitle.setForeground(new Color(230, 200, 150)); // Gold
        guideTitle.setBounds(0, 30, 320, 40);
        sidebar.add(guideTitle);

        JLabel guideSub = new JLabel("Select a module below:", SwingConstants.CENTER);
        guideSub.setFont(new Font("Verdana", Font.ITALIC, 14));
        guideSub.setForeground(new Color(150, 165, 180));
        guideSub.setBounds(0, 65, 320, 20);
        sidebar.add(guideSub);

        // Sidebar Navigation Buttons
        String[] modules = {
            "GSM \"Fabric Weight\" Analyzer", 
            "Fabric Recommender", 
            "Fit Ease Standardizer", 
            "Stretch Ratio Calculator", 
            "Visual Fabric Comparator"
        };
        
        navButtons = new JButton[5];
        int btnY = 120;
        for (int i = 0; i < 5; i++) {
            final String moduleName = modules[i];
            navButtons[i] = new SidebarButton(moduleName);
            navButtons[i].setBounds(20, btnY, 280, 50);
            
            navButtons[i].addActionListener(e -> {
                cardLayout.show(contentCards, moduleName);
                updateButtonStyles(moduleName);
            });
            sidebar.add(navButtons[i]);
            btnY += 65;
        }

        // Back Button in Sidebar
        JButton backBtn = new RoundedButton("BACK TO MENU", new Color(180, 60, 60), 20, 0, Color.WHITE);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Verdana", Font.BOLD, 16));
        backBtn.setBounds(20, 500, 280, 55);
        backBtn.addActionListener(e -> this.dispose());
        sidebar.add(backBtn);

        // ==========================================
        // RIGHT CONTENT AREA (Card Layout)
        // ==========================================
        cardLayout = new CardLayout();
        contentCards = new RoundedPanel(25, new Color(196, 178, 145, 0)); // Transparent wrapper
        contentCards.setBounds(370, 40, 780, 580);
        contentCards.setLayout(cardLayout);
        mainPanel.add(contentCards);

        // 1. GSM Card
        contentCards.add(buildInstructionCard(
            "GSM (Fabric Weight) Analyzer",
            "Determines the precise density of a fabric to identify its optimal structural applications and drape limits.",
            new String[]{
                "Cut a perfect square or rectangular sample of your fabric.",
                "Use a precision scale to find the exact weight in grams.",
                "Input the Length, Width, and Weight into the respective fields.",
                "Click Calculate to view its weight category and ideal garment types."
            },
            "PRO TIP: For maximum accuracy, cut a perfect 10cm x 10cm square. This provides a clean 100cm² area, minimizing rounding errors when scaling up to grams per square meter."
        ), modules[0]);

        // 2. Recommender Card
        contentCards.add(buildInstructionCard(
            "Fabric Substitute Recommender",
            "Sources intelligent, budget-friendly material alternatives based on specific physical properties.",
            new String[]{
                "Select the desired Drape (how the fabric falls).",
                "Select the desired Finish (how the fabric catches light).",
                "Select the desired Weight (how heavy/opaque the fabric is).",
                "Click 'Get Recommendation' to generate a carousel of options.",
                "Click any generated fabric card to read its specific properties."
            },
            "PRO TIP: Prioritize matching 'Drape' and 'Weight' over 'Finish'. You can often fake a glossy finish with linings, but you cannot fake the structural behavior of the fabric."
        ), modules[1]);

        // 3. Fit Ease Card
        contentCards.add(buildInstructionCard(
            "Fit Ease Standardizer",
            "Applies mathematically perfect wearing ease to raw body measurements to generate finished garment specs.",
            new String[]{
                "Input the client's raw body measurements (Bust, Waist, Hips).",
                "Select the body part you are working on.",
                "Select the desired Garment Silhouette (Skin-tight to Loose).",
                "Click 'Generate Spec' to build the full-body dashboard."
            },
            "PRO TIP: The 'Skin-tight' setting assumes 0.0\" ease. Only use this silhouette if your fabric contains at least 5% spandex/elastane to ensure mobility."
        ), modules[2]);

        // 4. Stretch Card
        contentCards.add(buildInstructionCard(
            "Stretch Ratio Calculator",
            "Calculates precise pattern reductions based on the stretch percentage of knit fabrics.",
            new String[]{
                "Cut a standard 10cm swatch of your fabric.",
                "Stretch it comfortably against a ruler to find its maximum stretch length.",
                "Input the Relaxed Length (10cm), Stretched Length, and Target Body Spec.",
                "Click Analyze to generate your final pattern cut length."
            },
            "PRO TIP: For 4-way stretch knits, calculate horizontal stretch for circumferences (Bust/Waist) and vertical stretch for the garment length to prevent torso sagging."
        ), modules[3]);

        // 5. Comparator Card
        contentCards.add(buildInstructionCard(
            "Visual Fabric Comparator",
            "Evaluates aesthetic harmony, color contrast, and structural compatibility between mixed materials.",
            new String[]{
                "Click the slots to upload Main Fabric, Lining, and Hardware.",
                "Use the dropdowns to define the physical characteristics of each item.",
                "Click 'Run Coordination Analysis' to process the data.",
                "Review the analysis dashboard for drape warnings, texture notes, and color harmony."
            },
            "PRO TIP: Always photograph your swatches in indirect, natural daylight. Artificial warm or cool lighting will skew the RGB values and cause inaccurate coordination readings."
        ), modules[4]);

        // Set initial active state
        updateButtonStyles(modules[0]);
    }

    // ==========================================
    // UI BUILDER FOR INSTRUCTION CARDS
    // ==========================================
    private JPanel buildInstructionCard(String title, String purpose, String[] steps, String tip) {
        JPanel panel = new RoundedPanel(25, new Color(245, 245, 250));
        panel.setLayout(null);

        // Header Banner
        JPanel header = new RoundedPanel(20, new Color(55, 70, 85));
        header.setBounds(20, 20, 740, 70);
        header.setLayout(null);
        panel.add(header);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(25, 15, 600, 40);
        header.add(titleLabel);

        // Purpose Section
        JLabel purposeTitle = new JLabel("PURPOSE");
        purposeTitle.setFont(new Font("Verdana", Font.BOLD, 14));
        purposeTitle.setForeground(new Color(150, 160, 170));
        purposeTitle.setBounds(30, 110, 200, 20);
        panel.add(purposeTitle);

        JTextArea purposeText = new JTextArea(purpose);
        purposeText.setFont(new Font("Verdana", Font.PLAIN, 16));
        purposeText.setForeground(new Color(60, 70, 80));
        purposeText.setBackground(new Color(245, 245, 250));
        purposeText.setLineWrap(true);
        purposeText.setWrapStyleWord(true);
        purposeText.setEditable(false);
        purposeText.setBounds(30, 135, 720, 50);
        panel.add(purposeText);

        // Step-by-Step Section (Dark Dashboard Style)
        JPanel stepsPanel = new RoundedPanel(20, new Color(40, 45, 55));
        stepsPanel.setBounds(20, 200, 740, 260);
        stepsPanel.setLayout(null);
        panel.add(stepsPanel);

        JLabel stepsTitle = new JLabel("HOW TO USE:");
        stepsTitle.setFont(new Font("Verdana", Font.BOLD, 14));
        stepsTitle.setForeground(new Color(230, 200, 150));
        stepsTitle.setBounds(20, 15, 200, 20);
        stepsPanel.add(stepsTitle);

        int stepY = 45;
        for (int i = 0; i < steps.length; i++) {
            // Step Number Circle
            JPanel numCircle = new RoundedPanel(15, new Color(75, 120, 170));
            numCircle.setBounds(20, stepY, 30, 30);
            numCircle.setLayout(new BorderLayout());
            JLabel numLabel = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            numLabel.setFont(new Font("Verdana", Font.BOLD, 14));
            numLabel.setForeground(Color.WHITE);
            numCircle.add(numLabel, BorderLayout.CENTER);
            stepsPanel.add(numCircle);

            // Step Text
            JLabel stepText = new JLabel(steps[i]);
            stepText.setFont(new Font("Verdana", Font.PLAIN, 15));
            stepText.setForeground(new Color(220, 225, 230));
            stepText.setBounds(65, stepY, 650, 30);
            stepsPanel.add(stepText);

            stepY += 45;
        }

        // Pro-Tip Section
        JPanel tipPanel = new RoundedPanel(15, new Color(255, 240, 200));
        tipPanel.setBounds(20, 485, 740, 70);
        tipPanel.setLayout(null);
        
        // Dark gold left accent border
        JPanel tipAccent = new RoundedPanel(15, new Color(220, 170, 80));
        tipAccent.setBounds(0, 0, 10, 70);
        tipPanel.add(tipAccent);

        JLabel tipText = new JLabel("<html>" + tip + "</html>");
        tipText.setFont(new Font("Verdana", Font.BOLD, 13));
        tipText.setForeground(new Color(120, 90, 40));
        tipText.setBounds(30, 10, 690, 50);
        tipPanel.add(tipText);

        panel.add(tipPanel);

        return panel;
    }

    private void updateButtonStyles(String activeModule) {
        for (JButton btn : navButtons) {
            if (btn.getText().equals(activeModule)) {
                btn.setBackground(new Color(75, 120, 170)); // Active Blue
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(55, 65, 75)); // Inactive Dark Slate
                btn.setForeground(new Color(150, 165, 180));
            }
        }
    }

    // ==========================================
    // CUSTOM UI COMPONENTS
    // ==========================================
    class SidebarButton extends JButton {
        public SidebarButton(String text) {
            super(text);
            setFont(new Font("Verdana", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
        }
    }

    class RoundedPanel extends JPanel {
        private int r; Color c;
        public RoundedPanel(int r, Color c) { this.r = r; this.c = c; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
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
        SwingUtilities.invokeLater(() -> new InstructionsPage(null).setVisible(true));
    }
}