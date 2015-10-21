package esvm.controllers;

import esvm.App;
import esvm.dialogs.StandartDialogs;
import esvm.vm.ESVM;
import esvm.vm.InterruptsManager;
import esvm.vm.compiler.Assembler;
import esvm.vm.desc.AsmLine;
import esvm.vm.desc.Vmspec;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLAMain implements Initializable{
    @FXML private TabPane tabPane;
    @FXML private Button tbbtnOpenClass;
    @FXML private Button tbbtnOpenDump;
    @FXML private Button tbbtnRun;
    @FXML public Button tbbtnDown;
    @FXML public Button tbbtnStop;
    @FXML public Button tbbtnResume;
    @FXML public Button tbbtnItegrate;
    @FXML public Button tbbtnSave;

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
        ImageView iv1 = new ImageView();
        Image image1 = new Image("file:images/class.png", 16, 16, true, true);
        iv1.setImage(image1);
        tbbtnOpenClass.setGraphic(iv1);

        ImageView iv2 = new ImageView();
        Image image2 = new Image("file:images/dump.png", 16, 16, true, true);
        iv2.setImage(image2);
        tbbtnOpenDump.setGraphic(iv2);

        ImageView iv3 = new ImageView();
        Image image3 = new Image("file:images/run.png", 16, 16, true, true);
        iv3.setImage(image3);
        tbbtnRun.setGraphic(iv3);

        ImageView iv4 = new ImageView();
        Image image4 = new Image("file:images/down.png", 16, 16, true, true);
        iv4.setImage(image4);
        tbbtnDown.setGraphic(iv4);

        ImageView iv5 = new ImageView();
        Image image5 = new Image("file:images/stop.png", 16, 16, true, true);
        iv5.setImage(image5);
        tbbtnStop.setGraphic(iv5);

        ImageView iv6 = new ImageView();
        Image image6 = new Image("file:images/resume.png", 16, 16, true, true);
        iv6.setImage(image6);
        tbbtnResume.setGraphic(iv6);

        ImageView iv7 = new ImageView();
        Image image7 = new Image("file:images/integrate.png", 16, 16, true, true);
        iv7.setImage(image7);
        tbbtnItegrate.setGraphic(iv7);

        ImageView iv8 = new ImageView();
        Image image8 = new Image("file:images/resume.png", 16, 16, true, true);
        iv8.setImage(image8);
        tbbtnSave.setGraphic(iv8);
    }

    private void initTabs() {
        FXMLLoader loaderIOTab = new FXMLLoader(getClass().getResource("iotab.fxml"));
        FXMLLoader loaderDumpTab = new FXMLLoader(getClass().getResource("dumptab.fxml"));
        FXMLLoader loaderDDMTab = new FXMLLoader(getClass().getResource("ddmtab.fxml"));
        FXMLLoader loaderGcTab = new FXMLLoader(getClass().getResource("gctab.fxml"));
        FXMLLoader loaderAsmTab = new FXMLLoader(getClass().getResource("asm.fxml"));
        FXMLLoader loaderCfcTab = new FXMLLoader(getClass().getResource("cfc.fxml"));
        VBox ioTab = null;
        VBox dumpTab = null;
        VBox ddmTab = null;
        VBox gcTab = null;
        VBox asmTab = null;
        VBox cfcTab = null;
        try {
            ioTab = (VBox)loaderIOTab.load();
            dumpTab = (VBox)loaderDumpTab.load();
            ddmTab = (VBox)loaderDDMTab.load();
            gcTab = (VBox)loaderGcTab.load();
            asmTab = (VBox)loaderAsmTab.load();
            cfcTab = (VBox)loaderCfcTab.load();
            tabPane.getTabs().get(0).setContent(ioTab);
            tabPane.getTabs().get(1).setContent(dumpTab);
            tabPane.getTabs().get(2).setContent(ddmTab);
            tabPane.getTabs().get(3).setContent(gcTab);
            tabPane.getTabs().get(4).setContent(asmTab);
            tabPane.getTabs().get(5).setContent(cfcTab);
            fxmlaDumpTab = loaderDumpTab.getController();
            fxmladdmTab = loaderDDMTab.getController();
            fxmlaioTab = loaderIOTab.getController();
            tabPane.getTabs().get(0).setOnSelectionChanged(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    tbbtnItegrate.setDisable(true);
                    tbbtnSave.setDisable(true);
                }
            });
            tabPane.getTabs().get(1).setOnSelectionChanged(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    tbbtnItegrate.setDisable(true);
                    tbbtnSave.setDisable(true);
                }
            });
            tabPane.getTabs().get(2).setOnSelectionChanged(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    tbbtnItegrate.setDisable(true);
                    tbbtnSave.setDisable(true);
                }
            });
            tabPane.getTabs().get(3).setOnSelectionChanged(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    tbbtnItegrate.setDisable(true);
                    tbbtnSave.setDisable(true);
                }
            });
            tabPane.getTabs().get(4).setOnSelectionChanged(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    tbbtnItegrate.setDisable(false);
                    tbbtnSave.setDisable(false);
                }
            });

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
        loadClass(classFile);
    }

    private void loadClass(File file) {
        App.getInstance().esvm = new ESVM(new Vmspec(128, 2, 128, file.getPath()));
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

        App.getInstance().breakpoints = new boolean[App.getInstance().esvm.getGlobal().getInstructionsCount()];
        for (int i = 0; i < App.getInstance().esvm.getGlobal().getInstructionsCount(); i++) { //Заполняем массив брекпоинтов пустышками
            App.getInstance().breakpoints[i] = false;
        }
        AsmLine[] asmLines = App.getInstance().esvm.getDisassembler().getAsm();
        fxmladdmTab.initCodePane(asmLines);
        fxmladdmTab.initBinPane(file);
        fxmladdmTab.initStackPane();
        fxmladdmTab.initDebug();
        fxmlaioTab.classLoaded();
        App.getInstance().loadMemoryDupmFromVm(fxmlaDumpTab);
        tbbtnRun.setDisable(false);
    }

    /**
     * Запускает свознове выполнение программы
     *
     */
    private void actionRun() {
        App.getInstance().esvm.getExecutor().execute();
        tbbtnDown.setDisable(false);
        tbbtnStop.setDisable(false);
        tbbtnResume.setDisable(false);
        tbbtnRun.setDisable(true); //Обработчик прерывания завершения работы программы
        App.getInstance().esvm.getInterruptsManager().setInterruptInterface_1_1(new InterruptsManager.InterruptInterface_1_1() {
            @Override
            public void onInterrupt_interface_1_1() {
                tbbtnDown.setDisable(true);
                tbbtnStop.setDisable(true);
                tbbtnResume.setDisable(true);
                tbbtnRun.setDisable(false);
            }
        });
    }

    private void actionDown() {
        if (fxmladdmTab.currentLine != App.getInstance().breakpoints.length - 1) {
            App.getInstance().breakpoints[fxmladdmTab.currentLine + 1] = true;
        } else {
            tbbtnDown.setDisable(true);
            tbbtnStop.setDisable(true);
            tbbtnResume.setDisable(true);
            tbbtnRun.setDisable(false);
        }
        App.getInstance().esvm.getGlobal().getExecutorThread().resume();
        fxmladdmTab.dehighlightAsmAll();
    }

    private void actionStop() {
        App.getInstance().esvm.getGlobal().getExecutorThread().stop();
        tbbtnDown.setDisable(true);
        tbbtnStop.setDisable(true);
        tbbtnResume.setDisable(true);
        fxmladdmTab.dehighlightAsmAll();
    }

    private void actionResume() {
        App.getInstance().esvm.getGlobal().getExecutorThread().resume();
        tbbtnDown.setDisable(true);
        tbbtnStop.setDisable(true);
        tbbtnResume.setDisable(true);
        fxmladdmTab.dehighlightAsmAll();
    }

    /**
     * Сохраняет ассеблерный код из вкладки asm в class-файл
     *
     */
    private void actionSave() {
        Assembler assembler = new Assembler();
        assembler.setOnErrorListener(new Assembler.ErrorListener() {
            @Override
            public void onErrorListener(String text) {
                FXMLAAsmTab.getInstance().putError(text);
            }
        });
        String code = FXMLAAsmTab.getInstance().getCode();
        if (code == null || Objects.equals(code, "")) {
            StandartDialogs.showErrorDialog("Assembler editor error", "Code is empty");
            return;
        }
        ArrayList<Byte> bytes = assembler.trnslate(FXMLAAsmTab.getInstance().getCode());
        if (bytes != null) {
            byte[] bytesprim = new byte[bytes.size()];
            for (int i = 0; i < bytesprim.length; i++) {
                bytesprim[i] = bytes.get(i);
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ESVM Class", "*.esvmc"), new FileChooser.ExtensionFilter("All Files", "*.*"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bytesprim);
                    fos.close();
                    if (!file.getPath().contains(".esvmc")) {
                        File filed = new File(file.getPath() + ".esvmc");
                        Files.copy(file.toPath(), filed.toPath());
                        Files.delete(file.toPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void actionIntegrate() {
        Assembler assembler = new Assembler();
        assembler.setOnErrorListener(new Assembler.ErrorListener() {
            @Override
            public void onErrorListener(String text) {
                FXMLAAsmTab.getInstance().putError(text);
            }
        });
        String code = FXMLAAsmTab.getInstance().getCode();
        if (code == null || Objects.equals(code, "")) {
            StandartDialogs.showErrorDialog("Assembler editor error", "Code is empty");
            return;
        }
        ArrayList<Byte> bytes = assembler.trnslate(FXMLAAsmTab.getInstance().getCode());
        if (bytes != null) {
            byte[] bytesprim = new byte[bytes.size()];
            for (int i = 0; i < bytesprim.length; i++) {
                bytesprim[i] = bytes.get(i);
            }
            File file = new File("/home/serbis/Projects/JAVA/ESVM/integ.esvmc");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytesprim);
                fos.close();
                loadClass(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void actionExit() {
        stage.close();
    }

    @FXML protected void handleslmenuOpenClassAction(ActionEvent event) {
        actionOpenClass();
    }

    @FXML protected void handleslmenuOpenDumpAction(ActionEvent event) {
        actionOpenDump();
    }

    @FXML protected void handleslmenuExitAction(ActionEvent event) {
        actionExit();
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

    @FXML protected void handlesltbbtnDownAction(ActionEvent event) {
        actionDown();
    }

    @FXML protected void handlesltbbtnStopAction(ActionEvent event) {
        actionStop();
    }

    @FXML protected void handlesltbbtnResumeAction(ActionEvent event) {
        actionResume();
    }

    @FXML protected void handlesltbbtnIntegrateAction(ActionEvent event) {
        actionIntegrate();
    }

    @FXML protected void handlesltbbtnSaveAction(ActionEvent event) {
        actionSave();
    }



}
