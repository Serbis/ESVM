package esvm.vm.instructions;

/**
 * Created by serbis on 13.10.15.
 */
public class Set extends Instruction{
    public short arg1; //id переменной
    public int arg2; //размер данных
    public byte[] arg3; //данные
    public final static byte code = 32;

    public Set() {
        asm = "Set";
    }
}
