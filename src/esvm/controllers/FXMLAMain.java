package esvm.controllers;

import esvm.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLAMain implements Initializable{
    @FXML private TabPane tabPane;

    private static FXMLAMain instance;
    private Stage stage;

    public FXMLADumpTab fxmlaDumpTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initToolBar();
        initTabs();
        instance = this;
    }

    /**
     * Проводит инициализацию панели быстрого доступа
     *
     */
    private void initToolBar() {

    }

    private void initTabs() {
        FXMLLoader loaderIOTab = new FXMLLoader(getClass().getResource("iotab.fxml"));
        FXMLLoader loaderDumpTab = new FXMLLoader(getClass().getResource("dumptab.fxml"));
        FXMLLoader loaderDDMTab = new FXMLLoader(getClass().getResource("ddmtab.fxml"));
        FXMLLoader loaderGcTab = new FXMLLoader(getClass().getResource("gctab.fxml"));
        VBox ioTab = null;
        VBox dumpTab = null;
        VBox ddmTab = null;
        VBox gcTab = null;
        try {
            ioTab = (VBox)loaderIOTab.load();
            dumpTab = (VBox)loaderDumpTab.load();
            ddmTab = (VBox)loaderDDMTab.load();
            gcTab = (VBox)loaderGcTab.load();
            tabPane.getTabs().get(0).setContent(ioTab);
            tabPane.getTabs().get(1).setContent(dumpTab);
            tabPane.getTabs().get(2).setContent(ddmTab);
            tabPane.getTabs().get(3).setContent(gcTab);
            fxmlaDumpTab = loaderDumpTab.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLAMain getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void actionOpenClass() {

    }

    private void actionOpenDump() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ESVM Dump", "*.dump"), new FileChooser.ExtensionFilter("All Files", "*.*"));
        File dumpFile = fileChooser.showOpenDialog(null);

        if (dumpFile != null) {
            App.getInstance().setWorkMode(App.WorkMode.DUMP);
            fxmlaDumpTab.setHexGrig(App.getInstance().constructHexLayout(dumpFile, 128, 15));
        }
    }

    private void actionOpenExit() {
        stage.close();
    }

    @FXML protected void handleslmenuOpenClassAction(ActionEvent event) {
        actionOpenClass();
    }

    @FXML protected void handleslmenuOpenDumpAction(ActionEvent event) {
        actionOpenDump();
    }

    @FXML protected void handleslmenuExitAction(ActionEvent event) {
        actionOpenExit();
    }

    @FXML protected void handlesltbbtnOpenClassAction(ActionEvent event) {
        actionOpenClass();
    }

    @FXML protected void handlesltbbtnOpenDumpAction(ActionEvent event) {
        actionOpenDump();
    }



}
