package esvm.vm;

import esvm.vm.exceptions.ClassVerificationErrorException;
import esvm.vm.instructions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Загрузчик классов
 */
public class ClassLoader {
    private byte[] stream;
    private ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    private Set set;

    public ClassLoader() {

    }

    /**
     * Загружает класс в головоной сет инструкций. Запускает верефикацию
     * которая на данном этапе развития системы является процедурой AOT
     * копмиляции
     *
     * @param file файл класса
     */
    public void load(File file) {
        stream = readFile(file);

        if (stream != null) {
            for (int i = 0; i < stream.length; i++) {
                try {
                    i = i + verificate(stream[i], i);
                } catch (ClassVerificationErrorException e) {
                    e.printStackTrace();
                }
            }
        }
        Global.getInstance().iSet = instructions;
    }

    /**
     * Читает файл класса и возващает массив байт
     *
     * @param file файл класса
     * @return массив байт
     */
    private byte[] readFile(File file) {
        byte[] bytes;

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            bytes = new byte[(int) raf.length()];
            for (int i = 0; i < raf.length(); i++) {
                raf.seek(i);
                bytes[i] = raf.readByte();
            }
            raf.close();

            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Проводит покомандную верефикацию байт-кода. Производит проверки:
     * 1. Синтаксический ошибки в структуре кода
     *
     * @param bt входной байт для анализа
     * @param pos позиция входного байта в потоке
     * @return интертор - на сколько нужно перепрыгуть к следующему байту
     * @throws ClassVerificationErrorException в случае если обнаружена
     *      синтаксическая ошибка в байт-коде
     */
    private int verificate(byte bt, int pos) throws ClassVerificationErrorException {
        ByteBuffer arg1;
        ByteBuffer arg2;
        ByteBuffer arg3;

        switch (bt) {
            case Add.code:
                Add add = new Add();
                add.offset = pos;
                instructions.add(add);
                return 0;

            case Sub.code:
                Sub sub = new Sub();
                sub.offset = pos;
                instructions.add(sub);
                return 0;

            case Push.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Push push = new Push(arg1.getInt());
                push.offset = pos;
                instructions.add(push);
                return 4;

            case Pop.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                Pop pop = new Pop(arg1.getShort());
                pop.offset = pos;
                instructions.add(pop);
                return 2;

            case Cmp.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4]});
                Cmp cmp = new Cmp(arg1.getShort(), arg2.getShort());
                cmp.offset = pos;
                instructions.add(cmp);
                return 4;

            case Inc.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                Inc inc = new Inc(arg1.getShort());
                inc.offset = pos;
                instructions.add(inc);
                return 2;

            case Dec.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                Dec dec = new Dec(arg1.getShort());
                dec.offset = pos;
                instructions.add(dec);
                return 2;

            case Mul.code:
                Mul mul = new Mul();
                mul.offset = pos;
                instructions.add(mul);
                return 0;

            case IMul.code:
                IMul iMul = new IMul();
                iMul.offset = pos;
                instructions.add(iMul);
                return 0;

            case Movis.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                Movis movis = new Movis(arg1.getShort());
                movis.offset = pos;
                instructions.add(movis);
                return 2;

            case Movos.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                Movos movos = new Movos(arg1.getShort());
                movos.offset = pos;
                instructions.add(movos);

                return 2;

            case Xchg.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4]});
                Xchg xchg = new Xchg(arg1.getShort(), arg2.getShort());
                xchg.offset = pos;
                instructions.add(xchg);
                return 4;

            case Lea.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4]});
                Lea lea = new Lea(arg1.getShort(), arg2.getShort());
                lea.offset = pos;
                instructions.add(lea);
                return 4;

            case Int.code:
                Int iint = new Int(stream[pos + 1], stream[pos + 2]);
                iint.offset = pos;
                instructions.add(iint);
                return 2;

            case Loop.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4], stream[pos + 5], stream[pos + 6]});
                Loop loop = new Loop(arg1.getShort(), arg2.getInt());
                loop.offset = pos;
                instructions.add(loop);
                return 6;

            case Jmp.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jmp jmp = new Jmp(arg1.getInt());
                jmp.offset = pos;
                instructions.add(jmp);
                return 4;

            case Div.code:
                Div div = new Div();
                div.offset = pos;
                instructions.add(div);
                return 0;

            case IDiv.code:
                IDiv iDiv = new IDiv();
                iDiv.offset = pos;
                instructions.add(iDiv);
                return 0;

            case Je.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Je je = new Je(arg1.getInt());
                je.offset = pos;
                instructions.add(je);
                return 4;

            case Jz.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jz jz = new Jz(arg1.getInt());
                jz.offset = pos;
                instructions.add(jz);
                return 4;

            case Jg.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jg jg = new Jg(arg1.getInt());
                jg.offset = pos;
                instructions.add(jg);
                return 4;

            case Jge.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jge jge = new Jge(arg1.getInt());
                jge.offset = pos;
                instructions.add(jge);
                return 4;

            case Jl.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jl jl = new Jl(arg1.getInt());
                jl.offset = pos;
                instructions.add(jl);
                return 4;

            case Jle.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jle jle = new Jle(arg1.getInt());
                jle.offset = pos;
                instructions.add(jle);
                return 4;

            case Jne.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jne jne = new Jne(arg1.getInt());
                jne.offset = pos;
                instructions.add(jne);
                return 4;

            case Jnge.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jnge jnge = new Jnge(arg1.getInt());
                jnge.offset = pos;
                instructions.add(jnge);
                return 4;

            case Jnl.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jnl jnl = new Jnl(arg1.getInt());
                jnl.offset = pos;
                instructions.add(jnl);
                return 4;

            case Jnle.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                Jnle jnle = new Jnle(arg1.getInt());
                jnle.offset = pos;
                instructions.add(jnle);
                return 4;

            case Out.code:
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 2], stream[pos + 3], stream[pos + 4], stream[pos + 5]});
                Out out = new Out(stream[pos + 1], arg2.getInt());
                out.offset = pos;
                instructions.add(out);
                return 5;

            case Inp.code:
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 2], stream[pos + 3]});
                Inp inp = new Inp(stream[pos + 1], arg2.getShort());
                inp.offset = pos;
                instructions.add(inp);
                return 3;

            case Db.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4]});
                Db db = new Db(arg1.getShort(), arg2.getShort());
                db.offset = pos;
                instructions.add(db);
                return 4;

            case Set.code:
                set = new Set();
                set.offset = pos;
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4], stream[pos + 5], stream[pos + 6]});
                set.arg1 = arg1.getShort();
                set.arg2 = arg2.getInt();
                byte[] data = new byte[set.arg2];
                for (int o = 0; o < data.length; o++) {
                    data[o] = stream[pos + 7 + o];
                }
                set.arg3 = data;
                instructions.add(set);
                return 6 + data.length;

            case Pushv.code:
                arg1 = ByteBuffer.wrap(new byte[]{stream[pos + 1], stream[pos + 2]});
                Pushv pushv = new Pushv(arg1.getShort());
                pushv.offset = pos;
                instructions.add(pushv);
                return 2;

            case Outv.code:
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 2], stream[pos + 3]});
                Outv outv = new Outv(stream[pos + 1], arg2.getShort());
                outv.offset = pos;
                instructions.add(outv);
                return 3;

            default:
                throw new ClassVerificationErrorException("Verification error bytecode at offset " + String.valueOf(pos));

        }
    }
}
