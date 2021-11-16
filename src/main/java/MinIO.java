import io.minio.*;
import io.minio.errors.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class MinIO {

    private static MinioClient minioClient;

    /**
     * Establishes the Server connection to the Cloud
     *
     * @throws IOException
     */
    public static MinioClient connection() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileReader("config.properties"));
        // Instantiate the minio client with the endpoint and access keys as shown below.
        minioClient =
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
     *
     * @param path       = "C:/Users/name/Documents/test.txt"
     * @param fileName   = "ordner/test.txt"
     * @param bucketName = "mytest"
     */
    public static void setDataToUpdate(String path, String fileName, String bucketName, String rootPath)
            throws IOException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, InvalidKeyException {
        // formats fileName fro, "ordner\datei.txt" to "ordner/"
        fileName = path.substring(rootPath.length()+1);
        System.out.println(fileName);
        fileName = fileName.replace("\\", "/");
        System.out.println(fileName);

        uploadFiles(path, fileName, bucketName.toLowerCase());
    }



    public static void setDirectoryToUpdate(String path, String directoryName, String bucketName, String rootPath)
            throws IOException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, InvalidKeyException {
        // formats fileName fro, "ordner\datei.txt" to "ordner/"
        directoryName = path.substring(rootPath.length()+1);
        System.out.println(directoryName);
        directoryName = directoryName.replace("\\", "/");
        System.out.println(directoryName);

        uploadDirectory(bucketName.toLowerCase(), directoryName);
    }

    /**
     * creates a new bucked if needed, and uploads the given File
     * <p>
     * TODO: Errorhandler
     *
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
    private static void uploadFiles(String file, String fileName, String bucketName)
            throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException,
            InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {

        // Check if the bucket already exists
        boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            // Create a new bucket
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

        } else {
            System.out.println("Bucket '" + bucketName + "' already exists.1");
        }

        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        // take the last part behind / as name ordenr/document
                        // TODO: diffenez zwischen bucket und file als filename übergeben -> Obrder
                        .object(fileName)
                        // file to upload
                        .filename(file)
                        .build());
        System.out.println("bucket: " + bucketName + " " + fileName + " "+ file);
        System.out.println(
                "Successfully uploaded to bucket.");
    }


    /**
     * Adds a Directory to a (existing) Bucket
     *
     * @param bucketName
     * @param directoryName
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public static void uploadDirectory(String bucketName, String directoryName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {

        boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if (!found) {
            // Create a new bucket
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

        } else {
            System.out.println("Bucket '" + bucketName + "' already exists.1");
        }

        // Create object ends with '/' (also called as folder or directory).
        // TODO: create unterordner in ordner
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(directoryName + "/").stream(
                        new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());

        System.out.println("Successfully added directory");
    }
}


/**
 * // Upload unknown sized input stream.
 * minioClient.putObject(
 * PutObjectArgs.builder().bucket("my-bucketname").object("my-objectname").stream(
 * inputStream, -1, 10485760)
 * .contentType("video/mp4")
 * .build());
 **/