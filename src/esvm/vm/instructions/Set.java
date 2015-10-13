package esvm.vm.instructions;

import java.util.ArrayList;

/**
 * Created by serbis on 13.10.15.
 */
public class Set extends Instruction{
    public short arg1;
    public ArrayList<Byte> arg2;
    public final static byte code = 32;

    public Set() {

    }
}
