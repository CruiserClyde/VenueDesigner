package app.gui;

import app.domain.Controller;
import app.domain.seat.Seat;
import app.domain.UIPanel;

import javax.swing.*;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static app.gui.GUIUtils.isNotNumber;
import static app.gui.GUIUtils.colorToArray;

final class SeatEdition extends JFrame {
    private JPanel panelMain;
    private JTextField price;
    private JButton okButton;
    private JButton cancelButton;
    private JButton colorButton;
    private JButton offersButton;

    private final ColorPicker colorPicker = new ColorPicker();

    SeatEdition(Controller controller, Seat seat, UIPanel panel) {
        Objects.requireNonNull(seat);
        Objects.requireNonNull(panel);
        setContentPane(panelMain);
        setSize(300, 400);
        setVisible(true);

        ArrayList<Seat> listSeats = new ArrayList<>();
        listSeats.add(seat);

        price.setText(String.format(Locale.ROOT,"%.2f", seat.getPrice()));
        colorButton.addActionListener(e -> colorPicker.setVisible(true));
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
            seat.setPrice(Double.parseDouble(price.getText()));
            seat.getShape().setColor(colorToArray(colorPicker.getColor()));
            controller.saveRoom();
            setVisible(false);
            dispose();
            panel.repaint();
        });

        cancelButton.addActionListener(e -> {
            colorPicker.setVisible(false);
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
