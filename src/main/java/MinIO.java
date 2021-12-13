import io.minio.*;
import io.minio.errors.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Logger;

public class MinIO {
    // Logging
    static Logger logger = Logger.getLogger(MinIO.class.getName());

    private static MinioClient minioClient;

    private static Boolean retry = true;

    public static void setRetry(boolean value){
        retry = value;
    }


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
    public static void setDataToUpdate(String path, String fileName, String bucketName, String rootPath) {
        // formats fileName from, "ordner\datei.txt" to "ordner/"
        fileName = path.substring(rootPath.length() + 1);
        fileName = fileName.replace("\\", "/");

        setRetry(true);
        uploadFiles(path, fileName, bucketName.toLowerCase());
    }


    /**
     * defines the directory to upload
     * Format:
     *
     * @param path          = "C:/Users/name/Documents/NewDirectory" - path for the cloud
     * @param directoryName = "NewDirectory"
     * @param bucketName    = "Documents"
     * @param rootPath      = "C:\Users\name\documents\test" - local path
     */
    public static void setDirectoryToUpdate(String path, String directoryName, String bucketName, String rootPath) {
        // formats directoryName from "ordner\datei.txt" to "ordner/"
        directoryName = path.substring(rootPath.length() + 1);
        directoryName = directoryName.replace("\\", "/");

        setRetry(true);
        uploadDirectory(bucketName.toLowerCase(), directoryName);
    }

    /**
     * defines the file to delete from the clod after a rename
     * Format:
     *
     * @param path       = "C:/Users/name/Documents/test.txt"
     * @param fileName   = "ordner/test.txt"
     * @param bucketName = "mytest"
     */
    public static void setDataToDelete(String path, String fileName, String bucketName, String rootPath) {
        // formats fileName from, "ordner\datei.txt" to "ordner/"
        fileName = path.substring(rootPath.length() + 1);
        fileName = fileName.replace("\\", "/");

        setRetry(true);
        deleteFile(fileName, bucketName.toLowerCase());
    }

    /**
     * creates a new bucked if needed, and uploads the given File
     */
    private static void uploadFiles(String file, String fileName, String bucketName) {
        // Check if the bucket already exists
        logger.info("bucket: " + bucketName + " " + fileName + " " + file);
        //retry = true;
        while (retry) {
            boolean found = false;
            try {
                found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!found) {
                    // Create a new bucket
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

                } else {
                    System.out.println("Bucket '" + bucketName + "' already exists.");
                }
                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(bucketName)
                                // take the last part behind / as name folder/document
                                .object(fileName)
                                // file to upload
                                .filename(file)
                                .build());
                logger.info("Successfully uploaded to bucket.");
                break;

            } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | ErrorResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
                logger.warning("Error: " + e);
                ErrorDialog.showError(e.getMessage());
            }
        }
    }


    /**
     * Adds a Directory to a (existing) Bucket
     *
     * @param bucketName
     * @param directoryName
     */
    private static void uploadDirectory(String bucketName, String directoryName) {
        // retry = true;
        while (retry) {
            try {
                boolean found =
                        minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

                if (!found) {
                    // Create a new bucket
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

                } else {
                    System.out.println("Bucket '" + bucketName + "' already exists.1");
                }

                // Create object ends with '/' (also called as folder or directory).
                minioClient.putObject(
                        PutObjectArgs.builder().bucket(bucketName).object(directoryName + "/").stream(
                                new ByteArrayInputStream(new byte[]{}), 0, -1)
                                .build());

                System.out.println("Successfully added directory");
            } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
                logger.warning("Error: " + e);
                ErrorDialog.showError(e.getMessage());
            }
        }
    }


    /**
     * Removes a file from the cloud after a Renaming
     * this operation only gets called if "removeFromLocal" is set to false
     *
     * @param fileName
     * @param bucketName
     */
    private static void deleteFile(String fileName, String bucketName) {
        //retry = true;
        while (retry) {
            try {

                boolean found =
                        minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

                if (found) {
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(fileName)
                                    .build());

                    System.out.println("Removed file: " + fileName + " from Cloud.");
                } else {
                    System.out.println("File: " + fileName + " not in Cloud.");
                }
            } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}

