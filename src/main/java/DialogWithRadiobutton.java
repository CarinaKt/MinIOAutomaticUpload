import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"),"\\AppData\\MinIO");

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

            try {

                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                    System.out.println("Directory created");
                    logger.info("Directory created" + path);
                }

                File myObj = new File( path.toString()+"\\uploadConfig.ini");
                if (myObj.createNewFile()) {
                    logger.info("File created: " + myObj.getName());

                    FileWriter myWriter = new FileWriter(myObj);
                    directory = directory.replace("\\", "\\\\");
                    System.out.println(directory);
                    myWriter.write("path=" + directory + "\n");
                    myWriter.write("removeExportedData=" + removeFromLocalRadioButton.isSelected() + "\n");
                    myWriter.write("uploadExistingData=" + uploadToTheCloudRadioButton.isSelected() + "\n");
                    myWriter.close();

                } else {
                    logger.info("File already exists.");
                }
            } catch (IOException e) {
                logger.warning("An error occurred." + e);
                e.printStackTrace();
            }

        }else {
            logger.warning("no existing folder");
            // TODO: Ask to create the Folder/ change the entry
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        logger.info("Close Dialog");
    }

    public static void loadDialog() {
        // Only Open if ini.txt doesnt exists
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"),"\\AppData\\MinIO\\uploadConfig.ini");
        if (!Files.exists(path)) {
            DialogWithRadiobutton myDialog = new DialogWithRadiobutton();
            myDialog.createUIComponents();
            myDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            myDialog.pack();
            myDialog.setVisible(true);
        } else {
            Logger.getAnonymousLogger().info("File already exists: " + path);
            System.exit(0);
        }
    }

    private void createUIComponents() {
        // set default path as input
        sourceFolderPathTextField.setText(FileSystems.getDefault().getPath(System.getProperty("user.home"),
                "\\FolderToTrack").toString());

        // Radiobutton "exported Data"
        removeFromLocalRadioButton.setSelected(true);
        keepLocalTooRadioButton.setSelected(false);

        // Radiobutton "existing Data"
        nothingRadioButton.setSelected(true);
        uploadToTheCloudRadioButton.setSelected(false);

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

        uploadToTheCloudRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(uploadToTheCloudRadioButton.isSelected()){
                    nothingRadioButton.setSelected(false);
                }else{
                    nothingRadioButton.setSelected(true);
                }
                logger.info("uploadToTheCloudRadioButton.isSelected(): " + uploadToTheCloudRadioButton.isSelected()
                        + "\n" + "nothingRadioButton: " + nothingRadioButton.isSelected());
            }
        });
        nothingRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nothingRadioButton.isSelected()) {
                    uploadToTheCloudRadioButton.setSelected(false);
                } else {
                    uploadToTheCloudRadioButton.setSelected(true);
                }
                logger.info("nothingRadioButton.isSelected(): " + nothingRadioButton.isSelected()
                        + "\n" + "uploadToTheCloudRadioButton: " + uploadToTheCloudRadioButton.isSelected());
            }
        });

    }

}
