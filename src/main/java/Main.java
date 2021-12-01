import io.minio.MinioClient;


public class Main {
    public static void main(String[] args) throws Exception {

        // Connection to your MinIo Server
        MinioClient minioClient = MinIO.connection();
        // watch for changes
        //new FileWatcher().watcher();
        new WatchServiceRecursive().watcher();

    }
}
