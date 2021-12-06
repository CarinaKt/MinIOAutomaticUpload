import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;


public class WatchServiceRecursive {
    // Logging
    Logger logger =  Logger.getLogger(this.getClass().getName());
    private static Map<WatchKey, Path> keyPathMap = new HashMap<>();


    /**
     * Reads in the Directory Path from the .ini file in
     * C:/User/Name/AppData/MinIo
     * @param key- the property to read out of the file
     *              "path": string path of Folder to track an d upload
     *              "removeExportedData": true/false
     *              "uploadExistingData": true/false
     * @return
     */
    private String getProperty(String key) {
        String propertyValue = "";
        Properties prop = new Properties();

        try {
            Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"),"\\AppData\\MinIO\\uploadConfig.ini");
            prop.load(new FileReader(path.toString()));

            propertyValue = prop.getProperty(key);
            logger.info("read property Value: " + propertyValue + " from key: " + key);

        } catch (FileNotFoundException e) {
            logger.warning("An error occurred: " + e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.warning("An error occurred: " + e);
            e.printStackTrace();
        }

        //returns directory name
        return propertyValue;
    }


    // Directory to trac
    final String directory = getProperty("path");
    // Handel Uploaded Data
    final boolean removeFromLocal = getProperty("removeExportedData").equals("true");
    // Handel existing Data
    final boolean uploadExistingData = getProperty("uploadExistingData").equals("true");

    // the directory path to watch on
    final Path rootPath = Path.of(directory);



    public void watcher () {

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            registerDir(rootPath, watchService);
            startListening(watchService);
        } catch (Exception e) { // registerDir(rootPath, watchService);
            e.printStackTrace();
        }
    }

    private void registerDir (Path path, WatchService watchService) throws
            IOException {

        if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            return;
        }

        logger.info("registering: " + path);
        // TODO: lade vorhandenes in die Cloud

        WatchKey key = path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE);
        keyPathMap.put(key, path);


        for (File f : path.toFile().listFiles()) {
            registerDir(f.toPath(), watchService);
        }
    }

    private void startListening (WatchService watchService) throws Exception {
        while (true) {
            WatchKey wk = watchService.take();
            for (WatchEvent<?> event : wk.pollEvents()) {
                logger.info("Event: "+ event.kind() + " count: "+event.count()+ " context: " +
                        event.context() +  " Context type: " + ((Path) event.context()).getClass());

                //the context is always a Path.
                final Path changed = (Path) event.context();
                String fileDirectory = rootPath.getFileName().toString();

                //do something useful here
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    //this is not a complete path
                    Path path = (Path) event.context(); //name des Files

                    //need to get parent path
                    Path parentPath = keyPathMap.get(wk); // pfad des directory in dem der File ist

                    //get complete path
                    path = parentPath.resolve(path);


                    if(!Files.isDirectory(path)) { // File
                        MinIO.setDataToUpdate(path.toString(), changed.toString(), fileDirectory, rootPath.toString());
                        if(removeFromLocal){
                            deleteLocal(path);
                        }
                    }else if (Files.isDirectory(path)) { // Directory
                        registerDir(path, watchService);
                        MinIO.setDirectoryToUpdate(path.toString(), changed.toString(), fileDirectory, rootPath.toString());
                    }
                }
            }
            if(!wk.reset()){
                keyPathMap.remove(wk);
            }
            if(keyPathMap.isEmpty()){
                break;
            }
        }
    }

    /**
     * Removes the given path from the local Filesystem
     * @param path
     */
    private void deleteLocal(Path path){
        if(Files.exists(path)) {
            File f = new File(path.toString());
            f.delete();
            logger.info("delete from local: " + path);
        }
    }
}