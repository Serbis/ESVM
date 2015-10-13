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

    /**
     * Создазает виртальные объекты на соновании полученной на вход
     * спецификации
     *
     * @param vmspec спецификация виртуальной машины
     */
    public ESVM(Vmspec vmspec) {
        memoryManager = new MemoryManager();
        classLoader = new ClassLoader();
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
}
