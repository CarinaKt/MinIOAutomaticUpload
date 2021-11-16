import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class WatchServiceRecursiveExample {
    private static Map<WatchKey, Path> keyPathMap = new HashMap<>();


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


    // Directory to trac
    final String directory = getDirectory();
    // the directory path to watch on
    final Path rootPath = FileSystems.getDefault().getPath(System.getProperty("user.home"), directory);


    public void watcher () throws Exception {

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            registerDir(rootPath, watchService);
            startListening(watchService);
        }
    }

    private void registerDir (Path path, WatchService watchService) throws
            IOException {

        if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {

            return;
        }

        System.out.println("registering: " + path);
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
                System.out.printf("Event... kind=%s, count=%d, context=%s Context type=%s%n",
                        event.kind(),
                        event.count(), event.context(),
                        ((Path) event.context()).getClass());

                //the context is always a Path.
                final Path changed = (Path) event.context();
                String fileDirectory = rootPath.getFileName().toString();

                //do something useful here
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    //this is not a complete path
                    Path path = (Path) event.context(); //name des Fils

                    //need to get parent path
                    Path parentPath = keyPathMap.get(wk); // pfad des directory in dem der File ist

                    //get complete path
                    path = parentPath.resolve(path);


                    if(!Files.isDirectory(path)) { // File
                        MinIO.setDataToUpdate(path.toString(), changed.toString(), fileDirectory, rootPath.toString());
                    }else if (Files.isDirectory(path)) { // Directory
                        registerDir(path, watchService);
                        System.out.println("print: "+Files.isDirectory(path) +" "+ changed.toString() +" "+fileDirectory + " "+ rootPath.toString() );
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
}