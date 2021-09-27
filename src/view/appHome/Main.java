package view.appHome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/view/appHome/appLogin.fxml"));
        primaryStage.setTitle("Company Management System");
        primaryStage.getIcons().add(new Image("/lib/images/favicon.png"));
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }
    static public  Stage getPrimaryStage(){
        return Main.primaryStage;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
