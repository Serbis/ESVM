package esvm.controllers;


import esvm.dialogs.StandartDialogs;
import esvm.models.RowModelSingle;
import esvm.models.XCellSingle;
import esvm.vm.desc.constpool.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLABcTypeSelectorTab implements Initializable{
    @FXML private ListView lvList;

    private static FXMLABcTypeSelectorTab instance;
    private Stage stage;
    private SelectorMode mode;
    private ArrayList<ArrayList<String>> constDialogLabels = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> attrDialogLabels = new ArrayList<ArrayList<String>>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
    }

    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLABcTypeSelectorTab getInstance() {
        return instance;
    }

    public void setMode(SelectorMode selectorMode) {
        mode = selectorMode;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initList() {
        ContextMenu contextMenu = new ContextMenu();
        lvList.setCellFactory(new Callback<ListView<RowModelSingle>, ListCell<RowModelSingle>>() {
            @Override
            public ListCell<RowModelSingle> call(javafx.scene.control.ListView<RowModelSingle> param) {
                return new XCellSingle(contextMenu);
            }
        });
        ArrayList<RowModelSingle> rowModels = new ArrayList<RowModelSingle>();
        if (mode.equals(SelectorMode.CONSTANT)) {
            rowModels.add(new RowModelSingle("constant_Integer"));
            rowModels.add(new RowModelSingle("constant_Float"));
            rowModels.add(new RowModelSingle("constant_String"));
            rowModels.add(new RowModelSingle("constant_Utf_8"));
            rowModels.add(new RowModelSingle("constant_MethodHandle"));
            rowModels.add(new RowModelSingle("constant_MethodType"));
        } else {
            rowModels.add(new RowModelSingle("attribute_Code"));
        }
        lvList.getItems().addAll(rowModels);
    }
    private void actionOK() {
        int index = lvList.getSelectionModel().getSelectedIndex();
        if (mode.equals(SelectorMode.CONSTANT)) {
            if (index == 0) {
                String result = StandartDialogs.showTextInputDialog("constant_Integer", "", "", "bytes (in INT)");
                if (result != null) {
                    FXMLACFCTab.getInstance().addItemToConstantPool(String.valueOf("type=" + "constant_Integer                 " + "value=" + result));
                    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
                    byteBuffer.putInt(Integer.parseInt(result));
                    FXMLACFCTab.getInstance().constantPoolArray.add(new ConstInteger(byteBuffer.array()));
                    stage.close();
                }
            } else if (index == 1) {
                String result = StandartDialogs.showTextInputDialog("constant_Float", "", "", "bytes (in FLOAT)");
                if (result != null) {
                    FXMLACFCTab.getInstance().addItemToConstantPool(String.valueOf("type=" + "constant_Float                 " + "value=" + result));
                    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
                    byteBuffer.putFloat(Float.parseFloat(result));
                    FXMLACFCTab.getInstance().constantPoolArray.add(new ConstFloat(byteBuffer.array()));
                    stage.close();
                }
            } else if (index == 2) {
                String result = StandartDialogs.showTextInputDialog("constant_String", "", "", "string_index");
                if (result != null) {
                    FXMLACFCTab.getInstance().addItemToConstantPool(String.valueOf("type=" + "constant_String                 " + "string_index=" + result));
                    FXMLACFCTab.getInstance().constantPoolArray.add(new ConstString(Short.parseShort(result)));
                    stage.close();
                }
            } else if (index == 3) {
                String result = StandartDialogs.showTextInputDialog("constant_Utf_8", "", "", "bytes (in UTF-8)");
                if (result != null) {
                    FXMLACFCTab.getInstance().addItemToConstantPool(String.valueOf("type=" + "constant_Utf_8                 " + "lenght=" + String.valueOf(result.length()) + "                value=" + result));
                    FXMLACFCTab.getInstance().constantPoolArray.add(new ConstUtf_8(result.length(), result.getBytes()));
                    stage.close();
                }
            } else if (index == 4) {
                StandartDialogs.DialogRetTwoField result = StandartDialogs.showTwoFieldDialog(new StandartDialogs.DialogTwoFieldDesctrption("reference_kind", "reference_index", "constant_MethodHandle", ""));
                if (result != null) {
                    FXMLACFCTab.getInstance().addItemToConstantPool(String.valueOf("type=" + "constant_MethodHandle  " + "reference_index=" + result.f1 + " reference_kind=" + result.f2));
                    FXMLACFCTab.getInstance().constantPoolArray.add(new ConstMethodHandle((byte) Integer.parseInt(result.f1), Short.parseShort(result.f2)));
                    stage.close();
                }
            } else if (index == 5) {
                String result = StandartDialogs.showTextInputDialog("constant_MethodType", "", "", "descriptor_index");
                if (result != null) {
                    FXMLACFCTab.getInstance().addItemToConstantPool(String.valueOf("type=" + "constant_MethodType                 " + "descriptor_index=" + result));
                    FXMLACFCTab.getInstance().constantPoolArray.add(new ConstMethodType(Short.parseShort(result)));
                    stage.close();
                }
            }
        } else {
            if (index == 0) {
                FXMLAAsmTab fxmlaAsmTab;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("asm.fxml"));
                VBox pane = null;
                try {
                    pane = (VBox) loader.load();
                    fxmlaAsmTab = loader.getController();
                    Scene scene = new Scene(pane);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Code");
                    fxmlaAsmTab.setStage(stage);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.close();
            }
        }
    }

    @FXML protected void handleslbtnCancelAction(ActionEvent event) {
        stage.close();
    }

    @FXML protected void handleslbtnOkAction(ActionEvent event) {
        actionOK();
    }

    public enum SelectorMode {
        CONSTANT, ATTRIBUTE
    }
}
