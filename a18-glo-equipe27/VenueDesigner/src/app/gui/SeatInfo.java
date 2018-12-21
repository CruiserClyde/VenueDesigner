package app.gui;

import app.domain.Offer;
import app.domain.seat.Seat;
import app.domain.section.Section;

import javax.swing.*;
import java.util.Locale;
import java.util.stream.Collectors;

final class SeatInfo extends JDialog{
    private JPanel panelMain;
    private JTextField number;
    private JTextField row;
    private JTextField section;
    private JTextField price;
    private JTextField elevation;
    private JTextArea offers;

    SeatInfo() {
        setContentPane(panelMain);
        setUndecorated(true);
        setSize(300, 400);
    }

    void update(Seat hoveredSeat, Section hoveredSection) {
        number.setText(hoveredSeat.getNumber()+ "");
        section.setText(hoveredSection.getName());
        row.setText(hoveredSeat.getRow()+1+ "");
        price.setText(String.format(Locale.ROOT,"%.2f", hoveredSeat.getPrice()));
        elevation.setText(String.format(Locale.ROOT,"%.2f", hoveredSection.getElevation()));
        offers.setText(hoveredSeat.getOffers().stream().map(Offer::toString).collect(Collectors.joining("\n", "- ", "")));
    }
}
