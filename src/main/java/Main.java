import com.sun.net.httpserver.Authenticator;
import io.minio.MinioClient;

import java.io.IOException;


public class Main {
    public static void main(String[] args)  {

        // Input Dialog
        DialogWithRadiobutton.loadDialog();
        // Connection to your MinIo Server
        try {
            MinioClient minioClient = MinIO.connection();
        } catch (IOException e) {
            ErrorDialog.showError();
            e.printStackTrace();
        }
        // watch for changes
        new WatchServiceRecursive().watcher();


    }
}
