import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.imageio.ImageIO;

public class CreditsPage extends JFrame {

    private Image bgImage;
    private Image profilePic;
    private TextileMetricHome parentApp;

    public CreditsPage(TextileMetricHome parentApp) {
        this.parentApp = parentApp;
        
        setTitle("TextileMetric - Developer Credits");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load Background and Profile Picture
        try {
            URL bgUrl = getClass().getResource("/IMAGES/featureBackground.jpg");
            if (bgUrl != null) bgImage = ImageIO.read(bgUrl);
            
            URL picUrl = getClass().getResource("/IMAGES/professional_picture.png");
            if (picUrl != null) profilePic = ImageIO.read(picUrl);
        } catch (Exception e) {
            System.out.println("Images not found. Ensure professional_picture.png is in the /IMAGES/ folder.");
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
        // SMALL TOP-LEFT BACK BUTTON
        // ==========================================
        JButton smallBackBtn = new RoundedButton("BACK", new Color(180, 60, 60), 15, 0, Color.WHITE);
        smallBackBtn.setForeground(Color.WHITE);
        smallBackBtn.setFont(new Font("Verdana", Font.BOLD, 12));
        smallBackBtn.setBounds(20, 20, 100, 35);
        mainPanel.add(smallBackBtn);
        
        smallBackBtn.addActionListener(e -> {
            if (this.parentApp != null) {
                this.parentApp.showView("HOME");
                this.parentApp.setVisible(true);
            }
            this.dispose();
        });

        // ==========================================
        // CV / RESUME CARD (WIDENED LAYOUT)
        // ==========================================
        JPanel cvCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw Left Sidebar (Dark Slate)
                g2.setColor(new Color(45, 55, 65));
                g2.fillRoundRect(0, 0, 350, getHeight(), 25, 25);
                g2.fillRect(330, 0, 20, getHeight()); 
                
                // Draw Right Content Area Background (Clean White/Cream)
                g2.setColor(new Color(245, 248, 250));
                g2.fillRoundRect(350, 0, getWidth() - 350, getHeight(), 25, 25);
                g2.fillRect(350, 0, 20, getHeight()); 
            }
        };
        cvCard.setBounds(50, 75, 1100, 550);
        cvCard.setOpaque(false);
        mainPanel.add(cvCard);

