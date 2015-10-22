package esvm.vm.desc.constpool;

/**
 * Created by serbis on 21.10.15.
 */
public class ConstUtf_8  extends ClassConstant {
    public int lenght;
    public byte[] bytes;

    public ConstUtf_8(int lenght, byte[] bytes) {
        name = "constant_Utf_8";
        tag = 1;
        this.lenght = lenght;
        this.bytes = bytes;
    }
}
