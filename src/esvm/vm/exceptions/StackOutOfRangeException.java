package esvm.vm.exceptions;

/**
 * Исключение менеджера памяти. Вызывается в случае невозможности провети
 * аллцирование памяти.
 *
 */
public class StackOutOfRangeException extends Exception {
    public StackOutOfRangeException(String text) {
        super(text);
    }
}
