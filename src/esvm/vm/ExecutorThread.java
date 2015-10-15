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
    public Thread thread;
    public boolean stoped = false;
    private Pointer pointer;

    public ExecutorThread() {
        thread = new Thread(this, "Executor thread");
        thread.start(); // Запускаем поток
        Global.getInstance().interruptsManager.setThread(thread);
        Global.getInstance().setExecutorThread(this);
    }

    @Override
    public void run() {
        for (int i = 0; i < Global.getInstance().iSet.size(); i++) {
            switch (Global.getInstance().iSet.get(i).asm) {
                case "Add":

                    break;

                case "Sub":

                    break;

                case "Push":

                    break;

                case "Pop":
                    Pop pop = (Pop) Global.getInstance().iSet.get(i);
                    pointer = Global.getInstance().getVarPointerById(pop.arg1);
                    try {
                        byte[] bytes = ByteBuffer.allocate(4).putInt(Global.getInstance().memoryManager.pop()).array();
                        Global.getInstance().memoryManager.writeBlock(pointer, bytes);
                    } catch (NullReferenceException | MemoryOutOfRangeException | MemoryNullBlockException e) {
                        e.printStackTrace();
                    }

                    break;

                case "Cmp":

                    break;

                case "Inc":

                    break;

                case "Dec":

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

                    break;

                case "Lea":

                    break;

                case "Int":
                    Int intetrrupt = (Int) Global.getInstance().iSet.get(i);
                    try {
                        Global.getInstance().interruptsManager.exexInterrupt(intetrrupt.arg1, intetrrupt.arg2);
                    } catch (InterruptNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

                case "Loop":

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
                    Out out  = (Out) Global.getInstance().iSet.get(i);
                    Global.getInstance().ports[out.arg1] = out.arg2;
                    break;

                case "Inp":

                    break;

                case "Db":
                    Db db = (Db) Global.getInstance().iSet.get(i);
                    try {
                        pointer = Global.getInstance().memoryManager.allocate(db.arg2);
                        Global.getInstance().varMap.add(new Var(db.arg1, pointer, db.arg2));
                    } catch (MemoryAllocateException e) {
                        e.printStackTrace();
                    }
                    break;

                case "Set":
                    try {
                        Set set = (Set) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(set.arg1);
                        Global.getInstance().memoryManager.writeBlock(pointer, set.arg3);
                    } catch (MemoryNullBlockException | MemoryOutOfRangeException e) {
                        e.printStackTrace();
                    }
                    break;

                case "Pushv":

                    break;

                case "Outv":
                    try {
                        Outv outv  = (Outv) Global.getInstance().iSet.get(i);
                        pointer = Global.getInstance().getVarPointerById(outv.arg2);
                        byte[] vlaue = Global.getInstance().memoryManager.readBlock(pointer);
                        ByteBuffer byteBuffer = ByteBuffer.wrap(vlaue);
                        Global.getInstance().ports[0] = byteBuffer.getInt();
                    } catch (MemoryNullBlockException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
