package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Mul extends Instruction{
    public final static byte code = 8;

    public Mul() {
        asm = "Mul";
    }
}
