package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Dec extends Instruction{
    public short arg1;
    public static final byte code = 7;

    public Dec(short arg1) {
        this.arg1 = arg1;
        asm = "Dec";
    }
}
