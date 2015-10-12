package esvm.vm.exceptions;

/**
 * Исключение менеджера памяти. Вызывается в случае невозможности провети
 * аллцирование памяти.
 *
 */
public class NullReferenceException extends Exception {
    public NullReferenceException(String text) {
        super(text);
    }
}
