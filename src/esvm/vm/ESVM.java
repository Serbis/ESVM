package esvm.vm;

import esvm.vm.desc.Vmspec;
import esvm.vm.exceptions.MemoryDetermineException;

/**
 * Головной класс виртуальной машины
 */
public class ESVM {
    private MemoryManager memoryManager;

    /**
     * Создазает виртальные объекты на соновании полученной на вход
     * спецификации
     *
     * @param vmspec спецификация виртуальной машины
     */
    public ESVM(Vmspec vmspec) {
        memoryManager = new MemoryManager();
        try {
            memoryManager.determineMemory(vmspec.memory_bs, vmspec.memory_blockcount, vmspec.memory_stacksize);
        } catch (MemoryDetermineException e) {
            e.printStackTrace();
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
}
