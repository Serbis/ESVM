package esvm.vm.compiler;

import esvm.vm.instructions.*;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Транслятор ассеблерного представления в байт-код
 */
public class Assembler {
    public interface ErrorListener {
        public void onErrorListener(String text);
    }

    private ErrorListener errorListener;

    public Assembler() {

    }

    /**
     * Транслирует входящий поток ассеблерного кода в байт-код
     *
     * @param asmline ассеблер
     * @return массив байт байт-код
     */
    public ArrayList<Byte> trnslate(String asmline) {
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        String[] instructs = asmline.split(";");

        for (int i = 0; i < instructs.length; i++) {
            String[] dup = instructs[i].split("\\(");
            String com = dup[0].replaceAll(" ", "");
            Selector selector = codeSelector(com);
            if (selector != null) {
                bytes.add(selector.code);
            } else {
                errorListener.onErrorListener("Unknown instruction on line " + String.valueOf(i));
                return null;
            }
            dup[1] = dup[1].replaceAll("\\)", "");
            String[] args = dup[1].split(",");
            int len = args.length;
            if (Objects.equals(args[0], "")) {
                len = 0;
            }
            if (len == selector.args) {
                for (int j = 0; j < len; j++) {
                    String a = args[j].replace(" ", "");
                    byte[] ab = convertArgToBytes(a, selector.code, j);

                    for (byte anAb : ab) {
                        bytes.add(anAb);
                    }
                }
            } else {
                errorListener.onErrorListener("Instructions on line " + String.valueOf(i) + " requires " + String.valueOf(selector.args) + " arguments, but found " + args.length);
            }
        }

        return bytes;
    }

    /**
     * Возращает селектор инструкций с кодом опрарации и количеством ее
     * аргементов
     *
     * @return селектор
     */
    private Selector codeSelector(String inst) {
        Selector selector = new Selector();

        switch (inst) {
            case "Add":
                selector.code = Add.code;
                selector.args = 0;
                return selector;

            case "Sub":
                selector.code = Sub.code;
                selector.args = 0;
                return selector;

            case "Push":
                selector.code = Push.code;
                selector.args = 2;
                return selector;

            case "Pop":
                selector.code = Pop.code;
                selector.args = 1;
                return selector;

            case "Cmp":
                selector.code = Cmp.code;
                selector.args = 2;
                return selector;

            case "Inc":
                selector.code = Inc.code;
                selector.args = 1;
                return selector;

            case "Dec":
                selector.code = Dec.code;
                selector.args = 1;
                return selector;

            case "Mul":
                selector.code = Mul.code;
                selector.args = 0;
                return selector;

            case "IMul":
                selector.code = IMul.code;
                selector.args = 0;
                return selector;

            case "Method":
                selector.code = Method.code;
                selector.args = 1;
                return selector;

            case "Movos":
                selector.code = Movos.code;
                selector.args = 1;
                return selector;

            case "Xchg":
                selector.code = Xchg.code;
                selector.args = 2;
                return selector;

            case "Lea":
                selector.code = Lea.code;
                selector.args = 2;
                return selector;

            case "Int":
                selector.code = Int.code;
                selector.args = 2;
                return selector;

            case "Loop":
                selector.code = Loop.code;
                selector.args = 2;
                return selector;

            case "Jmp":
                selector.code = Jmp.code;
                selector.args = 1;
                return selector;

            case "Div":
                selector.code = Div.code;
                selector.args = 0;
                return selector;

            case "IDiv":
                selector.code = IDiv.code;
                selector.args = 0;
                return selector;

            case "Je":
                selector.code = Je.code;
                selector.args = 1;
                return selector;

            case "Jz":
                selector.code = Jz.code;
                selector.args = 1;
                return selector;

            case "Jg":
                selector.code = Jg.code;
                selector.args = 1;
                return selector;

            case "Jge":
                selector.code = Jge.code;
                selector.args = 1;
                return selector;

            case "Jl":
                selector.code = Jl.code;
                selector.args = 1;
                return selector;

            case "Jle":
                selector.code = Jle.code;
                selector.args = 1;
                return selector;

            case "Jne":
                selector.code = Jne.code;
                selector.args = 1;
                return selector;

            case "Jnge":
                selector.code = Jnge.code;
                selector.args = 1;
                return selector;

            case "Jnl":
                selector.code = Jnl.code;
                selector.args = 1;
                return selector;

            case "Jnle":
                selector.code = Jnge.code;
                selector.args = 1;
                return selector;

            case "Out":
                selector.code = Out.code;
                selector.args = 2;
                return selector;

            case "Inp":
                selector.code = Inp.code;
                selector.args = 2;
                return selector;

            case "Db":
                selector.code = Db.code;
                selector.args = 3;
                return selector;

            case "Set":
                selector.code = Set.code;
                selector.args = 3;
                return selector;

            case "Pushv":
                selector.code = Pushv.code;
                selector.args = 1;
                return selector;

            case "Outv":
                selector.code = Outv.code;
                selector.args = 2;
                return selector;
        }

        return null;
    }

