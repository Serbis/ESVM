package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.StackOverflowException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Pushv extends Instruction{
    public short arg1;
    public final static byte code = 33;

    public Pushv(short arg1) {
        this.arg1 = arg1;
        asm = "Pushv";
    }

    public void exec() throws MemoryNullBlockException, StackOverflowException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
        //Global.getInstance().memoryManager.push(Global.getInstance().memoryManager.readBlock(pointer));
    }
}
