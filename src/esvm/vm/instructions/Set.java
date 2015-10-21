package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;

/**
 * Created by serbis on 13.10.15.
 */
public class Set extends Instruction{
    public short arg1; //id переменной
    public int arg2; //размер данных
    public byte[] arg3; //данные
    public final static byte code = 32;

    public Set() {
        asm = "Set";
    }

    public void exec() throws MemoryOutOfRangeException, MemoryNullBlockException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        Global.getInstance().memoryManager.writeBlock(pointer, this.arg3);
    }
}
