
# ConfigSyncLibrary

`ConfigSyncLibrary` is a Java library that periodically syncs configuration files from an AWS S3 bucket. This library allows users to specify the S3 bucket details, AWS credentials, and sync interval using a properties file. It also supports sync failure notifications through a custom listener.

## Features

- **Periodic Sync**: Automatically syncs a configuration file from an AWS S3 bucket at specified intervals.
- **AWS S3 Integration**: Uses AWS SDK for S3 object retrieval.
- **Failure Notification**: Implements a customizable listener to handle sync failures.
- **Configurable via Properties File**: Configure all required parameters in a `.properties` file.

## Requirements

- Java 11 or later
- AWS SDK for Java (included in dependencies)
- Maven for building the project

## Installation

1. Clone the repository or download the ZIP file.
2. Navigate to the project directory.
3. Run the following command to install dependencies and build the project:

   ```bash
   mvn clean install
   ```

## Configuration

Create a properties file named `config.properties` in the `src/main/resources` folder with the following content:

```properties
aws.accessKey=your_access_key
aws.secretKey=your_secret_key
aws.region=your_region
s3.bucket=your_bucket
s3.objectKey=your_object_key
local.path=path/to/your/local/config.json
sync.interval=60000  # Sync interval in milliseconds (example: 60000ms = 1 minute)
```

- **aws.accessKey**: AWS Access Key for authentication.
- **aws.secretKey**: AWS Secret Key for authentication.
- **aws.region**: The AWS region of your S3 bucket.
- **s3.bucket**: The name of the S3 bucket.
- **s3.objectKey**: The key (file path) of the object to sync.
- **local.path**: Path on the local machine where the file will be stored.
- **sync.interval**: Sync interval in milliseconds.

## Usage

1. **Initialize the Library**:
   
   Use the `ConfigSyncService` class to set up and start the sync process. Ensure you pass the `Properties` object loaded from the `config.properties` file and a failure listener.

   ```java
   import com.example.configsync.ConfigSyncService;
   import com.example.configsync.SyncFailureListener;
   
   import java.io.FileInputStream;
   import java.io.IOException;
   import java.util.Properties;

   public class Main {
       public static void main(String[] args) {
           Properties properties = new Properties();
           try {
               properties.load(new FileInputStream("src/main/resources/config.properties"));
           } catch (IOException e) {
               System.err.println("Failed to load properties file: " + e.getMessage());
               return;
           }

           SyncFailureListener failureListener = new SyncFailureListener() {
               @Override
               public void onSyncFailure(Exception e) {
                   System.err.println("Sync failed: " + e.getMessage());
               }
           };

           ConfigSyncService configSyncService = new ConfigSyncService(properties, failureListener);
           configSyncService.start();

           // To stop the sync process, you can call:
           // configSyncService.stop();
       }
   }
   ```

2. **Sync Failures**:
   Implement the `SyncFailureListener` interface to receive notifications on sync failures. For instance, you could log the error or send an alert.

   ```java
   SyncFailureListener failureListener = e -> {
       System.err.println("Sync failed: " + e.getMessage());
       // Additional handling code (e.g., send an alert)
   };
   ```

3. **Start and Stop Syncing**:
   - Call `start()` on the `ConfigSyncService` instance to begin syncing.
   - Call `stop()` to stop syncing.

## Example Test

A simple JUnit test is included to verify the start and stop functionality of the `ConfigSyncService`. You can find it in `src/test/java/com/example/configsync/ConfigSyncServiceTest.java`.

To run the test:

```bash
mvn test
```

## License

This project is licensed under the MIT License.
