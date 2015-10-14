package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Db extends Instruction{
    public short arg1;
    public short arg2;
    public static final byte code = 31;

    public Db(short arg1, short arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Db";
    }
}
