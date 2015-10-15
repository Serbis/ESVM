package esvm.vm;

import esvm.vm.desc.AsmLine;
import esvm.vm.instructions.*;

import java.util.ArrayList;

/**
 * Класс дизассемблера байт-кода
 */
public class Disassembler {

    public Disassembler() {

    }

    /**
     * Брете головной сет иснтрукций и производит его дизассемблирование.
     * Формирует массив объектов AsmLine в каждом содержится текст
     * ассемблерного кода и сещение по адресу в бинарном файле
     *
     * @return массив объектов строк
     */
    public AsmLine[] getAsm() {
        ArrayList<Instruction> lociset = Global.getInstance().iSet;
        AsmLine[] asmLines = new AsmLine[lociset.size()];

        for (int i = 0; i < lociset.size(); i++) {
            AsmLine asmLine = new AsmLine(lociset.get(i).asm, lociset.get(i).offset);
            switch (lociset.get(i).asm) {
                case "Add":
                    asmLine.com += "();";
                    break;

                case "Sub":
                    asmLine.com += "();";
                    break;

                case "Push":
                    Push push = (Push) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(push.arg1) + ");";
                    break;

                case "Pop":
                    Pop pop = (Pop) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(pop.arg1) + ");";
                    break;

                case "Cmp":
                    Cmp cmp = (Cmp)  lociset.get(i);
                    asmLine.com += "(" + String.valueOf(cmp.arg1) + ", " + cmp.arg2 + ");";
                    break;

                case "Inc":
                    Inc inc = (Inc) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(inc.arg1) + ");";
                    break;

                case "Dec":
                    Dec dec = (Dec) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(dec.arg1) + ");";
                    break;

                case "Mul":
                    asmLine.com += "();";
                    break;

                case "IMul":
                    asmLine.com += "();";
                    break;

                case "Movis":
                    Movis movis = (Movis) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(movis.arg1) + ");";
                    break;

                case "Movos":
                    Movos movos = (Movos) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(movos.arg1) + ");";
                    break;

                case "Xchg":
                    Xchg xchg = (Xchg)  lociset.get(i);
                    asmLine.com += "(" + String.valueOf(xchg.arg1) + ", " + xchg.arg2 + ");";
                    break;

                case "Lea":
                    Lea lea = (Lea)  lociset.get(i);
                    asmLine.com += "(" + String.valueOf(lea.arg1) + ", " + lea.arg2 + ");";
                    break;

                case "Int":
                    Int iint = (Int)  lociset.get(i);
                    asmLine.com += "(" + String.valueOf(iint.arg1) + ", " + iint.arg2 + ");";
                    break;

                case "Loop":
                    Loop loop = (Loop) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(loop.arg1) + ");";
                    break;

                case "Jmp":
                    Jmp jmp = (Jmp) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jmp.arg1) + ");";
                    break;

                case "Div":
                    asmLine.com += "();";
                    break;

                case "IDiv":
                    asmLine.com += "();";
                    break;

                case "Je":
                    Je je = (Je) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(je.arg1) + ");";
                    break;

                case "Jz":
                    Jz jz = (Jz) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jz.arg1) + ");";
                    break;

                case "Jg":
                    Jg jg = (Jg) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jg.arg1) + ");";
                    break;

                case "Jge":
                    Jge jge = (Jge) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jge.arg1) + ");";
                    break;

                case "Jl":
                    Jl jl = (Jl) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jl.arg1) + ");";
                    break;

                case "Jle":
                    Jle jle = (Jle) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jle.arg1) + ");";
                    break;

                case "Jne":
                    Jne jne = (Jne) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jne.arg1) + ");";
                    break;

                case "Jnge":
                    Jnge jnge = (Jnge) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jnge.arg1) + ");";
                    break;

                case "Jnl":
                    Jnl jnl = (Jnl) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jnl.arg1) + ");";
                    break;

                case "Jnle":
                    Jnle jnle = (Jnle) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(jnle.arg1) + ");";
                    break;

                case "Out":
                    Out out = (Out)  lociset.get(i);
                    asmLine.com += "(" + String.valueOf(out.arg1) + ", " + out.arg2 + ");";
                    break;

                case "Inp":
                    Inp inp = (Inp)  lociset.get(i);
                    asmLine.com += "(" + String.valueOf(inp.arg1) + ", " + inp.arg2 + ");";
                    break;

                case "Db":
                    Db db = (Db)  lociset.get(i);
                    asmLine.com += "(" + String.valueOf(db.arg1) + ", " + db.arg2 + ");";
                    break;

                case "Set":
                    Set set = (Set)  lociset.get(i);
                    String data = "";
                    for (int o = 0; o < set.arg3.length; o++) {
                        String hex = Integer.toHexString(set.arg3[o]).toUpperCase();
                        if (hex.length() == 1) {
                            hex = "0" + hex;
                        }
                        data += hex + " ";
                    }
                    asmLine.com += "(" + String.valueOf(set.arg1) + ", " + set.arg2 + ", " + data + ");";
                    break;

                case "Pushv":
                    Pushv pushv = (Pushv) lociset.get(i);
                    asmLine.com += "(" + String.valueOf(pushv.arg1) + ");";
                    break;

                case "Outv":
                    Outv outv = (Outv)  lociset.get(i);
                    asmLine.com += "(" + String.valueOf(outv.arg1) + ", " + outv.arg2 + ");";
                    break;

            }
            asmLines[i] = asmLine;
        }

        return asmLines;
    }
}
