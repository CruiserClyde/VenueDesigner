package app.gui;

import app.domain.Controller;
import app.domain.Stage;
import app.domain.priceAlgo.AlgoType;
import app.domain.priceAlgo.PriceAlgo;
import app.domain.priceAlgo.PriceAlgoFactory;
import app.domain.section.Section;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

import static app.gui.GUIUtils.isNotNumber;

final class AutoPrices extends JFrame{
    private JPanel panelMain;
    private JRadioButton seatBtn;
    private JRadioButton rowBtn;
    private JRadioButton sectionBtn;
    private JButton okBtn;
    private JTextField min;
    private JTextField max;

    AutoPrices(Controller controller, List<Section> sections, Stage stage) {
        Objects.requireNonNull(sections);
        Objects.requireNonNull(stage);
        setLocation(300,300);
        setContentPane(panelMain);
        setSize(200, 200);
        setVisible(true);

        seatBtn.setSelected(true);
        okBtn.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            setPrices(sections, stage, Double.parseDouble(min.getText()), Double.parseDouble(max.getText()));
            controller.saveRoom();
            setVisible(false);
            dispose();
        });
    }

    private void setPrices(List<Section> sections, Stage stage, double min, double max){
        AlgoType type;
        if (seatBtn.isSelected()) {
            type = AlgoType.Seat;
        } else if (rowBtn.isSelected()) {
            type = AlgoType.Row;
        } else {
            type = AlgoType.Section;
        }
        PriceAlgo algo = PriceAlgoFactory.create(type);
        algo.extremeDistribution(sections, stage.getShape().computeCentroid(), min, max);
    }

    private boolean isValidForm() {
        if (
                isNotNumber(max.getText()) ||
                isNotNumber(min.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
