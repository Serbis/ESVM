package esvm.vm.instructions;

import esvm.vm.Global;

/**
 * Created by serbis on 13.10.15.
 */
public class Jle extends Instruction{
    public int arg1;
    public final static byte code = 24;

    public Jle(int arg1) {
        this.arg1 = arg1;
        asm = "Jle";
    }

    public int exec(int i) {
        if (Global.getInstance().boolOpFloag == Global.BoolLogic.LESS || Global.getInstance().boolOpFloag == Global.BoolLogic.EQUAL ) {
            Global.getInstance().boolOpFloag = Global.BoolLogic.INDEF;
            return this.arg1 - 1;
        }

        return i;
    }
}
