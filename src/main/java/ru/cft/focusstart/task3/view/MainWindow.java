package ru.cft.focusstart.task3.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.task3.enums.ButtonType;
import ru.cft.focusstart.task3.enums.GameImage;
import ru.cft.focusstart.task3.listeners.CellEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    private final static Logger log = LoggerFactory.getLogger(MainWindow.class.getName());

    private final static String usernamePrefix = "User: ";

    private final Container contentPane;
    private final GridBagLayout mainLayout;

    private JMenuItem newGameMenu;
    private JMenuItem highScoresMenu;
    private JMenuItem gameModeSettingsMenu;
    private JMenuItem exitMenu;

    private JMenuItem helpInfoMenu;
    private JMenuItem aboutMenu;

    private JMenuItem usernameSettingsMenu;

    private CellEventListener listener;

    private JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel bombsCounterLabel;

    private JLabel usernameLabel;


    public MainWindow() {
        super("Minesweeper");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowClosingListener();

        setResizable(false);

        createMenu();

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);

        contentPane.setBackground(new Color(109, 170, 44, 255));
    }

    private void addWindowClosingListener() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                log.info("Main windows closed. [Program exit]");
            }
        });
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        gameMenu.add(newGameMenu = new JMenuItem("New Game"));
        gameMenu.addSeparator();
        gameMenu.add(highScoresMenu = new JMenuItem("High Scores"));
        gameMenu.add(gameModeSettingsMenu = new JMenuItem("Field settings"));
        gameMenu.addSeparator();
        gameMenu.add(exitMenu = new JMenuItem("Exit"));

        JMenu helpMenu = new JMenu("Help");

        helpMenu.add(helpInfoMenu = new JMenuItem("Controls"));
        helpMenu.add(aboutMenu = new JMenuItem("About"));

        JMenu settingsMenu = new JMenu("Settings");

        settingsMenu.add(usernameSettingsMenu = new JMenuItem("Username"));

        usernameLabel = createUsernameLabel();

        menuBar.add(gameMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);
        menuBar.add(usernameLabel);
        setJMenuBar(menuBar);
    }

    public void setNewGameMenuAction(ActionListener listener) {
        newGameMenu.addActionListener(listener);
    }

    public void setHighScoresMenuAction(ActionListener listener) {
        highScoresMenu.addActionListener(listener);
    }

    public void setSettingsMenuAction(ActionListener listener) {
        gameModeSettingsMenu.addActionListener(listener);
    }

    public void setExitMenuAction(ActionListener listener) {
        exitMenu.addActionListener(listener);
    }

    public void setHelpInfoMenuAction(ActionListener listener) {
        helpInfoMenu.addActionListener(listener);
    }

    public void setAboutMenuAction(ActionListener listener) {
        aboutMenu.addActionListener(listener);
    }

    public void setUsernameSettingsMenuAction(ActionListener listener) {
        usernameSettingsMenu.addActionListener(listener);
    }

    public void setCellListener(CellEventListener listener) {
        this.listener = listener;
    }

    public void setCellImage(int x, int y, GameImage gameImage) {
        cellButtons[y][x].setIcon(gameImage.getImageIcon());
    }

    public void setBombsCount(int bombsCount) {
        bombsCounterLabel.setText(String.valueOf(bombsCount));
    }

    public void setTimerValue(int value) {
        timerLabel.setText(String.valueOf(value));
    }

    public void updateUsernameLabel(String name) {
        String fullName = usernamePrefix + name;
        this.usernameLabel.setText(fullName);
    }

    public void createGameField(int rowsCount, int colsCount) {
        contentPane.removeAll();
        setPreferredSize(new Dimension(20 * colsCount + 90, 20 * rowsCount + 110));

        addButtonsPanel(createButtonsPanel(rowsCount, colsCount));
        addTimerImage();
        addTimerLabel(timerLabel = new JLabel("0"));
        addBombCounter(bombsCounterLabel = new JLabel("0"));
        addBombCounterImage();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createButtonsPanel(int numberOfRows, int numberOfCols) {
        cellButtons = new JButton[numberOfRows][numberOfCols];
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(20 * numberOfCols, 20 * numberOfRows));
        buttonsPanel.setLayout(new GridLayout(numberOfRows, numberOfCols, 0, 0));

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                final int x = col;
                final int y = row;

                cellButtons[y][x] = new JButton(GameImage.CLOSED.getImageIcon());
                cellButtons[y][x].addMouseListener(new MouseAdapter() {

                    boolean mouse1 = false;
                    boolean mouse2 = false;
                    boolean doubleClickPerformed = false;

                    private void checkButton(boolean mouse1, boolean mouse2) {
                        if (mouse1 & mouse2) {
                            listener.onMouseClick(x, y, ButtonType.MIDDLE_BUTTON);
                            doubleClickPerformed = true;
                            return;
                        }
                        if (mouse1) {
                            if (doubleClickPerformed) {
                                doubleClickPerformed = false;
                            } else {
                                listener.onMouseClick(x, y, ButtonType.LEFT_BUTTON);
                            }
                            return;
                        }
                        if (mouse2) {
                            if (doubleClickPerformed) {
                                doubleClickPerformed = false;
                            } else {
                                listener.onMouseClick(x, y, ButtonType.RIGHT_BUTTON);
                            }
                            return;
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (listener == null) {
                            return;
                        }

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            checkButton(mouse1, mouse2);
                            mouse1 = false;
                        }

                        if (SwingUtilities.isRightMouseButton(e)) {
                            checkButton(mouse1, mouse2);
                            mouse2 = false;
                        }

                        if (SwingUtilities.isMiddleMouseButton(e)) {
                            listener.onMouseClick(x, y, ButtonType.MIDDLE_BUTTON);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (listener == null) {
                            return;
                        }

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            mouse1 = true;
                        }

                        if (SwingUtilities.isRightMouseButton(e)) {
                            mouse2 = true;
                        }
                    }


                });
                buttonsPanel.add(cellButtons[y][x]);
            }
        }

        return buttonsPanel;
    }

    private JLabel createUsernameLabel() {
        JLabel label = new JLabel();

        label.setForeground(Color.gray);

        return label;
    }

    private void addButtonsPanel(JPanel buttonsPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 20, 5, 20);
        mainLayout.setConstraints(buttonsPanel, gbc);
        contentPane.add(buttonsPanel);
    }

    private void addTimerImage() {
        JLabel label = new JLabel(GameImage.TIMER.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private void addTimerLabel(JLabel timerLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        mainLayout.setConstraints(timerLabel, gbc);
        contentPane.add(timerLabel);
    }

    private void addBombCounter(JLabel bombsCounterLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.7;
        mainLayout.setConstraints(bombsCounterLabel, gbc);
        contentPane.add(bombsCounterLabel);
    }

    private void addBombCounterImage() {
        JLabel label = new JLabel(GameImage.BOMB.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 5, 0, 20);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

//    @Override
//    public void onModelCellChanged(int x, int y, GameImage type) {
//        this.setCellImage(x, y, type);
//    }
//
//    @Override
//    public void onMineCounterChanged(int newMinesCounterNumber) {
//        this.setBombsCount(newMinesCounterNumber);
//    }

}
