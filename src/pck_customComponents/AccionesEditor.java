package pck_customComponents;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class AccionesEditor extends AbstractCellEditor implements TableCellEditor {

    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
    private final JButton btnEditar = new JButton("✏️");
    private final JButton btnEliminar = new JButton("🗑️");
    private final JButton btnSolicitar = new JButton("❓");

    private final JTable table;
    private final AccionesHandler handler;
    private final int idColumnIndex;   
    private final boolean showSolicitar;

    public AccionesEditor(JTable table, int idColumnIndex, AccionesHandler handler, boolean showSolicitar) {
        this.table = table;
        this.handler = handler;
        this.idColumnIndex = idColumnIndex;
        this.showSolicitar = showSolicitar;

        btnEditar.setFocusable(false);
        btnEliminar.setFocusable(false);
        btnSolicitar.setFocusable(false);
        btnSolicitar.setVisible(showSolicitar);

        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnSolicitar);

        btnEditar.addActionListener(e -> {
            int viewRow = table.getEditingRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Integer id = safeGetId(modelRow);
                if (id != null) handler.editar(id, modelRow);
            }
            stopCellEditing();
        });

        btnEliminar.addActionListener(e -> {
            int viewRow = table.getEditingRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Integer id = safeGetId(modelRow);
                if (id != null) handler.eliminar(id, modelRow);
            }
            stopCellEditing();
        });

        btnSolicitar.addActionListener(e -> {
            int viewRow = table.getEditingRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Integer id = safeGetId(modelRow);
                if (id != null) handler.solicitar(id, modelRow);
            }
            stopCellEditing();
        });
    }

    private Integer safeGetId(int modelRow) {
        try {
            Object val = table.getModel().getValueAt(modelRow, idColumnIndex);
            if (val == null) return null;
            if (val instanceof Number) return ((Number) val).intValue();
            String s = String.valueOf(val).trim();
            if (s.isEmpty() || s.equals("—") || s.equalsIgnoreCase("null")) return null;
            return Integer.parseInt(s);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() { return null; }
}
