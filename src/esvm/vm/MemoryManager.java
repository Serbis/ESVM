package esvm.vm;

import esvm.fields.Address;
import esvm.vm.desc.Block;
import esvm.vm.desc.Pointer;
import esvm.vm.exceptions.MemoryAllocateException;
import esvm.vm.exceptions.MemoryDetermineException;
import esvm.vm.exceptions.MemoryNullBlockException;
import esvm.vm.exceptions.MemoryOutOfRangeException;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by serbis on 09.10.15.
 */
public class MemoryManager {
    private byte[] MEMORY;
    private ArrayList<Block> blocks = new ArrayList<Block>();

    private int bs;
    private int blockcount;
    private int memorysize;

    public MemoryManager() {

    }

    /**
     * Создает виртуальную мамять размером bs*count байт
     *
     * @param bs размер блока
     * @param count количество блоков
     * @throws MemoryDetermineException не придумал пока причину вызова
     */
    public void determineMemory(int bs, int count) throws MemoryDetermineException{
        this.bs = bs;
        blockcount = count;
        memorysize = count * bs;
        MEMORY = new byte[memorysize]; //Созадли пустой массив байт
        for (int i = 0; i < memorysize; i++) { //Заполняем его нулями
            MEMORY[i] = 0;
        }
        int a;
        a = 1 + 1;
    }

    /**
     * Выделяет в памяти область заданного раземра
     *
     * @param size разбер в байтах
     * @throws MemoryAllocateException в случае нехватики свободной памяти
     * @return указатель на созданную область памяти
     */
    public Pointer allocate(int size) throws MemoryAllocateException {

        return  new Pointer(0,0);
    }

    /**
     * Производит абсолютное выделение памяти с затиранием занятых областей
     *
     * @param address адрес начала записи
     * @param size размер
     * @return указатель на созданную область памяти
     * @throws MemoryOutOfRangeException в сулчае если выделяемый блок памяти
     *      выходит за гранизы адрессного пространства
     */
    public Pointer callocate(Address address, int size) throws MemoryOutOfRangeException{

        return new Pointer(0, 0);
    }

    /**
     * Перемещает область памяти по указанному в указателе адресу
     *
     * @param pointer указатель на новый адрес в памяти
     * @return указатель на новый адрес
     * @throws MemoryAllocateException в случае если новое положение
     *      перекрывает существующий блок
     * @throws MemoryOutOfRangeException в случае если новое положение блока
     *      выходит за границы адрессного пространства
     * @throws MemoryNullBlockException в случае если по указанному в
     *      указателе адресу нет существующего блока
     */
    public Pointer rellocate(Pointer pointer) throws MemoryAllocateException, MemoryNullBlockException, MemoryOutOfRangeException{

        return new Pointer(0, 0);
    }

    /**
     * Освобождает блок памяти по указанному в указателе адресу
     *
     * @param pointer указатель на блок
     * @throws MemoryNullBlockException в случае если по указанному в
     *      указателе адресу нет существующего блока
     */
    public void free(Pointer pointer) throws MemoryNullBlockException {

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

        return new byte[2];
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
        if (pointer.page * pointer.offset > bytes.length - 1) {
            throw new MemoryOutOfRangeException("Attempt to address at overseas address space. " +
                    "The requested address is " + String.valueOf(blockcount + ":" + bs) +" , the maximum possible address of " + String.valueOf(pointer.page + ":" + pointer.offset));
        }
        for (int i = 0; i < bytes.length; i++) {
            MEMORY [(pointer.page * pointer.offset) + i] = bytes[i];
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


        return new byte[2];
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
        if (startpage + count > blockcount) { //Проверям на то, что мы не вылезем за пределы адрессного пространства
            throw new MemoryOutOfRangeException("The requested dump is beyond the limit of the address space. " +
            "The ultimate requested page " + String.valueOf(startpage + count) +" , but the memory is only " + String.valueOf(blockcount));
        }
        byte[] signbs = ByteBuffer.allocate(4).putInt(bs).array();
        byte[] signcount = ByteBuffer.allocate(4).putInt(blockcount).array();
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




}
