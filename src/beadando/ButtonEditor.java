package beadando;


import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ButtonEditor extends DefaultCellEditor {
    private final JButton button;
    private int currentRow;
    private final JTable table;
    private final DefaultTableModel model;
    private final JFrame parentFrame;  

    public ButtonEditor(JTable table, DefaultTableModel model, JFrame parentFrame) {
        super(new JCheckBox());
        this.table = table;
        this.model = model;
        this.parentFrame = parentFrame;  
        
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            fireEditingStopped();
            SwingUtilities.invokeLater(this::performAction);
        });
    }

	    private void performAction() {
	        int modelRow = table.convertRowIndexToModel(currentRow);
	        int column = table.getEditingColumn();
	        int rowId = (Integer) model.getValueAt(modelRow, 0);
	        
	        if (column == 7) {
	            editData(rowId);
	        } else if (column == 8) {
	            deleteData(rowId);
	        }
	    }

 @Override
 public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
     currentRow = row;
     if (column == 7) {
         button.setText("Módosítás");
     } else if (column == 8) {
         button.setText("Törlés");
     }
     return button;
 }

 @Override
 public Object getCellEditorValue() {
     return "";
 }
 

public void editData(int rowId) {
	System.out.println("Edit");
    new EditBookDialog(parentFrame, rowId, model);
}

public void deleteData(int rowId) {
	System.out.println("Delete");
    for (int i = 0; i < model.getRowCount(); i++) {
        if ((int) model.getValueAt(i, 0) == rowId) {
            model.removeRow(i);
            break;
        }
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
        for (int i = 0; i < model.getRowCount(); i++) {
            writer.write(model.getValueAt(i, 0) + ", " +
                    model.getValueAt(i, 1) + ", " +
                    model.getValueAt(i, 2) + ", " +
                    model.getValueAt(i, 3) + ", " +
                    model.getValueAt(i, 4) + ", " +
                    model.getValueAt(i, 5) + ", " +
                    model.getValueAt(i, 6) + "\n"); 
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}