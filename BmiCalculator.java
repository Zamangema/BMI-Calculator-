import javax.swing.*;
import java.awt.*;

public class BmiCalculator
{
    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("BMI Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel with padding
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Colors
        Color bgColor = new Color(230, 240, 250);
        Color primary = new Color(70, 130, 180);
        panel.setBackground(bgColor);

        // GridBag setup
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
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

        JLabel resultLabel = new JLabel("Your BMI will appear here", JLabel.CENTER);

        // Fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        weightLabel.setFont(labelFont);
        heightLabel.setFont(labelFont);
        metricBtn.setFont(labelFont);
        imperialBtn.setFont(labelFont);

        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Colors
        weightLabel.setForeground(primary);
        heightLabel.setForeground(primary);

        metricBtn.setBackground(bgColor);
        imperialBtn.setBackground(bgColor);

        calcButton.setBackground(primary);
        calcButton.setForeground(Color.BLACK);
        calcButton.setFocusPainted(false);
        calcButton.setFont(new Font("Arial", Font.BOLD, 14));

        // ===== Layout =====

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(weightLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(weightField, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(heightLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(heightField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(metricBtn, gbc);

        gbc.gridx = 1;
        panel.add(imperialBtn, gbc);

        // Row 3 - Button (centered)
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(calcButton, gbc);

        // Row 4 - Result
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(resultLabel, gbc);

        // ===== Logic =====
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

                resultLabel.setText(String.format("BMI: %.2f (%s)", bmi, category));

                if (bmi < 18.5) {
                    resultLabel.setForeground(Color.BLUE);
                } else if (bmi < 25) {
                    resultLabel.setForeground(Color.GREEN);
                } else if (bmi < 30) {
                    resultLabel.setForeground(Color.ORANGE);
                } else {
                    resultLabel.setForeground(Color.RED);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input!");
            }
        });

        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static double calculateBMI(int unitChoice,double weight,double height)
    {
        double totalBMI;
        if(unitChoice == 1)
        {
            totalBMI = weight / (height * height);
        }else{
            totalBMI = (703 * weight) / (height * height);
        }

        return totalBMI;
    }

    public static String determineBMICategory(double bmi)
    {
        if(bmi < 18.5)
        {
            return "Underweight";
        }
        else if(bmi < 25){
            return "Normal weight";
        }
        else if(bmi < 30){
            return "Overweight";
        }
        else if(bmi < 35)
        {
            return "Obese";
        }else
        {
            return "Severely obese";
        }
    }
}