package esvm.dialogs;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import java.util.Optional;

/**
 * Created by serbis on 22.09.15.
 */
public class StandartDialogs {
    public StandartDialogs() {

    }

    public static void showErrorDialog(String title, String text) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(text);
            alert.showAndWait();
    }

    public static boolean showConfirmationDialog(String title, String text, String header) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    public static String showTextInputDialog(String title, String text, String header, String etText) {
        TextInputDialog dialog = new TextInputDialog(etText);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(text);
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            return result.get();
        }

        return null;
    }

    public static void showExceptionDialog(String exceptionText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка проверки базы данных");
        alert.setContentText("При проверке базы данных были обнаружены ошибки");

        Label label = new Label("");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static DialogRetTwoField showTwoFieldDialog(DialogTwoFieldDesctrption desctrption) {
        Dialog<DialogRetTwoField> dialog = new Dialog<>();
        dialog.setTitle(desctrption.title);
        dialog.setHeaderText(desctrption.header);
        dialog.setResizable(true);
        Label label1 = new Label(desctrption.text1);
        Label label2 = new Label(desctrption.text2);
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.setResultConverter(new Callback<ButtonType, DialogRetTwoField>() {
            @Override
            public DialogRetTwoField call(ButtonType b) {
                if (b == buttonTypeOk) {
                    return new DialogRetTwoField(text1.getText(), text2.getText());
                }
                return null;
            }
        });
        dialog.showAndWait();
        Optional<DialogRetTwoField> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }

    public static DialogRetThreeField showThreeFieldDialog(DialogThreeFieldDesctrption desctrption) {
        Dialog<DialogRetThreeField> dialog = new Dialog<>();
        dialog.setTitle(desctrption.title);
        dialog.setHeaderText(desctrption.header);
        dialog.setResizable(true);
        Label label1 = new Label(desctrption.text1);
        Label label2 = new Label(desctrption.text2);
        Label label3 = new Label(desctrption.text3);
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(text3, 2, 3);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.setResultConverter(new Callback<ButtonType, DialogRetThreeField>() {
            @Override
            public DialogRetThreeField call(ButtonType b) {
                if (b == buttonTypeOk) {
                    return new DialogRetThreeField(text1.getText(), text2.getText(), text3.getText());
                }
                return null;
            }
        });
        dialog.showAndWait();
        Optional<DialogRetThreeField> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }

    public static class DialogTwoFieldDesctrption {
        public String text1;
        public String text2;
        public String title;
        public String header;

        public DialogTwoFieldDesctrption(String text1, String text2, String title, String header) {
            this.text1 = text1;
            this.text2 = text2;
            this.title = title;
            this.header = header;
        }
    }

    public static class DialogThreeFieldDesctrption {
        public String text1;
        public String text2;
        public String text3;
        public String title;
        public String header;

        public DialogThreeFieldDesctrption(String text1, String text2, String text3, String title, String header) {
            this.text1 = text1;
            this.text2 = text2;
            this.text3 = text3;
            this.title = title;
            this.header = header;
        }
    }

    public static class DialogRetTwoField {
        public String f1;
        public String f2;

        public DialogRetTwoField(String f1, String f2) {
            this.f1 = f1;
            this.f2 = f2;
        }
    }

    public static class DialogRetThreeField {
        public String f1;
        public String f2;
        public String f3;

        public DialogRetThreeField(String f1, String f2, String f3) {
            this.f1 = f1;
            this.f2 = f2;
            this.f3 = f3;
        }
    }
}
