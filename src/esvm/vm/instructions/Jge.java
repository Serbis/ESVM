package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Jge extends Instruction{
    public short arg1;
    public final static byte code = 22;

    public Jge(short arg1) {
        this.arg1 = arg1;
    }
}