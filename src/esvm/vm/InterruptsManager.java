package esvm.vm;

import esvm.vm.exceptions.InterruptNotFoundException;
import esvm.vm.exceptions.MemoryNullBlockException;

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
    public interface InterruptInterface_1_0 {       //Переход курсора интерпретатора
        public void onInterrupt_interface_1_0(int pos);
    }
    public interface InterruptInterface_1_1 {       //Окончание инструкций
        public void onInterrupt_interface_1_1();
    }
    public interface InterruptInterface_1_2 {       //Исключение
        public void onInterrupt_interface_1_2(String text);
    }
    public interface InterruptInterface_1_3 {       //Переход в другой метод
        public void onInterrupt_interface_1_3(int id);
    }

    private InterruptInterface_0_0 interruptInterface_0_0;  //Интерфейс stdout
    private InterruptInterface_0_1 interruptInterface_0_1;  //Интерфейс stdin
    private InterruptInterface_1_0 interruptInterface_1_0;
    private InterruptInterface_1_1 interruptInterface_1_1;
    private InterruptInterface_1_2 interruptInterface_1_2;
    private InterruptInterface_1_3 interruptInterface_1_3;

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
        } else if (type == 1) {
            if (subtype == 0) {
                interrupt_1_0();
            } else if (subtype == 1) {
                interrupt_1_1();
            } else if (subtype == 2) {
                interrupt_1_2();
            } else if (subtype == 3) {
                interrupt_1_3();
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

    /**
     * Класс : Системное прерывание
     * Подкласс : Переход курсора интерпретатора
     *
     */
    private void interrupt_1_0() {
        try {
            interruptInterface_1_0.onInterrupt_interface_1_0(Global.getInstance().ports[2]);
        } catch (NullPointerException ignored){}
    }

    /**
     * Класс : Системное прерывание
     * Подкласс : Окончание потока инстркций
     *
     */
    private void interrupt_1_1() {
        try {
            interruptInterface_1_1.onInterrupt_interface_1_1();
        } catch (NullPointerException ignored){}
    }

    /**
     * Класс : Системное прерывание
     * Подкласс : Иселючение
     *
     */
    private void interrupt_1_2() {
        try {
            byte[] bytes = Global.getInstance().memoryManager.readBlock(Global.getInstance().getVarPointerById((short) Global.getInstance().ports[2]));
            String text = new String(bytes);
            interruptInterface_1_2.onInterrupt_interface_1_2(text);
            Global.getInstance().memoryManager.free(Global.getInstance().getVarPointerById((short) Global.getInstance().ports[2]));
        } catch (NullPointerException ignored){} catch (MemoryNullBlockException e) {
            e.printStackTrace();
        }
    }

    /**
     * Класс : Системное прерывание
     * Подкласс : Переход в метод
     *
     */
    private void interrupt_1_3() {
        interruptInterface_1_3.onInterrupt_interface_1_3(Global.getInstance().ports[2]);
    }

    public void setInterruptInterface_0_0(InterruptInterface_0_0 interruptInterface_0_0) {
        this.interruptInterface_0_0 = interruptInterface_0_0;
    }

    public void setInterruptInterface_0_1(InterruptInterface_0_1 interruptInterface_0_1) {
        this.interruptInterface_0_1 = interruptInterface_0_1;
    }

    public void setInterruptInterface_1_0(InterruptInterface_1_0 interruptInterface_1_0) {
        this.interruptInterface_1_0 = interruptInterface_1_0;
    }

    public void setInterruptInterface_1_1(InterruptInterface_1_1 interruptInterface_1_1) {
        this.interruptInterface_1_1 = interruptInterface_1_1;
    }

    public void setInterruptInterface_1_2(InterruptInterface_1_2 interruptInterface_1_2) {
        this.interruptInterface_1_2 = interruptInterface_1_2;
    }

    public void setInterruptInterface_1_3(InterruptInterface_1_3 interruptInterface_1_3) {
        this.interruptInterface_1_3 = interruptInterface_1_3;
    }
}
