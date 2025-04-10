

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

public class FavoriteEditor extends DefaultCellEditor {
    public FavoriteEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    @Override
    public Object getCellEditorValue() {
        Boolean selected = (Boolean) super.getCellEditorValue();
        // Auto-save logic would go here
        return selected;
    }
}