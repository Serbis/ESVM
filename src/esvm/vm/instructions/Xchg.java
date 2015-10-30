package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.IncompatibleTypesException;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;

/**
 * Created by serbis on 13.10.15.
 */
public class Xchg extends Instruction{
    public short arg1;
    public short arg2;
    public final static byte code = 12;

    public Xchg(short arg1, short arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Xchg";
    }

    public void exec() throws MemoryNullBlockException, MemoryOutOfRangeException, IncompatibleTypesException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        pointer2 = Global.getInstance().getVarPointerById(this.arg2);
        byte[] var1b = Global.getInstance().memoryManager.readBlock(pointer);
        byte[] var2b = Global.getInstance().memoryManager.readBlock(pointer2);
        if (var1b.length != var2b.length) {
            throw new IncompatibleTypesException("...");
        }
        byte[] median = var1b;
        var1b = var2b;
        var2b = median;
        Global.getInstance().memoryManager.writeBlock(pointer, var1b);
        Global.getInstance().memoryManager.writeBlock(pointer, var2b);




    }
}
