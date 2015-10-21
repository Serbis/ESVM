package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Loop extends Instruction{
    public short arg1;
    public int arg2;
    public final static byte code = 15;

    public Loop(short arg1, int arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Loop";
    }

    public int exec(int i) throws MemoryOutOfRangeException, MemoryNullBlockException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
        int counter = byteBuffer.getInt();
        if (counter != 0) {
            counter--;
            byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.putInt(counter);
            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
            return this.arg2 - 1;
        }
        return i;
    }
}
