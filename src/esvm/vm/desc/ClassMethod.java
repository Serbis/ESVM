package esvm.vm.desc;


import esvm.enums.AccessFlags;
import esvm.vm.desc.constpool.ClassConstant;

/**
 * Описывает элемент сегмента Method в Class файле
 */
public class ClassMethod extends ClassConstant {
    public AccessFlags accessFlags;
    public short name_index;
    public short descriptor_index;
    public short code_index;

    public ClassMethod(AccessFlags accessFlags, short name_index, short descriptor_index, short code_index) {
        name = "constant_MethodHandle";
        this.accessFlags = accessFlags;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
        this.code_index = code_index;
    }
}
