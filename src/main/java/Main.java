import io.minio.MinioClient;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        // Connection to your MinIo Server
        MinioClient minioClient = MinIO.connection();
        // watch for changes
        new FileWatcher().watcher(minioClient);

    }
}
