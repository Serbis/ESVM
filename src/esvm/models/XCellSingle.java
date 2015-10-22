package esvm.models;

import esvm.rows.FXMLARowSingle;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class XCellSingle extends ListCell<RowModelSingle> {
    private String lastItem;
    private ArrayList<FXMLARowSingle> rowcon = new ArrayList<>();
    private RowModelMaps rowModelMaps;


    public static <T> Callback<ListView<T>,ListCell<T>> forListView(ContextMenu contextMenu) {
        return forListView(contextMenu, null);
    }

    public static <T> Callback<ListView<T>,ListCell<T>> forListView(final ContextMenu contextMenu, final Callback<ListView<T>,ListCell<T>> cellFactory) {
        return new Callback<ListView<T>,ListCell<T>>() {
            @Override public ListCell<T> call(ListView<T> listView) {
                ListCell<T> cell = cellFactory == null ? new DefaultListCell<T>() : cellFactory.call(listView);
                cell.setContextMenu(contextMenu);
                return cell;
            }
        };
    }

    @Override
    protected void updateItem(RowModelSingle item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class

        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            FXMLARowSingle fxmlaRowSingle;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../rows/rowsingle.fxml"));
            HBox hbox = null;
            try {
                hbox = (HBox)loader.load();
                fxmlaRowSingle = loader.getController();
                rowcon.add(fxmlaRowSingle);
                fxmlaRowSingle.lbText.setText(item.text);
                //fxmlaLvrow.lbName.setText(item != null ? item : "<null>");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //rowcon.get(getIndex()).lbName.setText(item!=null ? item : "<null>");
            //label.setText(item!=null ? item : "<null>");
            setGraphic(hbox);
            System.out.println(String.valueOf("index " + getIndex()));
        }
    }

    public XCellSingle(ContextMenu contextMenu) {
        setContextMenu(contextMenu);
    }
}


