package esvm.vm.exceptions;

/**
 * Исключение менеджера памяти. Вызывается в случае попытки обратиться
 * к несущеcтвующему блоку памяти
 *
 */
public class ClassVerificationErrorException extends Exception {
    public ClassVerificationErrorException(String text) {
        super(text);
    }
}
