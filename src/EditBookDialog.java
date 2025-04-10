

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class EditBookDialog extends JDialog {
    private JTextField titleField, authorField, pagesField, dateField, isbnField, categoryField;
    private JButton saveButton;
    private int rowId;
    private DefaultTableModel model;
    
    public EditBookDialog(JFrame parentFrame, int rowId, DefaultTableModel model) {
        super(parentFrame, "Könyv módosítása", true);
        this.rowId = rowId;
        this.model = model;
        
        setSize(400, 300);
        setLayout(new GridLayout(7, 2, 5, 5));
        
        int selectedRow = -1;
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int) model.getValueAt(i, 0) == rowId) {
                selectedRow = i;
                break;
            }
        }

        if (selectedRow != -1) {
            String title = (String) model.getValueAt(selectedRow, 1);
            String author = (String) model.getValueAt(selectedRow, 2);
            String pages = (String) model.getValueAt(selectedRow, 3);
            String date = (String) model.getValueAt(selectedRow, 4);
            String isbn = (String) model.getValueAt(selectedRow, 5);
            String category = (String) model.getValueAt(selectedRow, 6);

            titleField = new JTextField(title);
            authorField = new JTextField(author);
            pagesField = new JTextField(pages);
            dateField = new JTextField(date);
            isbnField = new JTextField(isbn);
            categoryField = new JTextField(category);

            saveButton = new JButton("Mentés");
            saveButton.addActionListener(e -> saveEditedData());

            // Enter billentyű figyelése minden mezőben
            KeyAdapter enterKeyAdapter = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        saveEditedData();
                    }
                }
            };

            titleField.addKeyListener(enterKeyAdapter);
            authorField.addKeyListener(enterKeyAdapter);
            pagesField.addKeyListener(enterKeyAdapter);
            dateField.addKeyListener(enterKeyAdapter);
            isbnField.addKeyListener(enterKeyAdapter);
            categoryField.addKeyListener(enterKeyAdapter);
            
            add(new JLabel("Cím:"));
            add(titleField);
            add(new JLabel("Szerző:"));
            add(authorField);
            add(new JLabel("Oldalszám:"));
            add(pagesField);
            add(new JLabel("Kiadás Dátuma:"));
            add(dateField);
            add(new JLabel("ISBN:"));
            add(isbnField);
            add(new JLabel("Kategória:"));
            add(categoryField);

            add(new JLabel()); 
            add(saveButton);

            setLocationRelativeTo(parentFrame);
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "A kiválasztott könyv nem található!", "Hiba", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void saveEditedData() {
        String title = titleField.getText();
        String author = authorField.getText();
        String pages = pagesField.getText();
        String date = dateField.getText();
        String isbn = isbnField.getText();
        String category = categoryField.getText();

        if (title.isEmpty() || author.isEmpty() || pages.isEmpty() || date.isEmpty() || isbn.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Minden mezőt ki kell tölteni!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Integer.parseInt(pages); 
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Az oldalszámnak számnak kell lennie!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(date);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "A dátumnak a következő formátumban kell lennie: yyyy-MM-dd!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isbn.matches("\\d{13}")) {
            JOptionPane.showMessageDialog(this, "Az ISBN formátumának 13 számjegyűnek kell lennie!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = -1;
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int) model.getValueAt(i, 0) == rowId) {
                selectedRow = i;
                break;
            }
        }

        if (selectedRow != -1) {
            model.setValueAt(title, selectedRow, 1);
            model.setValueAt(author, selectedRow, 2);
            model.setValueAt(pages, selectedRow, 3);
            model.setValueAt(date, selectedRow, 4);
            model.setValueAt(isbn, selectedRow, 5);
            model.setValueAt(category, selectedRow, 6); 

            saveToFile();

            JOptionPane.showMessageDialog(this, "A könyv adatainak módosítása sikeres!", "Siker", JOptionPane.INFORMATION_MESSAGE);
        }

        dispose();
    }

    private void saveToFile() {
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