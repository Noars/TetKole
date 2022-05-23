package utils.buttons;

import javafx.scene.control.Button;

public class Buttons extends Button {

    public Buttons() {
        super();
        getStyleClass().add("Buttons");
        this.applyCss();

    }

    public Buttons(String name) {
        super(name);
        getStyleClass().add("Buttons");
        this.applyCss();
    }
}
