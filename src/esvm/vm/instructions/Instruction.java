package esvm.vm.instructions;

import esvm.vm.desc.Pointer;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 13.10.15.
 */
public abstract  class Instruction {
    public int offset;
    public String asm;
    public Pointer pointer;
    public Pointer pointer2;
    public ByteBuffer byteBuffer;
    public ByteBuffer byteBuffer2;
}
