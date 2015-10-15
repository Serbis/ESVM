package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Outv extends Instruction{
    public byte arg1;
    public short arg2;
    public final static byte code = 34;

    public Outv(byte arg1, short arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Outv";
    }
}
