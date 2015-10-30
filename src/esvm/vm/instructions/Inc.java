package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.desc.StackObject;
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
        StackObject.StackDataType type = Global.getInstance().getVarTypeById(arg1);
        byte[] vara = Global.getInstance().memoryManager.readBlock(pointer);
        byteBuffer = ByteBuffer.wrap(vara);
        if (type == StackObject.StackDataType.SHORT) {
            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
            short v = byteBuffer.getShort();
            v++;
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort(v);
            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
        } else if (type == StackObject.StackDataType.INT) {
            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
            int v = byteBuffer.getInt();
            v++;
            byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.putInt(v);
            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
        } else if (type == StackObject.StackDataType.FLOAT) {
            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
            float v = byteBuffer.getFloat();
            v++;
            byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.putFloat(v);
            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
        } else {
            //Тут нужно как-то сгенерировать проверку типов
        }
    }
}
