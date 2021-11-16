import io.minio.MinioClient;
import io.minio.errors.*;

import java.io.IOException;
import java.nio.file.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher {

    /**
     * Reads in the Directory Name from the command line
     * TODO: Validation
     * @return
     */
    private String getDirectory() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a directory: ");
        //reads directory nme
        return sc.nextLine();
    }

    public void searchDirectory(String name) {
        FileSystems.getDefault().getFileStores();
    }


/*    private void registerRecursive(final Path root) throws IOException {
        // register all subfolders
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }*/


    // Directory to trac
    final String directory = getDirectory();
    // the directory path to watch on
    final Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), directory);

    public void watcher() {
        try ( WatchService watchService = FileSystems.getDefault().newWatchService()) {
            // listen for events
            final WatchKey watchKey = path.register(
                    watchService,
                    ENTRY_CREATE ,// new or rename
                    ENTRY_MODIFY
            );

            System.out.println("Watching " + directory + " for changes");

            while (true) {
                final WatchKey wk = watchService.take();
                for (WatchEvent<?> event : wk.pollEvents()) {
                    //the context is always a Path.
                    final Path changed = (Path) event.context();
                    String fileDirectory = path.getFileName().toString();
                    // send path to MInIO API
                    Path finalPath = Path.of(path + "\\" + changed);
                    System.out.println(event.kind());
                    System.out.println("prinnt: "+Files.isDirectory(finalPath) +" "+ changed.toString() +" "+finalPath);

                    if(!Files.isDirectory(finalPath)) { // File
                        MinIO.setDataToUpdate(finalPath.toString(), changed.toString(), fileDirectory, path.toString());
                    }else if (Files.isDirectory(finalPath)){ // Directory

                        MinIO.uploadDirectory(fileDirectory, changed.toString());
                    }
                }
                // reset the key
                boolean valid = wk.reset();
                if (!valid) {
                    System.out.println("Key has been unregisterede");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        }
    }

}




