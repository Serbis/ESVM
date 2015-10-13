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
    boolean setmode = false;
    private Set set;
    ArrayList<Byte> setbyte = new ArrayList<Byte>();

    public ClassLoader() {

    }

    public int load(File file) {
        stream = readFile(file);

        if (stream != null) {
            for (int i = 0; i < stream.length; i++) {
                try {
                    i = i + verification(stream[i], i);
                } catch (ClassVerificationErrorException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

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

    private int verification(byte bt, int pos) throws ClassVerificationErrorException {
        ByteBuffer arg1;
        ByteBuffer arg2;
        ByteBuffer arg3;

        if (setmode) {
            if (bt != 127) {
                setbyte.add(bt);
                return 0;
            } else {
                set.arg2 = setbyte;
                instructions.add(set);
                setmode = false;
                return 0;
            }
        }

        switch (bt) {
            case Add.code:
                instructions.add(new Add());
                return 0;

            case Sub.code:
                instructions.add(new Sub());
                return 0;

            case Push.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2], stream[pos + 3], stream[pos + 4]});
                instructions.add(new Push(arg1.getInt()));
                return 4;

            case Pop.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Pop(arg1.getShort()));
                return 2;

            case Cmp.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4]});
                instructions.add(new Cmp(arg1.getShort(), arg2.getShort()));
                return 4;

            case Inc.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Inc(arg1.getShort()));
                return 2;

            case Dec.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Dec(arg1.getShort()));
                return 2;

            case Mul.code:
                instructions.add(new Mul());
                return 0;

            case IMul.code:
                instructions.add(new IMul());
                return 0;

            case Movis.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Movis(arg1.getShort()));
                return 2;

            case Movos.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Movos(arg1.getShort()));
                return 2;

            case Xchg.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4]});
                instructions.add(new Xchg(arg1.getShort(), arg2.getShort()));
                return 4;

            case Lea.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4]});
                instructions.add(new Lea(arg1.getShort(), arg2.getShort()));
                return 4;

            case Int.code:
                instructions.add(new Int(stream[pos + 1], stream[pos + 2]));
                return 2;

            case Loop.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Loop(arg1.getShort()));
                return 2;

            case Jmp.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jmp(arg1.getShort()));
                return 2;

            case Div.code:
                instructions.add(new Div());
                return 0;

            case IDiv.code:
                instructions.add(new IDiv());
                return 0;

            case Je.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Je(arg1.getShort()));
                return 2;

            case Jz.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jz(arg1.getShort()));
                return 2;

            case Jg.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jg(arg1.getShort()));
                return 2;

            case Jge.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jge(arg1.getShort()));
                return 2;

            case Jl.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jl(arg1.getShort()));
                return 2;

            case Jle.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jle(arg1.getShort()));
                return 2;

            case Jne.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jne(arg1.getShort()));
                return 2;

            case Jnge.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jnge(arg1.getShort()));
                return 2;

            case Jnl.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jnl(arg1.getShort()));
                return 2;

            case Jnle.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Jnle(arg1.getShort()));
                return 2;

            case Out.code:
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 2], stream[pos + 3]});
                instructions.add(new Out(stream[pos + 1], arg2.getShort()));
                return 3;

            case Inp.code:
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 2], stream[pos + 3]});
                instructions.add(new Inp(stream[pos + 1], arg2.getShort()));
                return 3;

            case Db.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                arg2 = ByteBuffer.wrap(new byte[] {stream[pos + 3], stream[pos + 4]});
                instructions.add(new Db(arg1.getShort(), arg2.getShort()));
                return 4;

            case Set.code:
                set = new Set();
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                set.arg1 = arg1.getShort();
                setmode = true;
                return 1;

            case Pushv.code:
                arg1 = ByteBuffer.wrap(new byte[] {stream[pos + 1], stream[pos + 2]});
                instructions.add(new Pushv(arg1.getShort()));
                return 2;

            default:
                throw new ClassVerificationErrorException("Verification error bytecode at offset " + String.valueOf(pos));

        }

    }
}
