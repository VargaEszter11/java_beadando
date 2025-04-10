package beadando;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DeleteButtonRenderer extends JButton implements TableCellRenderer {
    public DeleteButtonRenderer() {
        setText("Törlés");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        return this;
    }
}
