package app.gui;

import app.domain.*;
import app.domain.seat.Seat;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class OfferAttribution extends JFrame{
    private JButton addOfferButton;
    private JButton removeOfferButton;
    private JButton okButton;
    private JList<Offer> seatOffers;
    private JPanel panelMain;
    private JPanel panelAdd;
    private JButton addOffersToSeat;
    private JList<Offer> listOffers;
    private JList<Seat> listSeatsJlist;
    private JScrollPane scrollPaneSeatOffers;
    private JScrollPane scrollPaneSeats;
    private JButton addOfferToAllButton;
    private JButton removeOfferFromAllButton;

    OfferAttribution(Controller controller, ArrayList<Seat> listSeats) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        setSize(500, 400);
        setVisible(true);
        panelAdd.setVisible(false);

        scrollPaneSeatOffers.setViewportView(seatOffers);
        scrollPaneSeats.setViewportView(listSeatsJlist);

        listSeatsJlist.setSelectedIndex(0);

        DefaultListModel<Offer> modelSeatOffer = new DefaultListModel<>();
        seatOffers.setModel(modelSeatOffer);

        DefaultListModel<Offer> modelOffer = new DefaultListModel<>();
        for (Offer offer : controller.getRoom().getOffers()) {
            modelOffer.addElement(offer);
        }
        listOffers.setModel(modelOffer);

        DefaultListModel<Seat> modelSeat = new DefaultListModel<>();
        for (Seat seat : listSeats) {
            modelSeat.addElement(seat);
        }
        listSeatsJlist.setModel(modelSeat);

        addOfferButton.addActionListener(e -> {
            panelAdd.setVisible(true);
        });

        removeOfferButton.addActionListener(e -> {
            Seat seat = listSeatsJlist.getSelectedValue();
            Offer offer = seatOffers.getSelectedValue();
            if (offer == null) {
                return;
            }
                if (seat.getOffers().contains(offer)) {
                    seat.getOffers().remove(offer);
                }
            modelSeatOffer.removeElement(offer);
        });

        addOffersToSeat.addActionListener(e -> {
            List<Offer> offerList = listOffers.getSelectedValuesList();
            Seat seat = listSeatsJlist.getSelectedValue();
                List<Offer> seatOffer = seat.getOffers();
                for (Offer offer : offerList) {
                    if (seatOffer.contains(offer)) {
                        continue;
                    }
                    seatOffer.add(offer);
                    modelSeatOffer.addElement(offer);
                }
        });

        addOfferToAllButton.addActionListener(e -> {
            List<Offer> offerList = listOffers.getSelectedValuesList();
            for (Seat seat : listSeats) {
                List<Offer> seatOffer = seat.getOffers();
                for (Offer offer : offerList) {
                    if (seatOffer.contains(offer)) {
                        continue;
                    }
                    seatOffer.add(offer);
                }
            }
            Seat seat = listSeatsJlist.getSelectedValue();
            modelSeatOffer.removeAllElements();
            for (Offer offer : seat.getOffers()) {
                modelSeatOffer.addElement(offer);
            }
        });

        removeOfferFromAllButton.addActionListener(e -> {
            Offer offer = seatOffers.getSelectedValue();
            if (offer == null) {
                return;
            }
            for (Seat seat : listSeats) {
                if (seat.getOffers().contains(offer)) {
                    seat.getOffers().remove(offer);
                }
                modelSeatOffer.removeElement(offer);
            }
        });

        listSeatsJlist.addListSelectionListener( e -> {
            Seat seat = listSeatsJlist.getSelectedValue();
            modelSeatOffer.removeAllElements();
            for (Offer offer : seat.getOffers()) {
                modelSeatOffer.addElement(offer);
            }
        });

        okButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }
}
