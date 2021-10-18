import io.minio.MinioClient;
import io.minio.errors.MinioException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class Main {
    public static void main(String[] args)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        try {
            // Connection to your MinIo Server
            MinioClient minioClient = MinIO.connection();
            // Uploads a file to your MinIo Server
            MinIO.upload(minioClient);

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }

    }
}
