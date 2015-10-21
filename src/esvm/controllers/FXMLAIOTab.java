package esvm.controllers;

import esvm.App;
import esvm.vm.InterruptsManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by serbis on 20.09.15.
 */
public class FXMLAIOTab implements Initializable{
    @FXML private AnchorPane anchorPane;
    @FXML private VBox vBox;
    @FXML private Label lbIO;
    @FXML private TextField tfInput;


    private static FXMLAIOTab instance;
    private Stage stage;
    private String iotext = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        instance = this;
    }



    /**
     * Возращает инстанс контроллера
     *
     * @return инстанс контроллера
     */
    public static FXMLAIOTab getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void classLoaded() {
        App.getInstance().esvm.getInterruptsManager().setInterruptInterface_0_0(new InterruptsManager.InterruptInterface_0_0() {
            @Override
            public void onInterrupt_interface_0_0(int data) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            char ch = (char) data;
                            iotext += ch;
                            lbIO.setText(iotext);
                        } catch (Exception e) {

                        }
                    }
                });

            }
        });

        App.getInstance().esvm.getInterruptsManager().setInterruptInterface_0_1(new InterruptsManager.InterruptInterface_0_1() {
            @Override
            public void onInterrupt_interface_0_1() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        iotext += "\n" + ">";
                        lbIO.setText(iotext);
                    }
                });
            }
        });
    }



    @FXML protected void handleslbtnEnterAction(ActionEvent event) {
        if (App.getInstance().esvm.getGlobal().getExecutorThread().stoped) {
            String text = tfInput.getText();
            int count = 0;
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                //try {
                //    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
                //    byteBuffer.putChar(ch);
                    //App.getInstance().esvm.getMemoryManager().push(byteBuffer.array());
               //     count++;
               // } catch (StackOverflowException e) {
                //    e.printStackTrace();
                //}
            }
            App.getInstance().esvm.getGlobal().putToPort(2, count);     //Размещаем в порте ввода размер записанных в стек значений!!!
            App.getInstance().esvm.getGlobal().getExecutorThread().stoped = false;
            App.getInstance().esvm.getGlobal().getExecutorThread().resume();
        }
    }
}
