package beadando;

import javax.swing.SwingUtilities;

public class KonyvKezeloMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login(null);

            if (login.isSuccessfulLogin()) {
                new FileChooser();
            } else {
                System.exit(0); 
            }
        });
    }
}
