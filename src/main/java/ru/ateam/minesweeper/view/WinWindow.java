package ru.ateam.minesweeper.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ateam.minesweeper.controller.MinesweeperGameController;
import ru.ateam.minesweeper.enums.UserStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class WinWindow extends JDialog {
    private final static Logger log = LoggerFactory.getLogger(WinWindow.class.getName());

    private ActionListener newGameListener;
    private ActionListener exitListener;

    private final Container contentPane;

    private int winTime;
    private String winMessage;
    private UserStatus status;
    private String name;
    private int time;

    public WinWindow(JFrame owner) {
        super(owner, "Win", false);

        this.contentPane = getContentPane();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(300, 130));
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void addWindowClosingListener(MinesweeperGameController controller) {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                controller.onStartNewGameAction();
            }
        });
    }

    public void updateData(int time, UserStatus status, String name) {
        this.contentPane.removeAll();

        if (status != UserStatus.USER_EXISTS) {
            this.winMessage = "You won, " + name + "! " + time + " sec. New record!";
        } else {
            this.winMessage = "You won, " + name + "! " + time + "sec.";
        }

        GridBagLayout layout = new GridBagLayout();
        this.contentPane.setLayout(layout);

        this.contentPane.add(createLoseLabel(layout));
        this.contentPane.add(createNewGameButton(layout));
        this.contentPane.add(createExitButton(layout));

        pack();
    }

    public void setNewGameListener(ActionListener newGameListener) {
        this.newGameListener = newGameListener;
    }

    public void setExitListener(ActionListener exitListener) {
        this.exitListener = exitListener;
    }

    private JLabel createLoseLabel(GridBagLayout layout) {
        JLabel label = new JLabel(winMessage);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        layout.setConstraints(label, gbc);
        return label;
    }

    private JButton createNewGameButton(GridBagLayout layout) {
        JButton newGameButton = new JButton("New game");
        newGameButton.setPreferredSize(new Dimension(100, 25));

        newGameButton.addActionListener(e -> {
            dispose();

            if (newGameListener != null) {
                newGameListener.actionPerformed(e);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(15, 0, 0, 0);
        layout.setConstraints(newGameButton, gbc);

        return newGameButton;
    }

    private JButton createExitButton(GridBagLayout layout) {
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100, 25));

        exitButton.addActionListener(e -> {
            dispose();

            if (exitListener != null) {
                exitListener.actionPerformed(e);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(15, 5, 0, 0);
        layout.setConstraints(exitButton, gbc);

        return exitButton;
    }
}
