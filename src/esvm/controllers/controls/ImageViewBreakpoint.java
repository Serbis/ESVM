package esvm.controllers.controls;

import javafx.scene.image.ImageView;

/**
 * Created by serbis on 08.10.15.
 */
public class ImageViewBreakpoint extends ImageView {
    private int pos;

    public ImageViewBreakpoint() {
        prefHeight(16);
        prefWidth(16);
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
