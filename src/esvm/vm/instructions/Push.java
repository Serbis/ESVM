package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Push extends Instruction{
    public int arg1;
    public final static byte code = 3;

    public Push(int arg1) {
        this.arg1 = arg1;
    }
}
