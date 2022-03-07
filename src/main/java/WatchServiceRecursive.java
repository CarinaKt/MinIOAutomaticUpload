import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class WatchServiceRecursive {
    // Logging
    Logger logger = Logger.getLogger(this.getClass().getName());
    private Map<WatchKey, Path> keyPathMap = new HashMap<>();


    // Directory to trac
    String directory;
    // Handel Uploaded Data
    boolean removeFromLocal;
    // the directory path to watch on
    Path rootPath;

    public void watcher() throws InterruptedException {

        directory = SaveConfigurations.getProperty("path");
        removeFromLocal = SaveConfigurations.getProperty("removeExportedData").equals("true");
        rootPath = Path.of(directory);

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            registerDir(rootPath, watchService);
            startListening(watchService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerDir(Path path, WatchService watchService) throws IOException {

        if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            return;
        }

        logger.info("registering: " + path);

        WatchKey key = path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        keyPathMap.put(key, path);


        for (File f : path.toFile().listFiles()) {
            registerDir(f.toPath(), watchService);
        }
    }

    private void startListening(WatchService watchService) throws Exception {
        while (true) {
            WatchKey wk = watchService.take();
            for (WatchEvent<?> event : wk.pollEvents()) {
                logger.info("Event: " + event.kind() + " count: " + event.count() + " context: " +
                        event.context() + " Context type: " + ((Path) event.context()).getClass());

                //the context is always a Path.
                final Path changed = (Path) event.context();
                String fileDirectory = rootPath.getFileName().toString();

                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    //this is not a complete path
                    Path path = (Path) event.context(); //name of file

                    //need to get parent path
                    Path parentPath = keyPathMap.get(wk); //path of directory where the file is located

                    //get complete path
                    path = parentPath.resolve(path);


                    if (!Files.isDirectory(path)) { // File
                        MinIO.setDataToUpdate(path.toString(), changed.toString(), fileDirectory, rootPath.toString());
                        if (removeFromLocal) {
                            deleteLocal(path);
                        }
                    } else if (Files.isDirectory(path)) { // Directory
                        registerDir(path, watchService);
                        MinIO.setDirectoryToUpdate(path.toString(), changed.toString(), fileDirectory, rootPath.toString());
                    }
                }
                // a deleted Directory/ Renamed rove from cloud only if the option remove from local
                // after upload is selected
                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE && !removeFromLocal) {

                    //this is not a complete path
                    Path path = (Path) event.context(); //name of files

                    //need to get parent path
                    Path parentPath = keyPathMap.get(wk); // path of directory where the file is stored

                    //get complete path
                    path = parentPath.resolve(path);

                    // remove only files not Directory
                    if (!Files.isDirectory(path)) { // File
                        // remove from cloud
                        MinIO.setDataToDelete(path.toString(), changed.toString(), fileDirectory, rootPath.toString());
                    }
                }

            }
            if (!wk.reset()) {
                keyPathMap.remove(wk);
            }
            if (keyPathMap.isEmpty()) {
                break;
            }
        }
    }

    /**
     * Removes the given path from the local Filesystem
     *
     * @param path
     */
    private void deleteLocal(Path path) {
        if (Files.exists(path)) {
            File f = new File(path.toString());
            f.delete();
            logger.info("delete from local: " + path);
        }

    }
}