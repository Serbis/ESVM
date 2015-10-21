package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Inc extends Instruction{
    public short arg1;
    public final static byte code = 6;

    public Inc(short arg1) {
        this.arg1 = arg1;
        asm = "Inc";
    }

    public void exec() throws MemoryNullBlockException, MemoryOutOfRangeException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        byte[] var = Global.getInstance().memoryManager.readBlock(pointer);
        byteBuffer = ByteBuffer.wrap(var);
        if (var.length == 2) {
            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
            int vShort = byteBuffer.getShort();
            vShort++;
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putInt(vShort);
            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
        } else if (var.length == 4) {
            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
            int vInt = byteBuffer.getInt();
            vInt++;
            byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.putInt(vInt);
            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
        } else {
            //Тут нужно как-то сгенерировать проверку типов
        }
    }
}
