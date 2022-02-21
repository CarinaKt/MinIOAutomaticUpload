import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

public class SaveConfigurations {

    // Logging
    static Logger logger = Logger.getLogger("SaveConfigurations");

    private static final Path path =
            FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini");
    private static final Path sourcePath =
            FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO");


    /**
     * Reads in the Directory Path from the .ini file in
     * C:/User/Name/AppData/MinIo
     *
     * @param key- the property to read out of the file
     *             "path": string path of Folder to track and upload
     *             "removeExportedData": true/false
     * @return the Value to the key found in the .ini File
     */
    public static String getProperty(String key) throws InterruptedException {

        String propertyValue = "";
        Properties prop = new Properties();

        synchronized (SaveConfigurations.class) {
            while (!sourcePath.toFile().exists()) {
                SaveConfigurations.class.wait();
            }
            try {
                try {
                    Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini");
                    prop.load(new FileReader(path.toString()));

                    propertyValue = prop.getProperty(key);
                    logger.info("read property Value: " + propertyValue + " from key: " + key);


                } catch (IOException e) {
                    logger.warning("An error occurred: " + e);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //returns directory name
        return propertyValue;
    }


    /**
     * Puts the given information from the Dialog into the configuration.ini File
     * C:/User/Name/AppData/MinIo
     *
     * @param directory- the path of the Folder to track and upload
     * @param selected-  "removeExportedData": true/false
     */
    public static void setProperty(String directory, boolean selected) {

        synchronized (SaveConfigurations.class) {

            try {

                if (!Files.exists(path)) {
                    Files.createDirectory(sourcePath);
                    logger.info("Directory created" + path);
                }

                File myObj = new File(sourcePath.toString() + "\\uploadConfig.ini");
                if (myObj.createNewFile()) {
                    logger.info("File created: " + myObj.getName());

                    FileWriter myWriter = new FileWriter(myObj);
                    directory = directory.replace("\\", "\\\\");
                    myWriter.write("path=" + directory + "\n");
                    myWriter.write("removeExportedData=" + selected + "\n");
                    myWriter.close();

                } else {
                    logger.info("File already exists.");
                }
            } catch (IOException e) {
                logger.warning("An error occurred." + e);
                e.printStackTrace();
            }

            SaveConfigurations.class.notify();
        }
    }
}


