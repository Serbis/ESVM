package esvm.vm.desc;

/**
 * Created by serbis on 20.10.15.
 */
public class StackObject {
    public StackDataType stackDataType;
    public byte[] data;

    public StackObject(byte[] data, StackDataType stackDataType) {
        this.data = data;
        this.stackDataType = stackDataType;
    }

    public enum StackDataType {
        INT, FLOAT, SHORT, BYTE, BOOLEAN, STRING
    }
}
