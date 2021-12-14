import org.junit.Assert;
import org.junit.Before;
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
    @Before
    void start() throws IOException {
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini");
        Files.deleteIfExists(FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini"));
        Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini"));
        String directory = String.valueOf(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test"));
        FileWriter myWriter = new FileWriter(String.valueOf(path));
        directory = directory.replace("\\", "\\\\");
        myWriter.write("path=" + directory + "\n");
        myWriter.write("removeExportedData=" + "true" + "\n");
        myWriter.close();
        WatchServiceRecursive w = new WatchServiceRecursive();
    }

    @BeforeEach
    void setUp() throws IOException {
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini");
        Files.createDirectory(Path.of(System.getProperty("user.home") + "/Documents/test"));
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
        File file = new File(System.getProperty("user.home") + "/Documents/test");
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
    void testFolder() throws IOException {
        //WatchServiceRecursive w = new WatchServiceRecursive();
        Files.createDirectory(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/hallo"));
        Assert.assertTrue(Files.exists(Path.of(System.getProperty("user.home") + "/Documents/test/hallo")));
    }

    @Test
    void testFile() throws IOException {
        // WatchServiceRecursive w = new WatchServiceRecursive();
        Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/hallo.txt"));
        Assert.assertTrue(Files.exists(Path.of(System.getProperty("user.home") + "/Documents/test/hallo.txt")));
    }

    @Test
    void testFileInFolder() throws IOException {
        Files.createDirectory(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/o"));
        Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/o/hallo.txt"));
        Assert.assertTrue(Files.exists(Path.of(System.getProperty("user.home") + "/Documents/test/o/hallo.txt")));
    }

    @Test
    void testFileKeepExportedData() throws IOException {
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini");
        Files.deleteIfExists(FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini"));
        Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini"));
        String directory = String.valueOf(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test"));
        FileWriter myWriter = new FileWriter(String.valueOf(path));
        directory = directory.replace("\\", "\\\\");
        myWriter.write("path=" + directory + "\n");
        myWriter.write("removeExportedData=" + "false" + "\n");
        myWriter.close();

        Files.createDirectory(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/o"));
        Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/o/hallo.txt"));
        Assert.assertTrue(Files.exists(Path.of(System.getProperty("user.home") + "/Documents/test/o/hallo.txt")));
    }

    @Test
    void testFileInFolderRename() throws IOException {
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini");
        Files.deleteIfExists(FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini"));
        Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini"));
        String directory = String.valueOf(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test"));
        FileWriter myWriter = new FileWriter(String.valueOf(path));
        directory = directory.replace("\\", "\\\\");
        myWriter.write("path=" + directory + "\n");
        myWriter.write("removeExportedData=" + "false" + "\n");
        myWriter.close();

        Files.createDirectory(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/o"));
        Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/o/hallo.txt"));
        Path p = FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/o/hallo1.txt");
        Files.move(FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Documents/test/o/hallo.txt"), p);
        Assert.assertTrue(Files.exists(Path.of(System.getProperty("user.home") + "/Documents/test/o/hallo1.txt")));
    }


}