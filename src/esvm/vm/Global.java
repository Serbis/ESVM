package esvm.vm;

import esvm.vm.instructions.Instruction;

import java.util.ArrayList;

/**
 * Created by serbis on 13.10.15.
 */
public class Global {
    public ArrayList<Instruction> iSet;
    //Карта переменных
    //Порт ввода вывода
    private static Global instance;

    public Global() {
        instance = this;
    }

    public static Global getInstance() {
        return instance;
    }
}
