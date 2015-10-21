package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Inp extends Instruction{
    public byte arg1;
    public short arg2;
    public final static byte code = 30;

    public Inp(byte arg1, short arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Inp";
    }

    public void exec() throws MemoryOutOfRangeException, MemoryNullBlockException {
        pointer = Global.getInstance().getVarPointerById(this.arg2);
        byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(Global.getInstance().ports[this.arg1]);
        Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
    }
}
