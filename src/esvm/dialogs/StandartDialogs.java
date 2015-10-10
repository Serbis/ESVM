package esvm.dialogs;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

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
}
