package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Add extends Instruction{
    public final static byte code = 1;

    public Add() {
        asm = "Add";
    }

    public void exec() {
        // Global.getInstance().memoryManager.push(Global.getInstance().memoryManager.pop() + Global.getInstance().memoryManager.pop());

    }
}
