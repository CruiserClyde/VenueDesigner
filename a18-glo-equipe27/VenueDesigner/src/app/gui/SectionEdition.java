package app.gui;

import app.domain.Controller;
import app.domain.seat.Seat;
import app.domain.section.SeatedSection;
import app.domain.VitalSpace;
import app.domain.UIPanel;

import javax.swing.*;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static app.gui.GUIUtils.colorToArray;
import static app.gui.GUIUtils.isNotInteger;
import static app.gui.GUIUtils.isNotNumber;

final class SectionEdition extends JFrame {
    private JTextField name;
    private JTextField columns;
    private JTextField rows;
    private JTextField elevation;
    private JButton okButtton;
    private JButton cancelButton;
    private JPanel panelMain;
    private JTextField vitalSpaceWidth;
    private JTextField vitalSpaceHeight;
    private JTextField price;
    private JButton colorButton;
    private JButton offersButton;
    private ArrayList<Seat> listSeats = new ArrayList<>();

    private final ColorPicker colorPicker = new ColorPicker();

    SectionEdition(Controller controller, SeatedSection section, UIPanel panel) {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(section);
        Objects.requireNonNull(panel);
        setContentPane(panelMain);
        setSize(300,400);
        setVisible(true);

        for (int i = 0; i < section.getSeats().length; i++) {
            for (int j = 0; j < section.getSeats()[i].length; j++) {
                listSeats.add(section.getSeats()[i][j]);
            }
        }

        VitalSpace vitalSpace = section.getVitalSpace();
        name.setText(section.getName());
        columns.setText(section.getColumns() + "");
        rows.setText(section.getRows() + "");
        elevation.setText(String.format(Locale.ROOT,"%.2f",section.getElevation()));
        vitalSpaceWidth.setText(String.format(Locale.ROOT,"%.2f",vitalSpace.getWidth()));
        vitalSpaceHeight.setText(String.format(Locale.ROOT,"%.2f",vitalSpace.getHeight()));
        price.setText(String.format(Locale.ROOT,"%.2f", 0.0));

        colorButton.addActionListener(e -> {
            colorPicker.setVisible(true);
        });

        colorPicker.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                colorButton.setBackground(colorPicker.getColor());
            }
        });

        offersButton.addActionListener(e -> new OfferAttribution(controller, listSeats));

        okButtton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            int nbColums = Integer.parseInt(columns.getText());
            int nbRows = Integer.parseInt(rows.getText());
            double spaceWidth = Double.parseDouble(vitalSpaceWidth.getText());
            double spaceHeight = Double.parseDouble(vitalSpaceHeight.getText());
            if (controller.validateSectionDimensions(section, spaceWidth, spaceHeight)) {
                section.setName(name.getText());
                section.setDimensions(nbColums, nbRows);
                section.setElevation(Double.parseDouble(elevation.getText()));
                if (spaceWidth != vitalSpace.getWidth() || spaceHeight != vitalSpace.getHeight()) {
                    section.setVitalSpace(new VitalSpace(spaceWidth, spaceHeight));
                }
                section.forEachSeats(seat -> {
                    seat.setPrice(Double.parseDouble(price.getText()));
                    seat.getShape().setColor(colorToArray(colorPicker.getColor()));
                });
                controller.saveRoom();
                setVisible(false);
                dispose();
                panel.repaint();
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
                isNotInteger(columns.getText()) ||
                        isNotInteger(rows.getText()) ||
                        isNotNumber(elevation.getText()) ||
                        isNotNumber(vitalSpaceWidth.getText()) ||
                        isNotNumber(vitalSpaceHeight.getText()) ||
                        isNotNumber(price.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
