package application;

import java.sql.Connection;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
//testt 2
public class Main extends Application {

    public static Connection conn = DBConnect.getConnection();

    @Override
    public void start(Stage primaryStage) {
        if (conn != null) System.out.println("Connected!");
        else System.out.println("Connection FAILED!");

        StoreUI storeUI = new StoreUI(primaryStage, conn);
        Scene scene = storeUI.createScene();

        primaryStage.setTitle("Lady90s Store");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /////////saajaaaaaaa
    ///randd 

    public static void main(String[] args) {
        launch(args);
    }
}
