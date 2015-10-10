package esvm;

import esvm.controllers.FXMLAMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        App app = new App();

        FXMLAMain fxmlaMain;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("controllers/main.fxml"));
        VBox pane = null;
        try {
            pane = (VBox)loader.load();
            fxmlaMain = loader.getController();
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.setTitle("ExoScript Virtual Machine Debugger");
            fxmlaMain.setStage(primaryStage);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
