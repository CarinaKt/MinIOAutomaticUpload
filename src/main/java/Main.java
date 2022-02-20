import java.io.IOException;
import java.nio.file.FileSystems;


public class Main {
    public static void main(String[] args) throws IOException {
        // Connection to your MinIO Server
        //MinIO.connection();
        // Input Dialog

       // DialogWithRadiobutton.loadDialog();

        DialogWithRadiobutton.loadDialog();


//        String p = SaveConfigurations.getProperty("path");
//        System.out.println(p);

        if (FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini").toFile().exists()) {
            new WatchServiceRecursive().start();
        }
        //new WatchServiceRecursive().start();

       /* try {
            if (!FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini").toFile().exists()) {
                SaveConfigurations.available.wait();
                SaveConfigurations.available.acquire();
                new WatchServiceRecursive().watcher();
            }
            if (FileSystems.getDefault().getPath(System.getProperty("user.home"), "\\AppData\\MinIO\\uploadConfig.ini").toFile().exists()) {
                new WatchServiceRecursive().watcher();
            }
            //new WatchServiceRecursive().start();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }
}
