package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Lea extends Instruction{
    public short arg1;
    public short arg2;
    public final static byte code = 13;

    public Lea(short arg1, short arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Lea";
    }

    public void exec() throws MemoryOutOfRangeException, MemoryNullBlockException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        pointer2 = Global.getInstance().getVarPointerById(this.arg2);
        byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putShort((short) pointer.page);
        byteBuffer.putShort((short) pointer.offset);
        Global.getInstance().memoryManager.writeBlock(pointer2, byteBuffer.array());
    }
}
