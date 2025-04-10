package beadando;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FileChooser extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DbKonyvKezelo dbkonyv = new DbKonyvKezelo();


	public FileChooser() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnBookstxt = new JButton("books.txt");
		btnBookstxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new KonyvKezeloApp();
				dispose();
			}
		});
		btnBookstxt.setBounds(70, 70, 101, 25);
		contentPane.add(btnBookstxt);
		
		JLabel lblCim = new JLabel("Válassz fájlt, amiből beolvasnád az adatokat");
		lblCim.setBounds(70, 12, 319, 15);
		contentPane.add(lblCim);
		
		JButton btnBooksdb = new JButton("booksdb");
		btnBooksdb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbkonyv.registration();
				dbkonyv.connect();
				dbkonyv.disconnect();
				dispose();
			}
		});
		btnBooksdb.setBounds(272, 70, 117, 25);
		contentPane.add(btnBooksdb);
		
		setVisible(true);
	}
}
