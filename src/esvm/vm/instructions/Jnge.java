package esvm.vm.instructions;

import esvm.vm.Global;

/**
 * Created by serbis on 13.10.15.
 */
public class Jnge extends Instruction{
    public int arg1;
    public final static byte code = 26;

    public Jnge(int arg1) {
        this.arg1 = arg1;
        asm = "Jnge";
    }

    public int exec(int i) {
        if (Global.getInstance().boolOpFloag != Global.BoolLogic.GROSS && Global.getInstance().boolOpFloag != Global.BoolLogic.EQUAL ) {
            Global.getInstance().boolOpFloag = Global.BoolLogic.INDEF;
            return this.arg1 - 1;
        }
        return i;
    }
}
