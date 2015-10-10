package esvm.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLAIOTab implements Initializable{
    @FXML private AnchorPane anchorPane;
    @FXML private VBox vBox;


    private static FXMLAIOTab instance;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        instance = this;
    }



    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLAIOTab getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    @FXML protected void handlesltbbtnModeVideoAction(ActionEvent event) {

    }
}
