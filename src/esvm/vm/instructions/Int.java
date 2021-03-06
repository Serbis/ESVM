package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.InterruptNotFoundException;

/**
 * Created by serbis on 13.10.15.
 */
public class Int extends Instruction{
    public byte arg1;
    public byte arg2;
    public final static byte code = 14;

    public Int(byte arg1, byte arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Int";
    }

    public void exec() throws InterruptNotFoundException {
        Global.getInstance().interruptsManager.exexInterrupt(this.arg1, this.arg2);
    }
}
