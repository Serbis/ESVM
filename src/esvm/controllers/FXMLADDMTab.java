package esvm.controllers;

import esvm.App;
import esvm.controllers.controls.ImageViewBreakpoint;
import esvm.controllers.controls.LabelCode;
import esvm.models.RowModelMaps;
import esvm.models.XCell;
import esvm.vm.InterruptsManager;
import esvm.vm.desc.AsmLine;
import esvm.vm.exceptions.MemoryNullBlockException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLADDMTab implements Initializable{
    @FXML private ScrollPane asmScroll;
    @FXML private ScrollPane binScroll;
    @FXML private ScrollPane mapsScroll;
    @FXML private ScrollPane stackdumpScroll;
    @FXML private TextArea taExceptions;

    private static FXMLADDMTab instance;
    private Stage stage;
    private boolean[] breaks;
    private final Background backgroundSelect = new Background(new BackgroundFill(Color.web("9E9E9E"), new CornerRadii(1), new Insets(0.0,0.0,0.0,0.0)));
    private final Background backgroundHighlight = new Background(new BackgroundFill(Color.web("FF7A7A"), new CornerRadii(1), new Insets(0.0,0.0,0.0,0.0)));
    final Label[] lastBinLabel = {null};
    final Label[] lastAsmHighlitLabel = {null};
    public int currentLine = 0;
    private String exceptionsText = "";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initToolBar();
        setAsmSelectListener();

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
    public static FXMLADDMTab getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setAsmSelectListener() {
        App.getInstance().setOnAsmSelectListener(new App.OnAsmSelectListener() {
            @Override
            public void onAsmSelect(App.HexTableClass hexTableClass, int offset) {
                setBinSelectPosition(offset);
            }
        });
    }

    /**
     * Инициализирует панель ассмблера. Принимает массив объектов AsmLine
     * из которого формирует построчно массив меток, индексы строк и
     * breakpoinты. Вызывается при загрузке класс файла
     *
     * @param lines массив ассемблерных строк
     */
    public void initCodePane(AsmLine[] lines) {
        breaks = new boolean[lines.length];
        final Label[] lastLabel = {null};

        HBox hBox = new HBox();
        GridPane gridPaneCode = new GridPane();
        gridPaneCode.setPadding(new Insets(5));
        gridPaneCode.setHgap(10);
        gridPaneCode.setVgap(3);
        gridPaneCode.getColumnConstraints().add(new ColumnConstraints(300, 300, 300));

        GridPane gridPaneLeftSide = new GridPane();
        gridPaneLeftSide.setPadding(new Insets(5));
        gridPaneLeftSide.setHgap(10);
        gridPaneLeftSide.setVgap(3);
        gridPaneLeftSide.getColumnConstraints().add(new ColumnConstraints(20, 20, 20));
        gridPaneLeftSide.getColumnConstraints().add(new ColumnConstraints(20, 20, 20));

        for (int i = 0; i < lines.length; i++) {
            breaks[i] = false;
            LabelCode labelc = new LabelCode(lines[i].com);
            labelc.setPrefHeight(20);
            labelc.setPrefWidth(300);
            labelc.setOffset(lines[i].byteoffset);
            Label labeln = new Label(String.valueOf(i + 1));
            labeln.setPrefHeight(20);
            ImageViewBreakpoint bp = new ImageViewBreakpoint();
            Image imagecircle = new Image("file:images/break.png", 16, 16, true, true);
            Image imageblank = new Image("file:images/blank.png", 16, 16, true, true);
            bp.setImage(imageblank);
            bp.setPos(i);
            bp.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (!breaks[bp.getPos()]) {
                        bp.setImage(imagecircle);
                        breaks[bp.getPos()] = true;
                        App.getInstance().breakpoints[bp.getPos()] = true;
                    } else {
                        bp.setImage(imageblank);
                        breaks[bp.getPos()] = false;
                        App.getInstance().breakpoints[bp.getPos()] = false;
                    }
                }
            });
            labelc.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (lastLabel[0] != null) {
                        lastLabel[0].setBackground(null);
                        labelc.setBackground(backgroundSelect);
                        lastLabel[0] = labelc;
                        App.getInstance().onAsmSelectListener.onAsmSelect(App.HexTableClass.BIN, labelc.getOffset());
                    } else {
                        labelc.setBackground(backgroundSelect);
                        lastLabel[0] = labelc;
                    }
                }
            });
            gridPaneLeftSide.add(bp, 0, i);
            gridPaneLeftSide.add(labeln, 1, i);
            gridPaneCode.add(labelc, 0, i);
        }

        hBox.getChildren().addAll(gridPaneLeftSide, gridPaneCode);
        asmScroll.setContent(hBox);
    }

    /**
     * Инициализирует заполнение панели сырого байт-кода. Вызывается при
     * загрузке класс файла
     *
     * @param file
     */
    public void initBinPane(File file) {
        binScroll.setContent(App.getInstance().constructHexLayout(file, 128, 9, App.HexTableClass.BIN));
    }

    /**
     * Инициализирует панель дампа стека
     *
     */
    public void initStackPane() {
        //App.getInstance().loadStackDupmFromVm(this);
    }

    /**
     * Определяет обработчик прерывания смены инструкции в вм, и прерывания
     * исключений. Останавливает выполнение и передает управление в метод
     * breakExec.
     *
     */
    public void initDebug() {
        App.getInstance().esvm.getInterruptsManager().setInterruptInterface_1_0(new InterruptsManager.InterruptInterface_1_0() {
            @Override
            public void onInterrupt_interface_1_0(int pos) {
                if (App.getInstance().breakpoints[pos]) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            breakExec(pos);
                        }
                    });
                    App.getInstance().esvm.getGlobal().getExecutorThread().pause();
                }
            }
        });
        App.getInstance().esvm.getInterruptsManager().setInterruptInterface_1_2(new InterruptsManager.InterruptInterface_1_2() {
            @Override
            public void onInterrupt_interface_1_2(String text) {
                putException(text);
            }
        });
    }

    /**
     *
     *
     * @param pos позиция останова
     */
    private void breakExec(int pos) {
        currentLine = pos;
        highLightAsm(pos);
        initMapPane();
        initStackPane();
        App.getInstance().loadMemoryDupmFromVm(FXMLADumpTab.getInstance());
        FXMLAMain.getInstance().tbbtnDown.setDisable(false);
        FXMLAMain.getInstance().tbbtnResume.setDisable(false);
        FXMLAMain.getInstance().tbbtnStop.setDisable(false);
    }

    /**
     * Инциализирует панель карт переменных и методов в режие стопа
     *
     */
    private void initMapPane() {
        mapsScroll.setContent(null);
        ListView listView = new ListView();
        MenuItem deleteMenuItem = new MenuItem("...");
        ContextMenu contextMenu = new ContextMenu(deleteMenuItem);

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        listView.setCellFactory(new Callback<javafx.scene.control.ListView<RowModelMaps>, ListCell<RowModelMaps>>() {
            @Override
            public ListCell<RowModelMaps> call(javafx.scene.control.ListView<RowModelMaps> param) {
                return new XCell(contextMenu);
            }
        });
        ArrayList<RowModelMaps> rowModels = new ArrayList<RowModelMaps>();
        for (int i = 0; i < App.getInstance().esvm.getGlobal().varMap.size(); i++) {
            String id = "id=" + String.valueOf(App.getInstance().esvm.getGlobal().varMap.get(i).id);
            String type = "  type=";
            String value = "  value=";
            try {
                byte[] vb = App.getInstance().esvm.getMemoryManager().readBlock(App.getInstance().esvm.getGlobal().varMap.get(i).pointer);
                ByteBuffer byteBuffer = ByteBuffer.wrap(vb);
                if (vb.length == 4) {
                    type += "int";
                    value += String.valueOf(byteBuffer.getInt());
                } else if (vb.length == 2) {
                    type += "short";
                    value += String.valueOf(byteBuffer.getShort());
                } else if (vb.length == 1) {
                    value += String.valueOf(byteBuffer.getChar());
                    type += "byte";
                } else {
                    value += "undef";
                    for (byte aVb : vb) {
                        String hex = Integer.toHexString(aVb).toUpperCase();
                        if (hex.length() == 1) {
                            hex = "0" + hex;
                        }
                        value += hex + " ";
                    }
                }
            } catch (MemoryNullBlockException e) {
                e.printStackTrace();
            }

            rowModels.add(new RowModelMaps(id, type, value));
        }

        listView.getItems().addAll(rowModels);
        mapsScroll.setContent(listView);
    }

    /**
     * Устанавливает маркер в массиве сырого байт-кода. Вызывается в случае
     * выбора какой либо ассемлерной строки
     *
     * @param pos
     */
    public void setBinSelectPosition(int pos) {
        VBox vBox = (VBox) binScroll.getContent();
        GridPane gridPane = (GridPane) vBox.getChildren().get(0);
        int dec = Math.round(pos / 8) + 1;

        Label label = (Label) gridPane.getChildren().get(pos + dec);

        if (lastBinLabel[0] != null) {
            lastBinLabel[0].setBackground(null);
            label.setBackground(backgroundSelect);
            lastBinLabel[0] = label;
        } else {
            label.setBackground(backgroundSelect);
            lastBinLabel[0] = label;
        }
    }

    /**
     * Назначает контент для сролла дампа стека
     *
     * @param vBox vBox
     */
    public void setStackDumpPane(VBox vBox) {
        stackdumpScroll.setContent(vBox);
    }


    /**
     * Подсвечивает в блоке ассемблерного кода строку, на которой стоит
     * текущий оснатнов
     *
     * @param pos номер строки
     */
    public void highLightAsm(int pos) {
        HBox hBox = (HBox) asmScroll.getContent();
        GridPane gridPane = (GridPane) hBox.getChildren().get(1);

        Label label = (Label) gridPane.getChildren().get(pos);

        if (lastAsmHighlitLabel[0] != null) {
            lastAsmHighlitLabel[0].setBackground(null);
            label.setBackground(backgroundHighlight);
            lastAsmHighlitLabel[0] = label;
        } else {
            label.setBackground(backgroundHighlight);
            lastAsmHighlitLabel[0] = label;
        }
    }

    /**
     * Убирает все подсветки с ассембленого кода
     *
     */
    public void dehighlightAsmAll() {
        lastAsmHighlitLabel[0].setBackground(null);
    }

    public void putException(String text) {
        exceptionsText += text + String.valueOf(exceptionsText.length()) + "\n";

        taExceptions.setText(exceptionsText);
    }

}
