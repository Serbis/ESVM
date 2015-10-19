
package esvm.vm;

import esvm.vm.desc.Block;
import esvm.vm.desc.Pointer;
import esvm.vm.exceptions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by serbis on 09.10.15.
 */
public class MemoryManager {
    private byte[] MEMORY;
    private byte[] STACK;
    private ArrayList<Block> blocks = new ArrayList<Block>();

    private int bs;
    private int pageCount;
    private int memorysize;
    private int stacksizeinbyte;
    private int stacksizeinint;
    private int stackpointer = -4; //Указатель на вершину стека

    public MemoryManager() {

    }

    /**
     * Создает виртуальную мамять размером bs*count байт
     *
     * @param bs размер блока
     * @param count количество блоков
     * @throws MemoryDetermineException не придумал пока причину вызова
     */
    public void determineMemory(int bs, int count, int stacksize) throws MemoryDetermineException{
        this.bs = bs;
        pageCount = count;
        memorysize = count * bs;
        stacksizeinbyte = stacksize;
        //if (stacksize % 4 != 0) {
        //    throw new MemoryDetermineException("Stack size is not a multiple of 4");
        //}
        stacksizeinint = stacksize / 4;
        MEMORY = new byte[memorysize]; //Созадли пустой массив байт
        for (int i = 0; i < memorysize; i++) { //Заполняем его нулями
            MEMORY[i] = 0;
        }
        STACK = new byte[stacksizeinbyte];
        for (int i = 0; i < stacksizeinbyte; i++) {
            STACK[i] = 0;
        }
    }

    /**
     * Выделяет в памяти область заданного раземра
     *
     * @param size разбер в байтах
     * @throws MemoryAllocateException в случае нехватики свободной памяти
     * @return указатель на созданную область памяти
     */
    public Pointer allocate(int size) throws MemoryAllocateException {
        boolean free;
        for (int i = 0; i < MEMORY.length; i++) { //Проходим по всей памяти
            free = true;
            if (MEMORY[i] == 0) { //до первого нулевого байта
                int blsa;
                int blss;
                for (int j = 0; j < blocks.size(); j++) { //и проверяем, не чей-то ли это байт
                    blsa = blocks.get(j).start.page * bs + blocks.get(j).start.offset;
                    blss = blsa + blocks.get(j).size;
                    if (i >= blsa && i < blss) {
                        free = false;
                        break; //если этот байт уже занят, выходим из цикла
                    }
                }
                if (free) { //если байт ничейный, аллоцируем новый блок
                    Pointer pointer = new Pointer(Math.round(i / bs), i - (Math.round(i / bs) * bs));
                    blocks.add(new Block(pointer, size));
                    return pointer;
                }
            }
        }

        throw new MemoryAllocateException("For allocating insufficient memory");
    }

    /**
     * Производит абсолютное выделение памяти не производя проверку на
     * занятость
     *
     * @param pointer адрес начала записи
     * @param size размер
     * @return указатель на созданную область памяти
     * @throws MemoryOutOfRangeException в сулчае если выделяемый блок памяти
     *      выходит за гранизы адрессного пространства
     */
    public Pointer callocate(Pointer pointer, int size) throws MemoryOutOfRangeException{
        blocks.add(new Block(pointer, size));

        return pointer;
    }

