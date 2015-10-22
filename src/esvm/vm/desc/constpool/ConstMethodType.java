package esvm.vm.desc.constpool;

/**
 * Created by serbis on 21.10.15.
 */
public class ConstMethodType  extends ClassConstant {
    public short descriptor_index;

    public ConstMethodType(short descriptor_index) {
        name = "constant_MethodType";
        tag = 16;
        this.descriptor_index = descriptor_index;
    }
}
