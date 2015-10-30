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
        int isn = 0; //Номер набора инструкций
        int in = Global.getInstance().code.get(0).size(); //Количество инструкций в текущем наборе
        for (int i = 0; i < in; i++) {
            try {
                Global.getInstance().ports[2] = i;
                Global.getInstance().interruptsManager.exexInterrupt((byte) 1, (byte) 0);
            } catch (InterruptNotFoundException e) {
                e.printStackTrace();
            }

            try {
                switch (Global.getInstance().code.get(isn).get(i).asm) {
                    case "Add":
                        Add add = (Add) Global.getInstance().code.get(isn).get(i);
                        add.exec();
                        break;

                    case "Sub":
                        Sub sub = (Sub) Global.getInstance().code.get(isn).get(i);
                        sub.exec();
                        break;

                    case "Push":
                        Push push = (Push) Global.getInstance().code.get(isn).get(i);
                        push.exec();
                        break;

                    case "Pop":
                        Pop pop = (Pop) Global.getInstance().code.get(isn).get(i);
                        pop.exec();
                        break;

                    case "Cmp":
                        Cmp cmp = (Cmp) Global.getInstance().code.get(isn).get(i);
                        cmp.exec();
                        break;

                    case "Inc":
                        Inc inc = (Inc) Global.getInstance().code.get(isn).get(i);
                        inc.exec();
                        break;

                    case "Dec":
                        Dec dec = (Dec) Global.getInstance().code.get(isn).get(i);
                        dec.exec();
                        break;

                    case "Mul":
                        Mul mul = (Mul) Global.getInstance().code.get(isn).get(i);
                        mul.exec();
                        break;

                    case "IMul":

                        break;

                    case "Method":
                        Method method = (Method) Global.getInstance().code.get(isn).get(i);
                        isn = method.arg1;
                        i = 0;
                        in = Global.getInstance().code.get(isn).size();
                        Global.getInstance().ports[2] = isn;
                        Global.getInstance().interruptsManager.exexInterrupt((byte) 1, (byte) 3);
                        break;

                    case "Movos":

                        break;

                    case "Xchg":
                        Xchg xchg = (Xchg) Global.getInstance().code.get(isn).get(i);
                        xchg.exec();
                        break;

                    case "Lea":
                        Lea lea = (Lea) Global.getInstance().code.get(isn).get(i);
                        lea.exec();
                        break;

                    case "Int":
                        Int intetrrupt = (Int) Global.getInstance().code.get(isn).get(i);
                        intetrrupt.exec();
                        break;

                    case "Loop":
                        Loop loop = (Loop) Global.getInstance().code.get(isn).get(i);
                        i = loop.exec(i);
                        break;

                    case "Jmp":
                        Jmp jmp = (Jmp) Global.getInstance().code.get(isn).get(i);
                        i = jmp.exec();
                        break;

                    case "Div":
                        Div div = (Div) Global.getInstance().code.get(isn).get(i);
                        div.exec();
                        break;

                    case "IDiv":
                        //IDiv iDiv = (IDiv) Global.getInstance().code.get(isn).get(i);
                        //iDiv.exec();
                        break;

                    case "Je": //Если равно
                        Je je = (Je) Global.getInstance().code.get(isn).get(i);
                        i = je.exec(i);
                        break;

                    case "Jz": //Если ноль
                        Jz jz = (Jz) Global.getInstance().code.get(isn).get(i);
                        i = jz.exec(i);
                        break;

                    case "Jg": //Если больше
                        Jg jg = (Jg) Global.getInstance().code.get(isn).get(i);
                        i = jg.exec(i);
                        break;

                    case "Jge": //Если больше или равно
                        Jge jge = (Jge) Global.getInstance().code.get(isn).get(i);
                        i = jge.exec(i);
                        break;

                    case "Jl": //Если меньше
                        Jl jl = (Jl) Global.getInstance().code.get(isn).get(i);
                        i = jl.exec(i);
                        break;

                    case "Jle": //Если меньше или равно
                        Jle jle = (Jle) Global.getInstance().code.get(isn).get(i);
                        i = jle.exec(i);
                        break;

                    case "Jne": //Если не равно
                        Jne jne = (Jne) Global.getInstance().code.get(isn).get(i);
                        i = jne.exec(i);
                        break;

                    case "Jnge": //Если не больше или равно
                        Jnge jnge = (Jnge) Global.getInstance().code.get(isn).get(i);
                        i = jnge.exec(i);
                        break;

                    case "Jnl": //Если не меньше
                        Jnl jnl = (Jnl) Global.getInstance().code.get(isn).get(i);
                        i = jnl.exec(i);
                        break;

                    case "Jnle": //Если не меньше или равно
                        Jnle jnle = (Jnle) Global.getInstance().code.get(isn).get(i);
                        i = jnle.exec(i);
                        break;

                    case "Out":
                        Out out = (Out) Global.getInstance().code.get(isn).get(i);
                        out.exec();
                        break;

                    case "Inp":
                        Inp inp = (Inp) Global.getInstance().code.get(isn).get(i);
                        inp.exec();
                        break;

                    case "Db":
                        Db db = (Db) Global.getInstance().code.get(isn).get(i);
                        db.exec();
                        break;

                    case "Set":
                        Set set = (Set) Global.getInstance().code.get(isn).get(i);
                        set.exec();
                        break;

                    case "Pushv":
                        Pushv pushv = (Pushv) Global.getInstance().code.get(isn).get(i);
                        pushv.exec();
                        break;

                    case "Outv":
                        Outv outv = (Outv) Global.getInstance().code.get(isn).get(i);
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
            Global.getInstance().code.clear();
            Global.getInstance().varMap.clear();
            Global.getInstance().memoryManager.determineMemory(Global.getInstance().vmspec.memory_bs, Global.getInstance().vmspec.memory_blockcount, Global.getInstance().vmspec.memory_stacksize);
        } catch (MemoryDetermineException e) {
            e.printStackTrace();
        }
        thread.stop(); // самоликвидация потока исполнения
    }


}
