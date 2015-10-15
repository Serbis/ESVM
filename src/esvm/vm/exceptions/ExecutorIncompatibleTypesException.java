package esvm.vm.exceptions;

/**
 * Исключение исполнения. Возникае в случае попытки записать в переменную
 * значение превыжающее размер аллоцированной памяти.
 *
 */
public class ExecutorIncompatibleTypesException extends Exception {
    public ExecutorIncompatibleTypesException(String text) {
        super(text);
    }
}
