package esvm.vm.exceptions;

import esvm.vm.Global;

/**
 * Исключение прерываний. Вызывается в случае если происходит
 * попоытка выполнить несуществующей прерывание
 *
 */
public class InterruptNotFoundException extends Exception {
    public InterruptNotFoundException(String text) {
        super(text);
        Global.getInstance().createExceptionInturrupt(text);
    }
}
