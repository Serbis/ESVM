package esvm;


import esvm.controllers.FXMLADumpTab;
import esvm.controllers.controls.LabelMeta;
import esvm.dialogs.StandartDialogs;
import esvm.fields.Address;
import esvm.fields.ByteModel;
import esvm.vm.ESVM;
import esvm.vm.desc.Pointer;
import esvm.vm.desc.Vmspec;
import esvm.vm.exceptions.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by serbis on 07.10.15.
 */
public class App {
    public interface OnByteSelectListener { //Интерфес вызывает при выборе какого либо байта в hex таблицах
        public void onByteSelect(HexTableClass hexTableClass, Address address, int value);
    }

    OnByteSelectListener onByteSelectListener;

    public static App instance;
    public ESVM esvm;
    private WorkMode mode;


    public App() {
        esvm = new ESVM(new Vmspec(128, 2, 128));
        try {
            esvm.getMemoryManager().write(new Pointer(0, 127), new byte[] {45});
            esvm.getMemoryManager().write(new Pointer(1, 0), new byte[] {127, 127});
            //byte[] bytes = esvm.getMemoryManager().read(new Pointer(1, 127), 3);
            //String[] hexes = new String[bytes.length];
            //for (int i = 0; i < bytes.length; i++) {
            //    hexes[i] = Integer.toHexString(bytes[i]);
            //}
            //StandartDialogs.showErrorDialog("READ", String.valueOf(hexes[0] + hexes[1] + hexes[2]));
            //Pointer alpointer = esvm.getMemoryManager().allocate(4);
            //esvm.getMemoryManager().allocate(2);
            //esvm.getMemoryManager().allocate(4);
            //for (int i = 0; i < 61; i++) {
           //     esvm.getMemoryManager().allocate(4);
           // }
            //esvm.getMemoryManager().writeBlock(new Pointer(1, 2), new byte[]{34, 34, 34, 34});
           // esvm.getMemoryManager().writeBlock(new Pointer(1, 6), new byte[]{22, 22, 22, 22});
            //esvm.getMemoryManager().writeBlock(new Pointer(1, 10), new byte[]{43, 43, 43, 43});
            esvm.getMemoryManager().callocate(new Pointer(1, 6), 4);
            esvm.getMemoryManager().callocate(new Pointer(0, 0), 4);
            esvm.getMemoryManager().writeBlock(new Pointer(1, 6), new byte[]{22, 22, 22, 22});
            esvm.getMemoryManager().rellocate(new Pointer(1, 6), new Pointer(0, 4));
            esvm.getMemoryManager().push(6511);
            esvm.getMemoryManager().push(49634);
            StandartDialogs.showErrorDialog("STACK", String.valueOf(esvm.getMemoryManager().pop() + "/" + esvm.getMemoryManager().pop()));

        } catch (MemoryOutOfRangeException e) {
            e.printStackTrace();
        } catch (MemoryAllocateException e) {
            e.printStackTrace();
        } catch (MemoryNullBlockException e) {
            e.printStackTrace();
        } catch (StackOverflowException e) {
            e.printStackTrace();
        } catch (NullReferenceException e) {
            e.printStackTrace();
        }
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public void setWorkMode(WorkMode workMode) {
        mode = workMode;
    }

    public VBox constructHexLayout(File file, int bs, int bytesPerString) {
        int rowCounter = 0;
        int colCounter = 1;
        boolean newline = true;
        int offset = -1;
        long len = file.length();
        final Background backgroundAddr = new Background(new BackgroundFill(Color.web("9E9E9E"), new CornerRadii(1), new Insets(0.0,0.0,0.0,0.0)));
        final Label[] lastLabel = {null};
        ArrayList<ByteModel> bytesField = new ArrayList<ByteModel>();



        double bc =  (double) len / bs;
        //int blockCount = (int) new BigDecimal(bc).setScale(0, RoundingMode.UP).doubleValue();
        int blockCount = (int) bc;

        ArrayList<ArrayList<String>> hexList = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Byte>> byteArray = new ArrayList<ArrayList<Byte>>();

        for (int i = 0; i < blockCount; i++) {
            ArrayList<Byte> bytes;
            if (i == 0) {
                bytes = readBlockFromFile(file, bs, i * bs, true);
            } else {
                bytes = readBlockFromFile(file, bs, i * bs, false);
            }
            byteArray.add(bytes);
        }


        for (int i = 0; i < byteArray.size(); i++) {
            ArrayList<String> hexArray = new ArrayList<String>();
            for (int j = 0; j < byteArray.get(i).size(); j++) {
                hexArray.add(Integer.toHexString(byteArray.get(i).get(j).intValue()));
            }
            hexList.add(hexArray);

        }

        VBox vBox = new VBox();
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        gridpane.getColumnConstraints().add(new ColumnConstraints(200, 200, 200));
        for (int i = 0; i < bytesPerString; i++) {
            gridpane.getColumnConstraints().add(new ColumnConstraints(30, 30, 30));
        }

        for (int i = 0; i < hexList.size(); i++) {
            for (int j = 0; j < hexList.get(i).size(); j++) {
                offset++;
                if (newline) { //Если это новая трока
                    Label label = new Label(String.valueOf("0x" + Integer.toHexString(i) + ":0x" + Integer.toHexString(offset).toUpperCase()));
                    label.setPrefWidth(100);
                    label.setBackground(backgroundAddr);
                    label.setTextFill(Color.WHITE);
                    gridpane.add(label, 0, rowCounter);
                    newline = false;
                    colCounter++;
                }

                String hexV = hexList.get(i).get(j).toUpperCase();
                if (hexV.length() == 1) {
                    hexV = "0" + hexV;
                }
                final LabelMeta label = new LabelMeta(hexV);
                label.setAdress(i, offset);
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (lastLabel[0] != null) {
                            lastLabel[0].setBackground(null);
                            label.setBackground(backgroundAddr);
                            lastLabel[0] = label;
                            onByteSelectListener.onByteSelect(HexTableClass.DUMP, new Address(label.getBlock(), label.getOffset()), Integer.parseInt(label.getText(), 16));
                        } else {
                            label.setBackground(backgroundAddr);
                            lastLabel[0] = label;
                        }

                    }
                });
                gridpane.add(label, colCounter, rowCounter);

                if (colCounter == bytesPerString) { //Елси это последний байт в строке
                    colCounter = 1;
                    rowCounter++;
                    newline = true;
                } else {
                    colCounter++;
                }
            }
            offset = 0;
        }

        vBox.getChildren().add(gridpane);

        return vBox;

    }

    public ArrayList<Byte> readBlockFromFile(File file, int bs, int offset, boolean delsign) {
        ArrayList<Byte> bytes = new ArrayList<Byte>();

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(offset);
            int bss = 8;
            int bsslen;
            if (delsign) {
                bsslen = bs + 8;
            } else {
                bsslen = bs;
            }
            for (int i = bss; i < bsslen; i++) {
                raf.seek(offset + (i));
                bytes.add(raf.readByte());
            }
            raf.close();
        } catch (EOFException e) {
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;

    }

    public void loadDupmFromVm(FXMLADumpTab fxmlaDumpTab) {
        int signbs;
        int signcount;
        try {
            File file = App.getInstance().esvm.getMemoryManager().dump(-1, -1);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(0);
            signbs = raf.readInt();
            raf.seek(4);
            signcount = raf.readInt();
            fxmlaDumpTab.setHexGrig(constructHexLayout(file, signbs, 11));
            raf.close();
        } catch (MemoryOutOfRangeException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnByteSelectListener(OnByteSelectListener onByteSelectListener) {
        this.onByteSelectListener = onByteSelectListener;
    }

    public enum WorkMode {
        FREE, DUMP, CLASS
    }

    public enum HexTableClass {
        DUMP, ASCII, BIN
    }
}
