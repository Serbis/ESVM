package esvm.vm.desc;

/**
 * Описывает блок памяти. Содержит начальный адрес блок и его размер
 */
public class Block {
    public Pointer start;
    public int size;

    public Block(Pointer start, int size) {
        this.start = start;
        this.size = size;
    }
}