    /**
     * Перемещает область памяти по указанному в указателе адресу
     *
     * @param blockpointer указатель на перемещаемый блок
     * @param newpointer указатель на новый адрес в памяти
     * @return указатель на новый адрес
     * @throws MemoryAllocateException в случае если новое положение
     *      перекрывает существующий блок
     * @throws MemoryOutOfRangeException в случае если новое положение блока
     *      выходит за границы адрессного пространства
     * @throws MemoryNullBlockException в случае если по указанному в
     *      указателе адресу нет существующего блока
     */
    public Pointer rellocate(Pointer blockpointer, Pointer newpointer) throws MemoryAllocateException, MemoryNullBlockException, MemoryOutOfRangeException{
        boolean exist = false;
        int bi = 0;

        for (int i = 0; i < blocks.size(); i++) { //проверяем существует блок к которому хотим обратиться
            if (blocks.get(i).start.page == blockpointer.page && blocks.get(i).start.offset == blockpointer.offset) {
                bi = i;
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new MemoryNullBlockException("There is no block with address " + blockpointer.page + ":" + blockpointer.offset);
        }
        int oldad = blocks.get(bi).start.page * bs + blocks.get(bi).start.offset;
        int newad = newpointer.page * bs + newpointer.offset;

        if (newad + blocks.get(bi).size > MEMORY.length) { //Проверяем не выходит ли блок на новом адресе за пределы памяти
            throw new MemoryNullBlockException("New block address " + newpointer.page + ":" + newpointer.offset + " beyond the limit of the address space");
        }
        int conba;
        for (int i = 0; i < blocks.size(); i++) { //проверяем не перетрет ли блок другие блоки
            for (int j = 0; j < blocks.get(i).size; j++) {
                conba = blocks.get(i).start.page * bs + blocks.get(i).start.offset + j;
                for (int m = 0; m < blocks.get(bi).size; m++) {
                    if (conba == newad + m) {
                        throw new MemoryAllocateException("New block address " + newpointer.page + ":" + newpointer.offset + " covers the block at " +
                                blocks.get(i).start.page + ":" + blocks.get(i).start.offset);
                    }
                }
            }

        }

        for (int i = 0; i < blocks.get(bi).size; i++) {
            MEMORY[newad + i] = MEMORY[oldad + i];
            MEMORY[oldad + i] = 0;
        }
        blocks.get(bi).start = newpointer;

        return newpointer;
    }

    /**
     * Освобождает блок памяти по указанному в указателе адресу
     *
     * @param pointer указатель на блок
     * @throws MemoryNullBlockException в случае если по указанному в
     *      указателе адресу нет существующего блока
     */
    public void free(Pointer pointer) throws MemoryNullBlockException {
        boolean exist = false;
        int bi = 0;

        for (int i = 0; i < blocks.size(); i++) { //проверяем существует блок к которому хотим обратиться
            if (blocks.get(i).start.page == pointer.page && blocks.get(i).start.offset == pointer.offset) {
                bi = i;
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new MemoryNullBlockException("There is no block with address " + pointer.page + ":" + pointer.offset);
        }
        int ma = blocks.get(bi).start.page * bs + blocks.get(bi).start.offset;
        for (int i = 0; i < blocks.get(bi).size; i++) { //Пишем блок
            MEMORY[ma + i] = 0;
        }
        blocks.remove(bi);
    }

    /**
     * Читает память по адресу из указателя на n байт вперед
     *
     * @param pointer указатель на начало области чтения
     * @param size длинна в байтах
     * @return массив считанных байт
     * @throws MemoryOutOfRangeException в случае если считываемая область
     *      памяти выходит за граница адрессного пространста
     */
    public byte[] read(Pointer pointer, int size) throws MemoryOutOfRangeException{
        int lastba = (pointer.page * bs) + (pointer.offset + size);
        if (lastba > MEMORY.length) {
            Pointer req = new Pointer(Math.round(lastba / bs) + 1, lastba - (Math.round(lastba / bs) * bs));
            throw new MemoryOutOfRangeException("Attempt to address at overseas address space. " +
                    "The requested address is " + String.valueOf(req.page + ":" + req.offset) +" , the maximum possible address of " + String.valueOf(pageCount + ":" + bs));
        }
        byte[] bytes = new byte[size];
        int sta = pointer.page * bs + pointer.offset;
        for (int i = 0; i < size; i++) {
            bytes[i] = MEMORY[sta + i];
        }

        return bytes;
    }

    /**
     * Производит аболютную запись по указанному в указателе адресу без
     * проверки на затирание существующих данных
     *
     * @param pointer указатель начала записи
     * @param bytes записываемые данные
     * @throws MemoryOutOfRangeException в случае если считываемая область
     *      памяти выходит за граница адрессного пространста
     */
    public void write(Pointer pointer, byte[] bytes) throws MemoryOutOfRangeException {
        int lastba = (pointer.page * bs) + (pointer.offset + bytes.length);
        if (lastba > MEMORY.length) {
            Pointer req = new Pointer(Math.round(lastba / bs) + 1, lastba - (Math.round(lastba / bs) * bs));
            throw new MemoryOutOfRangeException("Attempt to address at overseas address space. " +
                    "The requested address is " + String.valueOf(req.page + ":" + req.offset) +" , the maximum possible address of " + String.valueOf(pageCount + ":" + bs));
        }
        int sta = (pointer.page * bs) + pointer.offset;
        for (int i = 0; i < bytes.length; i++) {
            MEMORY [sta + i] = bytes[i];
        }
    }

    /**
     * Читает блок по указанному в указателе адресу
     *
     * @param pointer указатель на блок
     * @return массив считанных байт
     * @throws MemoryNullBlockException в случае если по указанному в
     *      указателе адресу нет существующего блока
     */
    public byte[] readBlock(Pointer pointer) throws MemoryNullBlockException{
        boolean exist = false;
        int bi = 0;

        for (int i = 0; i < blocks.size(); i++) { //проверяем существует блок к которому хотим обратиться
            if (blocks.get(i).start.page == pointer.page && blocks.get(i).start.offset == pointer.offset) {
                bi = i;
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new MemoryNullBlockException("There is no block with address " + pointer.page + ":" + pointer.offset);
        }
        int ma = blocks.get(bi).start.page * bs + blocks.get(bi).start.offset;
        byte[] bytes = new byte[blocks.get(bi).size];
        for (int i = 0; i < blocks.get(bi).size; i++) { //Пишем блок
            bytes[i] = MEMORY[ma + i];
        }

        return bytes;
    }

    /**
     * Записывает блок по указанному в указателе адресу
     *
     * @param pointer указатель на блок
     * @param bytes записываемные данные
     * @throws MemoryNullBlockException в случае если по указанному в
     *      указателе адресу нет существующего блока
     * @throws MemoryOutOfRangeException в случае если размер данных превышает
     *      размеры блока
     */
    public void writeBlock(Pointer pointer, byte[] bytes) throws MemoryNullBlockException, MemoryOutOfRangeException {
        boolean exist = false;
        int bi = 0;

        for (int i = 0; i < blocks.size(); i++) { //проверяем существует блок к которому хотим обратиться
            if (blocks.get(i).start.page == pointer.page && blocks.get(i).start.offset == pointer.offset) {
                bi = i;
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new MemoryNullBlockException("There is no block with address " + pointer.page + ":" + pointer.offset);
        }
        if (blocks.get(bi).size != bytes.length) { //Проверяем на размерность блока размерности данных
            throw new MemoryOutOfRangeException("Block at adress " + pointer.page + ":" + pointer.offset + "size is not equal to the data size");
        }
        int ma = blocks.get(bi).start.page * bs + blocks.get(bi).start.offset;
        for (int i = 0; i < bytes.length; i++) { //Пишем блок
            MEMORY[ma + i] = bytes[i];
        }
    }

    /**
     * Помещает значение в стек
     *
     * @param integer значение
     * @throws StackOverflowException в случае преполнения стека
     */
    public void push(int integer) throws StackOverflowException{
        if (stackpointer != stacksizeinint - 4) {
            byte[] byteArray;

            stackpointer += 4;
            byteArray = ByteBuffer.allocate(4).putInt(integer).array();
            for (int i = 0; i < byteArray.length; i++) {
                STACK[stackpointer + i] = byteArray[i];
            }

        } else {
            throw new StackOverflowException("Stack overflow, the maximum stack size " + String.valueOf(stacksizeinbyte));
        }
    }

    /**
     * Извлекает последнее значение из стека
     *
     * @return значение
     * @throws NullReferenceException в случае если стек пуст
     */
    public int pop() throws NullReferenceException{
        if (stackpointer >= 0) {
            byte[] bytes = new byte[4];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = STACK[stackpointer + i];
                STACK[stackpointer + i] = 0;
            }
            stackpointer -= 4;
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            return byteBuffer.getInt();
        } else {
            throw new NullReferenceException("Stack is empty");
        }
    }

    /**
     * Возвращает объем свободной памяти
     *
     * @return число байт
     */
    public int getFreeMemorySize() {
        int counter = 0;

        for (int i = 0; i < blocks.size(); i++) {
            counter = counter + blocks.get(i).size;
        }

        return memorysize - counter;
    }

    /**
     * Возвращает общий объем памяти
     *
     * @return число байт
     */
    public int getMemorySize() {

        return memorysize;
    }

    /**
     * Проводит процедуру дефрагментации памяти
     *
     */
    public void defrag() {
        ArrayList<Block> free = new ArrayList<Block>(); //Массив пустых сегментов
        ArrayList<Block> exist = new ArrayList<Block>(); //Массив занятых сегментов
        int ba;
        boolean draw = true; //Флаг ничейного байта
        boolean drawcollect = false; //Флаг процесса сборки свободного блока
        int drawcounter = 0; //Счетчик пустых байтов
        int drawstarter = 0; //Начала области путоты

        for (int i = 0; i < MEMORY.length; i++) {
            draw = true;
            for (int j = 0; j < blocks.size(); i++) {
                ba = blocks.get(j).start.page * bs + blocks.get(j).start.offset;
                if (ba == i) {
                    i += blocks.get(j).size - 1;
                    draw = false;
                    drawcollect = false;
                    if (drawcounter > 0) {
                        free.add(new Block(new Pointer(Math.round(drawcounter / bs), drawcounter - (Math.round(drawcounter / bs) * bs)), drawcounter));
                    }
                    break;
                }
            }
            if (draw) {
                if (!drawcollect) {
                    drawcollect = true;
                    drawstarter = i;
                }
                drawcounter++;
            }
        }
    }

    /**
     * Создает дапм памяти от и до указанного блока. Может принимать на вход
     * null, в таком случае будет создаен дамп всей памяти сразу
     *
     * @param startpage начальный блок
     * @param count количество дампируемых блоков
     * @return ссылка на файл с дампом
     * @throws MemoryOutOfRangeException в случае если колчество указанных
     *      блоков физически превышает размер блоков памяти
     */
    public File dump(int startpage, int count) throws MemoryOutOfRangeException {
        if (startpage + count > pageCount) { //Проверям на то, что мы не вылезем за пределы адрессного пространства
            throw new MemoryOutOfRangeException("The requested dump is beyond the limit of the address space. " +
            "The ultimate requested page " + String.valueOf(startpage + count) +" , but the memory is only " + String.valueOf(pageCount));
        }
        byte[] signbs = ByteBuffer.allocate(4).putInt(bs).array();
        byte[] signcount = ByteBuffer.allocate(4).putInt(pageCount).array();
        File file = new File("/home/serbis/Projects/JAVA/ESVM/memory.dump");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(signbs);
            fos.write(signcount);
            if (startpage == -1 || count == -1) { //Елси где-то null
                fos.write(MEMORY); //То просто пишем всю память в файл
            } else { //Если есть размеры дампы
                for (int i = 0; i < count; i++) { //то вычленяем из памяти нужные блоки и пишем их туда же
                    byte[] page = new byte[bs];
                    for (int j = 0; j < bs; j++) {
                        page[j] = MEMORY[(startpage * bs) + i];
                    }
                    fos.write(page);
                }
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * Создает дапм стека от и до указанного значения. Может принимать на вход
     * null, в таком случае будет создаен дамп всего стека сразу
     *
     * @param startvalue начальное значение
     * @param count количество дампируемых значений
     * @return ссылка на файл с дампом
     * @throws MemoryOutOfRangeException в случае если колчество указанных
     *      занчений физически превышает размер стека
     */
    public File dumpstack(int startvalue, int count) throws StackOutOfRangeException {
        if (startvalue + count > STACK.length / 4) { //Проверям на то, что мы не вылезем за пределы адрессного пространства
            throw new StackOutOfRangeException("The requested dump is beyond the limit of the address space. " +
                    "The ultimate requested page " + String.valueOf(startvalue + count) +" , but the stack is only " + String.valueOf(pageCount));
        }
        byte[] signbs = ByteBuffer.allocate(4).putInt(bs).array();
        byte[] signcount = ByteBuffer.allocate(4).putInt(pageCount).array();
        File file = new File("/home/serbis/Projects/JAVA/ESVM/stack.dump");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(signbs);
            fos.write(signcount);
            if (startvalue == -1 || count == -1) { //Елси где-то null
                fos.write(STACK); //То просто пишем весь стек в файл
            } else { //Если есть размеры дампы
                //for (int i = 0; i < count; i++) { //то вычленяем из памяти нужные блоки и пишем их туда же
                //    byte[] page = new byte[4];
                //    for (int j = 0; j < bs; j++) {
                //        page[j] = MEMORY[(startpage * bs) + i];
                //    }
                //    fos.write(page);
                //}
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }


}
