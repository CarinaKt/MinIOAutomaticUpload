import java.io.IOException;
import java.nio.file.*;

public class FileWatcher {

    //the directory to watch on C:/Users/Carina/Documents/
    final Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "Documents");

    public void watcher() {
        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            //events listen for
            final WatchKey watchKey = path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}




