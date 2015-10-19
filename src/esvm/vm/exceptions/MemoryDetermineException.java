package esvm.vm.exceptions;

import esvm.vm.Global;

/**
 * Исключение менеджера памяти. Вызывает в случае ошибки создания блока
 * виртуальной памяти. Причину вызова пока не придумал
 *
 */
public class MemoryDetermineException extends Exception {
    public MemoryDetermineException(String text) {
        super(text);
        Global.getInstance().createExceptionInturrupt(text);
    }
}
