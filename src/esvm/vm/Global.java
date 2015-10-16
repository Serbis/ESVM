package esvm.vm;

import esvm.vm.desc.Pointer;
import esvm.vm.desc.Var;
import esvm.vm.exceptions.InterruptNotFoundException;
import esvm.vm.exceptions.MemoryAllocateException;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;
import esvm.vm.instructions.Instruction;

import java.util.ArrayList;

/**
 * Created by serbis on 13.10.15.
 */
public class Global {
    public ArrayList<Instruction> iSet;
    public ArrayList<Var> varMap = new ArrayList<Var>();
    public ArrayList<Short> releasedId = new ArrayList<>();
    public int[] ports = new int[255];
    private static Global instance;
    public InterruptsManager interruptsManager;
    public MemoryManager memoryManager;
    private ExecutorThread executorThread;   //Экземпляр потока исполнения

    public Global() {
        initPorts();

        instance = this;
    }

    public static Global getInstance() {
        return instance;
    }

    /**
     * Возращает первый свободный id для переменной
     *
     * @return id
     */
    public short getFreeVarId() {
        if (releasedId.size() > 0) {
            return releasedId.get(releasedId.size());
        } else {
            return (short) varMap.size();
        }
    }

    /**
     * Возращает указатель на переменную в памяти по ее id
     *
     * @param id
     * @return
     */
    public Pointer getVarPointerById(short id) {
        for (Var aVarMap : varMap) {
            if (aVarMap.id == id) {
                return aVarMap.pointer;
            }
        }

        return null;
    }

    private void initPorts() {
        for (int i = 0; i < ports.length; i++) {
            ports[i] = 0;
        }
    }

    public ExecutorThread getExecutorThread() {
        return executorThread;
    }

    public void setExecutorThread(ExecutorThread executorThread) {
        this.executorThread = executorThread;
    }

    public void putToPort(int portnum, int value) {
        ports[portnum] = value;
    }

    public int getInstructionsCount() {
        return iSet.size();
    }

    /**
     * Создает прерывание вывода исключению
     *
     * @param text текст исключения
     * @return
     */
    public void createExceptionInturrupt(String text) {
        byte[] textb = new byte[text.length()];
        for (int i = 0; i < textb.length; i++) {
            char ch = text.charAt(i);
            textb[i] = (byte) ch;
        }
        try {
            Pointer pointer = memoryManager.allocate(textb.length);
            memoryManager.writeBlock(pointer, textb);
            short id = getFreeVarId();
            varMap.add(new Var(id, pointer, textb.length));
            ports[2] = id;
            interruptsManager.exexInterrupt((byte) 1, (byte) 2);
        } catch (MemoryAllocateException | MemoryNullBlockException | MemoryOutOfRangeException | InterruptNotFoundException e) {
            e.printStackTrace();
        }
    }


}
