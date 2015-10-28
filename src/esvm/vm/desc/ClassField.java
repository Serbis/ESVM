package esvm.vm.desc;

import esvm.enums.AccessFlags;

/**
 * Описывает элемент сегмента Fields в Class файле
 */
public class ClassField {
    public AccessFlags accessFlags;
    public short name_index;
    public short descriptor_index;


    public ClassField(AccessFlags accessFlags, short name_index, short descriptor_index) {
        this.accessFlags = accessFlags;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
    }
}
