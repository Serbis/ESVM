package esvm.vm.desc.constpool;

/**
 * Created by serbis on 21.10.15.
 */
public class ConstInteger  extends ClassConstant {
    public byte[] bytes;

    public ConstInteger(byte[] bytes) {
        tag = 3;
        name = "constant_Integer";
        this.bytes = bytes;
    }
}
