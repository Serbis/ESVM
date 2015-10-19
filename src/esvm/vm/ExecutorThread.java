package esvm.vm;

import esvm.vm.desc.Pointer;
import esvm.vm.desc.Var;
import esvm.vm.exceptions.*;
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
                        Global.getInstance().memoryManager.push(Global.getInstance().memoryManager.pop() + Global.getInstance().memoryManager.pop());
                        break;

                    case "Sub":
                        Global.getInstance().memoryManager.push(Global.getInstance().memoryManager.pop() - Global.getInstance().memoryManager.pop());
                        break;

                    case "Push":
                        Push push = (Push) Global.getInstance().iSet.get(i);
                        Global.getInstance().memoryManager.push(push.arg1);
                        break;

                    case "Pop":
                        Pop pop = (Pop) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(pop.arg1);
                        byte[] bytes = ByteBuffer.allocate(4).putInt(Global.getInstance().memoryManager.pop()).array();
                        Global.getInstance().memoryManager.writeBlock(pointer, bytes);

                        break;

                    case "Cmp":
                        Cmp cmp = (Cmp) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(cmp.arg1);
                        pointer2 = Global.getInstance().getVarPointerById(cmp.arg2);
                        byte[] var1 = Global.getInstance().memoryManager.readBlock(pointer);
                        byte[] var2 = Global.getInstance().memoryManager.readBlock(pointer2);
                        boolean diff = false;
                        if (var1.length == var2.length) {
                            Global.getInstance().boolOpFloag = Global.BoolLogic.EQUAL;
                        } else {
                            Global.getInstance().boolOpFloag = Global.BoolLogic.NOT_EQUAL;
                            break;
                        }

                        for (int j = 0; j < var1.length; j++) {
                            if (var1[j] != var2[j]) {
                                diff = true;
                            }
                            if (diff) {
                                Global.getInstance().boolOpFloag = Global.BoolLogic.NOT_EQUAL;
                                break;
                            }
                        }

                        break;

                    case "Inc":
                        Inc inc = (Inc) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(inc.arg1);
                        byte[] var = Global.getInstance().memoryManager.readBlock(pointer);
                        byteBuffer = ByteBuffer.wrap(var);
                        if (var.length == 2) {
                            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
                            int vShort = byteBuffer.getShort();
                            vShort++;
                            byteBuffer = ByteBuffer.allocate(2);
                            byteBuffer.putInt(vShort);
                            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
                        } else if (var.length == 4) {
                            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
                            int vInt = byteBuffer.getInt();
                            vInt++;
                            byteBuffer = ByteBuffer.allocate(4);
                            byteBuffer.putInt(vInt);
                            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
                        } else {
                            //Тут нужно как-то сгенерировать проверку типов
                        }
                        break;

                    case "Dec":
                        Dec dec = (Dec) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(dec.arg1);
                        byte[] vara = Global.getInstance().memoryManager.readBlock(pointer);
                        byteBuffer = ByteBuffer.wrap(vara);
                        if (vara.length == 2) {
                            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
                            int vShort = byteBuffer.getShort();
                            vShort--;
                            byteBuffer = ByteBuffer.allocate(2);
                            byteBuffer.putInt(vShort);
                            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
                        } else if (vara.length == 4) {
                            byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
                            int vInt = byteBuffer.getInt();
                            vInt--;
                            byteBuffer = ByteBuffer.allocate(4);
                            byteBuffer.putInt(vInt);
                            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
                        } else {
                            //Тут нужно как-то сгенерировать проверку типов
                        }
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
                        pointer = Global.getInstance().getVarPointerById(xchg.arg1);
                        pointer2 = Global.getInstance().getVarPointerById(xchg.arg2);
                        byte[] var1b = Global.getInstance().memoryManager.readBlock(pointer);
                        byte[] var2b = Global.getInstance().memoryManager.readBlock(pointer2);
                        if (var1b.length != var2b.length) {
                            //Тут исключение несовместимости типов
                        }
                        byte[] median = var1b;
                        var1b = var2b;
                        var2b = median;
                        Global.getInstance().memoryManager.writeBlock(pointer, var1b);
                        Global.getInstance().memoryManager.writeBlock(pointer, var2b);
                        break;

                    case "Lea":
                        Lea lea = (Lea) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(lea.arg1);
                        pointer2 = Global.getInstance().getVarPointerById(lea.arg2);
                        byteBuffer = ByteBuffer.allocate(4);
                        byteBuffer.putShort((short) pointer.page);
                        byteBuffer.putShort((short) pointer.offset);
                        Global.getInstance().memoryManager.writeBlock(pointer2, byteBuffer.array());
                        break;

                    case "Int":
                        Int intetrrupt = (Int) Global.getInstance().iSet.get(i);
                        Global.getInstance().interruptsManager.exexInterrupt(intetrrupt.arg1, intetrrupt.arg2);
                        break;

                    case "Loop":
                        Loop loop = (Loop) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(loop.arg1);
                        byteBuffer = ByteBuffer.wrap(Global.getInstance().memoryManager.readBlock(pointer));
                        int counter = byteBuffer.getInt();
                        if (counter != 0) {
                            i = loop.arg2 - 1;
                            counter--;
                            byteBuffer = ByteBuffer.allocate(4);
                            byteBuffer.putInt(counter);
                            Global.getInstance().memoryManager.writeBlock(pointer, byteBuffer.array());
                        }
                        break;

                    case "Jmp":

                        break;

                    case "Div":

                        break;

                    case "IDiv":

                        break;

                    case "Je":

                        break;

                    case "Jz":

                        break;

                    case "Jg":


                    case "Jge":

                        break;

                    case "Jl":

                        break;

                    case "Jle":

                        break;

                    case "Jne":

                        break;

                    case "Jnge":

                        break;

                    case "Jnl":

                        break;

                    case "Jnle":

                        break;

                    case "Out":
                        Out out = (Out) Global.getInstance().iSet.get(i);
                        Global.getInstance().ports[out.arg1] = out.arg2;
                        break;

                    case "Inp":

                        break;

                    case "Db":
                        Db db = (Db) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().memoryManager.allocate(db.arg2);
                        Global.getInstance().varMap.add(new Var(db.arg1, pointer, db.arg2));

                        break;

                    case "Set":
                        Set set = (Set) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(set.arg1);
                        Global.getInstance().memoryManager.writeBlock(pointer, set.arg3);
                        break;

                    case "Pushv":

                        break;

                    case "Outv":
                        Outv outv = (Outv) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(outv.arg2);
                        byte[] vlaue = Global.getInstance().memoryManager.readBlock(pointer);
                        byteBuffer = ByteBuffer.wrap(vlaue);
                        Global.getInstance().ports[0] = byteBuffer.getInt();
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
