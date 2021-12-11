import io.minio.MinioClient;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

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
        // add your code here
        try {
            MinioClient minioClient = MinIO.connection();
        } catch (IOException e) {
            ErrorDialog.showError();
            e.printStackTrace();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void setMessage() {
        // add your code here if necessary
        message.setText("Cant connect to the Cloud");
        dispose();
    }

    public static void showError() {
        ErrorDialog dialog = new ErrorDialog();
        dialog.setMessage();
        dialog.pack();
        dialog.setVisible(true);
    }


}
