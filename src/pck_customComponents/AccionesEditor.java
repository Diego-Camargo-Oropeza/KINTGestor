/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pck_customComponents;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author dieca
 */
public class AccionesEditor extends AbstractCellEditor implements TableCellEditor {

    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
    private final JButton btnEditar = new JButton("✏️");
    private final JButton btnEliminar = new JButton("🗑️");
    private final JButton btnSolicitar = new JButton("❓");
    private final JTable table;
    private final AccionesHandler handler;

    public AccionesEditor(JTable table, AccionesHandler handler) {
        this.table = table;
        this.handler = handler;

        btnEditar.setFocusable(false);
        btnEliminar.setFocusable(false);
        btnSolicitar.setFocusable(false);

        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnSolicitar);

        btnEditar.addActionListener(e -> {
            int viewRow = table.getEditingRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Object idVal = table.getModel().getValueAt(modelRow, 0); // col 0 = ID
                if (idVal instanceof Number) {
                    handler.editar(((Number) idVal).intValue(), modelRow);
                }
            }
            stopCellEditing();
        });

        btnEliminar.addActionListener(e -> {
            int viewRow = table.getEditingRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Object idVal = table.getModel().getValueAt(modelRow, 0); // col 0 = ID
                if (idVal instanceof Number) {
                    handler.eliminar(((Number) idVal).intValue(), modelRow);
                }
            }
            stopCellEditing();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
