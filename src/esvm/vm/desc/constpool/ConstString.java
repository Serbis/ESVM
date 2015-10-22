package esvm.vm.desc.constpool;

/**
 * Created by serbis on 21.10.15.
 */
public class ConstString  extends ClassConstant {
    public short string_index;

    public ConstString(short string_index) {
        name = "constant_String";
        tag = 8;
        this.string_index = string_index;
    }
}
