import javax.swing.*;
import java.awt.event.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class DialogWithRadiobutton extends JDialog {
    private JPanel DialogWithButton;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton removeFromLocalRadioButton;
    private JRadioButton keepLocalTooRadioButton;
    private JTextField sourceFolderPathTextField;
    private JRadioButton nothingRadioButton;
    private JRadioButton uploadToTheCloudRadioButton;
    private JPanel sourceFolder;
    private JPanel Button;
    private JPanel AdditionalOptions;

    // Logging
    Logger logger =  Logger.getLogger(this.getClass().getName());
    //Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"),"\\AppData\\MinIO");

    public DialogWithRadiobutton() {
        setContentPane(DialogWithButton);
        setTitle("Edit Options");
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
        // prof the path
        String directory = sourceFolderPathTextField.getText();

        if(Files.isDirectory(Path.of(directory))){
           logger.info("Directory exists!");
            SaveConfigurations.setProperty(directory, removeFromLocalRadioButton.isSelected());

        }else {
            logger.warning("no existing folder");
            DialogWithRadiobutton.loadDialog();
        }

        dispose();
        // new WatchServiceRecursive().watcher();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        logger.info("Close Dialog");
        System.exit(0);
    }

    public static void loadDialog() {
        // Only Open if ini.txt doesnt exists
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"),"\\AppData\\MinIO\\uploadConfig.ini");
        if (!Files.exists(path)) {
            DialogWithRadiobutton myDialog = new DialogWithRadiobutton();
            myDialog.setModalityType(ModalityType.APPLICATION_MODAL);
            myDialog.setAlwaysOnTop(true);
            myDialog.createUIComponents();
            myDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            myDialog.pack();
            myDialog.setVisible(true);
        } else {
            Logger.getAnonymousLogger().info("File already exists: " + path);
            //TODO new WatchServiceRecursive().watcher();
        }
    }

    private void createUIComponents() {
        // set default path as input
        sourceFolderPathTextField.setText(FileSystems.getDefault().getPath(System.getProperty("user.home"),
                "\\FolderToTrack").toString());

        // Radiobutton "exported Data"
        removeFromLocalRadioButton.setSelected(true);
        keepLocalTooRadioButton.setSelected(false);

        // Listener make sure that only one Radiobutton can be selected
        removeFromLocalRadioButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 if(removeFromLocalRadioButton.isSelected()){
                     keepLocalTooRadioButton.setSelected(false);
                 }else{
                     keepLocalTooRadioButton.setSelected(true);
                 }
                 logger.info("removeFromLocalRadioButton.isSelected(): " + removeFromLocalRadioButton.isSelected()
                         + "\n" + "keepLocalTooRadioButton: " + keepLocalTooRadioButton.isSelected());
             }
         });
        keepLocalTooRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(keepLocalTooRadioButton.isSelected()){
                    removeFromLocalRadioButton.setSelected(false);
                }else{
                    removeFromLocalRadioButton.setSelected(true);
                }
                logger.info("keepLocalTooRadioButton.isSelected(): " + keepLocalTooRadioButton.isSelected()
                        + "\n" + "removeFromLocalRadioButton: " + removeFromLocalRadioButton.isSelected());
            }
        });


    }

}
