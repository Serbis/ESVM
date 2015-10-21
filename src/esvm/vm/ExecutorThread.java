package esvm.vm;

import esvm.vm.desc.Pointer;
import esvm.vm.exceptions.InterruptNotFoundException;
import esvm.vm.exceptions.MemoryDetermineException;
import esvm.vm.instructions.*;

import java.nio.ByteBuffer;

/**
 * Created by serbis on 15.10.15.
 */
public class ExecutorThread implements Runnable {
    private Thread thread;
    public boolean stoped = false;
    private Pointer pointer;
    private Pointer pointer2;
    private ByteBuffer byteBuffer;
    private ByteBuffer byteBuffer2;

    public ExecutorThread() {
        thread = new Thread(this, "Executor thread");
        thread.start(); // Запускаем поток
        Global.getInstance().interruptsManager.setThread(thread);
        Global.getInstance().setExecutorThread(this);
    }

    @Override
    public void run() {
        for (int i = 0; i < Global.getInstance().iSet.size(); i++) {
            try {
                Global.getInstance().ports[2] = i;
                Global.getInstance().interruptsManager.exexInterrupt((byte) 1, (byte) 0);
            } catch (InterruptNotFoundException e) {
                e.printStackTrace();
            }

            try {
                switch (Global.getInstance().iSet.get(i).asm) {
                    case "Add":
                        Add add = (Add) Global.getInstance().iSet.get(i);
                        add.exec();
                        break;

                    case "Sub":
                        Sub sub = (Sub) Global.getInstance().iSet.get(i);
                        sub.exec();
                        break;

                    case "Push":
                        Push push = (Push) Global.getInstance().iSet.get(i);
                        push.exec();
                        break;

                    case "Pop":
                        Pop pop = (Pop) Global.getInstance().iSet.get(i);
                        pop.exec();
                        break;

                    case "Cmp":
                        Cmp cmp = (Cmp) Global.getInstance().iSet.get(i);
                        cmp.exec();
                        break;

                    case "Inc":
                        Inc inc = (Inc) Global.getInstance().iSet.get(i);
                        inc.exec();
                        break;

                    case "Dec":
                        Dec dec = (Dec) Global.getInstance().iSet.get(i);
                        dec.exec();
                        break;

                    case "Mul":

                        break;

                    case "IMul":

                        break;

                    case "Movis":

                        break;

                    case "Movos":

                        break;

                    case "Xchg":
                        Xchg xchg = (Xchg) Global.getInstance().iSet.get(i);
                        xchg.exec();
                        break;

                    case "Lea":
                        Lea lea = (Lea) Global.getInstance().iSet.get(i);
                        lea.exec();
                        break;

                    case "Int":
                        Int intetrrupt = (Int) Global.getInstance().iSet.get(i);
                        intetrrupt.exec();
                        break;

                    case "Loop":
                        Loop loop = (Loop) Global.getInstance().iSet.get(i);
                        i = loop.exec(i);
                        break;

                    case "Jmp":
                        Jmp jmp = (Jmp) Global.getInstance().iSet.get(i);
                        i = jmp.exec();
                        break;

                    case "Div":

                        break;

                    case "IDiv":

                        break;

                    case "Je": //Если равно
                        Je je = (Je) Global.getInstance().iSet.get(i);
                        i = je.exec(i);
                        break;

                    case "Jz": //Если ноль
                        Jz jz = (Jz) Global.getInstance().iSet.get(i);
                        i = jz.exec(i);
                        break;

                    case "Jg": //Если больше
                        Jg jg = (Jg) Global.getInstance().iSet.get(i);
                        i = jg.exec(i);
                        break;

                    case "Jge": //Если больше или равно
                        Jge jge = (Jge) Global.getInstance().iSet.get(i);
                        i = jge.exec(i);
                        break;

                    case "Jl": //Если меньше
                        Jl jl = (Jl) Global.getInstance().iSet.get(i);
                        i = jl.exec(i);
                        break;

                    case "Jle": //Если меньше или равно
                        Jle jle = (Jle) Global.getInstance().iSet.get(i);
                        i = jle.exec(i);
                        break;

                    case "Jne": //Если не равно
                        Jne jne = (Jne) Global.getInstance().iSet.get(i);
                        i = jne.exec(i);
                        break;

                    case "Jnge": //Если не больше или равно
                        Jnge jnge = (Jnge) Global.getInstance().iSet.get(i);
                        i = jnge.exec(i);
                        break;

                    case "Jnl": //Если не меньше
                        Jnl jnl = (Jnl) Global.getInstance().iSet.get(i);
                        i = jnl.exec(i);
                        break;

                    case "Jnle": //Если не меньше или равно
                        Jnle jnle = (Jnle) Global.getInstance().iSet.get(i);
                        i = jnle.exec(i);
                        break;

                    case "Out":
                        Out out = (Out) Global.getInstance().iSet.get(i);
                        out.exec();
                        break;

                    case "Inp":
                        Inp inp = (Inp) Global.getInstance().iSet.get(i);
                        inp.exec();
                        break;

                    case "Db":
                        Db db = (Db) Global.getInstance().iSet.get(i);
                        db.exec();
                        break;

                    case "Set":
                        Set set = (Set) Global.getInstance().iSet.get(i);
                        set.exec();
                        break;

                    case "Pushv":
                        Pushv pushv = (Pushv) Global.getInstance().iSet.get(i);
                        pushv.exec();
                        break;

                    case "Outv":
                        Outv outv = (Outv) Global.getInstance().iSet.get(i);
                        outv.exec();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try { //Вызов прерывания после исполнения всех инструкций
            Global.getInstance().interruptsManager.exexInterrupt((byte) 1, (byte) 1);
        } catch (InterruptNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Останавливает поток выполнения
     *
     */
    public void pause() {
        stoped = true;
        thread.suspend();
    }

    /**
     * Возобновляет поток выполнения
     *
     */
    public void resume() {
        stoped = false;
        thread.resume();
    }

    /**
     * Пркращает выполнение инстркуций
     *
     */
    public void stop() {
        try { //дереминируем память вм
            Global.getInstance().iSet.clear();
            Global.getInstance().varMap.clear();
            Global.getInstance().memoryManager.determineMemory(Global.getInstance().vmspec.memory_bs, Global.getInstance().vmspec.memory_blockcount, Global.getInstance().vmspec.memory_stacksize);
        } catch (MemoryDetermineException e) {
            e.printStackTrace();
        }
        thread.stop(); // самоликвидация потока исполнения
    }


}
