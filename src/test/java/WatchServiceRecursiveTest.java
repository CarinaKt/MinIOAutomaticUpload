import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

class WatchServiceRecursiveTest {

    @BeforeEach
    void setUp() throws IOException {

        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"),"\\AppData\\MinIO\\uploadConfig.ini");
        Files.createDirectory(Path.of(System.getProperty("user.home") + "/Documents/test"));
        Files.deleteIfExists(FileSystems.getDefault().getPath(System.getProperty("user.home"),"\\AppData\\MinIO\\uploadConfig.ini"));
        Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.home"),"\\AppData\\MinIO\\uploadConfig.ini"));
        String directory = String.valueOf(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test"));
        FileWriter myWriter = new FileWriter(String.valueOf(path));
        directory = directory.replace("\\", "\\\\");
        System.out.println(directory);
        myWriter.write("path=" + directory + "\n");
        myWriter.write("removeExportedData=" + "true" + "\n");
        myWriter.close();

    }

    void createDirectory(String folder) throws IOException {
        Files.createDirectory(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/" + folder));
    }

    void createFile(String file) throws IOException {
        Files.createFile(Path.of(System.getProperty("user.home") + "/Documents/test/" + file));
    }

    void deleteFile(Path file) throws IOException {
        Files.deleteIfExists(Path.of(System.getProperty("user.home") + "/Documents/test/" + file));
    }

    void deleteFolder(String folder) throws IOException {
        Files.deleteIfExists(Path.of(System.getProperty("user.home") + "/Documents/test/" + folder));
    }

    @AfterEach
    void tearDown() throws IOException {
        File file = new File (System.getProperty("user.home") + "/Documents/test");
        deleteDir(file);
    }

        public static void deleteDir(File path) {
            for (File file : path.listFiles()) {
                if (file.isDirectory())
                    deleteDir(file);
                file.delete();
            }
            path.delete();
        }

    @Test
    void test1() throws IOException {

        WatchServiceRecursive w = new WatchServiceRecursive();
        Files.createDirectory(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/hallo"));
        Assert.assertTrue(Files.exists(Path.of(System.getProperty("user.home") + "/Documents/test/hallo")));

    }
}