package app.gui;

import app.domain.Controller;
import app.domain.Stage;
import app.domain.UIPanel;

import javax.swing.*;

import java.util.Locale;
import java.util.Objects;

import static app.gui.GUIUtils.isNotNumber;

final class StageEdition extends JFrame {
    private JTextField width;
    private JTextField height;
    private JTextField elevation;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel panelMain;

    StageEdition(Controller controller, Stage stage, UIPanel panel) {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(stage);
        Objects.requireNonNull(panel);
        setContentPane(panelMain);
        setSize(300,400);
        setVisible(true);

        width.setText(String.format(Locale.ROOT,"%.2f",stage.getWidth()));
        height.setText(String.format(Locale.ROOT,"%.2f",stage.getHeight()));
        elevation.setText(String.format(Locale.ROOT,"%.2f",stage.getElevation()));

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            double stageWidth = Double.parseDouble(width.getText());
            double stageHeight = Double.parseDouble(height.getText());
            if (controller.validateStageDimensions(stage, stageWidth, stageHeight)) {
                stage.setWidth(stageWidth);
                stage.setHeight(stageHeight);
                stage.setElevation(Double.parseDouble(elevation.getText()));
                setVisible(false);
                dispose();
                panel.repaint();
                controller.saveRoom();
            } else {
                JOptionPane.showMessageDialog(null, "Inconsistent dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean isValidForm() {
        if (
                isNotNumber(width.getText()) ||
                        isNotNumber(height.getText()) ||
                        isNotNumber(elevation.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not a number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
