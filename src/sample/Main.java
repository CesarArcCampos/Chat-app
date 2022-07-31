package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        System.setProperty("javax.net.ssl.keyStore", "myKeyStore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "ksw8z7a");
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 478, 396));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
