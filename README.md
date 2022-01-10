# MinIOAutomaticUpload
This application uploads changed data from a given source to your MinIO Cloud automatically.

To see how to install MinIO on a Raspberry Pi visit
https://www.linkedin.com/pulse/build-your-own-private-cloud-home-raspberry-pi-minio-huerta-arias
 
During the first run, a dialog appears where you can enter the path to the tract directory.
You can also decide whether the updated object is removed from local storage after it is updated to the cloud. These details get saved in a file called “uploadConfig.ini” at C:/User/Name/AppData/Minio.
To modify the upload behavior, modify this file.

Connect to the cloud by entering the endpoint, user, password, and region in a file named “config.properties”, as shown below:

endpoint=http://pi.minio:9000
user=user
password=password
region=ap-northeast-1

## Detect changes in the Filesystem
WatchServiceRecursive watches for events from the filesystem:
- Delete:
A file has been deleted locally. If the option to keep updated data locally too is set to true, the removed file will also be removed from the cloud. If the option to keep things locally is set to false, this event will be ignored.
- Create:
As a result of this event, a new directory or file has been created or renamed. As a result, the object connected to this event gets updated in the cloud.
 
MinIO
The application is able to upload files, directories, and also delete files. It is not able to delete a directory, which prevents losing data by mistake.

MinIO Api documentation: https://docs.min.io/?ref=con

## Naming pattern:
The MinIO uses the AmazonS3 Naming Patterns:
- Bucket names must be between 3 and 63 characters long.
- Bucket names can consist only of lowercase letters, numbers, dots (.), and hyphens (-).
- Bucket names must begin and end with a letter or number.
- Bucket names must not be formatted as an IP address (for example, 192.168.5.4).
- Bucket names must not start with the prefix xn--.
- Bucket names must not end with the suffix -s3alias. This suffix is reserved for access point alias names.
- Bucket names must be unique within a partition. A partition is a grouping of regions. AWS currently has three partitions: aws (Standard Regions), aws-cn (China Regions), and aws-us-gov (AWS GovCloud [US] Regions).
- Buckets used with Amazon S3 Transfer Acceleration can't have dots (.) in their names.
 
For more information, see https://docs.aws.amazon.com/AmazonS3/latest/userguide/bucketnamingrules.html
 
- Please don't use a name with „Neuer Ordner“ to initialize a Directory, this application won't update a Directory with this naming to the cloud.
 
Objects that already existed before the application was started will not be updated by this application.
 
Build for Windows Renamed files will be deleted in the cloud and updated with the new version (Windows 10). Attention: Windows 11 does not provide an event if a file's data changes, so it does not update anything.

## In case the Swing Dialog doesn't run: 
 1. download the JAR from https://repo1.maven.org/maven2/io/minio/minio/8.3.4/
 2. put it as a library into your project
 3. change in Gradle settings Build and run to: from Gradle to IntelliJ IDEA
 
 or first, compile it with Gradle and then switch to IntelliJ IDEA.
 Now the swing Dialog can execute.

