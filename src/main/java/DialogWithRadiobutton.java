import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogWithRadiobutton extends JDialog {
    private JPanel DialogWithButton;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton removeFromLocalRadioButton;
    private JRadioButton keepLocalTooRadioButton;
    private JTextField cUserCarinaTextField;
    private JRadioButton nothingRadioButton;
    private JRadioButton uploadToTheCloudRadioButton;
    private JPanel sourceFolder;
    private JPanel Button;
    private JPanel AdditionalOptions;

    public DialogWithRadiobutton() {
        setContentPane(DialogWithButton);
        setTitle("Edit options");
        getRootPane().setDefaultButton(buttonOK);
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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
        DialogWithButton.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DialogWithRadiobutton myDialog = new DialogWithRadiobutton();
        myDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        myDialog.pack();
        myDialog.setVisible(true);
        //System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
