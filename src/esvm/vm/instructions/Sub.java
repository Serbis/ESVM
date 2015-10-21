package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Sub extends Instruction{
    public final static byte code = 2;

    public Sub() {
        asm = "Sub";
    }

    public void exec() {
        //Global.getInstance().memoryManager.push(Global.getInstance().memoryManager.pop() - Global.getInstance().memoryManager.pop());
    }
}
