package esvm.vm.desc.constpool;

/**
 * Created by serbis on 21.10.15.
 */
public class ConstFloat extends ClassConstant {
    public byte[] bytes;

    public ConstFloat(byte[] bytes) {
        name = "constant_Float";
        tag = 4;
        this.bytes = bytes;
    }
}
