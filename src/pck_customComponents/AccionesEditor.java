package pck_customComponents;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
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

    public AccionesEditor(JTable table, int idColumnIndex, AccionesHandler handler) {
        this(table, idColumnIndex, handler, true);
    }

    public AccionesEditor(JTable table, int idColumnIndex, AccionesHandler handler, boolean showSolicitar) {
        this.table = table;
        this.idColumnIndex = idColumnIndex;
        this.handler = handler;
        this.showSolicitar = showSolicitar;

        for (JButton b : new JButton[]{btnEditar, btnEliminar, btnSolicitar}) {
            b.setFocusable(false);
            b.setMargin(new Insets(2, 6, 2, 6));
        }

        panel.add(btnEditar);
        panel.add(btnEliminar);
        if (showSolicitar) {
            panel.add(btnSolicitar);
        }

        btnEditar.addActionListener(e -> invokeHandler(ActionType.EDITAR));
        btnEliminar.addActionListener(e -> invokeHandler(ActionType.ELIMINAR));
        btnSolicitar.addActionListener(e -> invokeHandler(ActionType.SOLICITAR));
    }

    private enum ActionType {
        EDITAR, ELIMINAR, SOLICITAR
    }

    private void invokeHandler(ActionType type) {
        int viewRow = table.getEditingRow();
        try {
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Object idVal = table.getModel().getValueAt(modelRow, idColumnIndex);

                int id = (idVal instanceof Number)
                        ? ((Number) idVal).intValue()
                        : Integer.parseInt(String.valueOf(idVal));

                switch (type) {
                    case EDITAR ->
                        handler.editar(id, modelRow);
                    case ELIMINAR ->
                        handler.eliminar(id, modelRow);
                    case SOLICITAR ->
                        handler.solicitar(id, modelRow);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        } finally {
            stopCellEditing();
        }
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
