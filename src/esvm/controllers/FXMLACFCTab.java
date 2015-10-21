package esvm.controllers;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLACFCTab implements Initializable{

    private static FXMLACFCTab instance;
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
    public static FXMLACFCTab getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }



}
