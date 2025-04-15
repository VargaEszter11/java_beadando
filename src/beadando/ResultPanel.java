package beadando;


import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class ResultPanel extends JPanel {
    private JLabel resultsLabel;

    public ResultPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Search Results"));
        
        resultsLabel = new JLabel("Results will appear here");
        resultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultsLabel, BorderLayout.CENTER);
    }

    public void displayResults(int matchCount, String resultsHtml) {
        if (matchCount > 0) {
            resultsLabel.setText("<html>Found " + matchCount + " matches:<br>" + resultsHtml + "</html>");
        } else {
            resultsLabel.setText("No matches found");
        }
    }

    public void showAllBooksMessage() {
        resultsLabel.setText("Showing all books");
    }
}