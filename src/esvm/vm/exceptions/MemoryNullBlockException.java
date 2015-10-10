package esvm.vm.exceptions;

/**
 * Исключение менеджера памяти. Вызывается в случае попытки обратиться
 * к несущеcтвующему блоку памяти
 *
 */
public class MemoryNullBlockException extends Exception {
    public MemoryNullBlockException(String text) {
        super(text);
    }
}
