package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Jnge extends Instruction{
    public short arg1;
    public final static byte code = 26;

    public Jnge(short arg1) {
        this.arg1 = arg1;
    }
}
