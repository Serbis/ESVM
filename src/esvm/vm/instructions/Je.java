package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Je extends Instruction{
    public short arg1;
    public final static byte code = 19;

    public Je(short arg1) {
        this.arg1 = arg1;
        asm = "Je";
    }
}
