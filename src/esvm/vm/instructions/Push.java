package esvm.vm.instructions;

import esvm.vm.exceptions.StackOverflowException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Push extends Instruction{
    public int arg1;
    public final static byte code = 3;

    public Push(int arg1) {
        this.arg1 = arg1;
        asm = "Push";
    }

    public void exec() throws StackOverflowException {
        byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(this.arg1);
        //Global.getInstance().memoryManager.push(new StackObject(b));
    }
}
