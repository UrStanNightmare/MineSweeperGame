package ru.ateam.minesweeper.view;

import javax.swing.*;
import java.awt.*;

public class PlayerControlsWindow extends JDialog {
    private final static String LINE_SEPARATOR = System.lineSeparator();
    private final static String CONTROLS_INFO_STRING =
            "1)Left mouse button - opens cell " + LINE_SEPARATOR.repeat(2) +
                    "2)Right mouse button - place flag on cell." + LINE_SEPARATOR.repeat(2) +
                    "3)Scroll wheel (or Right mouse button + Left mouse button) - opens cells nearby if flags needed placed near." + LINE_SEPARATOR.repeat(2) +
                    "4)Player name can be changed from settings menu by clicking Username button." +
                    "Special window will appear. If you don't want to specify unique username, " +
                    "simply close the window and guest name will be set." + LINE_SEPARATOR.repeat(2) +
                    "5)If you find out that ambient music is too annoying for you, you can always turn it off by" +
                    "clicking Music On/Off button from Settings section. Bomb explosion can't be turned off so be" +
                    "prepared to be scared.";

    public PlayerControlsWindow(JFrame owner) {
        super(owner, "Controls", true);

        GridBagLayout layout = new GridBagLayout();

        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        int gridY = 0;

        contentPane.add(createTopLabel("Controls:", layout, gridY++));
        contentPane.add(createControlsArea(CONTROLS_INFO_STRING, layout, gridY++, this.getBackground()));
        contentPane.add(createOkButton("Understand!", layout, gridY++));

        setPreferredSize(new Dimension(300, 400));
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

    private JTextArea createControlsArea(String controlsInfoString, GridBagLayout layout, int gridY, Color color) {
        JTextArea area = new JTextArea(controlsInfoString);
        area.setBackground(color);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        area.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.weighty = 0.8f;

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
