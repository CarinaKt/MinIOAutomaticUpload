import javax.swing.*;
import java.awt.event.*;

public class ErrorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonRetry;
    private JButton buttonCancel;
    private JLabel message;

    public ErrorDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Error");
        getRootPane().setDefaultButton(buttonRetry);

        buttonRetry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRetry();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onRetry() {
        MinIO.setRetry(true);
        dispose();
    }

    private void onCancel() {
        MinIO.setRetry(false);
        dispose();
    }

    private void setMessage(String inputMessage) {
        // add the error message to the dialog
        message.setText(inputMessage);
        dispose();
    }

    public static void showError(String message) {
        ErrorDialog dialog = new ErrorDialog();
        dialog.setAlwaysOnTop(true);
        dialog.setMessage(message);
        dialog.pack();
        dialog.setVisible(true);
    }


}
