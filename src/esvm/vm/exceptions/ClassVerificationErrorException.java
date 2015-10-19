package esvm.vm.exceptions;

import esvm.vm.Global;

/**
 * Исключение менеджера памяти. Вызывается в случае попытки обратиться
 * к несущеcтвующему блоку памяти
 *
 */
public class ClassVerificationErrorException extends Exception {
    public ClassVerificationErrorException(String text) {
        super(text);
        Global.getInstance().createExceptionInturrupt(text);
    }
}
