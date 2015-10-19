package esvm.vm.exceptions;

import esvm.vm.Global;

/**
 * Исключение менеджера памяти. Вызывается в случае невозможности провети
 * аллцирование памяти.
 *
 */
public class MemoryOutOfRangeException extends Exception {
    public MemoryOutOfRangeException(String text) {
        super(text);
        Global.getInstance().createExceptionInturrupt(text);
    }
}
