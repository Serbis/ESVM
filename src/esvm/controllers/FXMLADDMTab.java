package esvm.controllers;

import esvm.App;
import esvm.controllers.controls.ImageViewBreakpoint;
import esvm.controllers.controls.LabelCode;
import esvm.vm.desc.AsmLine;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLADDMTab implements Initializable{
    @FXML private ScrollPane asmScroll;
    @FXML private ScrollPane binScroll;

    private static FXMLADDMTab instance;
    private Stage stage;
    private boolean[] breaks;
    private final Background backgroundSelect = new Background(new BackgroundFill(Color.web("9E9E9E"), new CornerRadii(1), new Insets(0.0,0.0,0.0,0.0)));
    final Label[] lastBinLabel = {null};


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
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(10);
        gridPane.setVgap(3);
        gridPane.getColumnConstraints().add(new ColumnConstraints(20, 20, 20));
        gridPane.getColumnConstraints().add(new ColumnConstraints(20, 20, 20));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300, 300, 300));

        for (int i = 0; i < lines.length; i++) {
            breaks[i] = false;
            LabelCode labelc = new LabelCode(lines[i].com);
            labelc.setPrefHeight(20);
            labelc.setPrefWidth(300);
            labelc.setOffset(lines[i].byteoffset);
            Label labeln = new Label(String.valueOf(i + 1));
            ImageViewBreakpoint bp = new ImageViewBreakpoint();
            Image image = new Image("file:images/break.png", 16, 16, true, true);
            //bp.setImage(image);
            bp.setPos(i);
            bp.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (!breaks[bp.getPos()]) {
                        bp.setImage(image);
                        breaks[bp.getPos()] = true;
                    } else {
                        bp.setImage(null);
                        breaks[bp.getPos()] = false;
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
            gridPane.add(bp, 0, i);
            gridPane.add(labeln, 1, i);
            gridPane.add(labelc, 2, i);
        }

        hBox.getChildren().addAll(gridPane);
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

}
