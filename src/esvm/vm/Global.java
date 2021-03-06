package esvm.vm;

import esvm.vm.desc.*;
import esvm.vm.desc.attributes.ClassAttribute;
import esvm.vm.desc.constpool.ClassConstant;
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
    public Vmspec vmspec; //Экземпляр конфигурации вм
    public BoolLogic boolOpFloag; //Флаг результат булевой операции
    public ArrayList<ClassConstant> counstant_pool = new ArrayList<ClassConstant>();
    public ArrayList<ClassField> fields = new ArrayList<ClassField>();
    public ArrayList<ClassMethod> methods = new ArrayList<ClassMethod>();
    public ArrayList<ClassAttribute> attributes = new ArrayList<ClassAttribute>();
    public ArrayList<ArrayList<Instruction>> code = new ArrayList<ArrayList<Instruction>>();

    public short access_flags;
    public short this_class;
    public short super_class;

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

    /**
     * Возаращает тип переменной по ее id
     *
     * @param id идентефикатор переменной
     * @return тип переменной
     */
    public StackObject.StackDataType getVarTypeById(int id) {
        for (Var aVarMap : varMap) {
            if (aVarMap.id == id) {
                return aVarMap.type;
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

    public int getInstructionsCount(int mid) {
        return code.get(mid).size();
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

    public enum BoolLogic {
        INDEF, EQUAL, NOT_EQUAL, NULL, GROSS, LESS, NOT_GROSS, NOT_LESS
    }


}
