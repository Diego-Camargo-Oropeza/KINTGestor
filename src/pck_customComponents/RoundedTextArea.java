/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pck_customComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextArea;

/**
 *
 * @author dieca
 */
public class RoundedTextArea extends JTextArea {

    private static final long serialVersionUID = 1L;

    // Propiedades editables desde el GUI Builder
    private int cornerRadius = 12;
    private Color backgroundColor = new Color(242, 242, 242); // gris claro
    private Color borderColor = new Color(204, 204, 204);     // gris borde
    private Color focusBorderColor = new Color(0, 178, 226);  // cian foco
    private int borderThickness = 1;
    private String placeholder = "";
    private Insets contentInsets = new Insets(8, 12, 8, 12);
    private boolean showPlaceholderWhenFocused = false;

    // ==== Constructores (necesario el no-args para GUI Builder) ====
    public RoundedTextArea() {
        super();
        initSafe();
    }

    public RoundedTextArea(int rows, int columns) {
        super(rows, columns);
        initSafe();
    }

    private void initSafe() {
        setOpaque(false);            // lo pintamos nosotros
        setBorder(null);             // sin borde swing
        setMargin(new Insets(0, 0, 0, 0));
        setLineWrap(true);           // típico en textareas modernas
        setWrapStyleWord(true);

        // Re-pintar cuando cambia el foco (borde y placeholder)
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                repaint();
            }
        });
    }

    // -------- Propiedades (get/set) --------
    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int v) {
        cornerRadius = Math.max(0, v);
        repaint();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color c) {
        backgroundColor = c;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color c) {
        borderColor = c;
        repaint();
    }

    public Color getFocusBorderColor() {
        return focusBorderColor;
    }

    public void setFocusBorderColor(Color c) {
        focusBorderColor = c;
        repaint();
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int v) {
        borderThickness = Math.max(0, v);
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String s) {
        placeholder = (s != null ? s : "");
        repaint();
    }

    public Insets getContentInsets() {
        return (Insets) contentInsets.clone();
    }

    public void setContentInsets(Insets i) {
        if (i != null) {
            contentInsets = (Insets) i.clone();
            revalidate();
            repaint();
        }
    }

    public boolean isShowPlaceholderWhenFocused() {
        return showPlaceholderWhenFocused;
    }

    public void setShowPlaceholderWhenFocused(boolean v) {
        showPlaceholderWhenFocused = v;
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

    // Fondo redondeado + placeholder
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
        } finally {
            g2.dispose();
        }

        // Texto/selección/caret
        super.paintComponent(g);

        // Placeholder (arriba-izquierda, respeta padding; soporta saltos de línea)
        if (shouldPaintPlaceholder()) {
            Graphics2D g2p = (Graphics2D) g.create();
            try {
                g2p.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2p.setColor(new Color(130, 130, 130));
                FontMetrics fm = g2p.getFontMetrics(getFont());

                int x = contentInsets.left + 1;
                int y = contentInsets.top + fm.getAscent();

                String[] lines = placeholder.split("\\R"); // soporta \n
                for (String line : lines) {
                    g2p.drawString(line, x, y);
                    y += fm.getHeight();
                    if (y > getHeight() - contentInsets.bottom) {
                        break; // evita salir del control
                    }
                }
            } finally {
                g2p.dispose();
            }
        }
    }

    // Borde redondeado (cambia con foco)
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
