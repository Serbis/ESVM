package esvm.vm.desc;

/**
 * Created by serbis on 15.10.15.
 */
public class Var {
    public short id;
    public Pointer pointer;
    public short size;

    public Var() {

    }

    public Var(short id, Pointer pointer, short size) {
        this.id = id;
        this.pointer = pointer;
        this.size = size;
    }
}
