package pck_customBtn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class CustomButton extends JButton {

    private boolean over;
    private Color color;
    private Color colorOver;
    private Color colorClick;
    private Color borderColor;
    private Color borderColorOver;
    private Color fontColorOver;

    private int radius;

    public CustomButton() {
        //Init color
        this.radius = 0;
        setColor(Color.WHITE);
        setBorderColor(borderColor);
        colorOver = new Color(220, 231, 239);
        colorClick = new Color(60, 106, 169);
        borderColor = new Color(64, 64, 64);
        borderColorOver = new Color(162, 186, 214);
        fontColorOver = new Color(38, 89, 159);
        setContentAreaFilled(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                setBackground(colorOver);
                setForeground(fontColorOver);
                setBorderColor(borderColorOver);
                over = true;
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(color);
                setForeground(Color.BLACK);
                setBorderColor(new Color(64, 64, 64));
                over = false;
            }

            @Override
            public void mousePressed(MouseEvent me) {
                setBackground(colorClick);
            }

            @Override
            public void mouseReleased(MouseEvent me) {

                if (over) {
                    setBackground(colorOver);
                } else {
                    setBackground(color);
                }

            }
        });
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public Color getBorderColorOver() {
        return borderColorOver;
    }

    public void setBorderColorOver(Color borderColorOver) {
        this.borderColorOver = borderColorOver;
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

    public Color getFontColorOver() {
        return fontColorOver;
    }

    public void setFontColorOver(Color fontColorOver) {
        this.fontColorOver = fontColorOver;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Pintar borde
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());
        //Borde de 2px
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        super.paintComponent(graphics);
    }
}
