package esvm.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLAGCTab implements Initializable{

    private static FXMLAGCTab instance;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initToolBar();

        instance = this;
    }

    /**
     * Проводит инициализацию панели быстрого доступа
     *
     */
    private void initToolBar() {

    }


    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLAGCTab getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    @FXML protected void handlesltbbtnModeVideoAction(ActionEvent event) {

    }
}
