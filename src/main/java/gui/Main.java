package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        /*Icon made by https://www.flaticon.com/authors/pixel-buddha licensed by CC 3.0 BY*/
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/appicon.png")));

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setResources(ResourceBundle.getBundle("localization/gui"));
        fxmlLoader.setLocation(getClass().getResource("/fxml/mainwindow.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("TW Action Organizer");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
