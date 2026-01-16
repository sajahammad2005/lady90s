package application;
	
import java.sql.Connection;

import javafx.application.Application;
import javafx.stage.Stage;

// test 
public class Main extends Application {
	static Connection conn = DBConnect.getConnection();
	@Override
	public void start(Stage primaryStage) {
		try {
			 if (conn != null) {
		            System.out.println("Connected!");
		        } else {
		            System.out.println("X");
		        }
			primaryStage.setScene(new CustomerScene(primaryStage).createScene());
			primaryStage.setTitle("Customer Stage");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
