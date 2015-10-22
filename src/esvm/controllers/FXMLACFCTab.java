package esvm.controllers;

import esvm.vm.desc.ClassField;
import esvm.vm.desc.ClassMethod;
import esvm.vm.desc.attributes.AttrCode;
import esvm.vm.desc.attributes.ClassAttribute;
import esvm.dialogs.StandartDialogs;
import esvm.enums.AccessFlags;
import esvm.models.RowModelSingle;
import esvm.models.XCellSingle;
import esvm.vm.compiler.Assembler;
import esvm.vm.desc.constpool.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLACFCTab implements Initializable{
    @FXML private ListView lvConstantPool;
    @FXML private ListView lvInterfaces;
    @FXML private ListView lvFields;
    @FXML private ListView lvMethds;
    @FXML private ListView lvAttributes;
    @FXML private TextField tfMinor;
    @FXML private TextField tfMajor;
    @FXML private TextField tfThis;
    @FXML private TextField tfSuper;
    @FXML private CheckBox cbPublic;
    @FXML private CheckBox cbPrivate;
    @FXML private CheckBox cbProtected;
    @FXML private CheckBox cbStatic;
    @FXML private CheckBox cbFinal;
    @FXML private CheckBox cbVolatile;
    @FXML private CheckBox cbTrtansient;
    @FXML private CheckBox cbSynthetic;
    @FXML private CheckBox cbEnum;

    private static FXMLACFCTab instance;
    private Stage stage;

    public ArrayList<ClassConstant> constantPoolArray = new ArrayList<ClassConstant>(); //Массив констант
    public ArrayList<ClassField> fieldsArray = new ArrayList<ClassField>(); //Массив полей
    public ArrayList<ClassMethod> methodArray = new ArrayList<ClassMethod>(); //Массив полей
    public ArrayList<ClassAttribute> attrArray = new ArrayList<ClassAttribute>(); //Массив аттрибутов

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        byte[] bytes1 = new byte[] {0, 0, 0, 1};
        byte[] bytes2 = new byte[] {62, 100, 100, 100};
        byte[] bytes3 = new byte[] {66, 66, 66, 66};
        constantPoolArray.add(new ConstInteger(bytes1));
        constantPoolArray.add(new ConstFloat(bytes2));
        constantPoolArray.add(new ConstString((short) 3));
        constantPoolArray.add(new ConstUtf_8(4, bytes3));
        constantPoolArray.add(new ConstMethodHandle((byte) 0, (short) 1));
        constantPoolArray.add(new ConstMethodType((short) 1));
        fieldsArray.add(new ClassField(AccessFlags.ACC_PUBLIC, (short) 1, (short) 1));
        methodArray.add(new ClassMethod(AccessFlags.ACC_PUBLIC, (short) 1, (short) 1, (short) 0));
        attrArray.add(new AttrCode("Push(1);Pop(1);Int(1, 1);"));

        initLVContants();
        initLVFields();
        initLVMethods();
        initLVAttributes();

        instance = this;
    }


    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLACFCTab getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void initLVContants() {
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");
        ContextMenu contextMenu = new ContextMenu(deleteMenuItem, editMenuItem);

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (int i = lvConstantPool.getSelectionModel().getSelectedIndex() + 1; i < constantPoolArray.size(); i++) {
                    RowModelSingle rowModelSingle = (RowModelSingle) lvConstantPool.getItems().get(i);
                    String str = String.valueOf(i - 1) + rowModelSingle.text.substring(1, rowModelSingle.text.length());
                    lvConstantPool.getItems().set(i, new RowModelSingle(str));
                    ClassConstant classConstant = constantPoolArray.get(i);
                    classConstant.tag = classConstant.tag + 1;
                    constantPoolArray.set(i, classConstant);

                }

                constantPoolArray.remove(lvConstantPool.getSelectionModel().getSelectedIndex());
                lvConstantPool.getItems().remove(lvConstantPool.getSelectionModel().getSelectedIndex());

            }
        });
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        lvConstantPool.setCellFactory(new Callback<ListView<RowModelSingle>, ListCell<RowModelSingle>>() {
            @Override
            public ListCell<RowModelSingle> call(javafx.scene.control.ListView<RowModelSingle> param) {
                return new XCellSingle(contextMenu);
            }
        });
        ArrayList<RowModelSingle> rowModels = new ArrayList<RowModelSingle>();
        StringBuilder sb;
        ByteBuffer byteBuffer;
        for (int i = 0; i < constantPoolArray.size(); i++) {
            sb = new StringBuilder();
            switch (constantPoolArray.get(i).name) {
                case "constant_Float":
                    ConstFloat constFloat = (ConstFloat) constantPoolArray.get(i);
                    sb.append(String.valueOf(i + "  "));
                    sb.append(String.valueOf("type=" + constFloat.name + "                 "));
                    byteBuffer = ByteBuffer.wrap(constFloat.bytes);
                    sb.append(String.valueOf("value=" + byteBuffer.getFloat()));
                    rowModels.add(new RowModelSingle(sb.toString()));
                    break;

                case "constant_Integer":
                    ConstInteger constInt = (ConstInteger) constantPoolArray.get(i);
                    sb.append(String.valueOf(i + "  "));
                    sb.append(String.valueOf("type=" + constInt.name + "              "));
                    byteBuffer = ByteBuffer.wrap(constInt.bytes);
                    sb.append(String.valueOf("value=" + byteBuffer.getInt()));
                    rowModels.add(new RowModelSingle(sb.toString()));
                    break;

                case "constant_String":
                    ConstString constString = (ConstString) constantPoolArray.get(i);
                    sb.append(String.valueOf(i + "  "));
                    sb.append(String.valueOf("type=" + constString.name + "                "));
                    sb.append(String.valueOf("string_index=" + constString.string_index));
                    rowModels.add(new RowModelSingle(sb.toString()));
                    break;

                case "constant_Utf_8":
                    ConstUtf_8 constUtf_8 = (ConstUtf_8) constantPoolArray.get(i);
                    sb.append(String.valueOf(i + "  "));
                    sb.append(String.valueOf("type=" + constUtf_8.name + "                 "));
                    sb.append(String.valueOf("length=" + constUtf_8.lenght + "                "));
                    sb.append(String.valueOf("value=" + new String(constUtf_8.bytes, StandardCharsets.UTF_8)));
                    rowModels.add(new RowModelSingle(sb.toString()));
                    break;

                case "constant_MethodHandle":
                    ConstMethodHandle constMethodHandle = (ConstMethodHandle) constantPoolArray.get(i);
                    sb.append(String.valueOf(i + "  "));
                    sb.append(String.valueOf("type=" + constMethodHandle.name + "  "));
                    sb.append(String.valueOf("reference_index=" + constMethodHandle.reference_index + " "));
                    sb.append(String.valueOf("reference_kind=" + constMethodHandle.reference_kind + " "));
                    rowModels.add(new RowModelSingle(sb.toString()));
                    break;

                case "constant_MethodType":
                    ConstMethodType constMethodType = (ConstMethodType) constantPoolArray.get(i);
                    sb.append(String.valueOf(i + "  "));
                    sb.append(String.valueOf("type=" + constMethodType.name + "     "));
                    sb.append(String.valueOf("descriptor_index=" + constMethodType.descriptor_index));
                    rowModels.add(new RowModelSingle(sb.toString()));
                    break;
            }
        }
        lvConstantPool.getItems().addAll(rowModels);
    }

    private void initLVFields() {
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");
        ContextMenu contextMenu = new ContextMenu(deleteMenuItem, editMenuItem);

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (int i = lvFields.getSelectionModel().getSelectedIndex() + 1; i < fieldsArray.size(); i++) {
                    RowModelSingle rowModelSingle = (RowModelSingle) lvFields.getItems().get(i);
                    String str = String.valueOf(i - 1) + rowModelSingle.text.substring(1, rowModelSingle.text.length());
                    lvFields.getItems().set(i, new RowModelSingle(str));
                }
                fieldsArray.remove(lvFields.getSelectionModel().getSelectedIndex());
                lvFields.getItems().remove(lvFields.getSelectionModel().getSelectedIndex());
            }
        });
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        lvFields.setCellFactory(new Callback<ListView<RowModelSingle>, ListCell<RowModelSingle>>() {
            @Override
            public ListCell<RowModelSingle> call(javafx.scene.control.ListView<RowModelSingle> param) {
                return new XCellSingle(contextMenu);
            }
        });
        ArrayList<RowModelSingle> rowModels = new ArrayList<RowModelSingle>();
        StringBuilder sb;
        for (int i = 0; i < fieldsArray.size(); i++) {
            sb = new StringBuilder();
            sb.append(String.valueOf(i + "  "));
            sb.append(String.valueOf(fieldsArray.get(i).accessFlags + "  "));
            sb.append(String.valueOf("name_index=" + fieldsArray.get(i).name_index + "  "));
            sb.append(String.valueOf("descriptor_index=" + fieldsArray.get(i).descriptor_index + "  "));
            rowModels.add(new RowModelSingle(sb.toString()));
        }
        lvFields.getItems().addAll(rowModels);
    }

    private void initLVMethods() {
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");
        ContextMenu contextMenu = new ContextMenu(deleteMenuItem, editMenuItem);

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (int i = lvMethds.getSelectionModel().getSelectedIndex() + 1; i < methodArray.size(); i++) {
                    RowModelSingle rowModelSingle = (RowModelSingle) lvMethds.getItems().get(i);
                    String str = String.valueOf(i - 1) + rowModelSingle.text.substring(1, rowModelSingle.text.length());
                    lvMethds.getItems().set(i, new RowModelSingle(str));
                }
                methodArray.remove(lvMethds.getSelectionModel().getSelectedIndex());
                lvMethds.getItems().remove(lvMethds.getSelectionModel().getSelectedIndex());
            }
        });
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        lvMethds.setCellFactory(new Callback<ListView<RowModelSingle>, ListCell<RowModelSingle>>() {
            @Override
            public ListCell<RowModelSingle> call(javafx.scene.control.ListView<RowModelSingle> param) {
                return new XCellSingle(contextMenu);
            }
        });
        ArrayList<RowModelSingle> rowModels = new ArrayList<RowModelSingle>();
        StringBuilder sb;
        for (int i = 0; i < methodArray.size(); i++) {
            sb = new StringBuilder();
            sb.append(String.valueOf(i + "  "));
            sb.append(String.valueOf(methodArray.get(i).accessFlags + "  "));
            sb.append(String.valueOf("name_index=" + methodArray.get(i).name_index + "  "));
            sb.append(String.valueOf("descriptor_index=" + methodArray.get(i).descriptor_index + "  "));
            sb.append(String.valueOf("code_index=" + methodArray.get(i).code_index + "  "));
            rowModels.add(new RowModelSingle(sb.toString()));
        }
        lvMethds.getItems().addAll(rowModels);
    }

    private void initLVAttributes() {
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");
        ContextMenu contextMenu = new ContextMenu(deleteMenuItem, editMenuItem);

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (int i = lvAttributes.getSelectionModel().getSelectedIndex() + 1; i < attrArray.size(); i++) {
                    RowModelSingle rowModelSingle = (RowModelSingle) lvAttributes.getItems().get(i);
                    String str = String.valueOf(i - 1) + rowModelSingle.text.substring(1, rowModelSingle.text.length());
                    lvAttributes.getItems().set(i, new RowModelSingle(str));
                }
                attrArray.remove(lvAttributes.getSelectionModel().getSelectedIndex());
                lvAttributes.getItems().remove(lvAttributes.getSelectionModel().getSelectedIndex());
            }
        });
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        lvAttributes.setCellFactory(new Callback<ListView<RowModelSingle>, ListCell<RowModelSingle>>() {
            @Override
            public ListCell<RowModelSingle> call(javafx.scene.control.ListView<RowModelSingle> param) {
                return new XCellSingle(contextMenu);
            }
        });
        ArrayList<RowModelSingle> rowModels = new ArrayList<RowModelSingle>();
        StringBuilder sb;
        for (int i = 0; i < attrArray.size(); i++) {
            sb = new StringBuilder();
            switch (attrArray.get(i).name) {
                case "attribute_code":
                    sb.append(String.valueOf(i + "  "));
                    sb.append(String.valueOf("attribute_code"));
                    rowModels.add(new RowModelSingle(sb.toString()));
                    break;

            }
        }
        lvAttributes.getItems().addAll(rowModels);
    }

    public void addItemToConstantPool(String arg1) {
        lvConstantPool.getItems().add(new RowModelSingle(String.valueOf(constantPoolArray.size()) + "  " + arg1));
    }

    public void addItemToAttributes(String arg1) {
        lvAttributes.getItems().add(new RowModelSingle(String.valueOf(attrArray.size()) + "  " + arg1));
    }

    public void saveClassFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ESVM Class", "*.esvmc"), new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(constructClassFile());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] constructClassFile() {
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        byte[] bt;
        ByteBuffer byteBuffer;
        bytes.add((byte) 111);    //Magic number
        bytes.add((byte) 111);
        byteBuffer = ByteBuffer.allocate(2);      //Minor V
        byteBuffer.putShort(Short.parseShort(tfMajor.getText()));
        bt = byteBuffer.array();
        for (byte aBt : bt) {
            bytes.add(aBt);
        }
        byteBuffer = ByteBuffer.allocate(2);    //Major V
        byteBuffer.putShort(Short.parseShort(tfMajor.getText()));
        bt = byteBuffer.array();
        for (byte aBt : bt) {
            bytes.add(aBt);
        }
        byteBuffer = ByteBuffer.allocate(2);    //Constant_pool_count
        byteBuffer.putShort(Short.parseShort(String.valueOf(constantPoolArray.size())));
        bt = byteBuffer.array();
        for (byte aBt : bt) {
            bytes.add(aBt);
        }
        for (int i = 0; i < constantPoolArray.size(); i++) { //Constant_pool
            bytes.add((byte) constantPoolArray.get(i).tag);
            switch (constantPoolArray.get(i).name) {
                case "constant_Float":
                    ConstFloat constFloat = (ConstFloat) constantPoolArray.get(i);
                    bt = constFloat.bytes;
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    break;

                case "constant_Integer":
                    ConstInteger constInt = (ConstInteger) constantPoolArray.get(i);
                    bt = constInt.bytes;
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    break;

                case "constant_String":
                    ConstString constString = (ConstString) constantPoolArray.get(i);
                    byteBuffer = ByteBuffer.allocate(2);
                    byteBuffer.putShort(Short.parseShort(String.valueOf(constString.string_index)));
                    bt = byteBuffer.array();
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    break;

                case "constant_Utf_8":
                    ConstUtf_8 constUtf_8 = (ConstUtf_8) constantPoolArray.get(i);
                    byteBuffer = ByteBuffer.allocate(2);
                    byteBuffer.putShort(Short.parseShort(String.valueOf(constUtf_8.lenght)));
                    bt = byteBuffer.array();
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    bt = constUtf_8.bytes;
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    break;

                case "constant_MethodHandle":
                    ConstMethodHandle constMethodHandle = (ConstMethodHandle) constantPoolArray.get(i);
                    byteBuffer = ByteBuffer.allocate(2);
                    byteBuffer.putShort(Short.parseShort(String.valueOf(constMethodHandle.reference_index)));
                    bt = byteBuffer.array();
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    bytes.add(constMethodHandle.reference_kind);
                    break;

                case "constant_MethodType":
                    ConstMethodType constMethodType = (ConstMethodType) constantPoolArray.get(i);
                    byteBuffer = ByteBuffer.allocate(2);
                    byteBuffer.putShort(Short.parseShort(String.valueOf(constMethodType.descriptor_index)));
                    bt = byteBuffer.array();
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    break;
            }
        }
        bytes.add((byte) 0); //Access_flag - тут пока пусто
        bytes.add((byte) 0);
        bytes.add((byte) 0); //Super_class - тут пока пусто
        bytes.add((byte) 0);
        bytes.add((byte) 0); //This_class - тут пока пусто
        bytes.add((byte) 0);
        bytes.add((byte) 0); //Interface_count - тут пока пусто
        bytes.add((byte) 0);
        byteBuffer = ByteBuffer.allocate(2);    //Field_count
        byteBuffer.putShort(Short.parseShort(String.valueOf(fieldsArray.size())));
        bt = byteBuffer.array();
        for (byte aBt : bt) {
            bytes.add(aBt);
        }
        for (int i = 0; i < fieldsArray.size(); i++) {  //Fields
            bytes.add((byte) 0); //af - они пока нелвые
            bytes.add((byte) 0); //af - они пока нелвые
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort(Short.parseShort(String.valueOf(fieldsArray.get(i).name_index)));
            bt = byteBuffer.array();
            for (byte aBt : bt) {
                bytes.add(aBt);
            }
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort(Short.parseShort(String.valueOf(fieldsArray.get(i).descriptor_index)));
            bt = byteBuffer.array();
            for (byte aBt : bt) {
                bytes.add(aBt);
            }
        }
        byteBuffer = ByteBuffer.allocate(2);    //Method_count
        byteBuffer.putShort(Short.parseShort(String.valueOf(methodArray.size())));
        bt = byteBuffer.array();
        for (byte aBt : bt) {
            bytes.add(aBt);
        }
        for (int i = 0; i < fieldsArray.size(); i++) {  //Methods
            bytes.add((byte) 0); //af - они пока нелвые
            bytes.add((byte) 0); //af - они пока нелвые
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort(Short.parseShort(String.valueOf(methodArray.get(i).name_index)));
            bt = byteBuffer.array();
            for (byte aBt : bt) {
                bytes.add(aBt);
            }
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort(Short.parseShort(String.valueOf(methodArray.get(i).descriptor_index)));
            bt = byteBuffer.array();
            for (byte aBt : bt) {
                bytes.add(aBt);
            }
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort(Short.parseShort(String.valueOf(methodArray.get(i).code_index)));
            bt = byteBuffer.array();
            for (byte aBt : bt) {
                bytes.add(aBt);
            }
        }
        byteBuffer = ByteBuffer.allocate(2);    //Attribute_count
        byteBuffer.putShort(Short.parseShort(String.valueOf(attrArray.size())));
        bt = byteBuffer.array();
        for (byte aBt : bt) {
            bytes.add(aBt);
        }
        for (int i = 0; i < attrArray.size(); i++) {    //Attribute
            switch (attrArray.get(i).name) {
                case "attribute_code":
                    AttrCode attrCode = (AttrCode) attrArray.get(i);
                    byteBuffer = ByteBuffer.allocate(2);
                    byteBuffer.putShort(Short.parseShort(String.valueOf(attrCode.tag)));
                    bt = byteBuffer.array();
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    Assembler assembler = new Assembler();
                    ArrayList<Byte> asm = assembler.trnslate(attrCode.code);
                    byteBuffer = ByteBuffer.allocate(4);
                    byteBuffer.putInt(asm.size());
                    bt = byteBuffer.array();
                    for (byte aBt : bt) {
                        bytes.add(aBt);
                    }
                    for (Byte anAsm : asm) {
                        bytes.add(anAsm);
                    }
            }
        }

        bt = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bt[i] = bytes.get(i);
        }

        return bt;
    }



    private void actionAddConstant(){
        FXMLABcTypeSelectorTab fxmlaBcTypeSelectorTab;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bctypeselector.fxml"));
        VBox pane = null;
        try {
            pane = (VBox)loader.load();
            fxmlaBcTypeSelectorTab = loader.getController();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Select adding type");
            fxmlaBcTypeSelectorTab.setStage(stage);
            fxmlaBcTypeSelectorTab.setMode(FXMLABcTypeSelectorTab.SelectorMode.CONSTANT);
            fxmlaBcTypeSelectorTab.initList();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actionAddInterface(){

    }

    private void actionAddField(){
        StandartDialogs.DialogRetTwoField result = StandartDialogs.showTwoFieldDialog(new StandartDialogs.DialogTwoFieldDesctrption("name_index", "descriptor_index", "Field", ""));
        if (result != null) {
            lvFields.getItems().add(new RowModelSingle(String.valueOf(attrArray.size()) + "  " + AccessFlags.ACC_PUBLIC + "  name_index=" + result.f1 + "  descriptor_index=" + result.f2));
            fieldsArray.add(new ClassField(AccessFlags.ACC_PUBLIC, Short.parseShort(result.f1), Short.parseShort(result.f2)));
        }
    }

    private void actionAddMethod(){
        StandartDialogs.DialogRetThreeField result = StandartDialogs.showThreeFieldDialog(new StandartDialogs.DialogThreeFieldDesctrption("name_index", "descriptor_index", "code_index", "Field", ""));
        if (result != null) {
            lvMethds.getItems().add(new RowModelSingle(String.valueOf(attrArray.size()) + "  " + AccessFlags.ACC_PUBLIC + "  name_index=" + result.f1 + "  descriptor_index=" + result.f2 + "  code_index=" + result.f3));
            methodArray.add(new ClassMethod(AccessFlags.ACC_PUBLIC, Short.parseShort(result.f1), Short.parseShort(result.f2), Short.parseShort(result.f3)));
        }
    }

    private void actionAddAttribute(){
        FXMLABcTypeSelectorTab fxmlaBcTypeSelectorTab;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bctypeselector.fxml"));
        VBox pane = null;
        try {
            pane = (VBox)loader.load();
            fxmlaBcTypeSelectorTab = loader.getController();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Select adding type");
            fxmlaBcTypeSelectorTab.setStage(stage);
            fxmlaBcTypeSelectorTab.setMode(FXMLABcTypeSelectorTab.SelectorMode.ATTRIBUTE);
            fxmlaBcTypeSelectorTab.initList();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML protected void handleslbtnAddConstantAction(ActionEvent event) {
        actionAddConstant();
    }

    @FXML protected void handleslbtnAddInterfaceAction(ActionEvent event) {
        actionAddInterface();
    }

    @FXML protected void handleslbtnAddFieldAction(ActionEvent event) {
        actionAddField();
    }

    @FXML protected void handleslbtnAddMethodAction(ActionEvent event) {
        actionAddMethod();
    }

    @FXML protected void handleslbtnAddAttributeAction(ActionEvent event) {
        actionAddAttribute();
    }

}
