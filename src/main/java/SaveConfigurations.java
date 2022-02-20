import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class SaveConfigurations {

    // Logging
    static Logger logger = Logger.getLogger("SaveConfigurations");

    private static final Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini");
    private static final Path sourcePath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO");

    private static final int MAX_AVAILABLE = 1;
    static final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    /**
     * Reads in the Directory Path from the .ini file in
     * C:/User/Name/AppData/MinIo
     *
     * @param key- the property to read out of the file
     *             "path": string path of Folder to track an d upload
     *             "removeExportedData": true/false
     *             "uploadExistingData": true/false
     * @return
     */
    public static synchronized String getProperty(String key) {

        String propertyValue = "";
        Properties prop = new Properties();

            System.out.println("true");
            try {
                available.acquire();

                try {
                    Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini");
                    prop.load(new FileReader(path.toString()));

                    propertyValue = prop.getProperty(key);
                    logger.info("read property Value: " + propertyValue + " from key: " + key);

                } catch (IOException e) {
                    logger.warning("An error occurred: " + e);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
        }

        available.release();

        //returns directory name
        return propertyValue;

    }

    public static void setProperty(String directory, boolean selected) {
        try {
            available.acquire();

            try {

                if (!Files.exists(path)) {
                    Files.createDirectory(sourcePath);
                    System.out.println("Directory created");
                    logger.info("Directory created" + path);
                }

                File myObj = new File(sourcePath.toString() + "\\uploadConfig.ini");
                if (myObj.createNewFile()) {
                    logger.info("File created: " + myObj.getName());

                    FileWriter myWriter = new FileWriter(myObj);
                    directory = directory.replace("\\", "\\\\");
                    System.out.println(directory);
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


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        available.release();
    }
}


