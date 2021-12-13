import com.sun.net.httpserver.Authenticator;
import io.minio.MinioClient;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        // Input Dialog
        DialogWithRadiobutton.loadDialog();
        // Connection to your MinIo Server
        MinIO.connection();
        // watch for changes
        new WatchServiceRecursive().watcher();


    }
}
