import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class FabricRecommenderApp extends JFrame {

    private Image bgImage;

    public static JTextArea descriptionBox;

    public FabricRecommenderApp() {
        setTitle("TextileMetric - Fabric Substitute Recommender");
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

        // Left Branding
        renderBranding(mainPanel);

        // Main Cards
        JPanel cardPanel = new RoundedPanel(25, new Color(196, 178, 145));
        cardPanel.setBounds(340, 40, 800, 600);
        cardPanel.setLayout(null);
        
        JPanel shadowPanel = new RoundedPanel(25, new Color(40, 40, 40, 150));
        shadowPanel.setBounds(345, 45, 800, 600);
        
        mainPanel.add(cardPanel);
        mainPanel.add(shadowPanel);

        JLabel titleLabel = new JLabel("\"Fabric Substitute\" Recommender");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(40, 20, 650, 40);
        cardPanel.add(titleLabel);

        // Center Graphic (Polaroid)
        JPanel polaroidPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.translate(200, 250);
                g2.rotate(Math.toRadians(5));
                g2.setColor(new Color(160, 150, 130, 150)); 
                g2.fillRoundRect(-175, -215, 370, 450, 5, 5);
                g2.setColor(new Color(245, 245, 240)); 
                g2.fillRoundRect(-180, -220, 370, 450, 5, 5);
                g2.rotate(Math.toRadians(-5));
                g2.translate(-200, -250);
                
                g2.setColor(new Color(160, 150, 130, 150)); 
                g2.fillRoundRect(25, 45, 360, 440, 5, 5);
                g2.setColor(Color.WHITE); 
                g2.fillRoundRect(20, 40, 360, 440, 5, 5);
                
                g2.setColor(new Color(220, 220, 220));
                g2.drawRect(35, 55, 330, 360);
                
                g2.setColor(new Color(160, 170, 180)); 
                g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(180, 10, 35, 100, 20, 20);
                g2.drawRoundRect(190, 25, 15, 60, 10, 10);
            }
        };
        polaroidPanel.setBounds(40, 80, 420, 500);
        polaroidPanel.setLayout(null);
        polaroidPanel.setOpaque(false);
        cardPanel.add(polaroidPanel);

        // Dropdowns
        String[] drapeOptions = {"Fluid", "Moderate", "Stiff"};
        String[] finishOptions = {"Matte", "Shiny", "Textured"};
        String[] weightOptions = {"Sheer", "Opaque", "Heavy"};

        polaroidPanel.add(createInputLabel("Drape:", 55, 130));
        JComboBox<String> drapeBox = createStyledDropdown(drapeOptions);
        drapeBox.setBounds(155, 125, 180, 40);
        polaroidPanel.add(drapeBox);

        polaroidPanel.add(createInputLabel("Finish:", 55, 200));
        JComboBox<String> finishBox = createStyledDropdown(finishOptions);
        finishBox.setBounds(155, 195, 180, 40);
        polaroidPanel.add(finishBox);

        polaroidPanel.add(createInputLabel("Weight:", 55, 270));
        JComboBox<String> weightBox = createStyledDropdown(weightOptions);
        weightBox.setBounds(155, 265, 180, 40);
        polaroidPanel.add(weightBox);

        // Right Side (Results Panel)
        JPanel resultsPanel = new RoundedPanel(25, new Color(121, 95, 83));
        resultsPanel.setBounds(480, 100, 280, 480); 
        resultsPanel.setLayout(null);
        cardPanel.add(resultsPanel);

        JButton getRecBtn = new RoundedButton("Get Recommendation", new Color(130, 230, 100), 20, 3, Color.WHITE);
        getRecBtn.setForeground(new Color(40, 60, 40));
        getRecBtn.setFont(new Font("Verdana", Font.BOLD, 15));
        getRecBtn.setBounds(15, 20, 250, 60);
        resultsPanel.add(getRecBtn);

        JLabel budgetLabel = new JLabel("Recommendations:");
        budgetLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        budgetLabel.setForeground(Color.WHITE);
        budgetLabel.setBounds(25, 100, 230, 30);
        resultsPanel.add(budgetLabel);

        // --- NEW HORIZONTAL SCROLL AREA ---
        Color panelBrown = new Color(121, 95, 83); 

        JPanel recsContainer = new JPanel();
        recsContainer.setLayout(new BoxLayout(recsContainer, BoxLayout.X_AXIS));
        
        recsContainer.setOpaque(true);
        recsContainer.setBackground(panelBrown);
        recsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(recsContainer);
        scrollPane.setBounds(15, 140, 250, 240);
        scrollPane.setBorder(null); 
        
        scrollPane.setOpaque(true);
        scrollPane.setBackground(panelBrown);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(panelBrown);
        
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        
        // Custom Scrollbar Style
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        
        // --- DYNAMIC DESCRIPTION BOX (Below the scrollbar) ---
        descriptionBox = new JTextArea("Click a fabric card to see its description.");
        descriptionBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionBox.setForeground(new Color(240, 235, 230)); // Soft off-white
        descriptionBox.setLineWrap(true);
        descriptionBox.setWrapStyleWord(true);
        descriptionBox.setOpaque(false);
        descriptionBox.setEditable(false);
        descriptionBox.setFocusable(false);

        descriptionBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Bounds adjusted to fit below the scrollbar and use available space
        descriptionBox.setBounds(15, 385, 250, 85);

        resultsPanel.add(descriptionBox);

        resultsPanel.add(scrollPane);

        // Button Action Logic
        getRecBtn.addActionListener(e -> {
            recsContainer.removeAll(); // Clear old results

            descriptionBox.setText("Click a fabric card to see its description.");

            String drape = drapeBox.getSelectedItem().toString();
            String finish = finishBox.getSelectedItem().toString();
            String weight = weightBox.getSelectedItem().toString();

            // Populate Fabric Cards (Sorted Lowest to Highest Price)
            if (drape.equals("Fluid") && finish.equals("Matte") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Cotton Voile", "Php 150/yd", "Cotton Voile.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Cotton Lawn", "Php 200/yd", "Cotton Lawn.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Rayon Georgette", "Php 200/yd", "Rayon Georgette.jpeg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Silk Chiffon", "Php 1,200/yd", "Silk Chiffon.jpg"));
            } 
            else if (drape.equals("Fluid") && finish.equals("Shiny") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Nylon Tricot", "Php 80/yd", "Nylon Tricot.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Lame Chiffon", "Php 180/yd", "Lame Chiffon.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Iridescent Chiffon", "Php 250/yd", "Iridescent Chiffon.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Tissue Silk", "Php 900/yd", "Tissue Silk.jpg"));
            } 
            else if (drape.equals("Fluid") && finish.equals("Textured") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Crinkle Georgette", "Php 150/yd", "Crinkle Georgette.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Chiffon Jacquard", "Php 250/yd", "Chiffon Jacquard.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Silk Plissé", "Php 1,500/yd", "Silk Plissé.jpg"));
            }  
            else if (drape.equals("Stiff") && finish.equals("Matte") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Tarlatan", "Php 80/yd", "Tarlatan.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Crinoline Netting", "Php 100/yd", "Crinoline Netting.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Cotton Organdy", "Php 200/yd", "Cotton Organdy.jpg"));
            } 
            else if (drape.equals("Stiff") && finish.equals("Shiny") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Crystal Organza", "Php 80/yd", "Crystal Organza.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Metallic Organza", "Php 150/yd", "Metallic Organza.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Silk Organza", "Php 1,200/yd", "Silk Organza.jpg"));
            }
            else if (drape.equals("Stiff") && finish.equals("Textured") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Crinoline", "Php 80/yd", "Crinoline.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Flocked Organza", "Php 200/yd", "Flocked Organza.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Embroidered Netting", "Php 500/yd", "Embroidered Netting.jpg"));
            }
            else if (drape.equals("Moderate") && finish.equals("Matte") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Cheesecloth", "Php 60/yd", "Cheesecloth.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Cotton Batiste", "Php 120/yd", "Cotton Batiste.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Linen Gauze", "Php 350/yd", "Linen Gauze.jpg"));
            }
            else if (drape.equals("Moderate") && finish.equals("Shiny") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Crystal Tulle", "Php 70/yd", "Crystal Tulle.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Soft Metallic Net", "Php 150/yd", "Soft Metallic Net.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Lurex Chiffon", "Php 200/yd", "Lurex Chiffon.jpg"));
            }
            else if (drape.equals("Moderate") && finish.equals("Textured") && weight.equals("Sheer")) {
                recsContainer.add(new FabricCard("Cotton Gauze", "Php 120/yd", "Cotton Gauze.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Crinkle Chiffon", "Php 150/yd", "Crinkle Chiffon.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Dotted Swiss", "Php 200/yd", "Dotted Swiss.jpg"));
            }
            else if (drape.equals("Fluid") && finish.equals("Matte") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Rayon Challis", "Php 150/yd", "Rayon Challis.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Cupro", "Php 400/yd", "Cupro.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Silk Crepe de Chine", "Php 1,800/yd", "Silk Crepe de Chine.jpg"));
            }
            else if (drape.equals("Fluid") && finish.equals("Shiny") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Polyester Satin", "Php 90/yd", "Polyester Satin.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Viscose Satin", "Php 350/yd", "Viscose Satin.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Silk Charmeuse", "Php 2,200/yd", "Silk Charmeuse.jpg"));
            }
            else if (drape.equals("Fluid") && finish.equals("Textured") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Pebble Crepe", "Php 160/yd", "Pebble Crepe.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Amunzen", "Php 200/yd", "Amunzen.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Sandwashed Silk", "Php 2,000/yd", "Sandwashed Silk.jpg"));
            }   
            else if (drape.equals("Moderate") && finish.equals("Matte") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Broadcloth", "Php 90/yd", "Cotton Broadcloth.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Chambray", "Php 160/yd", "Chambray.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Oxford Cloth", "Php 200/yd", "Oxford Cloth.jpg"));
            }
            else if (drape.equals("Moderate") && finish.equals("Shiny") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Acetate Lining", "Php 80/yd", "Acetate Lining.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Cotton Sateen", "Php 200/yd", "Cotton Sateen.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Polished Cotton", "Php 250/yd", "Polished Cotton.jpg"));
            }
            else if (drape.equals("Moderate") && finish.equals("Textured") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Seersucker", "Php 160/yd", "Seersucker.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Waffle Knit", "Php 200/yd", "Waffle Knit.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Linen", "Php 450/yd", "Linen.jpg"));
            }
            else if (drape.equals("Stiff") && finish.equals("Matte") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Quilting Cotton", "Php 140/yd", "Quilting Cotton.jpeg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Cotton Sateen", "Php 200/yd", "Cotton Sateen.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Gabardine", "Php 250/yd", "Gabardine.jpg"));
            }
            else if (drape.equals("Stiff") && finish.equals("Shiny") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Taffeta", "Php 120/yd", "Taffeta.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Faille", "Php 250/yd", "Faille.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Silk Shantung", "Php 1,500/yd", "Silk Shantung.jpg"));
            }
            else if (drape.equals("Stiff") && finish.equals("Textured") && weight.equals("Opaque")) {
                recsContainer.add(new FabricCard("Woven Dobby", "Php 200/yd", "Woven Dobby.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Cotton Piqué", "Php 250/yd", "Cotton Piqué.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Barkcloth", "Php 350/yd", "Barkcloth.jpg"));
            }
            else if (drape.equals("Fluid") && finish.equals("Matte") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Ponte de Roma", "Php 250/yd", "Ponte de Roma.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Lyocell", "Php 400/yd", "Lyocell.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Heavy Wool Crepe", "Php 1,200/yd", "Heavy Wool Crepe.jpg"));
            }
            else if (drape.equals("Fluid") && finish.equals("Shiny") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Panne Velvet", "Php 200/yd", "Panne Velvet.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Liquid Velour", "Php 250/yd", "Liquid Velour.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Silk Velvet", "Php 2,500/yd", "Silk Velvet.jpg"));
            }
            else if (drape.equals("Fluid") && finish.equals("Textured") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Stretch Cloqué", "Php 280/yd", "Stretch Cloqué.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Silk Matelassé", "Php 1,800/yd", "Silk Matelassé.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("40mm Silk Crepe", "Php 3,000/yd", "40mm Silk Crepe.jpg"));
            }
            else if (drape.equals("Moderate") && finish.equals("Textured") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Corduroy", "Php 250/yd", "Corduroy.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Bouclé", "Php 450/yd", "Bouclé.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Wool Tweed", "Php 900/yd", "Wool Tweed.jpg"));
            }
            else if (drape.equals("Moderate") && finish.equals("Matte") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Cotton Flannel", "Php 160/yd", "Cotton Flannel.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Washed Denim", "Php 280/yd", "Washed Denim.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Linen Suiting", "Php 600/yd", "Linen Suiting.jpg"));
            }
            else if (drape.equals("Moderate") && finish.equals("Shiny") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Heavy Velour", "Php 250/yd", "Heavy Velour.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Bridal Satin", "Php 300/yd", "Bridal Satin.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Cotton Velvet", "Php 600/yd", "Cotton Velvet.jpg"));
            }
            else if (drape.equals("Stiff") && finish.equals("Matte") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Cotton Canvas", "Php 160/yd", "Cotton Canvas.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Bull Denim", "Php 250/yd", "Bull Denim.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Waxed Canvas", "Php 600/yd", "Waxed Canvas.jpg"));
            }
            else if (drape.equals("Stiff") && finish.equals("Textured") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Heavy Tapestry", "Php 450/yd", "Heavy Tapestry.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Heavy Brocade", "Php 550/yd", "Heavy Brocade.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Bouclé Coating", "Php 800/yd", "Bouclé Coating.jpg"));
            }
            else if (drape.equals("Stiff") && finish.equals("Shiny") && weight.equals("Heavy")) {
                recsContainer.add(new FabricCard("Duchess Satin", "Php 300/yd", "Duchess Satin.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Brocade", "Php 450/yd", "Brocade.jpg"));
                recsContainer.add(Box.createRigidArea(new Dimension(15, 0)));
                recsContainer.add(new FabricCard("Mikado Silk", "Php 2,200/yd", "Mikado Silk.jpg"));
            }

            recsContainer.revalidate();
            recsContainer.repaint();

        });

        // BACK Button
        JButton backBtn = new RoundedButton("BACK", Color.WHITE, 20, 0, Color.WHITE);
        backBtn.setForeground(new Color(139, 80, 50));
        backBtn.setFont(new Font("Verdana", Font.BOLD, 24));
        backBtn.setBounds(40, 560, 180, 60);
        mainPanel.add(backBtn);
        backBtn.addActionListener(ev -> this.dispose());
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
                    if (imgUrl != null) logoImg = javax.imageio.ImageIO.read(imgUrl);
                } catch (Exception e) {}
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (logoImg != null) {
                    g2.drawImage(logoImg, 0, 0, LOGO_SIZE, LOGO_SIZE, this);
                }
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

    private JLabel createInputLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.BOLD, 22)); // BOLD LABEL
        label.setForeground(Color.BLACK);
        label.setBounds(x, y, 100, 30);
        return label;
    }

    private JComboBox<String> createStyledDropdown(String[] options) {
        JComboBox<String> box = new JComboBox<>(options);
        box.setFont(new Font("Verdana", Font.PLAIN, 18));
        box.setBackground(new Color(121, 95, 83));
        box.setForeground(Color.WHITE);
        box.setFocusable(false);
        box.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        box.setUI(new CustomComboBoxUI());
        box.setRenderer(new CustomComboBoxRenderer());
        return box;
    }

    // --- CUSTOM UI CLASSES ---

    class CustomComboBoxUI extends javax.swing.plaf.basic.BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton arrowButton = new JButton() {
                @Override
                public void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(121, 95, 83));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int x = getWidth() / 2 - 6;
                    int y = getHeight() / 2 - 2;
                    g2.drawLine(x, y, x + 5, y + 5);
                    g2.drawLine(x + 5, y + 5, x + 10, y);
                }
            };
            arrowButton.setBorder(BorderFactory.createEmptyBorder());
            arrowButton.setContentAreaFilled(false);
            return arrowButton;
        }
        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(new Color(121, 95, 83));
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    class CustomComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (isSelected) setBackground(new Color(145, 115, 100));
            else setBackground(new Color(121, 95, 83));
            setForeground(Color.WHITE);
            setFont(new Font("Verdana", Font.PLAIN, 18));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            return this;
        }
    }

    class CustomScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(121, 95, 83, 60)); 
            g2.fillRoundRect(trackBounds.x, trackBounds.y + 3, trackBounds.width, trackBounds.height - 6, 5, 5);
        }
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isDragging) g2.setColor(new Color(130, 230, 100)); 
            else if (isThumbRollover()) g2.setColor(new Color(145, 115, 103)); 
            else g2.setColor(new Color(121, 95, 83)); 
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y + 1, thumbBounds.width, thumbBounds.height - 2, 8, 8);
        }
        @Override protected JButton createDecreaseButton(int orientation) { return createInvisibleButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return createInvisibleButton(); }
        private JButton createInvisibleButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setOpaque(false); button.setContentAreaFilled(false); button.setBorderPainted(false);
            return button;
        }
    }

    class FabricCard extends JPanel {
        private Image fabricImg;
        private String name, price;

    public FabricCard(String name, String price, String imgFilename) {
            this.name = name; this.price = price;
            
            // --- THE FIX: Strictly lock the dimensions of the card ---
            Dimension lockedSize = new Dimension(130, 200);
            setPreferredSize(lockedSize);
            setMinimumSize(lockedSize);
            setMaximumSize(lockedSize);
            
            setOpaque(false);
            try {
                // LOOKS IN THE "Alternatives" FOLDER
                java.net.URL imgUrl = getClass().getResource("/IMAGES/Recommendations/" + imgFilename);
                if (imgUrl != null) fabricImg = ImageIO.read(imgUrl);
            } catch (Exception e) {}

            setCursor(new Cursor(Cursor.HAND_CURSOR)); // Turns mouse into a clicking hand
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    // When clicked, fetch the description from the dictionary and update the box
                    FabricRecommenderApp.descriptionBox.setText(getFabricDescription(name));
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(245, 240, 235)); 
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            if (fabricImg != null) {
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), 130, 15, 15));
                g2.drawImage(fabricImg, 0, 0, getWidth(), 130, this);
                g2.setClip(null);
            } else {
                g2.setColor(new Color(180, 170, 160));
                g2.fillRoundRect(0, 0, getWidth(), 130, 15, 15);
                g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString("No Image", 35, 70);
            }
            g2.setColor(new Color(220, 215, 210)); g2.drawLine(10, 130, getWidth() - 10, 130);
            g2.setColor(new Color(80, 60, 50)); g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString(name, (getWidth() - g2.getFontMetrics().stringWidth(name)) / 2, 155);
            g2.setColor(new Color(130, 200, 100)); g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.drawString(price, (getWidth() - g2.getFontMetrics().stringWidth(price)) / 2, 180);
        }
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
            g2.setFont(font); FontMetrics fm = g2.getFontMetrics();
            g2.setColor(shadowCol); g2.drawString(text, 2, fm.getAscent() + 2);
            g2.setColor(textCol); g2.drawString(text, 0, fm.getAscent());
        }
    }

    class RoundedPanel extends JPanel {
        private int r; Color c;
        public RoundedPanel(int r, Color c) { this.r = r; this.c = c; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
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

// --- FABRIC DESCRIPTION DICTIONARY ---
    private static String getFabricDescription(String fabricName) {
        switch (fabricName) {
            case "Cotton Voile": return "A crisp yet soft, semi-sheer cotton fabric that breathes beautifully, making it ideal for lightweight summer garments and delicate linings.";
            case "Cotton Lawn": return "Smoother and slightly more opaque than voile with a fine, fluid drape, this fabric is highly favored for crisp button-up shirts and breezy dresses.";
            case "Rayon Georgette": return "Mimicking silk perfectly for budget-friendly flowing dresses, this synthetic, sheer fabric features a subtly crinkled texture and excellent flow.";
            case "Silk Chiffon": return "As the premier choice for elegant, floating eveningwear, this ultimate luxury sheer features an impossibly light, airy drape and a soft matte finish.";
            case "Nylon Tricot": return "Traditionally used in vintage lingerie and delicate undergarments, this lightweight, sheer knit resists runs and offers a subtle, glossy sheen.";
            case "Lame Chiffon": return "Adding instant, glamorous sparkle to overlays and evening wraps, this fluid, sheer chiffon is interwoven with metallic threads to catch the light.";
            case "Iridescent Chiffon": return "Woven with dual-colored threads to color-shift in the light, this sheer fabric provides a magical, glowing effect for flowing skirts and capes.";
            case "Tissue Silk": return "Exceptionally thin and lightweight with a delicate, glassy sheen, this fabric crushes beautifully for high-end wraps or delicate artistic garments.";
            case "Crinkle Georgette": return "Adding beautiful dimension to flowing dresses while remaining wrinkle-resistant, this sheer, fluid fabric is heat-set with a permanent, pebbly texture.";
            case "Chiffon Jacquard": return "Offering a subtle, sophisticated tone-on-tone effect for elegant blouses, this lightweight sheer base features textured patterns woven directly into the cloth.";
            case "Silk Plissé": return "Creating organic, high-fashion volume without added weight, this sheer, fluid silk is chemically treated to have a distinctive, permanent puckered texture.";
            case "Tarlatan": return "Heavily used in theatrical costuming and stiff petticoats, this heavily starched, open-weave muslin stands rigidly away from the body.";
            case "Crinoline Netting": return "Acting as the hidden structural hero beneath massive bridal ballgowns, this scratchy, highly rigid netting is designed purely for architectural volume.";
            case "Cotton Organdy": return "Perfect for sculpted collars, cuffs, and historic reproductions, this crispest of all cotton fabrics offers a sheer matte finish and excellent structural hold.";
            case "Crystal Organza": return "A budget-friendly staple for party dresses and decorations, this synthetic, sheer fabric has an aggressive, glassy shine and a stiff drape.";
            case "Metallic Organza": return "Looking like shimmering foil while holding bold, sculptural shapes effortlessly, this stiff, sheer organza is woven extensively with metallic threads.";
            case "Silk Organza": return "Heavily favored for high-end bridal veils and luxurious facings, this premium, crisp sheer boasts a beautiful, natural luster and unmatched structural memory.";
            case "Crinoline": return "Most commonly used for structured petticoats and stiffening hems, this stiff, sheer mesh fabric is woven with heavy threads to maintain aggressive volume.";
            case "Flocked Organza": return "Providing excellent texture and structure for statement dresses, this stiff, transparent organza is decorated with raised, velvet-like patterns pressed onto the surface.";
            case "Embroidered Netting": return "Creating beautiful illusion necklines and structured, romantic overlays, this rigid, sheer mesh is heavily textured with dense, raised thread embroidery.";
            case "Cheesecloth": return "Originally for culinary use but great for rustic, relaxed garments, this loose, airy, matte cotton holds a moderate shape and breathes exceptionally well.";
            case "Cotton Batiste": return "A classic choice for heirloom sewing and lightweight sleepwear, this soft, semi-sheer cotton holds gathers beautifully while maintaining a delicate matte finish.";
            case "Linen Gauze": return "An excellent choice for breathable, beachy summer wear, this open-weave sheer retains the slubby texture and natural, slightly stiff body of linen.";
            case "Crystal Tulle": return "Serving as the definitive fabric for classic ballet tutus and sparkling veils, this fine, hexagonal netting is coated to have a glassy shine while holding a moderate shape.";
            case "Soft Metallic Net": return "Holding its shape moderately well to make it ideal for theatrical overlays, this flexible, sheer mesh is woven with metallic foil threads for a glittering finish.";
            case "Lurex Chiffon": return "Bridging the gap between flowing elegance and structured sparkle, this fluid chiffon is stabilized slightly by the inclusion of heavy, shiny metallic Lurex threads.";
            case "Cotton Gauze": return "Highly popular for relaxed, bohemian summer styles, this loose, breathable weave features a slightly crinkled, rustic texture and drapes moderately.";
            case "Crinkle Chiffon": return "Adding wonderful visual dimension to flowing skirts and scarves, this sheer chiffon features an accordion-like crinkle that gives it moderate stretch and texture.";
            case "Dotted Swiss": return "Bringing a sweet, vintage texture to blouses and children's wear, this sheer, matte cotton base is heavily textured with raised, woven tufts or dots.";
            case "Rayon Challis": return "A staple for comfortable, flowing summer dresses, this incredibly soft, opaque fabric flows like water and feels cool against the skin.";
            case "Cupro": return "Serving as an excellent vegan alternative for high-end slip dresses, this breathable, regenerated cellulose fabric perfectly mimics the luxurious drape of washed silk.";
            case "Silk Crepe de Chine": return "The gold standard for luxurious, flowing evening blouses, this premium opaque silk features a micro-texture and an exquisitely fluid drape.";
            case "Polyester Satin": return "A ubiquitous choice for prom dresses and affordable bridal wear, this highly reflective, fluid synthetic offers the classic liquid gold look on a budget.";
            case "Viscose Satin": return "An excellent mid-tier choice for glamorous slip dresses, this semi-synthetic satin offers a heavier, more luxurious liquid drape than standard polyester.";
            case "Silk Charmeuse": return "The pinnacle of luxury for bias-cut gowns and premium lingerie, this ultimate glossy silk features a brilliant, mirror-like shine and a heavy, fluid drape.";
            case "Pebble Crepe": return "Perfect for elegant, travel-friendly trousers, this heavy, opaque crepe flows beautifully and features a distinct, grainy texture that resists wrinkling.";
            case "Amunzen": return "Providing a fluid, flattering drape ideal for modest silhouettes, this textured crepe weave feels slightly sandy to the touch but does not cling to the body.";
            case "Sandwashed Silk": return "Offering a relaxed, incredibly soft drape for luxury casualwear, this opaque silk is chemically treated to have a fuzzy, sueded surface and a matte finish.";
            case "Broadcloth": return "The default utility fabric for button-up shirts and quilting, this standard, tightly woven cotton features a flat, matte surface and moderate structure.";
            case "Chambray": return "Perfect for casual, breathable button-down shirts, this soft, plain-weave cotton looks exactly like lightweight denim but drapes much more easily.";
            case "Oxford Cloth": return "The classic choice for preppy dress shirts, this durable basket-weave cotton holds a crisp shape while being slightly heavier and more textured than standard poplin.";
            case "Acetate Lining": return "The traditional standard for lining tailored jackets and winter coats, this opaque, highly shiny synthetic holds a moderate shape and resists static.";
            case "Cotton Sateen": return "Perfect for tailored dresses that require a touch of elegance, this cotton fabric is woven like satin to provide a smooth, slightly lustrous surface with good structure.";
            case "Polished Cotton": return "Great for structured 1950s-style skirts, this cotton is treated with a resin finish to create a crisp, chintz-like shine that holds its shape extremely well.";
            case "Seersucker": return "A classic choice for warm-weather suiting, this breathable cotton holds a moderate shape and features a distinctive woven-in puckered texture, usually striped.";
            case "Waffle Knit": return "Commonly used for thermals and robes, this moderately structured fabric features a recessed, grid-like pattern that traps warmth.";
            case "Linen": return "The ultimate choice for summer tailoring, this highly breathable, natural fabric wrinkles elegantly and is characterized by its slubby texture and crisp, moderate drape.";
            case "Quilting Cotton": return "Widely used for structured craft apparel, this medium-weight, matte cotton holds a crisp fold and stands slightly away from the body.";
            case "Gabardine": return "A highly durable choice for tailored suits and classic trench coats, this tough, tightly woven twill features a steep diagonal rib and a rigid, matte drape.";
            case "Taffeta": return "A dramatic, shiny choice for voluminous ballgowns and structured bows, this very crisp, smooth fabric stands on its own and makes a distinct rustling sound.";
            case "Faille": return "Often used for high-end evening wear, this slightly glossy, structured fabric features prominent, flat crosswise ribs and holds tailored shapes beautifully.";
            case "Silk Shantung": return "Providing a rich, organic texture to structured formalwear, this stiff, shiny silk is characterized by rough, irregular slub yarns woven throughout.";
            case "Woven Dobby": return "Adding a subtle, structured texture to tailored shirts and dresses, this stiff, opaque fabric features small, repeating geometric patterns woven directly into the cloth.";
            case "Cotton Piqué": return "Famous for classic polo shirts and formal collars, this stiff, medium-to-heavy cotton holds crisp lines beautifully with its woven waffle or ribbed texture.";
            case "Barkcloth": return "Providing excellent structure and vintage flair for mid-century style garments, this dense, stiff cotton fabric features a rough, pronounced texture that resembles tree bark.";
            case "Ponte de Roma": return "Incredibly flattering and perfect for structured winter dresses, this thick, heavy double-knit drapes smoothly and provides excellent warmth without clinging.";
            case "Lyocell": return "An excellent, sustainable choice for fluid trousers and trench coats, this heavy, durable eco-friendly fabric flows incredibly well, much like heavy water.";
            case "Heavy Wool Crepe": return "A premium choice for fluid, cold-weather suiting and skirts, this thick, warm fabric features a matte, spongy texture that falls into graceful, heavy folds.";
            case "Panne Velvet": return "Heavy, stretchy, and perfect for fluid evening gowns, this velvet features a plush pile that has been flattened in one direction to create a high-gloss look.";
            case "Liquid Velour": return "An ultra-comfortable choice for glamorous loungewear, this heavy, stretchy knit fabric pools and drapes effortlessly with its plush, shiny surface.";
            case "Silk Velvet": return "The definitive fabric for rich, vintage-inspired winter gowns, this ultra-luxurious, heavy fabric features a plush pile, brilliant shine, and a liquid drape.";
            case "Stretch Cloqué": return "Hugging the body securely while providing dramatic texture, this blistered, puckered heavy fabric is blended with spandex to allow for a fluid, heavy stretch.";
            case "Silk Matelassé": return "A sophisticated choice for textured evening jackets, this heavy, luxurious silk is woven to look thick and quilted, yet it drapes rather than standing stiff.";
            case "40mm Silk Crepe": return "The absolute highest tier of luxury for fluid bias-cut gowns, this exceptionally heavy silk features a prominent grainy texture that pools heavily like liquid metal.";
            case "Corduroy": return "A durable, textured staple for cold-weather trousers and jackets, this heavy woven cotton features distinct, raised vertical ridges known as wales.";
            case "Bouclé": return "Famous for classic Chanel-style jackets, this heavy outerwear fabric holds a moderate shape and is made with looped yarns to create a bulky, nubby texture.";
            case "Wool Tweed": return "A highly durable, traditional choice for winter coats and suiting, this rough, heavy woolen fabric is often woven with mixed, multi-colored yarns for a rustic texture.";
            case "Cotton Flannel": return "A classic choice for heavy winter shirts, this thick, brushed cotton wraps warmly around the body and offers a soft, matte, and fuzzy surface.";
            case "Washed Denim": return "Offering a moderate, wearable drape for everyday jeans and jackets, this thick, durable cotton twill has been aggressively washed to soften its rigid stiffness.";
            case "Linen Suiting": return "Providing a moderate, tailored drape for high-quality summer blazers, this thick, heavy version of linen maintains its matte finish and slubby texture.";
            case "Heavy Velour": return "Highly durable and often used for premium tracksuits or heavy drapery, this thick, densely piled knit features a shiny surface that holds its shape moderately well.";
            case "Bridal Satin": return "Holding architectural shapes flawlessly for structured wedding gowns, this very heavy, stiff satin (Peau de Soie) features a glowing, muted shine.";
            case "Cotton Velvet": return "Excellent for structured winter blazers and historical costumes, this fabric is heavier and stiffer than silk velvet, holding a tailored shape with a plush pile.";
            case "Cotton Canvas": return "Generally used for heavy utility wear, upholstery, or bags, this extremely durable, stiff, and protective matte fabric stands up entirely on its own.";
            case "Bull Denim": return "Used for heavy-duty jeans and rugged workwear, this heavy 3x1 twill weave is notoriously stiff and takes significant effort to break in.";
            case "Waxed Canvas": return "The ultimate choice for weather-resistant outerwear, this canvas is treated heavily with wax to make it waterproof, incredibly stiff, and prone to cracking lines.";
            case "Heavy Tapestry": return "Used for highly structured, dramatic coats, this exceptionally stiff, thick fabric stands entirely independently and features complex, multi-colored woven scenes.";
            case "Heavy Brocade": return "A luxurious, historical choice for structured bodices and corsetry, this stiff, unyielding fabric features raised, highly textured woven patterns.";
            case "Bouclé Coating": return "Providing excellent structure for oversized coats, this extremely heavy, stiff version of bouclé blocks winter winds with its deeply textured surface.";
            case "Duchess Satin": return "The premier choice for dramatic, structured haute couture, this gorgeous, ultra-heavy satin holds sculptural, architectural shapes with a brilliant shine.";
            case "Brocade": return "Providing immense support and elegance for structured evening wear, this stiff, shiny fabric features complex patterns woven directly into the rigid structure.";
            case "Mikado Silk": return "Highly popular in modern, architectural wedding gowns requiring precise structure, this luxurious, ultra-stiff blended silk features a subtle, glowing shine.";
            default: return "This versatile fabric option perfectly balances your selected drape, finish, and weight parameters for a reliable sewing foundation.";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FabricRecommenderApp().setVisible(true));
    }
}