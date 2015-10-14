package esvm.controllers.controls;

import javafx.scene.control.Label;

/**
 * Created by serbis on 08.10.15.
 */
public class LabelCode extends Label {
    private int stringnum;
    private int offset;

    public LabelCode() {

    }

    public LabelCode(String text) {
        super(text);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
