package beadando;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class EditButtonRenderer extends JButton implements TableCellRenderer {
    public EditButtonRenderer() {
        setText("Módosítás");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        return this;
    }
}
