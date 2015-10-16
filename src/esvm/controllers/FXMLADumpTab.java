package esvm.controllers;

import esvm.App;
import esvm.fields.Address;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLADumpTab implements Initializable{
    @FXML public AnchorPane hexPane;
    @FXML public AnchorPane asciiPane;
    @FXML public ScrollPane hexScroll;
    @FXML public TextField et8bitS;
    @FXML public TextField et32bitS;
    @FXML public TextField ethex;
    @FXML public TextField et8bit;
    @FXML public TextField et32bit;
    @FXML public TextField etoctal;
    @FXML public TextField et16bitS;
    @FXML public TextField et32bitD;
    @FXML public TextField etbin;
    @FXML public TextField et16bit;
    @FXML public TextField et64bitD;
    @FXML public CheckBox cbxRealTime;
    @FXML public Button btnRefresh;

    private static FXMLADumpTab instance;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initToolBar();
        setHexSelectListener();
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
    public static FXMLADumpTab getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setHexGrig(VBox vBox) {
        hexScroll.setContent(vBox);
    }

    /**
     * Определяет нажатие на какой либо байт в таблице
     *
     */
    private void setHexSelectListener() {
        App.getInstance().setOnByteSelectListener(new App.OnByteSelectListener() {
            @Override
            public void onByteSelect(App.HexTableClass hexTableClass, Address address, int value) {
                if (hexTableClass == App.HexTableClass.DUMP) {
                    String hexV = Integer.toHexString(value).toUpperCase();
                    if (hexV.length() == 1) {
                        hexV = "0" + hexV;
                    }
                    ethex.setText(hexV);
                    String octalV = Integer.toOctalString(value).toUpperCase();
                    if (octalV.length() == 2) {
                        octalV = "0" + octalV;
                    }
                    etoctal.setText(octalV);
                    String binV = Integer.toBinaryString(value).toUpperCase();
                    if (binV.length() < 8) {
                        for (int i = 0; i < 8 - binV.length() + 1; i++) {
                            binV = "0" + binV;
                        }
                    }
                    etbin.setText(binV);
                    et8bit.setText(String.valueOf(value));
                    et8bitS.setText(String.valueOf(value));
                }

            }
        });
    }


    @FXML protected void handleslcbxRealTimeAction(ActionEvent event) {

    }

    @FXML protected void handleslbtnRefreshAction(ActionEvent event) {
        App.getInstance().loadMemoryDupmFromVm(this);
    }
}
