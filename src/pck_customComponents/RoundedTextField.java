/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pck_customComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author dieca
 */
public class RoundedTextField extends JTextField {

    private static final long serialVersionUID = 1L;

    // Propiedades editables desde el GUI Builder
    private int cornerRadius = 12;
    private Color backgroundColor = new Color(242, 242, 242); // gris claro
    private Color borderColor = new Color(204, 204, 204);     // gris borde
    private Color focusBorderColor = new Color(0, 178, 226);  // cian foco
    private int borderThickness = 1;
    private String placeholder = "";                          // texto guía
    private Insets contentInsets = new Insets(8, 12, 8, 12);  // padding interno
    private boolean showPlaceholderWhenFocused = false;

    public RoundedTextField() {
        super();
        initSafe();
    }

    public RoundedTextField(int columns) {
        super(columns);
        initSafe();
    }

    private void initSafe() {
        setOpaque(false);            // lo pintamos nosotros
        setBorder(null);             // evitamos el borde swing por defecto
        setMargin(new Insets(0, 0, 0, 0));
        // Si quisieras lógica especial solo en runtime:
        // if (!java.beans.Beans.isDesignTime()) { ... }
    }

    // -------- Propiedades (get/set) --------
    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = Math.max(0, cornerRadius);
        repaint();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public Color getFocusBorderColor() {
        return focusBorderColor;
    }

    public void setFocusBorderColor(Color focusBorderColor) {
        this.focusBorderColor = focusBorderColor;
        repaint();
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = Math.max(0, borderThickness);
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder != null ? placeholder : "";
        repaint();
    }

    public Insets getContentInsets() {
        return (Insets) contentInsets.clone();
    }

    public void setContentInsets(Insets contentInsets) {
        if (contentInsets != null) {
            this.contentInsets = (Insets) contentInsets.clone();
            revalidate();
            repaint();
        }
    }

    public boolean isShowPlaceholderWhenFocused() {
        return showPlaceholderWhenFocused;
    }

    public void setShowPlaceholderWhenFocused(boolean show) {
        this.showPlaceholderWhenFocused = show;
        repaint();
    }

    // Para que el texto empiece después del padding
    @Override
    public Insets getInsets() {
        Insets i = super.getInsets();
        return new Insets(
                i.top + contentInsets.top,
                i.left + contentInsets.left,
                i.bottom + contentInsets.bottom,
                i.right + contentInsets.right
        );
    }

    // Pintamos fondo redondeado
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = cornerRadius * 2;

            // Fondo
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);

            // Llamamos al pintado estándar del texto
            super.paintComponent(g);

            // Placeholder
            if (shouldPaintPlaceholder()) {
                FontMetrics fm = g2.getFontMetrics(getFont());
                int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
                int textX = contentInsets.left + 1;

                g2.setColor(new Color(130, 130, 130)); // gris placeholder
                g2.drawString(placeholder, textX, textY);
            }
        } finally {
            g2.dispose();
        }
    }

    // Pintamos borde (cambia de color con foco)
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = cornerRadius * 2;

            g2.setStroke(new BasicStroke(Math.max(1, borderThickness)));
            g2.setColor(hasFocus() ? focusBorderColor : borderColor);
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
        } finally {
            g2.dispose();
        }
    }

    private boolean shouldPaintPlaceholder() {
        if (placeholder == null || placeholder.isEmpty()) {
            return false;
        }
        if (getText() != null && !getText().isEmpty()) {
            return false;
        }
        if (hasFocus() && !showPlaceholderWhenFocused) {
            return false;
        }
        return true;
    }
}
