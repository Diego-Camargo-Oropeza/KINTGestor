package pck_customComponents;

import java.awt.*;
import javax.swing.*;

public class RoundedPanel extends JPanel {

    private int radius = 16;
    private Color bg = Color.WHITE;

    public RoundedPanel() {
        setOpaque(false);
    }

    public RoundedPanel(int radius, Color bg) {
        this();
        this.radius = radius;
        this.bg = bg;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int r) {
        this.radius = r;
        repaint();
    }

    public Color getBackgroundColor() {
        return bg;
    }

    public void setBackgroundColor(Color c) {
        this.bg = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}
