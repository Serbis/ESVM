package esvm.vm;

import esvm.enums.AccessFlags;
import esvm.vm.desc.ClassField;
import esvm.vm.desc.ClassMethod;
import esvm.vm.desc.attributes.AttrCode;
import esvm.vm.desc.attributes.ClassAttribute;
import esvm.vm.desc.attributes.LocalVariableTRow;
import esvm.vm.desc.constpool.*;
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
    private Set set;
    byte[] btm;
    private final short VERSION = 1;
    private short constantCount = 0;
    private short interfaceCount = 0;
    private short fieldsCount = 0;
    private short methodsCount = 0;
    private short attrCount = 0;
    private int accessfpos = 0;
    private int fieldscpos = 0;
    private int methodcpos = 0;
    private int attrcpos = 0;

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
        //Global.getInstance().iSet = instructions;
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
     * Проводит разбор входящего class файла
     *
     * @param bt входной байт для анализа
     * @param pos позиция входного байта в потоке
     * @return интертор - на сколько нужно перепрыгуть к следующему байту
     * @throws ClassVerificationErrorException в случае если обнаружена
     *      синтаксическая ошибка в байт-коде
     */
    private int verificate(byte bt, int pos) throws ClassVerificationErrorException {
        ByteBuffer byteBuffer;

        if (pos == 0) { //Magic number
            btm = new byte[2];
            btm[0] = stream[pos];
            btm[1] = stream[pos + 1];
            byteBuffer = ByteBuffer.wrap(btm);
            short magic = byteBuffer.getShort();
            if (magic == 28527) {
                return 1;
            } else {
                throw new ClassVerificationErrorException("Incorrect magic number");
            }
        }
        if (pos == 2) { //MinorV
            btm = new byte[2];
            btm[0] = stream[pos];
            btm[1] = stream[pos + 1];
            byteBuffer = ByteBuffer.wrap(btm);
            short minorv = byteBuffer.getShort();
            if (minorv <= VERSION) {
                return 1;
            } else {
                throw new ClassVerificationErrorException("Inconsistency minimum version");
            }
        }
        if (pos == 4) { //MajorV
            btm = new byte[2];
            btm[0] = stream[pos];
            btm[1] = stream[pos + 1];
            byteBuffer = ByteBuffer.wrap(btm);
            short majorv = byteBuffer.getShort();
            if (majorv >= VERSION) {
                return 1;
            } else {
                throw new ClassVerificationErrorException("Inconsistency maximum version");
            }
        }
        if (pos == 6) { //Constant_pool_count
            btm = new byte[2];
            btm[0] = stream[pos];
            btm[1] = stream[pos + 1];
            byteBuffer = ByteBuffer.wrap(btm);
            constantCount = byteBuffer.getShort();
            return 1;
        }
        if (pos == 8) { //Constant_pool
            accessfpos = parceConstantPool(pos) + pos;
            return 0;
        }
        if (pos == accessfpos) { //Access_flags
            byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos], stream[pos + 1]});
            Global.getInstance().access_flags = byteBuffer.getShort();
            return 1;
        }
        if (pos == accessfpos + 2) {  //This_class
            byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos], stream[pos + 1]});
            Global.getInstance().this_class =  byteBuffer.getShort();
            return 1;
        }
        if (pos == accessfpos + 4) { //Super_class
            byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos], stream[pos + 1]});
            Global.getInstance().super_class =  byteBuffer.getShort();
            return 1;
        }
        if (pos == accessfpos + 6) { //Interface_count
            byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos], stream[pos + 1]});
            interfaceCount = byteBuffer.getShort();
            return 1;
        }
        if (pos == accessfpos + 8) { //Interfaces
            if (interfaceCount != 0) {
                fieldscpos = parceInterfaces(pos) + pos;
                return 0;
            } else {  //Fields_count
                fieldscpos = pos;
                byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos], stream[pos + 1]});
                fieldsCount = byteBuffer.getShort();
                return 0;
            }
        }
        if (pos == fieldscpos + 1) { //Fields
            if (fieldsCount != 0) {
                methodcpos = parceFields(pos + 1) + pos;
                return 0;
            }
        }
        if (pos == methodcpos + 1) { //Method_count
            byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos], stream[pos + 1]});
            methodsCount = byteBuffer.getShort();
            return 1;
        }
        if (pos == methodcpos + 3) { //Methods
            if (methodsCount != 0) {
                attrcpos = parceMethods(pos) + pos;
                return 0;
            }
        }
        if (pos == attrcpos - 2) { //Attribute_count
            byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos], stream[pos + 1]});
            attrCount = byteBuffer.getShort();
            return 1;
        }
        if (pos == attrcpos + 1) { //Attributes
            if (attrCount != 0) {
                parceAttribute(pos);
                return 0;
            }
        }
        return 0;

    }

    /**
     * Разбирает пул констант в class файле
     *
     * @param pos
     * @return
     */
    private int parceConstantPool(int pos) {
        int offset = 0;
        ByteBuffer byteBuffer;
        byte[] btm;

        for (int i = 0; i < constantCount; i++) {
            byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset]});
            short tag = stream[pos + offset];

            if (tag == 1) {
                byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 1], stream[pos + offset + 2]});
                btm = new byte[byteBuffer.getShort()];
                for (int j = 0; j < btm.length; j++) {
                    btm[j] = stream[pos + offset + 3 + j];
                }
                Global.getInstance().counstant_pool.add(new ConstUtf_8(btm.length, btm));
                offset += 3 + btm.length;
            } else if (tag == 3) {
                byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 1], stream[pos + offset + 2], stream[pos + offset + 3], stream[pos + offset + 4]});
                Global.getInstance().counstant_pool.add(new ConstInteger(byteBuffer.array()));
                offset += 5;
            } else if (tag == 4) {
                byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 1], stream[pos + offset + 2], stream[pos + offset + 3], stream[pos + offset + 4]});
                Global.getInstance().counstant_pool.add(new ConstFloat(byteBuffer.array()));
                offset += 5;
            } else if (tag == 8) {
                byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 1], stream[pos + offset + 2]});
                Global.getInstance().counstant_pool.add(new ConstString(byteBuffer.getShort()));
                offset += 3;
            } else if (tag == 15) {
                byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 1], stream[pos + offset + 2]});
                Global.getInstance().counstant_pool.add(new ConstMethodHandle(stream[pos + offset + 1], byteBuffer.getShort()));
                offset += 4;
            } else if (tag == 16) {
                byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 1], stream[pos + offset + 2]});
                Global.getInstance().counstant_pool.add(new ConstMethodType(byteBuffer.getShort()));
                offset += 3;
            }
        }

        ArrayList<ClassConstant> cp = Global.getInstance().counstant_pool;
        return offset;
    }

    /**
     * Разбирает блок интерфесов в class файле
     *
     * @param pos
     * @return
     */
    private int parceInterfaces(int pos) {
        int offset = 0;

        return offset;
    }

    /**
     * Разбирает блок полей в class файле
     *
     * @param pos
     * @return
     */
    private int parceFields(int pos) {
        int offset = 0;
        ByteBuffer byteBuffer;

        for (int i = 0; i < fieldsCount; i++) {
            byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 2], stream[pos + offset + 3]});
            short name = byteBuffer.getShort();
            byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 4], stream[pos + offset + 5]});
            short desc = byteBuffer.getShort();
            ClassField classField = new ClassField(AccessFlags.ACC_PUBLIC, name, desc);
            Global.getInstance().fields.add(classField);
            offset += 6;
        }

        ArrayList<ClassField> fi = Global.getInstance().fields;

        return offset;
    }

    /**
     * Разбирает блок методов в class файле
     *
     * @param pos
     * @return
     */
    private int parceMethods(int pos) {
        int offset = 2;
        ByteBuffer byteBuffer;

        for (int i = 0; i < methodsCount; i++) {
            byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset], stream[pos + offset + 1]});
            short name = byteBuffer.getShort();
            byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 2], stream[pos + offset + 3]});
            short desc = byteBuffer.getShort();
            byteBuffer = ByteBuffer.wrap(new byte[] {stream[pos + offset + 4], stream[pos + offset + 5]});
            short code = byteBuffer.getShort();
            ClassMethod classMethod = new ClassMethod(AccessFlags.ACC_PUBLIC, name, desc, code);
            Global.getInstance().methods.add(classMethod);
            offset += 8;
        }

        ArrayList<ClassMethod> me = Global.getInstance().methods;

        return offset;
    }

    /**
     * Разбирае блок аттрибутов в class файле
     *
     * @param pos
     */
    private void parceAttribute(int pos) {
        int offset = 0;
        ByteBuffer byteBuffer;
        byte[] btm;

        for (int i = 0; i < attrCount; i++) {
            byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos + offset], stream[pos + offset + 1]});
            short tag = byteBuffer.getShort();

            if (tag == 0) {
                AttrCode attrCode = new AttrCode();
                attrCode.local_varialbe_table = new ArrayList<LocalVariableTRow>();
                byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos + offset + 1], stream[pos + offset + 2], stream[pos + offset + 3], stream[pos + offset + 4]});
                btm = new byte[byteBuffer.getInt()];
                byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos + offset + 5], stream[pos + offset + 6]});
                int locvarlen = byteBuffer.getShort();
                offset += 6;
                for (int j = 0; j < locvarlen; j++) {
                    LocalVariableTRow localVariableTRow = new LocalVariableTRow();
                    byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos + offset + 1], stream[pos + offset + 2]});
                    localVariableTRow.name_index = byteBuffer.getShort();
                    byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos + offset + 3], stream[pos + offset + 4]});
                    localVariableTRow.name_decriptor = byteBuffer.getShort();
                    byteBuffer = ByteBuffer.wrap(new byte[]{stream[pos + offset + 5], stream[pos + offset + 6]});
                    localVariableTRow.index = byteBuffer.getShort();
                    attrCode.local_varialbe_table.add(localVariableTRow);
                    offset += 6;
                }
                for (int j = 0; j < btm.length; j++) {
                    btm[j] = stream[pos + offset + 1 + j];
                }
                attrCode.code = String.valueOf(Global.getInstance().code.size());
                Global.getInstance().attributes.add(attrCode);
                Global.getInstance().code.add(parceByteCode(btm));
                offset += btm.length + 2;
            }


        }
        ArrayList<ClassAttribute> atr = Global.getInstance().attributes;
        int a = 0;
        a =  1 + 1;
    }

    /**
     * Разбирает байт-код на команды
     *
     * @param bytes
     * @return
     */
    private ArrayList<Instruction> parceByteCode(byte[] bytes){
        ByteBuffer arg1;
        ByteBuffer arg2;
        ByteBuffer arg3;
        ArrayList<Instruction> instructions = new ArrayList<Instruction>();
        
        for (int i = 0; i < bytes.length; i++) {
            byte bt = bytes[i];
            switch (bt) {
                case Add.code:
                    Add add = new Add();
                    add.offset = i;
                    instructions.add(add);
                    i = i + 0;
                    break;

                case Sub.code:
                    Sub sub = new Sub();
                    sub.offset = i;
                    instructions.add(sub);
                    i = i + 0;
                    break;

                case Push.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Push push = new Push(arg1.getInt());
                    push.offset = i;
                    instructions.add(push);
                    i = i + 4;
                    break;

                case Pop.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    Pop pop = new Pop(arg1.getShort());
                    pop.offset = i;
                    instructions.add(pop);
                    i = i + 2;
                    break;

                case Cmp.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 3], bytes[i + 4]});
                    Cmp cmp = new Cmp(arg1.getShort(), arg2.getShort());
                    cmp.offset = i;
                    instructions.add(cmp);
                    i = i + 4;
                    break;

                case Inc.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    Inc inc = new Inc(arg1.getShort());
                    inc.offset = i;
                    instructions.add(inc);
                    i = i + 2;
                    break;

                case Dec.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    Dec dec = new Dec(arg1.getShort());
                    dec.offset = i;
                    instructions.add(dec);
                    i = i + 2;
                    break;

                case Mul.code:
                    Mul mul = new Mul();
                    mul.offset = i;
                    instructions.add(mul);
                    i = i + 0;
                    break;

                case IMul.code:
                    IMul iMul = new IMul();
                    iMul.offset = i;
                    instructions.add(iMul);
                    i = i + 0;
                    break;

                case Method.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    Method method = new Method(arg1.getShort());
                    method.offset = i;
                    instructions.add(method);
                    i = i + 2;
                    break;

                case Movos.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    Movos movos = new Movos(arg1.getShort());
                    movos.offset = i;
                    instructions.add(movos);
                    i = i + 2;
                    break;

                case Xchg.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 3], bytes[i + 4]});
                    Xchg xchg = new Xchg(arg1.getShort(), arg2.getShort());
                    xchg.offset = i;
                    instructions.add(xchg);
                    i = i + 4;
                    break;

                case Lea.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 3], bytes[i + 4]});
                    Lea lea = new Lea(arg1.getShort(), arg2.getShort());
                    lea.offset = i;
                    instructions.add(lea);
                    i = i + 4;
                    break;

                case Int.code:
                    Int iint = new Int(bytes[i + 1], bytes[i + 2]);
                    iint.offset = i;
                    instructions.add(iint);
                    i = i + 2;
                    break;

                case Loop.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 3], bytes[i + 4], bytes[i + 5], bytes[i + 6]});
                    Loop loop = new Loop(arg1.getShort(), arg2.getInt());
                    loop.offset = i;
                    instructions.add(loop);
                    i = i + 6;
                    break;

                case Jmp.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jmp jmp = new Jmp(arg1.getInt());
                    jmp.offset = i;
                    instructions.add(jmp);
                    i = i + 4;
                    break;

                case Div.code:
                    Div div = new Div();
                    div.offset = i;
                    instructions.add(div);
                    i = i + 0;
                    break;

                case IDiv.code:
                    IDiv iDiv = new IDiv();
                    iDiv.offset = i;
                    instructions.add(iDiv);
                    i = i + 0;
                    break;

                case Je.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Je je = new Je(arg1.getInt());
                    je.offset = i;
                    instructions.add(je);
                    i = i + 4;
                    break;

                case Jz.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jz jz = new Jz(arg1.getInt());
                    jz.offset = i;
                    instructions.add(jz);
                    i = i + 4;
                    break;

                case Jg.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jg jg = new Jg(arg1.getInt());
                    jg.offset = i;
                    instructions.add(jg);
                    i = i + 4;
                    break;

                case Jge.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jge jge = new Jge(arg1.getInt());
                    jge.offset = i;
                    instructions.add(jge);
                    i = i + 4;
                    break;

                case Jl.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jl jl = new Jl(arg1.getInt());
                    jl.offset = i;
                    instructions.add(jl);
                    i = i + 4;
                    break;

                case Jle.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jle jle = new Jle(arg1.getInt());
                    jle.offset = i;
                    instructions.add(jle);
                    i = i + 4;
                    break;

                case Jne.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jne jne = new Jne(arg1.getInt());
                    jne.offset = i;
                    instructions.add(jne);
                    i = i + 4;
                    break;

                case Jnge.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jnge jnge = new Jnge(arg1.getInt());
                    jnge.offset = i;
                    instructions.add(jnge);
                    i = i + 4;
                    break;

                case Jnl.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jnl jnl = new Jnl(arg1.getInt());
                    jnl.offset = i;
                    instructions.add(jnl);
                    i = i + 4;
                    break;

                case Jnle.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4]});
                    Jnle jnle = new Jnle(arg1.getInt());
                    jnle.offset = i;
                    instructions.add(jnle);
                    i = i + 4;
                    break;

                case Out.code:
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 2], bytes[i + 3], bytes[i + 4], bytes[i + 5]});
                    Out out = new Out(bytes[i + 1], arg2.getInt());
                    out.offset = i;
                    instructions.add(out);
                    i = i + 5;
                    break;

                case Inp.code:
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 2], bytes[i + 3]});
                    Inp inp = new Inp(bytes[i + 1], arg2.getShort());
                    inp.offset = i;
                    instructions.add(inp);
                    i = i + 3;
                    break;

                case Db.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 3], bytes[i + 4]});
                    Db db = new Db(arg1.getShort(), arg2.getShort());
                    db.offset = i;
                    instructions.add(db);
                    i = i + 4;
                    break;

                case Set.code:
                    set = new Set();
                    set.offset = i;
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 3], bytes[i + 4], bytes[i + 5], bytes[i + 6]});
                    set.arg1 = arg1.getShort();
                    set.arg2 = arg2.getInt();
                    byte[] data = new byte[set.arg2];
                    for (int o = 0; o < data.length; o++) {
                        data[o] = bytes[i + 7 + o];
                    }
                    set.arg3 = data;
                    instructions.add(set);
                    i = i + 6 + data.length;
                    break;

                case Pushv.code:
                    arg1 = ByteBuffer.wrap(new byte[]{bytes[i + 1], bytes[i + 2]});
                    Pushv pushv = new Pushv(arg1.getShort());
                    pushv.offset = i;
                    instructions.add(pushv);
                    i = i + 2;
                    break;

                case Outv.code:
                    arg2 = ByteBuffer.wrap(new byte[]{bytes[i + 2], bytes[i + 3]});
                    Outv outv = new Outv(bytes[i + 1], arg2.getShort());
                    outv.offset = i;
                    instructions.add(outv);
                    i = i + 3;
                    break;

                default:
                    try {
                        throw new ClassVerificationErrorException("Verification error bytecode at offset " + String.valueOf(i));
                    } catch (ClassVerificationErrorException e) {
                        e.printStackTrace();
                    }

            }
        }
        
        return instructions;
    }

}
