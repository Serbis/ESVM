package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Outv extends Instruction{
    public byte arg1;
    public short arg2;
    public final static byte code = 34;

    public Outv(byte arg1, short arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Outv";
    }

    public void exec() throws MemoryNullBlockException {
        pointer = Global.getInstance().getVarPointerById(this.arg2);
        byte[] vlaue = Global.getInstance().memoryManager.readBlock(pointer);
        byteBuffer = ByteBuffer.wrap(vlaue);
        Global.getInstance().ports[0] = byteBuffer.getInt();
    }
}
