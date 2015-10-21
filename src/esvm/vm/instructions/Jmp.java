package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Jmp extends Instruction{
    public int arg1;
    public final static byte code = 16;

    public Jmp(int arg1) {
        this.arg1 = arg1;
        asm = "Jmp";
    }

    public int exec() {
        return this.arg1 - 1;
    }
}
