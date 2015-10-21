package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Dec extends Instruction{
    public short arg1;
    public static final byte code = 7;

    public Dec(short arg1) {
        this.arg1 = arg1;
        asm = "Dec";
    }

    public void exec() throws MemoryNullBlockException, MemoryOutOfRangeException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        byte[] vara = Global.getInstance().memoryManager.readBlock(pointer);
        byteBuffer = ByteBuffer.wrap(vara);
        if (vara.length == 2) {
            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
            int vShort = byteBuffer.getShort();
            vShort--;
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putInt(vShort);
            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
        } else if (vara.length == 4) {
            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
            int vInt = byteBuffer.getInt();
            vInt--;
            byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.putInt(vInt);
            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
        } else {
            //Тут нужно как-то сгенерировать проверку типов
        }
    }
}
