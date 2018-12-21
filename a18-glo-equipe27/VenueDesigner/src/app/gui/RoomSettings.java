package app.gui;

import app.domain.*;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.selection.SelectionAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

import java.util.Locale;
import static app.gui.GUIUtils.isNotNumber;

final class RoomSettings extends JFrame {
    private JPanel panelMain;
    private JTextField roomWidthTextField;
    private JTextField roomHeightTextField;
    private JTextField vitalSpaceWidthTextField;
    private JTextField vitalSpaceHeightTextField;
    private JButton oKButton;
    private JButton cancelButton;

    RoomSettings(Controller controller, UIPanel ui, ActionEvent event) {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(ui);
        Objects.requireNonNull(event);
        setContentPane(panelMain);
        setSize(300, 400);
        setVisible(true);

        Room room = controller.getRoom();

        roomWidthTextField.setText(String.format(Locale.ROOT,"%.2f",room.getWidth()));
        roomHeightTextField.setText(String.format(Locale.ROOT,"%.2f",room.getHeight()));
        vitalSpaceWidthTextField.setText(String.format(Locale.ROOT,"%.2f",room.getVitalSpace().getWidth()));
        vitalSpaceHeightTextField.setText(String.format(Locale.ROOT,"%.2f",room.getVitalSpace().getHeight()));

        oKButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            double roomWidth = Double.parseDouble(roomWidthTextField.getText());
            double roomHeight = Double.parseDouble(roomHeightTextField.getText());
            double vitalSpaceWidth = Double.parseDouble(vitalSpaceWidthTextField.getText());
            double vitalSpaceHeight = Double.parseDouble(vitalSpaceHeightTextField.getText());
            if (event.getActionCommand().equals("New")) {
                controller.createRoom(roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight);
            } else {
                if (!controller.validateRoomDimensions(roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight)) {
                    JOptionPane.showMessageDialog(null, "Inconsistent dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                room.setDimensions(roomWidth, roomHeight);
                room.getVitalSpace().setWidth(vitalSpaceWidth);
                room.getVitalSpace().setHeight(vitalSpaceHeight);
                for (Section section: room.getSections()){
                    section.accept(new SelectionAdapter() {
                        @Override
                        public void visit(SeatedSection section) {
                            if (section.isRegular){section.refresh();}
                            else if (room.getStage().isPresent()){
                                section.autoSetSeat=true;
                                controller.autoSetSeat();
                            }
                        }
                    });
                }
            }
            controller.saveRoom();
            setVisible(false);
            dispose();
            ui.repaint();
        });

        cancelButton.addActionListener( e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean isValidForm() {
        if(
                roomWidthTextField.getText().isEmpty() ||
                roomHeightTextField.getText().isEmpty() ||
                vitalSpaceWidthTextField.getText().isEmpty() ||
                vitalSpaceHeightTextField.getText().isEmpty()
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (
                        isNotNumber(roomWidthTextField.getText()) ||
                        isNotNumber(roomHeightTextField.getText()) ||
                        isNotNumber(vitalSpaceWidthTextField.getText()) ||
                        isNotNumber(vitalSpaceHeightTextField.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
