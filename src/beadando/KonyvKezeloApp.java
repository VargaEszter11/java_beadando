package beadando;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class KonyvKezeloApp {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JButton addButton;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel searchLabel;
    private ResultPanel resultPanel;
    private JButton randomButton;

    public KonyvKezeloApp() {
        frame = new JFrame("Könyvkezelő alkalmazás");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);
        resultPanel = new ResultPanel();
        frame.getContentPane().add(resultPanel, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{"ID", "Cím", "Szerző", "Oldalszám", "Kiadás dátuma", "ISBN", "Kategória", "Törlés", ""}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) {
                    return Boolean.class; 
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8; 
            }
        };

        frame.getContentPane().setLayout(null);
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 50, 900, 259);
        frame.getContentPane().add(scrollPane);

        searchLabel = new JLabel("Keresés:");
        searchLabel.setBounds(10, 10, 87, 25);
        frame.getContentPane().add(searchLabel);
        
        searchField = new JTextField();
        searchField.setBounds(78, 10, 93, 25);
        frame.getContentPane().add(searchField);
        searchField.addActionListener(e -> {
            searchBooks();
            searchField.setText(""); 
        });
        
        searchButton = new JButton("Keresés");
        searchButton.setBounds(173, 10, 100, 25);
        searchButton.addActionListener(e -> searchBooks());
        frame.getContentPane().add(searchButton);

        addButton = new JButton("Hozzáadás");
        addButton.setBounds(716, 10, 172, 25);
        addButton.addActionListener(e -> addData()); 
        frame.getContentPane().add(addButton);
        
        randomButton = new JButton("Mit olvassak legközelebb?");
        randomButton.setBounds(484, 10, 220, 25);
        randomButton.addActionListener(e -> selectRandomBook());
        frame.getContentPane().add(randomButton);
        
        JButton btnRendezsCmSzerint = new JButton("Rendezés cím szerint");
        btnRendezsCmSzerint.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		sortBooksByTitle();
        	}
        });
        btnRendezsCmSzerint.setBounds(285, 10, 187, 25);
        frame.getContentPane().add(btnRendezsCmSzerint);
        
        JButton btnTrls = new JButton("Törlés");
        btnTrls.setBounds(771, 321, 117, 25);
        btnTrls.addActionListener(e -> deleteData(-1)); 
        frame.getContentPane().add(btnTrls);


        frame.getContentPane().add(btnTrls);
        
        table.getColumnModel().getColumn(7).setCellRenderer(new CheckBoxRenderer()); 
        table.getColumnModel().getColumn(7).setCellEditor(new CheckBoxEditor(new JCheckBox())); 

        table.getColumnModel().getColumn(8).setCellRenderer(new EditButtonRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox()));

        frame.setVisible(true);
        loadFromFile();
    }
    
    public DefaultTableModel getTableModel() {
        return this.model;
    }
    
    public void loadFromFile() {
        File file = new File("books.txt");
        if (!file.exists()) {
            return;
        }

        model.setRowCount(0); 

        int lastId = 0; 

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                
                if (parts.length == 7) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String title = parts[1];  
                        String author = parts[2];  
                        String pages = parts[3];  
                        String date = parts[4];  
                        String isbn = parts[5];  
                        String category = parts[6]; 

                        model.addRow(new Object[]{id, title, author, pages, date, isbn, category, false, "Módosítás"});

                        lastId = Math.max(lastId, id); 
                    } catch (NumberFormatException e) {
                        System.out.println("Hibás ID formátum a fájlban: " + parts[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Hiba történt a fájl beolvasása közben: " + e.getMessage());
        }
    }

    private void addData() {
        new AddBookDialog(frame, model);
    }
    
    private void editData(int rowId) {
        new EditBookDialog(frame, rowId, model);
    }

    private void deleteData(int rowId) {
    	for (int i = model.getRowCount() - 1; i >= 0; i--) {
            Object value = model.getValueAt(i, 7);
            if (Boolean.TRUE.equals(value)) {
                model.removeRow(i);
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

    private void searchBooks() {
        String searchText = searchField.getText().trim().toLowerCase();
        
        if (searchText.isEmpty()) {
            table.setRowHeight(30);
            resultPanel.showAllBooksMessage();
            return;
        }

        int matchCount = 0;
        StringBuilder resultsHtml = new StringBuilder("<html><table border='1'><tr>");
        
        for (int j = 0; j < model.getColumnCount() - 2; j++) {
            resultsHtml.append("<th>").append(model.getColumnName(j)).append("</th>");
        }
        resultsHtml.append("</tr>");
        
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean match = false;
            StringBuilder rowData = new StringBuilder("<tr>");
            
            for (int j = 0; j < model.getColumnCount() - 2; j++) {
                Object valueObj = model.getValueAt(i, j);
                String value = (valueObj != null) ? valueObj.toString() : "";
                rowData.append("<td>").append(value).append("</td>");
                
                if (value.toLowerCase().contains(searchText)) {
                    match = true;
                }
            }
            rowData.append("</tr>");
            
            if (!match) {
                table.setRowHeight(i, 1);
            } else {
                table.setRowHeight(i, 30); 
                matchCount++;
                resultsHtml.append(rowData);
            }
        }
        
        resultsHtml.append("</table></html>");
        
        if (matchCount > 0) {
            resultPanel.displayResults(matchCount, resultsHtml.toString());
        } else {
            resultPanel.displayResults(0, "No books found matching '" + searchText + "'");
        }
    }
    
    private void selectRandomBook() {
        if (model.getRowCount() == 0) {
            resultPanel.displayResults(0, "Nincsenek könyvek a listában");
            return;
        }

        Random rand = new Random();
        int randomRow = rand.nextInt(model.getRowCount());
        
        table.setRowSelectionInterval(randomRow, randomRow);
        table.scrollRectToVisible(table.getCellRect(randomRow, 0, true));
        
        int id = (int) model.getValueAt(randomRow, 0);
        String title = (String) model.getValueAt(randomRow, 1);
        String author = (String) model.getValueAt(randomRow, 2);
        
        resultPanel.displayResults(1, 
            "<html><b>Ezt olvasd:</b><br>" +
            "ID: " + id + "<br>" +
            "Cím: " + title + "<br>" +
            "Szerző: " + author + "</html>");
    }
    
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isPushed;
        private int rowId;
        private int column;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            rowId = (Integer) model.getValueAt(row, 0); 
            this.column = column;

            if (column == 8) {
                button.setText("Módosítás");
            } 

            isPushed = true;

            button.addActionListener(e -> {
                if (isPushed) {
                    if (column == 8) {
                        editData(rowId); 
                    }
                    isPushed = false;
                }
            });

            return button;
        }

        public Object getCellEditorValue() {
            isPushed = false;
            return "Művelet";
        }
    }
    
    class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	setSelected(Boolean.TRUE.equals(value));
            return this;
        }
    }

    class CheckBoxEditor extends DefaultCellEditor {
        public CheckBoxEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JCheckBox checkBox = (JCheckBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            checkBox.setSelected(Boolean.TRUE.equals(value));
            return checkBox;
        }

        public Object getCellEditorValue() {
            return Boolean.valueOf(((JCheckBox) getComponent()).isSelected());
        }
    }
    
    private void sortBooksByTitle() {
        int rowCount = model.getRowCount();
        if (rowCount <= 1) {
            return; 
        }

        List<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Object[] row = new Object[model.getColumnCount()];
            for (int j = 0; j < model.getColumnCount(); j++) {
                row[j] = model.getValueAt(i, j);
            }
            rows.add(row);
        }

        Collections.sort(rows, new Comparator<Object[]>() {
            public int compare(Object[] row1, Object[] row2) {
                String title1 = (String) row1[1]; 
                String title2 = (String) row2[1]; 
                return title1.compareTo(title2);
            }
        });

        model.setRowCount(0);
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }
  }

