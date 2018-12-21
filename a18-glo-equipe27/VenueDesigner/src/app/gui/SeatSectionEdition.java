package app.gui;

import app.domain.Controller;
import app.domain.seat.Seat;
import app.domain.seat.SeatSection;
import app.domain.UIPanel;

import javax.swing.*;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static app.gui.GUIUtils.colorToArray;

import static app.gui.GUIUtils.isNotNumber;

final class SeatSectionEdition extends JFrame {
    private JTextField price;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel panelMain;
    private JButton colorButton;
    private JButton offersButton;

    private final ColorPicker colorPicker = new ColorPicker();

    SeatSectionEdition(Controller controller, SeatSection seatSection, UIPanel panel) {
        Objects.requireNonNull(seatSection);
        Objects.requireNonNull(panel);
        setContentPane(panelMain);
        setSize(400, 400);
        setVisible(true);

        ArrayList<Seat> listSeats = new ArrayList<>();

        for (int i = 0; i < seatSection.getSeats().length; i++) {
            listSeats.add(seatSection.getSeats()[i]);
        }

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

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            for (Seat seat : seatSection.getSeats()) {
                seat.setPrice(Double.parseDouble(price.getText()));
                seat.getShape().setColor(colorToArray(colorPicker.getColor()));
            }
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
        if (isNotNumber(price.getText())) {
            JOptionPane.showMessageDialog(null, "One or more fields are not a number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
