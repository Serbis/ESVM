package esvm.rows;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLARowMaps implements Initializable{
    @FXML public ImageView iv;
    @FXML public Label lbName;
    @FXML public Label lbDate;
    @FXML public Label lbSize;

    private static FXMLARowMaps instance;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLARowMaps getInstance() {
        return instance;
    }

    }
