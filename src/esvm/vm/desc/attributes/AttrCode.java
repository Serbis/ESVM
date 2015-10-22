package esvm.vm.desc.attributes;

/**
 * Created by serbis on 21.10.15.
 */
public class AttrCode extends ClassAttribute {
    public String code;

    public AttrCode(String code) {
        name = "attribute_code";
        tag = 0;
        this.code = code;
    }
}
