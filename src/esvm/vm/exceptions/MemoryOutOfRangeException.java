package esvm.vm.exceptions;

/**
 * Исключение менеджера памяти. Вызывается в случае невозможности провети
 * аллцирование памяти.
 *
 */
public class MemoryOutOfRangeException extends Exception {
    public MemoryOutOfRangeException(String text) {
        super(text);
    }
}
