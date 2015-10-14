package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Jz extends Instruction{
    public short arg1;
    public final static byte code = 20;

    public Jz(short arg1) {
        this.arg1 = arg1;
        asm = "Jz";
    }
}
