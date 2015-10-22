package esvm.vm.desc.constpool;

/**
 * Created by serbis on 21.10.15.
 */
public class ConstMethodHandle  extends ClassConstant {
    public byte reference_kind;
    public short reference_index;

    public ConstMethodHandle(byte reference_kind, short reference_index) {
        name = "constant_MethodHandle";
        tag = 15;
        this.reference_kind = reference_kind;
        this.reference_index = reference_index;
    }
}
