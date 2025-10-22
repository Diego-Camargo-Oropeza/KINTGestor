package pck_customComponents;

import java.awt.*;
import javax.swing.*;

public class PillLabel extends JLabel {

    private Color pillColor = new Color(0, 178, 226);
    private Color textColor = Color.WHITE;

    public PillLabel() {
        setOpaque(false);
        setHorizontalAlignment(CENTER);
        setFont(getFont().deriveFont(Font.BOLD));
        setText("Active");
        setPreferredSize(new Dimension(64, 22));
    }

    public void setPillColor(Color c) {
        this.pillColor = c;
        repaint();
    }

    public void setTextColor(Color c) {
        this.textColor = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = getHeight();
        g2.setColor(pillColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // texto
        FontMetrics fm = g2.getFontMetrics(getFont());
        int textW = fm.stringWidth(getText());
        int textX = (getWidth() - textW) / 2;
        int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(textColor);
        g2.setFont(getFont());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
