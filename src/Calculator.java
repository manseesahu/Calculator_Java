import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {

    // Create a JFrame instance
    private JFrame frame;
    private JTextArea display;
    private StringBuilder input;
    private boolean darkMode = true; // Track the current mode

    // Constructor for the Calculator class
    public Calculator() {
        input = new StringBuilder();
        frame = new JFrame("3D Calculator");

        // Set up the frame
        frame.setSize(360, 500);  // Set the size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create display area
        display = new JTextArea();
        display.setFont(new Font("Arial", Font.BOLD, 30));
        display.setEditable(false);
        display.setOpaque(true);
        display.setBackground(Color.BLACK); // Default background
        display.setForeground(Color.YELLOW); // Default text color
        display.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        display.setPreferredSize(new Dimension(360, 100)); // Adjust size as needed

        // Add the display to the frame
        frame.add(display, BorderLayout.NORTH);

        // Create button panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5)); // Adjust grid to 5 rows
        panel.setBackground(Color.DARK_GRAY);
        frame.add(panel, BorderLayout.CENTER);

        // Button labels with new buttons
        String[] labels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            ".", "0", "^", "+",
            "C", "←", "Mode", "="
        };

        // Create buttons and add them to the panel
        for (String label : labels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 20)); // Adjusted font size
            button.setBackground(Color.white);
            button.setForeground(Color.BLACK);
            button.setOpaque(true);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            button.setPreferredSize(new Dimension(80, 80)); // Increased size for buttons

            // Add 3D effect
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusPainted(false);

            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        // Set the frame to be visible
        frame.setVisible(true);
    }

    // Inner class to handle button clicks
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String command = source.getText();

            if (command.equals("C")) {
                input.setLength(0);
                display.setText("");
            } else if (command.equals("←")) {
                if (input.length() > 0) {
                    // Remove the last character from the input
                    input.setLength(input.length() - 1);
                    display.setText(input.toString());
                }
            } else if (command.equals("Mode")) {
                toggleDarkMode(!darkMode); // Toggle between dark and light mode
            } else if (command.equals("=")) {
                try {
                    String result = evaluate(input.toString());
                    display.setText(result);
                    input.setLength(0);  // Clear input after evaluating
                } catch (Exception ex) {
                    display.setText("Error");
                }
            } else {
                // Append the clicked button's text to the input
                if (input.length() > 0 && isOperator(command) && isOperator(input.charAt(input.length() - 1))) {
                    // Replace the previous operator with the new one if an operator is already at the end
                    input.setLength(input.length() - 1);
                }
                input.append(command);
                display.setText(input.toString());
            }
        }

        private boolean isOperator(String s) {
            return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
        }

        private boolean isOperator(char c) {
            return c == '+' || c == '-' || c == '*' || c == '/';
        }

        private void toggleDarkMode(boolean darkMode) {
            Calculator.this.darkMode = darkMode;
            Color bgColor = darkMode ? Color.BLACK : Color.WHITE;
            Color fgColor = darkMode ? Color.YELLOW : Color.BLACK;
            Color buttonColor = darkMode ? Color.GRAY : Color.LIGHT_GRAY;

            display.setBackground(bgColor);
            display.setForeground(fgColor);

            for (Component comp : ((JPanel) frame.getContentPane().getComponent(1)).getComponents()) {
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    button.setBackground(buttonColor);
                    button.setForeground(fgColor);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createRaisedBevelBorder(),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                }
            }
        }

        private String evaluate(String expression) throws Exception {
            if (expression == null || expression.isEmpty()) {
                return "0";
            }

            // Replace operators with spaces to split the string
            expression = expression.replaceAll(" ", "");

            // Split the expression into numbers and operators
            String[] tokens = expression.split("(?=[-+/])|(?<=[-+/])");

            // Use a stack to evaluate the expression
            double result = 0;
            double temp = 0;
            char lastOperator = '+';

            for (String token : tokens) {
                if (token.matches("[0-9.]+")) {  // If token is a number
                    temp = Double.parseDouble(token);
                    switch (lastOperator) {
                        case '+': result += temp; break;
                        case '-': result -= temp; break;
                        case '*': result *= temp; break;
                        case '/':
                            if (temp == 0) throw new ArithmeticException("Cannot divide by zero");
                            result /= temp; break;
                    }
                } else if (token.matches("[+*/-]")) {  // If token is an operator
                    lastOperator = token.charAt(0);
                }
            }

            return String.valueOf(result);
        }
    }

    // Main method to run the Calculator
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
