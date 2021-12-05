package ru.cft.focusstart.task3.view;

import ru.cft.focusstart.task3.enums.GameType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoresWindow extends JDialog {
    public final static String DEFAULT_RECORD_TEXT = "PLAYER - TIME";
    public final static int RECORDS_COUNT = 5;

    private final List<JLabel> noviceRecordsLabels;
    private final List<JLabel> mediumRecordsLabels;
    private final List<JLabel> expertRecordsLabels;


    public HighScoresWindow(JFrame owner) {
        super(owner, "High Scores", true);

        GridBagLayout layout = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        int gridY = 0;


        contentPane.add(createLabel("Novice:", layout, gridY++, 0));
        noviceRecordsLabels = createLabels(RECORDS_COUNT, layout, gridY, 0);
        placeLabelsOnContentPane(contentPane, noviceRecordsLabels);
        gridY += noviceRecordsLabels.size();

        contentPane.add(createLabel("Medium:", layout, gridY++, 10));
        mediumRecordsLabels = createLabels(RECORDS_COUNT, layout, gridY, 0);
        placeLabelsOnContentPane(contentPane, mediumRecordsLabels);
        gridY += mediumRecordsLabels.size();

        contentPane.add(createLabel("Expert:", layout, gridY++, 10));
        expertRecordsLabels = createLabels(RECORDS_COUNT, layout, gridY, 0);
        placeLabelsOnContentPane(contentPane, expertRecordsLabels);
        gridY += expertRecordsLabels.size();

        contentPane.add(createCloseButton(layout, gridY++));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setPreferredSize(new Dimension(200, 400));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    private ArrayList<JLabel> createLabels(int size, GridBagLayout layout, int gridY, int margin) {
        ArrayList<JLabel> labels = new ArrayList<>(size);
        int yPos = gridY;
        for (int i = 0; i < size; i++) {
            labels.add(createLabel(DEFAULT_RECORD_TEXT, layout, yPos++, margin));
        }

        return labels;
    }

    private void placeLabelsOnContentPane(Container contentPane, List<JLabel> recordsLabels) {
        for (JLabel label : recordsLabels) {
            contentPane.add(label);
        }
    }

    public String createRecordText(String winnerName, int timeValue) {
        return winnerName + " - " + timeValue;
    }

    private JLabel createLabel(String labelText, GridBagLayout layout, int gridY, int margin) {
        JLabel label = new JLabel(labelText);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(margin, 0, 0, 0);
        layout.setConstraints(label, gbc);

        return label;
    }

    private JButton createCloseButton(GridBagLayout layout, int gridY) {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        okButton.setPreferredSize(new Dimension(60, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        layout.setConstraints(okButton, gbc);

        return okButton;
    }

    private void updateRecordsList(List<JLabel> list, List<String> usernames, List<Integer> times) {
        int inputSize = usernames.size();
        if (inputSize > RECORDS_COUNT) {
            inputSize = RECORDS_COUNT;
        }

        for (int i = 0; i < inputSize; i++) {
            JLabel label = list.get(i);

            String username = usernames.get(i);
            Integer time = times.get(i);

            String text = username + " - " + time;

            label.setText(text);
        }
    }


    public void setRecordByType(GameType type, List<String> usernames, List<Integer> times) {
        switch (type) {
            case NOVICE -> this.updateRecordsList(this.noviceRecordsLabels, usernames, times);
            case MEDIUM -> this.updateRecordsList(this.mediumRecordsLabels, usernames, times);
            case EXPERT -> this.updateRecordsList(this.expertRecordsLabels, usernames, times);
        }
    }


}
