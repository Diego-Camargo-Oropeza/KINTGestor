/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pck_customComponents;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author dieca
 */
public class AccionesRenderer extends JPanel implements TableCellRenderer {

    private final JButton btnEditar = new JButton("✏️");
    private final JButton btnEliminar = new JButton("🗑️");
    private final JButton btnSolicitar = new JButton("❓");
    private final boolean showSolicitar;

    public AccionesRenderer() {
        this(true);
    }

    public AccionesRenderer(boolean showSolicitar) {
        this.showSolicitar = showSolicitar;
        setOpaque(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 4, 2));

        // Botones “dummy” para render: deshabilitados y sin foco
        for (JButton b : new JButton[]{btnEditar, btnEliminar, btnSolicitar}) {
            b.setFocusable(false);
            b.setBorderPainted(false);
            b.setEnabled(false);
            b.setMargin(new Insets(2, 6, 2, 6));
        }

        add(btnEditar);
        add(btnEliminar);
        if (showSolicitar) {
            add(btnSolicitar);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
    }
}
