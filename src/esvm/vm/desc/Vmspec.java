package esvm.vm.desc;

/**
 * Описывает спецификацию запуска виртуальной машины
 */
public class Vmspec {
    public int memory_bs;
    public int memory_blockcount;
    public int memory_stacksize; //Кратно четырем
    public String classpath;

    public Vmspec(int memory_bs, int memory_blockcount, int memory_stacksize, String classpath) {
        this.memory_bs = memory_bs;
        this.memory_blockcount = memory_blockcount;
        this.memory_stacksize = memory_stacksize;
        this.classpath = classpath;
    }
}
