package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Loop extends Instruction{
    public short arg1;
    public int arg2;
    public final static byte code = 15;

    public Loop(short arg1, int arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Loop";
    }
}
