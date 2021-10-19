import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher {

    //the directory to watch on C:/Users/Carina/Documents/
    final Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "Documents");

    public void watcher() {
        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            // listen for events
            final WatchKey watchKey = path.register(
                    watchService,
                   // ENTRY_MODIFY, not in use at the moment
                    ENTRY_CREATE // new an rename
            );

            while (true) {
                final WatchKey wk = watchService.take();
                for (WatchEvent<?> event : wk.pollEvents()) {
                    //the context is always a Path.
                    final Path changed = (Path) event.context();
                    System.out.println(changed);
                }
                // reset the key
                boolean valid = wk.reset();
                if (!valid) {
                    System.out.println("Key has been unregisterede");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}




