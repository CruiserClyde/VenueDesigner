package app.gui;

import app.domain.Controller;
import app.domain.VitalSpace;
import app.domain.seat.Seat;
import app.domain.section.SeatedSection;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static app.gui.GUIUtils.colorToArray;
import static app.gui.GUIUtils.isNotNumber;

final class IrregularSectionEdition  extends JFrame {
    private JTextField name;
    private JTextField elevationText;
    private JPanel panelMain;
    private JTextField price;
    private JTextField vsHeightText;
    private JTextField vsWidthText;
    private JButton okButton;
    private JButton cancelButton;
    private JButton colorButton;
    private JButton offersButton;

    private final ColorPicker colorPicker = new ColorPicker();

    IrregularSectionEdition(Controller controller, SeatedSection section) {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(section);
        setContentPane(panelMain);
        setSize(300, 400);
        setVisible(true);

        ArrayList<Seat> listSeats = new ArrayList<>();

        for (int i = 0; i < section.getSeats().length; i++) {
            for (int j = 0; j < section.getSeats()[i].length; j++) {
                listSeats.add(section.getSeats()[i][j]);
            }
        }

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

        VitalSpace vitalSpace = section.getVitalSpace();
        name.setText(section.getName());
        elevationText.setText(String.format(Locale.ROOT,"%.2f", section.getElevation()));
        vsWidthText.setText(String.format(Locale.ROOT,"%.2f", vitalSpace.getWidth()));
        vsHeightText.setText(String.format(Locale.ROOT,"%.2f", vitalSpace.getHeight()));
        price.setText(String.format(Locale.ROOT,"%.2f", 0.0));

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }

            double spaceWidth = Double.parseDouble(vsWidthText.getText());
            double spaceHeight = Double.parseDouble(vsHeightText.getText());

            section.setName(name.getText());
            section.setElevation(Double.parseDouble(elevationText.getText()));
            section.setVitalSpace(new VitalSpace(spaceWidth, spaceHeight));
            boolean check = false;
            if(!section.autoSetSeat) {
                section.autoSetSeat = true;
                check = true;
            }
            setVisible(false);
            dispose();
            controller.autoSetSeat();
            section.forEachSeats(seat -> {
                seat.setPrice(Double.parseDouble(price.getText()));
                seat.getShape().setColor(colorToArray(colorPicker.getColor()));
            });
            controller.saveRoom();
            if (check) {
                section.autoSetSeat = false;
            }
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean isValidForm() {
        if (
            isNotNumber(elevationText.getText()) ||
            isNotNumber(vsWidthText.getText()) ||
            isNotNumber(vsHeightText.getText()) ||
            isNotNumber(price.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

