package esvm.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLAAsmTab implements Initializable{
    @FXML private TextArea taCode;
    @FXML private TextArea taErrors;

    private static FXMLAAsmTab instance;
    private Stage stage;
    private String iotext = "";
    private String errorsline = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        instance = this;
    }



    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLAAsmTab getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Возаращает введенный в поле ввода код без символов перевода строки
     *
     * @return строка с кодом
     */
    public String getCode() {
        return taCode.getText().replaceAll("\n", "");
    }

    /**
     * Помещает очередную ошибку в поле вывода ошибков
     *
     * @param text текст ошибки
     */
    public void putError(String text) {
        errorsline += text + "\n";
        taErrors.setText(errorsline);
    }


}
