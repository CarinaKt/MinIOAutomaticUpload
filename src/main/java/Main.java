import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Connection to your MinIO Server
        MinIO.connection();

        // Input Dialog
        DialogWithRadiobutton.loadDialog();

        //watch service
        new WatchServiceRecursive().watcher();

    }

}
