package app.gui;

import app.domain.Controller;
import app.domain.Offer;
import app.domain.Room;
import app.domain.UIPanel;
import app.domain.seat.Seat;
import app.domain.section.Section;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.gui.GUIUtils.isNotInteger;

final class OfferWindow extends JFrame{
    private JPanel panelMain;
    private JButton addButton;
    private JButton removeButton;
    private JButton okButton;
    private JRadioButton radioButtonPercent;
    private JRadioButton radioButtonDollar;
    private JButton okButtonOffer;
    private JButton cancelButtonOffer;

    private JTextField offerName;
    private JTextField amount;
    private JList<Offer> listOffers;
    private JLabel typeAmount;
    private JPanel panelEdit;
    private JButton editButton;
    private JTextField numberOfSeats;

    private boolean edition = false;

    OfferWindow(Controller controller, UIPanel ui) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        setSize(500, 400);
        setVisible(true);

        Room room = controller.getRoom();

        panelEdit.setVisible(false);
        ButtonGroup discount = new ButtonGroup();
        discount.add(radioButtonPercent);
        discount.add(radioButtonDollar);

        DefaultListModel<Offer> modelOffers = new DefaultListModel<>();
        for (Offer offer : room.getOffers()) {
            modelOffers.addElement(offer);
        }
        listOffers.setModel(modelOffers);

        okButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });

        addButton.addActionListener(e -> {
            resetEditPanel();
            panelEdit.setVisible(true);
            edition = false;
        });
        removeButton.addActionListener(e -> {
            Offer offer = listOffers.getSelectedValue();
            if (offer == null) {
                return;
            }
            modelOffers.removeElement(offer);
            room.getOffers().remove(offer);
            removeButton.setEnabled(listOffers.getSelectedValue() != null);
            editButton.setEnabled(listOffers.getSelectedValue() != null);
            resetEditPanel();
        });
        editButton.addActionListener(e -> onEdit());
        listOffers.addListSelectionListener(e ->{
            removeButton.setEnabled(listOffers.getSelectedValue() != null);
            editButton.setEnabled(listOffers.getSelectedValue() != null);
            showSeats(room); ui.repaint(); });

        removeButton.setEnabled(listOffers.getSelectedValue() != null);
        editButton.setEnabled(listOffers.getSelectedValue() != null);

        radioButtonPercent.addActionListener(e -> {
            if (radioButtonPercent.isSelected()) {
                typeAmount.setText("%");
            }
        });
        radioButtonDollar.addActionListener(e -> {
            if (radioButtonDollar.isSelected()) {
                typeAmount.setText("$");
            }
        });
        okButtonOffer.addActionListener(e -> {
            if (!isValidFormName() || !isValidType() || !isValidFormAmount()) {
                return;
            }
            if (!edition) {
                if (!isValidFormDblName(null)) {
                    return;
                }
                Offer offer = new Offer(offerName.getText(), Offer.DiscountMode.Dollar, Integer.parseInt(amount.getText()));
                modelOffers.addElement(offer);
                room.getOffers().add(offer);
            } else {
                if (!isValidFormDblName(listOffers.getSelectedValue())) {
                    return;
                }
                Offer offer = listOffers.getSelectedValue();
                offer.setName(offerName.getText());
                offer.setMode(Offer.DiscountMode.Dollar);
                offer.setDiscount(Integer.parseInt(amount.getText()));
            }
            resetEditPanel();
        });
        cancelButtonOffer.addActionListener(e -> resetEditPanel());
    }

    private void resetEditPanel() {
        panelEdit.setVisible(false);
        offerName.setText("");
        amount.setText("");
        radioButtonPercent.setSelected(false);
        radioButtonDollar.setSelected(false);
    }

    private void onEdit() {
        Offer offer = listOffers.getSelectedValue();
        if (offer == null) {
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
            edition = false;
            return;
        }
        edition = true;
        offerName.setText(offer.getName());
        amount.setText(offer.getDiscount() + "");
        if (offer.getMode() == Offer.DiscountMode.Dollar) {
            radioButtonDollar.setSelected(true);
            radioButtonPercent.setSelected(false);
        } else {
            radioButtonDollar.setSelected(false);
            radioButtonPercent.setSelected(true);
        }
        panelEdit.setVisible(true);
        removeButton.setEnabled(true);
        editButton.setEnabled(true);
    }

    private boolean isValidFormAmount() {
        if (isNotInteger(amount.getText()) || amount.getText().isEmpty() || Integer.parseInt(amount.getText())<0 ) {
            JOptionPane.showMessageDialog(null, "Sorry, amount field is not an integer or is empty or is negative", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isValidFormName() {
        if (offerName.getText().isEmpty() || offerName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, offer name field is empty ", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isValidType() {
        if (typeAmount.getText().isEmpty() || typeAmount.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, you have to choose the discount type", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isValidFormDblName(Offer offer) {
        ListModel<Offer> model = listOffers.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Offer o = model.getElementAt(i);
            if (o != offer && o.getName().equals(offerName.getText())) {
                offerName.setText("");
                JOptionPane.showMessageDialog(
                        this,
                        "Sorry, offer name already exists you have to choose another one ",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }
        return true;
    }

    private List<Offer> makeOffers() {
        ArrayList<Offer> offers = new ArrayList<>();
        ListModel<Offer> model = listOffers.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            offers.add(model.getElementAt(i));
        }
        return offers;
    }
    private void showSeats(Room room){
        Offer offer =listOffers.getSelectedValue();
        if (offer == null) { return; }
        int i=0;
        for (Section section: room.getSections()){
            for (Seat[] row : section.getSeats()){
                for (Seat seat: row){
                    seat.setSelected(false);
                    for (Offer o: seat.getOffers()){
                        if (o == offer){seat.setSelected(true);i++;}
                    }
                }
            }
        }
        numberOfSeats.setText(Integer.toString(i));
    }
}
