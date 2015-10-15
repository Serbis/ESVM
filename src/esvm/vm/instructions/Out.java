package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Out extends Instruction{
    public byte arg1;
    public int arg2;
    public final static byte code = 29;

    public Out(byte arg1, int arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Out";
    }
}
