package esvm.fields;

/**
 * Created by serbis on 08.10.15.
 */
public class Address {
    private int block;
    private int offset;

    public Address() {

    }

    public Address(int block, int offset) {
        this.block = block;
        this.offset = offset;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getBlock() {
        return block;
    }

    public int getOffset() {
        return offset;
    }
}
