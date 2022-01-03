#MinIOAutomaticUpload
This application uploads automatically changed data from a given source to your MinIO Cloud

Install MinIO on a Raspberry Pi: 
https://www.linkedin.com/pulse/build-your-own-private-cloud-home-raspberry-pi-minio-huerta-arias

build for Windows

saves config in .ini at C:/User/Name/AppData/Minio if you like to change the upload behavior change it in this file.

#
in case the Swing Dialog doesn't run: 
1. download the JAR from https://repo1.maven.org/maven2/io/minio/minio/8.3.4/
2. put it as library into your project
3. change in Gradle settings Build and run to: from Gradle to IntelliJ IDEA

or first compile it with Gradle and then switch to IntelliJ IDEA

now the swing Dialog is able to execute
#

Renamed files will be deleted in the cloud and updated with the new version. 
Renaming a Folder is not supported.
If you create a new Folder and rename this one you have to delete the folder "New Folder" by hand in the cloud.
This is meant to prevent losing data by mistake.


