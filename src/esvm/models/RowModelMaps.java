package esvm.models;

/**
 * Модель итема файла в таблице
 */
public class RowModelMaps {
    public String name;
    public String date;
    public String size;

    public RowModelMaps(String name, String date, String size) {
        this.name = name;
        this.date = date;
        this.size = size;
    }
}
