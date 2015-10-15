package esvm.vm;

import esvm.vm.exceptions.InterruptNotFoundException;

/**
 * Created by serbis on 15.10.15.
 */
public class InterruptsManager {
    public interface InterruptInterface_0_0 {
        public void onInterrupt_interface_0_0(int data);    //Интервес stdout
    }
    public interface InterruptInterface_0_1 {
        public void onInterrupt_interface_0_1();
    }

    private InterruptInterface_0_0 interruptInterface_0_0;  //Интервес stdin
    private InterruptInterface_0_1 interruptInterface_0_1;

    private Thread thread;

    public InterruptsManager() {

    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * Выполяет вызов метода прерывания либо производит исключение
     * если идет попытка вызова несуществующего исключения.
     *
     * @param type тип прерывание
     * @param subtype подтип прерывания
     * @throws InterruptNotFoundException в случае если вызываемого прерывания
     *      не существует
     */
    public void exexInterrupt(byte type, byte subtype) throws InterruptNotFoundException{
        if (type == 0) {
            if (subtype == 0) {
                interrupt_0_0();
            } else if (subtype == 1) {
                interrupt_0_1();
            } else {
                throw new InterruptNotFoundException("Interrupt " + String.valueOf(type + ":" + subtype) + " not found");
            }
        } else {
            throw new InterruptNotFoundException("Interrupt " + String.valueOf(type + ":" + subtype) + " not found");
        }
    }

    /**
     * Класс : Прерывание системы ввода-вывода
     * Подкласс : Стандартный вывод
     *
     */
    private void interrupt_0_0() {
        interruptInterface_0_0.onInterrupt_interface_0_0(Global.getInstance().ports[0]);
    }

    /**
     * Класс : Прерывание системы ввода-вывода
     * Подкласс : Стандартный ввод
     *
     */
    private void interrupt_0_1() {
        interruptInterface_0_1.onInterrupt_interface_0_1();
        Global.getInstance().getExecutorThread().stoped = true;
        thread.suspend();
    }

    public void setInterruptInterface_0_0(InterruptInterface_0_0 interruptInterface_0_0) {
        this.interruptInterface_0_0 = interruptInterface_0_0;
    }

    public void setInterruptInterface_0_1(InterruptInterface_0_1 interruptInterface_0_1) {
        this.interruptInterface_0_1 = interruptInterface_0_1;
    }
}