    /**
     * Конвертирует аргмеунт в массив байт в зависимости от того,
     * какой тип у аргумента конкретной инструкции
     *
     * @param arg строковое представление аргумента
     * @param instCode код инструкции
     * @return массив байт пердставления аргумента
     */
    private byte[] convertArgToBytes(String arg, int instCode, int argnum) {
        ByteBuffer byteBuffer;
        switch (instCode) {
            case Push.code:
                if (argnum == 0) {
                    return toByte(arg);
                } else {
                    return toBytes(arg);
                }

            case Pop.code:
                return toShort(arg);

            case Cmp.code:
                return toShort(arg);

            case Inc.code:
                return toShort(arg);

            case Dec.code:
                return toShort(arg);

            case Method.code:
                return toShort(arg);

            case Movos.code:
                return toShort(arg);

            case Xchg.code:
                return toShort(arg);

            case Lea.code:
                return toShort(arg);

            case Int.code:
                return toByte(arg);

            case Loop.code:
                if (argnum == 0) {
                    return toShort(arg);
                } else {
                    return toInt(arg);
                }

            case Jmp.code:
                return toInt(arg);

            case Je.code:
                return toInt(arg);

            case Jz.code:
                return toInt(arg);

            case Jg.code:
                return toInt(arg);

            case Jge.code:
                return toInt(arg);

            case Jl.code:
                return toInt(arg);

            case Jle.code:
                return toInt(arg);

            case Jne.code:
                return toInt(arg);

            case Jnge.code:
                return toInt(arg);

            case Jnl.code:
                return toInt(arg);

            case Jnle.code:
                return toInt(arg);

            case Out.code:
                if (argnum == 0) {
                    return toByte(arg);
                } else {
                    return toInt(arg);
                }

            case Inp.code:
                if (argnum == 0) {
                    return toByte(arg);
                } else {
                    return toShort(arg);
                }

            case Db.code:
                if (argnum == 0) {
                    return toShort(arg);
                } else if (argnum == 1) {
                    return toShort(arg);
                } else {
                    return toByte(arg);
                }

            case Set.code:
                if (argnum == 0) {
                    return toShort(arg);
                } else if (argnum == 1) {
                    return toInt(arg);
                } else {
                    return toBytes(arg);
                }

            case Pushv.code:
                return toShort(arg);

            case Outv.code:
                if (argnum == 0) {
                    return toByte(arg);
                } else {
                    return toShort(arg);
                }
        }
        return null;
    }

    private byte[] toShort(String arg) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.putShort(Short.parseShort(arg));
        return byteBuffer.array();
    }

    private byte[] toInt(String arg) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(Integer.parseInt(arg));
        return byteBuffer.array();
    }

    private byte[] toByte(String arg) {
        return new byte[] {Byte.parseByte(arg)};
    }

    /**
     * Конвертирует hex строку в массив байт
     *
     * @param hexs hex строка
     * @return массив байт
     */
    private byte[] toBytes(String hexs) {
        return DatatypeConverter.parseHexBinary(hexs);
    }



    /**
     * Описывает селектор инструкции. Содержит в себе количество аргуметов
     * инструкции и ее код.
     *
     */
    private class Selector {
        int args;
        byte code;

        public Selector() {

        }
    }

    public void setOnErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }
}
