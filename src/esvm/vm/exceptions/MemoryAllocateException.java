package esvm.vm.exceptions;

import esvm.vm.Global;

/**
 * Исключение менеджера памяти. Вызывается в случае невозможности провети
 * аллцирование памяти.
 *
 */
public class MemoryAllocateException extends Exception {
    public MemoryAllocateException(String text) {
        super(text);
        Global.getInstance().createExceptionInturrupt(text);
    }
}
