/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pck_customComponents;

import java.awt.Component;
import java.awt.FlowLayout;
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

    public AccionesRenderer() {
        setOpaque(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 4, 2));
        btnEditar.setFocusable(false);
        btnEliminar.setFocusable(false);
        btnEditar.setBorderPainted(false);
        btnEliminar.setBorderPainted(false);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnSolicitar.setEnabled(false);
        add(btnEditar);
        add(btnEliminar);
        add(btnSolicitar);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
    }
}
