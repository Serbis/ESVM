package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.desc.StackObject;
import esvm.vm.desc.Var;
import esvm.vm.exceptions.MemoryAllocateException;

/**
 * Created by serbis on 13.10.15.
 */
public class Db extends Instruction{
    public short arg1;
    public short arg2;
    public byte arg3;
    public static final byte code = 31;

    public Db(short arg1, short arg2, byte arg3) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        asm = "Db";
    }

    public void exec() throws MemoryAllocateException {
        pointer = Global.getInstance().memoryManager.allocate(this.arg2);
        Var var = new Var(this.arg1, pointer, this.arg2);
        if (arg3 == 1)
            var.type = StackObject.StackDataType.INT;
        else if (arg3 == 2)
            var.type = StackObject.StackDataType.FLOAT;
        else if (arg3 == 3)
            var.type = StackObject.StackDataType.BOOLEAN;
        else if (arg3 == 4)
            var.type = StackObject.StackDataType.BYTE;
        else if (arg3 == 6)
            var.type = StackObject.StackDataType.SHORT;
        else
            var.type = StackObject.StackDataType.STRING;
        Global.getInstance().varMap.add(var);
    }
}
