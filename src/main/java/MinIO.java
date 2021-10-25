import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;

import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class MinIO {
    /**
     * Establishes the Server connection to the Cloud
     *
     * @throws IOException
     */
    public static MinioClient connection() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileReader("config.properties"));
        // Instantiate the minio client with the endpoint and access keys as shown below.
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(prop.getProperty("endpoint"))
                        .credentials(prop.getProperty("user"), prop.getProperty("password"))
                        .region(prop.getProperty("region"))
                        .build();
        return minioClient;
    }

    /**
     * defines the file to upload
     * Format:
     * @param path       = "C:/Users/name/Documents/test.txt"
     * @param fileName   = "test.txt"
     * @param bucketName = "mytest"
     */
    public static void setDataToUpdate(MinioClient minioClient, String path, String fileName, String bucketName)
            throws IOException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, InvalidKeyException {

        upload(minioClient, path, fileName, bucketName.toLowerCase());
    }

    /**
     * creates a new bucked if needed, and uploads the given File
     *
     * @param minioClient: connection to the Cloud
     *                     TODO: Errorhandler
     * @throws IOException
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidResponseException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    private static void upload(MinioClient minioClient, String file, String fileName, String bucketName)
            throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException,
            InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {

        // Create a new bucket if not exist
        boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            // Create a new bucket
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } else {
            System.out.println("Bucket '" + bucketName + "' already exists.");
        }

        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        // take the last part behind / as name
                        .object(fileName)
                        // file to upload
                        .filename(file + "/" + fileName)
                        .build());
        System.out.println(
                "Successfully uploaded to bucket.");
    }
}
