package ru.cft.focusstart.task3.view;

import javax.swing.*;
import java.awt.*;

public class AboutWindow extends JDialog {
    private final static String RULES_STRING =
            "The board is divided into cells, with mines randomly distributed. " +
                    "To win, you need to flag all mines. " +
                    "The number on a cell shows the number of mines adjacent to it. " +
                    "Using this information, you can determine cells that are safe, and cells that contain mines. " +
                    "Cells suspected of being mines can be marked with a flag using the right mouse button.";

    private final static String RESULTS_STRING =
            "The results will be stored to local file stored on your disk drive. " +
                    "Don't delete the game and you will be able to improve your results. " +
                    "Choose your player name wisely to keep all attempts saved.";

    public AboutWindow(JFrame owner) {
        super(owner, "About", true);

        GridBagLayout layout = new GridBagLayout();

        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        int gridY = 0;
        contentPane.add(createTopLabel("Rules:", layout, gridY++));
        contentPane.add(createRulesArea(RULES_STRING, layout, gridY++, this.getBackground()));
        contentPane.add(createTopLabel("Results:", layout, gridY++));
        contentPane.add(createResultsArea(RESULTS_STRING, layout, gridY++, this.getBackground()));
        contentPane.add(createTopLabel("Thanks for playing!", layout, gridY++));
        contentPane.add(createOkButton("Understand!", layout, gridY++));

        setPreferredSize(new Dimension(300, 320));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    private JLabel createTopLabel(String labelText, GridBagLayout layout, int gridY) {
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBackground(Color.darkGray);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.weightx = 1f;
        gbc.weighty = 0.1f;

        layout.setConstraints(label, gbc);
        return label;
    }

    private JTextArea createRulesArea(String rulesText, GridBagLayout layout, int gridY, Color color) {
        JTextArea area = new JTextArea(rulesText);
        area.setBackground(color);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        area.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.weighty = 0.2f;

        layout.setConstraints(area, gbc);
        return area;
    }

    private JTextArea createResultsArea(String rulesText, GridBagLayout layout, int gridY, Color color) {
        JTextArea area = new JTextArea(rulesText);
        area.setBackground(color);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        area.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.weighty = 0.2f;

        layout.setConstraints(area, gbc);
        return area;
    }

    private JButton createOkButton(String buttonText, GridBagLayout layout, int gridY) {
        JButton button = new JButton(buttonText);

        button.addActionListener(e -> dispose());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.weightx = 1f;
        gbc.weighty = 0.1f;

        layout.setConstraints(button, gbc);
        return button;
    }

}
