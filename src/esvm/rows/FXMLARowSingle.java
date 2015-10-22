package esvm.rows;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLARowSingle implements Initializable{
    @FXML public Label lbText;

    private static FXMLARowSingle instance;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLARowSingle getInstance() {
        return instance;
    }

    }
