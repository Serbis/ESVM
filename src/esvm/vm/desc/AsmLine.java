package esvm.vm.desc;

/**
 * Created by serbis on 14.10.15.
 */
public class AsmLine {
    public String com;
    public int byteoffset;

    public AsmLine(String com, int byteoffset) {
        this.byteoffset = byteoffset;
        this.com = com;
    }
}
