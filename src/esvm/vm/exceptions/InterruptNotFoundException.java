package esvm.vm.exceptions;

/**
 * Исключение прерываний. Вызывается в случае если происходит
 * попоытка выполнить несуществующей прерывание
 *
 */
public class InterruptNotFoundException extends Exception {
    public InterruptNotFoundException(String text) {
        super(text);
    }
}
