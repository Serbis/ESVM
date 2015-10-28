package esvm.vm.desc.attributes;

/**
 * Описывает локальную переменную, применяется в таблице локальных перменных в
 * аттрибуте code
 */
public class LocalVariableTRow {
    public short name_index;
    public short name_decriptor;
    public short index;

    public LocalVariableTRow(short name_index, short name_decriptor, short index) {
        this.name_index = name_index;
        this.name_decriptor = name_decriptor;
        this.index = index;
    }
    public LocalVariableTRow() {

    }
}
