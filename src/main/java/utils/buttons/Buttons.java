package utils.buttons;

import javafx.scene.control.Button;

public class Buttons extends Button {

    /**
     * Class for create out customized Buttons with a specific Css.
     */
    public Buttons() {
        super();
        getStyleClass().add("Buttons");
        this.applyCss();

    }

}
