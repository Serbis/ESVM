package esvm.vm.desc.attributes;

import java.util.ArrayList;

/**
 * Created by serbis on 21.10.15.
 */
public class AttrCode extends ClassAttribute {
    public String code;
    public ArrayList<LocalVariableTRow> local_varialbe_table;

    public AttrCode(String code, ArrayList<LocalVariableTRow> local_varialbe_table) {
        name = "attribute_code";
        tag = 0;
        this.code = code;
        this.local_varialbe_table = local_varialbe_table;
    }

    public AttrCode() {

    }
}
