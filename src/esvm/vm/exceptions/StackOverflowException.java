package esvm.vm.exceptions;

/**
 * Исключение менеджера памяти. Вызывается в случае невозможности провети
 * аллцирование памяти.
 *
 */
public class StackOverflowException extends Exception {
    public StackOverflowException(String text) {
        super(text);
    }
}
