package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;
import esvm.vm.exceptions.NullReferenceException;

/**
 * Created by serbis on 13.10.15.
 */
public class Pop extends Instruction{
    public short arg1;
    public final static byte code = 4;

    public Pop(short arg1) {
        this.arg1 = arg1;
        asm = "Pop";
    }

    public void exec() throws NullReferenceException, MemoryOutOfRangeException, MemoryNullBlockException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        Global.getInstance().memoryManager.writeBlock(pointer, Global.getInstance().memoryManager.pop().data);
    }
}
