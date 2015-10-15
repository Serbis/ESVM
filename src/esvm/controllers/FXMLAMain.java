package esvm.controllers;

import esvm.App;
import esvm.vm.ESVM;
import esvm.vm.desc.AsmLine;
import esvm.vm.desc.Vmspec;
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
    public FXMLADDMTab fxmladdmTab;
    public FXMLAIOTab fxmlaioTab;

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
            fxmladdmTab = loaderDDMTab.getController();
            fxmlaioTab = loaderIOTab.getController();
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


    /**
     * Открывает дапм файл виртуальной машины через диалог выбора файла.
     *
     */
    private void actionOpenDump() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ESVM Dump", "*.dump"), new FileChooser.ExtensionFilter("All Files", "*.*"));
        File dumpFile = fileChooser.showOpenDialog(null);

        if (dumpFile != null) {
            App.getInstance().setWorkMode(App.WorkMode.DUMP);
            fxmlaDumpTab.setHexGrig(App.getInstance().constructHexLayout(dumpFile, 128, 15, App.HexTableClass.DUMP));
        }
    }

    /**
     * Открыват класс виртуальной машины для загрузки через дивлог выбора
     * файлов. Производит первичиную инициалзиацию вм и вкладом программы.
     *
     */
    private void actionOpenClass() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ESVM Class", "*.esvmc"), new FileChooser.ExtensionFilter("All Files", "*.*"));
        File classFile = fileChooser.showOpenDialog(null);

        App.getInstance().esvm = new ESVM(new Vmspec(128, 2, 128, classFile.getPath()));
       // try {
            //App.getInstance().esvm.getMemoryManager().write(new Pointer(0, 127), new byte[] {45});
            //App.getInstance().esvm.getMemoryManager().write(new Pointer(1, 0), new byte[] {127, 127});
            //App.getInstance().esvm.getMemoryManager().callocate(new Pointer(1, 6), 4);
            //App.getInstance().esvm.getMemoryManager().callocate(new Pointer(0, 0), 4);
            //App.getInstance().esvm.getMemoryManager().writeBlock(new Pointer(1, 6), new byte[]{22, 22, 22, 22});
            //App.getInstance().esvm.getMemoryManager().rellocate(new Pointer(1, 6), new Pointer(0, 4));
            //App.getInstance().esvm.getMemoryManager().push(6511);
            //App.getInstance().esvm.getMemoryManager().push(49634);
        //} catch (MemoryOutOfRangeException | MemoryAllocateException | MemoryNullBlockException | StackOverflowException e) {
        //    e.printStackTrace();
        //}

        AsmLine[] asmLines = App.getInstance().esvm.getDisassembler().getAsm();
        fxmladdmTab.initCodePane(asmLines);
        fxmladdmTab.initBinPane(classFile);
        fxmlaioTab.classLoaded();
        App.getInstance().loadDupmFromVm(fxmlaDumpTab);

    }

    /**
     * Запускает свознове выполнение программы
     *
     */
    private void actionRun() {
        App.getInstance().esvm.getExecutor().execute();
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

    @FXML protected void handlesltbbtnRunAction(ActionEvent event) {
        actionRun();
    }



}
