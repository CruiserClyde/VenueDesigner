package app.gui;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;

import app.domain.UIPanel;

final class DrawingPanel extends JPanel implements UIPanel {
    private Dimension size;
    private final GUIPainter painter;

    DrawingPanel(GUIPainter painter) {
        this.painter = Objects.requireNonNull(painter);
        size=new Dimension(2000,2000);
        revalidate();
    }

    public void setPreferredSize(int x, int y){
        size=new Dimension(x,y);
    }
    public Dimension getPreferredSize() { return size;}

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.DARK_GRAY.darker().darker());
        g.fillRect(0,0,getWidth(),getHeight());
        painter.draw((Graphics2D) g, this);
        revalidate();
    }
}
