package ru.cft.focusstart.task3.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.task3.listeners.RecordNameListener;

import javax.swing.*;
import java.awt.*;

public class SetUsernameWindow extends JDialog {
    private final static Logger log = LoggerFactory.getLogger(SetUsernameWindow.class.getName());

    private RecordNameListener nameListener;
    private String name;

    public SetUsernameWindow(JFrame frame) {
        super(frame, "Set name", true);

        JTextField nameField = new JTextField();

        GridLayout layout = new GridLayout(3, 1);
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        contentPane.add(new JLabel("Enter your name:"));
        contentPane.add(nameField);
        contentPane.add(createOkButton(nameField));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(210, 120));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public void setNameListener(RecordNameListener nameListener) {
        this.nameListener = nameListener;
    }

    public String getName() {
        return this.name;
    }

    private JButton createOkButton(JTextField nameField) {
        JButton button = new JButton("OK");
        button.addActionListener(e -> {
            dispose();

            this.name = nameField.getText();

            if (nameListener != null) {
                nameListener.onRecordNameEntered(nameField.getText());
            }
        });
        return button;
    }
}
