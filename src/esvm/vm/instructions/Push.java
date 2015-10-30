package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.desc.StackObject;
import esvm.vm.exceptions.StackOverflowException;

/**
 * Created by serbis on 13.10.15.
 */
public class Push extends Instruction{
    public byte arg1;
    public byte[] arg2;
    public final static byte code = 3;

    public Push(byte arg1, byte[] arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Push";
    }

    public void exec() throws StackOverflowException {
        if (arg1 == 1)
            Global.getInstance().memoryManager.push(arg2, StackObject.StackDataType.INT);
        else if (arg1 == 2)
            Global.getInstance().memoryManager.push(arg2, StackObject.StackDataType.FLOAT);
        else if (arg1 == 3)
            Global.getInstance().memoryManager.push(arg2, StackObject.StackDataType.BOOLEAN);
        else if (arg1 == 4)
            Global.getInstance().memoryManager.push(arg2, StackObject.StackDataType.BYTE);
        else if (arg1 == 5)
            Global.getInstance().memoryManager.push(arg2, StackObject.StackDataType.STRING);
        else
            Global.getInstance().memoryManager.push(arg2, StackObject.StackDataType.SHORT);
    }
}
