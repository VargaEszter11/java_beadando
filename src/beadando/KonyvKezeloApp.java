package beadando;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

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
        frame.add(resultPanel, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{"ID", "Cím", "Szerző", "Oldalszám", "Kiadás dátuma", "ISBN", "Kategória", "", ""}, 0);

        frame.getContentPane().setLayout(null);
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 50, 900, 313);
        frame.getContentPane().add(scrollPane);

        searchLabel = new JLabel("Keresés:");
        searchLabel.setBounds(10, 10, 60, 25);
        frame.getContentPane().add(searchLabel);
        
        searchField = new JTextField();
        searchField.setBounds(70, 10, 200, 25);
        frame.getContentPane().add(searchField);
        searchField.addActionListener(e -> {
            searchBooks();
            searchField.setText(""); 
        });
        
        searchButton = new JButton("Keresés");
        searchButton.setBounds(280, 10, 100, 25);
        searchButton.addActionListener(e -> searchBooks());
        frame.getContentPane().add(searchButton);

        addButton = new JButton("Hozzáadás");
        addButton.setBounds(800, 10, 100, 25);
        addButton.addActionListener(e -> addData()); 
        frame.getContentPane().add(addButton);
        
        randomButton = new JButton("Mit olvassak legközelebb?");
        randomButton.setBounds(650, 10, 140, 25);
        randomButton.addActionListener(e -> selectRandomBook());
        frame.getContentPane().add(randomButton);

        table.getColumnModel().getColumn(7).setCellRenderer(new EditButtonRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(new DeleteButtonRenderer());
       
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(table, model, frame));
        table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(table, model, frame));

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

                        model.addRow(new Object[]{id, title, author, pages, date, isbn, category});

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
  }

