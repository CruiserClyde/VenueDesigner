package app.gui;

import app.domain.Controller;
import app.domain.UIPanel;
import app.domain.section.StandingSection;

import javax.swing.*;
import java.util.Locale;
import java.util.Objects;

import static app.gui.GUIUtils.isNotNumber;
import static app.gui.GUIUtils.isNotInteger;

final class StandingSectionEdition extends JFrame{
    private JTextField name;
    private JButton okButton;
    private JPanel panelMain;
    private JTextField elevation;
    private JTextField price;
    private JButton cancelButton;
    private JTextField max;

    StandingSectionEdition(Controller controller, StandingSection section, UIPanel panel) {
        Objects.requireNonNull(section);
        Objects.requireNonNull(panel);
        setContentPane(panelMain);
        setSize(300, 400);
        setVisible(true);

        name.setText(section.getName());
        elevation.setText(String.format(Locale.ROOT,"%.2f",section.getElevation()));
        max.setText(section.getMax()+"");
        price.setText(String.format(Locale.ROOT,"%.2f",section.getPrice()));

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            section.setName(name.getText());
            section.setElevation(Double.parseDouble(elevation.getText()));
            section.setPrice(Double.parseDouble(price.getText()));
            section.setMax(Integer.parseInt(max.getText()));
            controller.saveRoom();
            setVisible(false);
            dispose();
            panel.repaint();

        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean isValidForm() {
        if (
                isNotNumber(elevation.getText()) ||
                isNotNumber(price.getText())||
                isNotInteger(max.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are invalid.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
