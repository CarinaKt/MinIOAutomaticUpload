import javax.swing.JOptionPane;

public class Dialogs{
    public static void main(String[] bla){

        // Aufruf der statischen Methode showConfirmDialog()
        int eingabe = JOptionPane.showConfirmDialog(null,
                "Geben Sie Ihr Einverständnis?",
                "Einverständnis",
                JOptionPane.YES_NO_CANCEL_OPTION);
        System.out.println(eingabe);
        System.exit(0);

    }
}
