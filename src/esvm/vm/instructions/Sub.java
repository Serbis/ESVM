package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.desc.StackObject;
import esvm.vm.exceptions.NullReferenceException;
import esvm.vm.exceptions.StackOverflowException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Sub extends Instruction{
    public final static byte code = 2;

    public Sub() {
        asm = "Sub";
    }

    public void exec() throws StackOverflowException {
        try {
            StackObject a = Global.getInstance().memoryManager.pop();
            StackObject b = Global.getInstance().memoryManager.pop();

            byteBuffer = ByteBuffer.wrap(a.data);
            byteBuffer2 = ByteBuffer.wrap(b.data);
            if ((a.stackDataType == StackObject.StackDataType.INT || a.stackDataType == StackObject.StackDataType.SHORT)// Если оба int или short
                    && (b.stackDataType == StackObject.StackDataType.INT || b.stackDataType == StackObject.StackDataType.SHORT)) {
                int result;
                if (a.stackDataType == StackObject.StackDataType.SHORT && b.stackDataType == StackObject.StackDataType.INT) {
                    result = byteBuffer.getShort() - byteBuffer2.getInt();
                } else if (a.stackDataType == StackObject.StackDataType.INT && b.stackDataType == StackObject.StackDataType.SHORT) {
                    result = byteBuffer.getInt() - byteBuffer2.getShort();
                } else if (a.stackDataType == StackObject.StackDataType.SHORT && b.stackDataType == StackObject.StackDataType.SHORT) {
                    result = byteBuffer.getShort() - byteBuffer2.getShort();
                } else {
                    result = byteBuffer.getInt() - byteBuffer2.getInt();
                }
                byteBuffer = ByteBuffer.allocate(4);
                byteBuffer.putInt(result);
                Global.getInstance().memoryManager.push(byteBuffer.array(), StackObject.StackDataType.INT);
            } else if (a.stackDataType == StackObject.StackDataType.FLOAT && b.stackDataType == StackObject.StackDataType.FLOAT) {
                float result = byteBuffer.getFloat() - byteBuffer2.getFloat();
                byteBuffer = ByteBuffer.allocate(4);
                byteBuffer.putFloat(result);
                Global.getInstance().memoryManager.push(byteBuffer.array(), StackObject.StackDataType.FLOAT);
            } else if (a.stackDataType == StackObject.StackDataType.FLOAT && (b.stackDataType == StackObject.StackDataType.SHORT || b.stackDataType == StackObject.StackDataType.INT)) {
                float result;
                if (b.stackDataType == StackObject.StackDataType.INT) {
                    result = byteBuffer.getFloat() - byteBuffer2.getInt();
                } else {
                    result = byteBuffer.getFloat() - byteBuffer2.getShort();
                }
                byteBuffer = ByteBuffer.allocate(4);
                byteBuffer.putFloat(result);
                Global.getInstance().memoryManager.push(byteBuffer.array(), StackObject.StackDataType.FLOAT);
            }  else if ((a.stackDataType == StackObject.StackDataType.INT || a.stackDataType == StackObject.StackDataType.SHORT) && b.stackDataType == StackObject.StackDataType.FLOAT) {
                float result;
                if (a.stackDataType == StackObject.StackDataType.INT) {
                    result = byteBuffer.getInt() - byteBuffer2.getFloat();
                } else {
                    result = byteBuffer.getShort() - byteBuffer2.getFloat();
                }
                byteBuffer = ByteBuffer.allocate(4);
                byteBuffer.putFloat(result);
                Global.getInstance().memoryManager.push(byteBuffer.array(), StackObject.StackDataType.FLOAT);
            }
        } catch (NullReferenceException e) {
            e.printStackTrace();
        }
    }
}
