package esvm.controllers.controls;

import javafx.scene.control.Label;

/**
 * Created by serbis on 08.10.15.
 */
public class LabelMeta extends Label {
    private int block;
    private int offset;

    public LabelMeta() {

    }

    public LabelMeta(String text) {
        super(text);
    }

    public void setAdress(int block, int offset) {
        this.block = block;
        this.offset = offset;
    }

    public int getBlock() {
        return block;
    }

    public int getOffset() {
        return offset;
    }
}
