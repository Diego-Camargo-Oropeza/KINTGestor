package pck_customBtn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class CustomButton extends JButton {

    private boolean over;
    private Color color;
    private Color colorOver;
    private Color colorClick;
    private Color borderColor;
    private int radius;

    public CustomButton() {
        //Init color
        setColor(Color.WHITE);
        colorOver = new Color(220, 231, 239);
        setContentAreaFilled(false);
        this.radius = 0;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        setBackground(color);
    }

    public Color getColorOver() {
        return colorOver;
    }

    public void setColorOver(Color colorOver) {
        this.colorOver = colorOver;
    }

    public Color getColorClick() {
        return colorClick;
    }

    public void setColorClick(Color colorClick) {
        this.colorClick = colorClick;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Pintar borde
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        //Borde de 2px
        g2.setColor(getBackground());
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        super.paintComponent(graphics);
    }
}
