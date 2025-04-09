package beadando;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public class AddBookDialog extends JDialog {
    private JTextField titleField, authorField, pagesField, dateField, isbnField, categoryField;
    private JButton saveButton;
    private DefaultTableModel model;

    public AddBookDialog(JFrame parentFrame, DefaultTableModel model) {
        super(parentFrame, "Új könyv hozzáadása", true);
        this.model = model;

        setSize(400, 300);
        setLayout(new GridLayout(7, 2, 5, 5));

        titleField = new JTextField();
        authorField = new JTextField();
        pagesField = new JTextField();
        dateField = new JTextField();
        isbnField = new JTextField();
        categoryField = new JTextField();

        saveButton = new JButton("Mentés");
        saveButton.addActionListener(e -> saveNewBookData());

        // Enter billentyű figyelése minden mezőben
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveNewBookData();
                }
            }
        };

        titleField.addKeyListener(enterKeyAdapter);
        authorField.addKeyListener(enterKeyAdapter);
        pagesField.addKeyListener(enterKeyAdapter);
        dateField.addKeyListener(enterKeyAdapter);
        isbnField.addKeyListener(enterKeyAdapter);
        categoryField.addKeyListener(enterKeyAdapter);

        // Vagy alternatív megoldás: a dialógus gyökérpaneljének beállítása
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "saveAction");
        getRootPane().getActionMap().put("saveAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveNewBookData();
            }
        });

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
    }

    private void saveNewBookData() {
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

        model.addRow(new Object[]{model.getRowCount() + 1, title, author, pages, date, isbn, category});

        saveToFile();

        JOptionPane.showMessageDialog(this, "A könyv hozzáadása sikeres!", "Siker", JOptionPane.INFORMATION_MESSAGE);
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