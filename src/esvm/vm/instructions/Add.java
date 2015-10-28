package esvm.vm.instructions;

import esvm.vm.Global;
import esvm.vm.desc.StackObject;
import esvm.vm.exceptions.IncompatibleTypesException;
import esvm.vm.exceptions.NullReferenceException;

/**
 * Created by serbis on 13.10.15.
 */
public class Add extends Instruction{
    public final static byte code = 1;

    public Add() {
        asm = "Add";
    }

    public void exec() throws IncompatibleTypesException {
        try {
            StackObject a = Global.getInstance().memoryManager.pop();
            StackObject b = Global.getInstance().memoryManager.pop();
            if(a.stackDataType != b.stackDataType)
                throw new IncompatibleTypesException("Type error while Add at offset :" + String.valueOf(offset));
            if (a.stackDataType == StackObject.StackDataType.INT) {

            } else if (a.stackDataType == StackObject.StackDataType.FLOAT) {

            } else if (a.stackDataType == StackObject.StackDataType.STRING) {

            } else {

            }
            //Global.getInstance().memoryManager.push();
        } catch (NullReferenceException e) {
            e.printStackTrace();
        }
        // Global.getInstance().memoryManager.push(Global.getInstance().memoryManager.pop() + Global.getInstance().memoryManager.pop());

    }
}
