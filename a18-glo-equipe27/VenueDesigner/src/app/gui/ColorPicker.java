package app.gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;

final class ColorPicker extends JFrame {
    private JPanel panelMain;
    private JColorChooser colorChooser;

    ColorPicker() {
        setContentPane(panelMain);
        setSize(600, 400);

        colorChooser = new JColorChooser(Color.BLACK);
        colorChooser.setBorder(null);
        colorChooser.setBounds(0, 0, 600, 300);
        colorChooser.setVisible(true);
        panelMain.add(colorChooser, BorderLayout.PAGE_START);
        colorChooser.getSelectionModel().addChangeListener(e -> {
            setVisible(false);
        });
    }

    public Color getColor() {
        return colorChooser.getSelectionModel().getSelectedColor();
    }
}
