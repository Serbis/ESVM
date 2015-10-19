package esvm.vm;

import esvm.vm.desc.Vmspec;
import esvm.vm.exceptions.MemoryDetermineException;

import java.io.File;

/**
 * Головной класс виртуальной машины
 */
public class ESVM {
    private MemoryManager memoryManager;
    private ClassLoader classLoader;
    private Disassembler disassembler;
    private Executor executor;
    private Global global;

    /**
     * Создазает виртальные объекты на соновании полученной на вход
     * спецификации
     *
     * @param vmspec спецификация виртуальной машины
     */
    public ESVM(Vmspec vmspec) {
        global = new Global();
        memoryManager = new MemoryManager();
        classLoader = new ClassLoader();
        executor = new Executor();
        Global.getInstance().vmspec = vmspec;
        Global.getInstance().memoryManager = memoryManager;
        Global.getInstance().interruptsManager = new InterruptsManager();
        try {
            memoryManager.determineMemory(vmspec.memory_bs, vmspec.memory_blockcount, vmspec.memory_stacksize);
        } catch (MemoryDetermineException e) {
            e.printStackTrace();
        }
        if (vmspec.classpath != null) {
            classLoader.load(new File(vmspec.classpath));
        }
    }

    /**
     * Возращает менеджер памяти
     *
     * @return экзмепляр менеджера памяти
     */
    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    /**
     * Возвращает загрузчик классов
     *
     * @return экземпляр загрузчика классов
     */
    public ClassLoader getClassLoader() { return classLoader; }

    /**
     * Возвращает дисзассемблер
     *
     * @return экземпляр дизассемблера
     */
    public Disassembler getDisassembler() {
        if (disassembler == null) {
            disassembler = new Disassembler();
        }

        return disassembler;
    }

    /**
     * Возвращает экзекутор
     *
     * @return экземпляр экзекутора
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * Возращает менеджер прерываний
     *
     * @return экзмепляр менеджера прерываний
     */
    public InterruptsManager getInterruptsManager() {
        return Global.getInstance().interruptsManager;
    }

    /**
     * Возращает менеджер глобальных обхектов
     *
     * @return экземпляр менеджела глабольных обхектов
     */
    public Global getGlobal() {
        return global;
    }
}
