package pck_table;

import pck_model.PrestamoRow;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class PrestamoTableModel extends AbstractTableModel {

    private final String[] cols = {"Folio", "Producto", "Cantidad", "Estado", "Prioridad", "Creado"};
    private final List<PrestamoRow> data;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public PrestamoTableModel(List<PrestamoRow> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int c) {
        return cols[c];
    }

    @Override
    public Object getValueAt(int row, int col) {
        PrestamoRow r = data.get(row);
        switch (col) {
            case 0:
                return r.getFolio();
            case 1:
                return r.getProducto();
            case 2:
                return r.getCantidad();
            case 3:
                return r.getEstado();
            case 4:
                return r.getPrioridad();
            case 5:
                return r.getFechaCreacion() == null ? "" : sdf.format(r.getFechaCreacion());
        }
        return null;
    }

    public PrestamoRow getRow(int row) {
        return data.get(row);
    }
}