        // ==========================================
        // LEFT SIDEBAR CONTENT (Profile & Titles)
        // ==========================================
        JPanel picPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 180, 180);
                
                if (profilePic != null) {
                    g2.setClip(new Ellipse2D.Float(5, 5, 170, 170));
                    g2.drawImage(profilePic, 5, 5, 170, 170, this);
                    g2.setClip(null);
                } else {
                    g2.setColor(new Color(150, 160, 170));
                    g2.fillOval(5, 5, 170, 170);
                }
            }
        };
        picPanel.setBounds(85, 30, 180, 180);
        picPanel.setOpaque(false);
        cvCard.add(picPanel);

        JLabel nameLabel = new JLabel("Lance T. Mendoza", SwingConstants.CENTER); 
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 22));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(0, 230, 350, 30);
        cvCard.add(nameLabel);

        JLabel roleLabel = new JLabel("3rd Year BSCpE Student", SwingConstants.CENTER);
        roleLabel.setFont(new Font("Verdana", Font.ITALIC, 14));
        roleLabel.setForeground(new Color(130, 230, 100)); 
        roleLabel.setBounds(0, 260, 350, 20);
        cvCard.add(roleLabel);

        // Contact & Real-world Information
        cvCard.add(createSidebarDetail("Email:", "04akymendoza@gmail.com", 320));
        cvCard.add(createSidebarDetail("Phone:", "+63 946 698 2853", 365));
        cvCard.add(createSidebarDetail("Location:", "Lucban, Quezon", 410));
        cvCard.add(createSidebarDetail("GitHub:", "MrLanceMendoza", 455));
        cvCard.add(createSidebarDetail("LinkedIn:", "in/lance-mendoza", 500));

        // ==========================================
        // RIGHT CONTENT AREA (SCROLLABLE)
        // ==========================================
        JPanel scrollContent = new JPanel(null);
        scrollContent.setPreferredSize(new Dimension(720, 1300)); 
        scrollContent.setOpaque(false);

        int padX = 35; // Pushed slightly left to give right side more room
        int textWidth = 660; // Widened

        // 1. Professional Summary (Bio)
        JLabel bioTitle = new JLabel("PROFESSIONAL SUMMARY");
        bioTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        bioTitle.setForeground(new Color(55, 70, 85));
        bioTitle.setBounds(padX, 30, 400, 20);
        scrollContent.add(bioTitle);

        String bioStr = "<html><div style='text-align: justify;'>A dedicated 3rd-year Computer Engineering (BSCpE) student at Southern Luzon State University with a strong foundation in hardware-software integration. Adept at bridging physical and digital systems, with focused interests in circuit design, data communication, and system administration. Committed to developing robust engineering solutions and continuously expanding technical expertise.</div></html>";
        JLabel bioText = new JLabel(bioStr);
        bioText.setFont(new Font("Verdana", Font.PLAIN, 13));
        bioText.setForeground(new Color(80, 90, 100));
        bioText.setVerticalAlignment(SwingConstants.TOP);
        bioText.setBounds(padX, 60, textWidth, 90); 
        scrollContent.add(bioText);

        // 2. Education 
        JLabel eduTitle = new JLabel("EDUCATION");
        eduTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        eduTitle.setForeground(new Color(55, 70, 85));
        eduTitle.setBounds(padX, 170, 400, 20);
        scrollContent.add(eduTitle);

        String eduStr = "<html><body style='font-family: Verdana; font-size: 13px; color: #505A64; margin: 0;'>" +
            "<p style='margin-top: 5px; margin-bottom: 12px;'><b style='color: #374655; font-size: 14px;'>BS Computer Engineering (2023 - Present)</b><br>Southern Luzon State University (SLSU) · Main Campus, Lucban, Quezon<br><i>GWA: 1.76 | Relevant: Data Structures, Microprocessors, Networks, Operating Systems</i></p>" +
            "<p style='margin-top: 0;'><b style='color: #374655; font-size: 14px;'>Senior High School · STEM Strand (2021 - 2023)</b><br>Atimonan National Comprehensive High School<br><i>Graduated with High Honors</i></p>" +
            "</body></html>";
        JLabel eduText = new JLabel(eduStr);
        eduText.setVerticalAlignment(SwingConstants.TOP);
        eduText.setBounds(padX, 200, textWidth, 160);
        scrollContent.add(eduText);

        // 3. Interactive Projects Section 
        JLabel projTitle = new JLabel("MAJOR PROJECTS (Click to view details)");
        projTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        projTitle.setForeground(new Color(55, 70, 85));
        projTitle.setBounds(padX, 380, 400, 20); 
        scrollContent.add(projTitle);

        int projY = 415;
        int projGap = 45; 

        JButton btnProj1 = createProjectButton("Government Management System (Java)");
        btnProj1.setBounds(padX, projY, textWidth, 35);
        btnProj1.addActionListener(e -> showProjectDialog("Government Management System", "A Java-based application designed to streamline fund management in the government sector by facilitating the secure collection and categorization of public funds. It features real-time tracking of allocations, automated notifications, and detailed reporting to enhance transparency and ensure financial accountability.<br><br><b>Technologies:</b> Java"));
        scrollContent.add(btnProj1);

        JButton btnProj2 = createProjectButton("Mapping Provincial Poverty Concentration (Python)");
        btnProj2.setBounds(padX, projY + projGap, textWidth, 35);
        btnProj2.addActionListener(e -> showProjectDialog("Mapping Provincial Poverty Concentration", "Developed a Python-based interactive mapping tool using Google Colab that visualizes poverty concentration across Philippine provinces by combining socioeconomic and geographic data. The system generates a social aid priority score for each region, empowering policymakers and NGOs to effectively allocate resources to the most vulnerable communities.<br><br><b>Technologies:</b> Python, GeoMap"));
        scrollContent.add(btnProj2);

        JButton btnProj3 = createProjectButton("FurEver Friends – Animal Adoption Platform (Web)");
        btnProj3.setBounds(padX, projY + (projGap * 2), textWidth, 35);
        btnProj3.addActionListener(e -> showProjectDialog("FurEver Friends", "A collaboratively developed full-stack web page designed to facilitate animal adoptions through a responsive and intuitive user interface. The platform integrates PHP and database management to efficiently process adoption requests, track center operations, and showcase available pets.<br><br><b>Technologies:</b> HTML, CSS, PHP, JavaScript"));
        scrollContent.add(btnProj3);

        JButton btnProj4 = createProjectButton("dirEngine (Web)");
        btnProj4.setBounds(padX, projY + (projGap * 3), textWidth, 35);
        btnProj4.addActionListener(e -> showProjectDialog("dirEngine", "An interactive anime recommendation webpage developed specifically for anime watchers to easily discover and curate new shows based on targeted preferences.<br><br><b>Technologies:</b> MySQL, HTML, CSS, JavaScript"));
        scrollContent.add(btnProj4);

        // 4. Certifications
        JLabel certTitle = new JLabel("CERTIFICATIONS");
        certTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        certTitle.setForeground(new Color(55, 70, 85));
        certTitle.setBounds(padX, 615, 400, 20);
        scrollContent.add(certTitle);

        String certsHtml = "<html><body style='font-family: Verdana; font-size: 13px; color: #505A64; margin: 0;'>" +
            "<p style='margin-top: 5px; margin-bottom: 5px;'><b style='color: #374655;'>• ICpEP | TechTambayan:</b> Where Data and People Click</p>" +
            "<p style='margin-top: 0;'><b style='color: #374655;'>• 1BESO MANAGEMENT CONSULTANCY SERVICES:</b> Certified Safety Officer 2</p>" +
            "</body></html>";
        JLabel certText = new JLabel(certsHtml);
        certText.setVerticalAlignment(SwingConstants.TOP);
        certText.setBounds(padX, 645, textWidth, 80);
        scrollContent.add(certText);

        // 5. Technical Skills
        JLabel skillsTitle = new JLabel("TECHNICAL SKILLS");
        skillsTitle.setFont(new Font("Verdana", Font.BOLD, 16));
        skillsTitle.setForeground(new Color(55, 70, 85));
        skillsTitle.setBounds(padX, 745, 400, 20);
        scrollContent.add(skillsTitle);

        String skillsStr = "<html><div style='text-align: justify;'>A balanced proficiency in both hardware and software domains. Experienced in designing and assembling physical circuits, configuring virtualized OS environments, and developing logic-driven software applications. Capable of analyzing data communications and bridging hardware components with backend systems.</div></html>";
        JLabel skillsDesc = new JLabel(skillsStr);
        skillsDesc.setFont(new Font("Verdana", Font.PLAIN, 13));
        skillsDesc.setForeground(new Color(80, 90, 100));
        skillsDesc.setVerticalAlignment(SwingConstants.TOP);
        skillsDesc.setBounds(padX, 775, textWidth, 70); 
        scrollContent.add(skillsDesc);

        // TIGHTENED AND WIDENED SKILL BARS
        int barW = 325;
        int gap = 20;   
        int row1Y = 860; 
        int row2Y = 1030; 

        scrollContent.add(new SkillBar(
            "Programming (Python, Java, C/C++)", 92, 
            "Developing logic-driven software applications and backend algorithms. Proficient in object-oriented principles and capable of writing clean, maintainable code. Experienced in utilizing these languages to solve complex engineering problems and build functional applications.", 
            padX, row1Y, barW));
            
        scrollContent.add(new SkillBar(
            "Hardware (Circuit Design, Soldering)", 88, 
            "Designing, simulating, and assembling physical circuits and components. Skilled in using tools like Multisim for testing circuit behaviors before physical implementation. Adept at precise soldering techniques to build reliable, functioning hardware prototypes.", 
            padX + barW + gap, row1Y, barW));
            
        scrollContent.add(new SkillBar(
            "Networking (Data Comm)", 85, 
            "Analyzing protocols and bridging hardware components with data networks. Capable of configuring simulated network topologies and understanding low-level data transmission. Focused on ensuring secure and efficient communication between embedded devices and servers.", 
            padX, row2Y, barW));
            
        scrollContent.add(new SkillBar(
            "Systems (Linux, VirtualBox)", 85, 
            "Configuring and administering virtualized OS environments and Linux systems. Comfortable using the command line for system management, scripting, and advanced troubleshooting. Familiar with deploying virtual machines to test software across multiple platforms safely.", 
            padX + barW + gap, row2Y, barW));

        // --- Wrap scrollContent in a JScrollPane ---
        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBounds(350, 0, 750, 550); 
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        
        // Custom elegant scrollbar styling
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(180, 190, 200); 
                this.trackColor = new Color(245, 248, 250);    
            }
            @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton btn = new JButton(); btn.setPreferredSize(new Dimension(0, 0)); return btn;
            }
        });

        cvCard.add(scrollPane);
    }

    // ==========================================
    // HELPER METHODS & UI CLASSES
    // ==========================================

    private JButton createProjectButton(String title) {
        JButton btn = new JButton(title);
        btn.setFont(new Font("Verdana", Font.BOLD, 13));
        btn.setForeground(new Color(55, 70, 85));
        btn.setBackground(new Color(230, 235, 240)); 
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 15, 0, 0)); 
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(196, 178, 145)); 
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(230, 235, 240));
                btn.setForeground(new Color(55, 70, 85));
            }
        });
        return btn;
    }

    private void showProjectDialog(String title, String description) {
        ProjectDialog dialog = new ProjectDialog(this, title, description);
        dialog.setVisible(true);
    }

    private JPanel createSidebarDetail(String title, String value, int y) {
        JPanel p = new JPanel(null);
        p.setBounds(30, y, 290, 35);
        p.setOpaque(false);
        
        JLabel t = new JLabel(title);
        t.setFont(new Font("Verdana", Font.BOLD, 13));
        t.setForeground(new Color(150, 165, 180));
        t.setBounds(0, 8, 80, 20);
        p.add(t);
        
        JLabel v = new JLabel(value, SwingConstants.RIGHT);
        v.setFont(new Font("Verdana", Font.PLAIN, 12));
        v.setForeground(Color.WHITE);
        v.setBounds(80, 8, 210, 20);
        p.add(v);
        
        JPanel line = new JPanel();
        line.setBackground(new Color(80, 95, 110));
        line.setBounds(0, 34, 290, 1);
        p.add(line);
        
        return p;
    }

    // ==========================================
    // CUSTOM INTERACTIVE DIALOG (PROJECT MODAL)
    // ==========================================
    class ProjectDialog extends JDialog {
        public ProjectDialog(JFrame parent, String title, String description) {
            super(parent, true); // Modal blocks background input
            setUndecorated(true);
            
            setSize(550, 380); 
            setLocationRelativeTo(parent);
            setBackground(new Color(0, 0, 0, 0)); 

            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // White Body
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                    // Dark Slate Header
                    g2.setColor(new Color(45, 55, 65));
                    g2.fillRoundRect(0, 0, getWidth(), 50, 25, 25);
                    g2.fillRect(0, 25, getWidth(), 25); 
                }
            };
            mainPanel.setLayout(null);
            mainPanel.setOpaque(false);
            setContentPane(mainPanel);

            // Title
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Verdana", Font.BOLD, 15));
            titleLabel.setForeground(new Color(230, 200, 150)); // Gold text
            titleLabel.setBounds(15, 10, 520, 30); 
            mainPanel.add(titleLabel);

            // Text Description
            String htmlFormat = "<html><div style='text-align: justify; font-family: Verdana; font-weight: normal; font-size: 13px; color: #405060; padding: 5px; line-height: 1.4;'>" + description + "</div></html>";
            JLabel descLabel = new JLabel(htmlFormat);
            descLabel.setVerticalAlignment(SwingConstants.TOP);
            descLabel.setBounds(25, 65, 500, 230); 
            mainPanel.add(descLabel);

            // Close Button
            JButton closeBtn = new RoundedButton("Close Window", new Color(196, 178, 145), 20, 0, Color.WHITE);
            closeBtn.setForeground(Color.WHITE);
            closeBtn.setFont(new Font("Verdana", Font.BOLD, 14));
            closeBtn.setBounds(175, 310, 200, 40); 
            closeBtn.addActionListener(e -> dispose());
            mainPanel.add(closeBtn);
        }
    }

    // SKILLBAR
    class SkillBar extends JPanel {
        private String skillName;
        private int percentage;

        public SkillBar(String skillName, int percentage, String description, int x, int y, int width) {
            this.skillName = skillName;
            this.percentage = percentage;
            
            // Generous height to accommodate text comfortably
            setBounds(x, y, width, 160); 
            setOpaque(false);
            setLayout(null); 
            
            String htmlDesc = "<html><div style='text-align: justify; font-family: Verdana; font-size: 11px; color: #607080;'>" + description + "</div></html>";
            JLabel descLabel = new JLabel(htmlDesc);
            descLabel.setVerticalAlignment(SwingConstants.TOP);
            descLabel.setBounds(0, 35, width, 125); 
            add(descLabel);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(new Color(55, 70, 85));
            g2.setFont(new Font("Verdana", Font.BOLD, 12));
            g2.drawString(skillName, 0, 15);
            
            int barY = 22;
            int barHeight = 8;
            g2.setColor(new Color(220, 225, 230));
            g2.fillRoundRect(0, barY, getWidth(), barHeight, barHeight, barHeight);
            
            int filledWidth = (int) (getWidth() * (percentage / 100.0));
            g2.setColor(new Color(196, 178, 145)); 
            g2.fillRoundRect(0, barY, filledWidth, barHeight, barHeight, barHeight);
        }
    }

    class RoundedPanel extends JPanel {
        private int r; Color c;
        public RoundedPanel(int r, Color c) { this.r = r; this.c = c; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
        SwingUtilities.invokeLater(() -> new CreditsPage(null).setVisible(true));
    }
}