package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.exceptions.MemoryNullBlockException;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public class Cmp extends Instruction{
    public short arg1;
    public short arg2;
    public static final byte code = 5;

    public Cmp(short arg1, short arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        asm = "Cmp";
    }

    public void exec() throws MemoryNullBlockException {
        pointer = Global.getInstance().getVarPointerById(this.arg1);
        pointer2 = Global.getInstance().getVarPointerById(this.arg2);
        byte[] var1 = Global.getInstance().memoryManager.readBlock(pointer);
        byte[] var2 = Global.getInstance().memoryManager.readBlock(pointer2);
        byteBuffer = ByteBuffer.wrap(var1);
        try {
            if (var1.length == var2.length) {
                Global.getInstance().boolOpFloag = Global.BoolLogic.EQUAL;

                if (var1.length == 4) {
                    int a = byteBuffer.getInt();
                    byteBuffer = ByteBuffer.wrap(var2);
                    int b = byteBuffer.getInt();
                    int res = a - b;
                    if (res == 0) {
                        Global.getInstance().boolOpFloag = Global.BoolLogic.EQUAL;
                    } else if (res > 0) {
                        Global.getInstance().boolOpFloag = Global.BoolLogic.GROSS;
                    } else if (res < 0) {
                        Global.getInstance().boolOpFloag = Global.BoolLogic.LESS;
                    }
                } else if (var1.length == 2) {
                    short a = byteBuffer.getShort();
                    byteBuffer = ByteBuffer.wrap(var2);
                    short b = byteBuffer.getShort();
                    int res = a - b;
                    if (res == 0) {
                        Global.getInstance().boolOpFloag = Global.BoolLogic.EQUAL;
                    } else if (res > 0) {
                        Global.getInstance().boolOpFloag = Global.BoolLogic.GROSS;
                    } else if (res < 0) {
                        Global.getInstance().boolOpFloag = Global.BoolLogic.LESS;
                    }
                } else {
                    //Исключение
                }
            } else {
                //Исключение
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
