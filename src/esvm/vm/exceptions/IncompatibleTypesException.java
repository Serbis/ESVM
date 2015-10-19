package esvm.vm.exceptions;

import esvm.vm.Global;

/**
 * Исключение несовместимости
 *
 */
public class IncompatibleTypesException extends Exception {
    public IncompatibleTypesException(String text) {
        super(text);
        Global.getInstance().createExceptionInturrupt(text);
    }
}
