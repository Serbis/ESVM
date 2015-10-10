package esvm.fields;

/**
 * Created by serbis on 08.10.15.
 */
public class ByteModel {
    public int value;
    public int block;
    public int offset;

    public ByteModel(int value, int block, int offset) {
        this.value = value;
        this.block = block;
        this.offset = offset;
    }
}
