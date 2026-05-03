import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class BmiCalculator {

    static double lastBmi = 0;
    static String lastCategory = "";
    static String lastUnit = "";
    static double lastWeight = 0;
    static double lastHeight = 0;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("BMI Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Color bgColor = new Color(230, 240, 250);
        Color primary = new Color(70, 130, 180);
        panel.setBackground(bgColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel weightLabel = new JLabel("Weight:");
        JTextField weightField = new JTextField(10);
        JLabel heightLabel = new JLabel("Height:");
        JTextField heightField = new JTextField(10);

        JRadioButton metricBtn = new JRadioButton("Metric (kg, m)");
        JRadioButton imperialBtn = new JRadioButton("Imperial (lbs, in)");
        ButtonGroup group = new ButtonGroup();
        group.add(metricBtn);
        group.add(imperialBtn);

        JButton calcButton = new JButton("Calculate BMI");

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        resultPanel.setBackground(bgColor);
        JLabel resultLabel = new JLabel("Your BMI will appear here", JLabel.CENTER);
        JLabel moreInfoLink = new JLabel("<html><u>More Info</u></html>");
        moreInfoLink.setForeground(new Color(30, 100, 200));
        moreInfoLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        moreInfoLink.setVisible(false);
        resultPanel.add(resultLabel);
        resultPanel.add(moreInfoLink);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        weightLabel.setFont(labelFont);
        heightLabel.setFont(labelFont);
        metricBtn.setFont(labelFont);
        imperialBtn.setFont(labelFont);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        moreInfoLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        weightLabel.setForeground(primary);
        heightLabel.setForeground(primary);
        metricBtn.setBackground(bgColor);
        imperialBtn.setBackground(bgColor);
        calcButton.setBackground(primary);
        calcButton.setForeground(Color.BLACK);
        calcButton.setFocusPainted(false);
        calcButton.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(weightLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(weightField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(heightLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(heightField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(metricBtn, gbc);
        gbc.gridx = 1;
        panel.add(imperialBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weightx = 0; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(calcButton, gbc);

        gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(resultPanel, gbc);

        calcButton.addActionListener(e -> {
            try {
                double weight = Double.parseDouble(weightField.getText());
                double height = Double.parseDouble(heightField.getText());
                int unitChoice;
                if (metricBtn.isSelected()) {
                    unitChoice = 1;
                } else if (imperialBtn.isSelected()) {
                    unitChoice = 2;
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a unit!");
                    return;
                }

                double bmi = calculateBMI(unitChoice, weight, height);
                String category = determineBMICategory(bmi);

                lastBmi = bmi;
                lastCategory = category;
                lastUnit = (unitChoice == 1) ? "metric" : "imperial";
                lastWeight = weight;
                lastHeight = height;

                resultLabel.setText(String.format("BMI: %.2f (%s)", bmi, category));

                if (bmi < 18.5) resultLabel.setForeground(Color.BLUE);
                else if (bmi < 25) resultLabel.setForeground(new Color(0, 150, 0));
                else if (bmi < 30) resultLabel.setForeground(Color.ORANGE);
                else resultLabel.setForeground(Color.RED);

                moreInfoLink.setVisible(true);
                resultPanel.revalidate();
                resultPanel.repaint();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input!");
            }
        });

        moreInfoLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showEmailDialog(frame);
            }
        });

        frame.setContentPane(panel);
        frame.pack();
        frame.setMinimumSize(new Dimension(420, 280));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ===== Email Dialog =====
    static void showEmailDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Get Your Personalised Health Plan", true);
        dialog.setLayout(new BorderLayout());

        Color bgColor = new Color(230, 240, 250);
        Color primary = new Color(70, 130, 180);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Enter your email to receive your personalised health plan", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(primary);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        content.add(title, gbc);

        JLabel summary = new JLabel(String.format("Your BMI: %.2f — %s", lastBmi, lastCategory), JLabel.CENTER);
        summary.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        summary.setForeground(new Color(90, 90, 90));
        gbc.gridy = 1;
        content.add(summary, gbc);

        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLabel.setForeground(primary);
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridx = 0; gbc.weightx = 0;
        content.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        content.add(emailField, gbc);

        JButton submitBtn = new JButton("Submit");
        submitBtn.setBackground(primary);
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        submitBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weightx = 0; gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        content.add(submitBtn, gbc);

        JLabel statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        content.add(statusLabel, gbc);

        dialog.add(content, BorderLayout.CENTER);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(480, 220));
        dialog.setLocationRelativeTo(parent);

        submitBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid email address.");
                return;
            }
            submitBtn.setEnabled(false);
            statusLabel.setText("Generating your personalised health plan...");

            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() throws Exception {
                    return generateHealthPlan(email);
                }

                @Override
                protected void done() {
                    try {
                        String plan = get();
                        dialog.dispose();
                        showHealthPlanDialog(parent, email, plan);
                    } catch (Exception ex) {
                        statusLabel.setText("Error generating plan. Please try again.");
                        submitBtn.setEnabled(true);
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();
        });

        dialog.setVisible(true);
    }

    // ===== Call Anthropic API =====
    static String generateHealthPlan(String email) throws Exception {
        String weightUnit = lastUnit.equals("metric") ? "kg" : "lbs";
        String heightUnit = lastUnit.equals("metric") ? "m" : "inches";

        String prompt = String.format(
            "You are a certified health and wellness advisor. A person has used a BMI calculator and here are their details:\\n\\n" +
            "- BMI: %.2f\\n- Category: %s\\n- Weight: %.1f %s\\n- Height: %.2f %s\\n\\n" +
            "Write a warm, encouraging, detailed personalised health plan email. Include:\\n" +
            "1. Friendly greeting and explanation of their BMI\\n" +
            "2. Current health status and risks/benefits\\n" +
            "3. DIET PLAN - specific foods to eat, foods to avoid, meal timing, hydration\\n" +
            "4. GYM & EXERCISE PLAN - specific exercises, days per week, duration, intensity\\n" +
            "5. LIFESTYLE HABITS - sleep, stress, daily routines, screen time\\n" +
            "6. MENTAL HEALTH tips - body image and motivation\\n" +
            "7. Motivational closing message\\n\\n" +
            "Use section headers. Address them as 'Dear Friend'. Be supportive and empowering.",
            lastBmi, lastCategory, lastWeight, weightUnit, lastHeight, heightUnit
        );

        String requestBody =
            "{\"model\":\"claude-sonnet-4-20250514\",\"max_tokens\":2000," +
            "\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}]}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.anthropic.com/v1/messages"))
            .header("Content-Type", "application/json")
            .header("anthropic-version", "2023-06-01")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        int textStart = body.indexOf("\"text\":\"");
        if (textStart == -1) throw new RuntimeException("Unexpected API response: " + body);
        textStart += 8;
        int textEnd = body.lastIndexOf("\"}");
        String raw = body.substring(textStart, textEnd);
        return raw.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
    }

    // ===== Show Health Plan Dialog then Send Email =====
    static void showHealthPlanDialog(JFrame parent, String email, String plan) {
        JDialog planDialog = new JDialog(parent, "Your Personalised Health Plan", false);
        planDialog.setLayout(new BorderLayout(10, 10));

        Color bgColor = new Color(230, 240, 250);
        Color primary = new Color(70, 130, 180);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primary);
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        JLabel headerTitle = new JLabel("Your Personalised Health Plan", JLabel.LEFT);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerTitle.setForeground(Color.WHITE);
        JLabel headerSub = new JLabel("Sending to: " + email, JLabel.LEFT);
        headerSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerSub.setForeground(new Color(200, 220, 255));
        JPanel headerText = new JPanel(new GridLayout(2, 1, 0, 2));
        headerText.setBackground(primary);
        headerText.add(headerTitle);
        headerText.add(headerSub);
        header.add(headerText, BorderLayout.CENTER);

        JTextArea textArea = new JTextArea(plan);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(new Color(245, 250, 255));
        textArea.setForeground(new Color(30, 30, 50));
        textArea.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(180, 200, 220)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.setBackground(bgColor);
        footer.setBorder(BorderFactory.createEmptyBorder(8, 16, 10, 16));
        JLabel emailStatusLabel = new JLabel("⏳ Sending email...", JLabel.LEFT);
        emailStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        emailStatusLabel.setForeground(new Color(100, 100, 140));
        JButton copyBtn = new JButton("Copy Plan");
        copyBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        copyBtn.setBackground(primary);
        copyBtn.setForeground(Color.BLACK);
        copyBtn.setFocusPainted(false);
        copyBtn.addActionListener(e -> {
            textArea.selectAll();
            textArea.copy();
            textArea.select(0, 0);
            JOptionPane.showMessageDialog(planDialog, "Plan copied to clipboard!");
        });
        footer.add(emailStatusLabel, BorderLayout.CENTER);
        footer.add(copyBtn, BorderLayout.EAST);

        planDialog.add(header, BorderLayout.NORTH);
        planDialog.add(scrollPane, BorderLayout.CENTER);
        planDialog.add(footer, BorderLayout.SOUTH);
        planDialog.setSize(620, 580);
        planDialog.setLocationRelativeTo(parent);
        planDialog.setVisible(true);

        // Send email in background thread
        SwingWorker<Boolean, Void> emailWorker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                sendEmail(email, plan);
                return true;
            }

            @Override
            protected void done() {
                try {
                    get();
                    emailStatusLabel.setText("✅ Email sent successfully to " + email);
                    emailStatusLabel.setForeground(new Color(0, 130, 0));
                } catch (Exception ex) {
                    emailStatusLabel.setText("⚠ Email failed: " + ex.getMessage());
                    emailStatusLabel.setForeground(Color.RED);
                    ex.printStackTrace();
                }
            }
        };
        emailWorker.execute();
    }

    // ===== Send Email via Gmail SMTP =====
    static void sendEmail(String toEmail, String plan) throws Exception {
        // ============================================================
        //  CONFIGURE THESE TWO LINES WITH YOUR OWN DETAILS:
        // ============================================================
        final String FROM_EMAIL   = "zamakhatha@gmail.com";  // <-- your Gmail address
        final String APP_PASSWORD = "tyhr piac eysb wepy";   // <-- your Gmail App Password
        // ============================================================

        final String SUBJECT = "Your Personalised Health Plan from BMI Calculator";

        Properties props = new Properties();
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust",       "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL, "BMI Health Advisor"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(SUBJECT);

        // Plain text fallback
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(plan, "utf-8");

        // HTML version (nicely formatted)
        String htmlBody =
            "<html><body style='font-family:Segoe UI,Arial,sans-serif;color:#222;max-width:680px;margin:auto;'>"
            + "<div style='background:#466eb4;padding:20px 28px;border-radius:8px 8px 0 0;'>"
            + "<h2 style='color:white;margin:0;'>Your Personalised Health Plan</h2>"
            + "<p style='color:#c8dcff;margin:4px 0 0;'>Generated by BMI Health Advisor</p>"
            + "</div>"
            + "<div style='background:#f5faff;padding:24px 28px;border-radius:0 0 8px 8px;"
            + "border:1px solid #b4c8dc;border-top:none;'>"
            + "<pre style='white-space:pre-wrap;font-family:Segoe UI,Arial,sans-serif;"
            + "font-size:14px;line-height:1.7;'>"
            + plan.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            + "</pre></div>"
            + "<p style='color:#888;font-size:11px;text-align:center;margin-top:12px;'>"
            + "Always consult a healthcare professional before making major health changes."
            + "</p></body></html>";

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart("alternative");
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(htmlPart);
        message.setContent(multipart);

        Transport.send(message);
    }

    // ===== BMI Calculation =====
    public static double calculateBMI(int unitChoice, double weight, double height) {
        if (unitChoice == 1) return weight / (height * height);
        else return (703 * weight) / (height * height);
    }

    // ===== BMI Category =====
    public static String determineBMICategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal weight";
        else if (bmi < 30) return "Overweight";
        else if (bmi < 35) return "Obese";
        else return "Severely obese";
    }
}
