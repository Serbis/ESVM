package esvm.vm.instructions;

import esvm.vm.Global;

/**
 * Created by serbis on 13.10.15.
 */
public class Jz extends Instruction{
    public int arg1;
    public final static byte code = 20;

    public Jz(int arg1) {
        this.arg1 = arg1;
        asm = "Jz";
    }

    public int exec(int i) {
        if (Global.getInstance().boolOpFloag == Global.BoolLogic.NULL) {
            Global.getInstance().boolOpFloag = Global.BoolLogic.INDEF;
            return this.arg1 - 1;
        }

        return i;
    }
}
