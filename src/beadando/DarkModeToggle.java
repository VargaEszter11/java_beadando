package beadando;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class DarkModeToggle {
    private boolean darkMode = false;
    private final JFrame mainFrame;
    private final JTable table;
    private final DefaultTableModel model;
    
    private final Color lightBackground = new Color(240, 240, 240);
    private final Color lightForeground = Color.BLACK;
    private final Color darkBackground = new Color(60, 63, 65);
    private final Color darkForeground = new Color(187, 187, 187);
    private final Color darkSelection = new Color(75, 110, 175);
    
    public DarkModeToggle(JFrame frame, JTable table, DefaultTableModel model) {
        this.mainFrame = frame;
        this.table = table;
        this.model = model;
    }
    
    public JCheckBox createDarkModeCheckBox() {
        JCheckBox darkModeCheckBox = new JCheckBox("Témaváltás");
        darkModeCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        darkModeCheckBox.setOpaque(false);
        
        darkModeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleDarkMode(darkModeCheckBox.isSelected());
            }
        });
        
        return darkModeCheckBox;
    }
    
    private void toggleDarkMode(boolean enableDarkMode) {
        this.darkMode = enableDarkMode;
        
        mainFrame.getContentPane().setBackground(darkMode ? darkBackground : lightBackground);
        
        table.setBackground(darkMode ? darkBackground : lightBackground);
        table.setForeground(darkMode ? darkForeground : lightForeground);
        table.setSelectionBackground(darkMode ? darkSelection : UIManager.getColor("Table.selectionBackground"));
        table.setGridColor(darkMode ? new Color(100, 100, 100) : new Color(200, 200, 200));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(darkMode ? new Color(80, 80, 80) : UIManager.getColor("TableHeader.background"));
        header.setForeground(darkMode ? Color.WHITE : UIManager.getColor("TableHeader.foreground"));
        
        updateComponentTreeUI(mainFrame.getContentPane());
    }
    
    private void updateComponentTreeUI(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                updateComponentTreeUI((Container) comp);
            }
            
            if (comp instanceof JLabel || comp instanceof JButton || comp instanceof JCheckBox) {
                comp.setBackground(darkMode ? darkBackground : lightBackground);
                comp.setForeground(darkMode ? darkForeground : lightForeground);
                
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    button.setContentAreaFilled(!darkMode);
                    button.setBorderPainted(!darkMode);
                    button.setOpaque(darkMode);
                }
                
                if (comp instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) comp;
                    checkBox.setOpaque(false);
                }
            }
            
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                textField.setBackground(darkMode ? new Color(80, 80, 80) : Color.WHITE);
                textField.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                textField.setCaretColor(darkMode ? Color.WHITE : Color.BLACK);
            }
        }
    }
}
