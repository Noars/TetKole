package application.ui.pane;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class SettingsPane extends BorderPane {

    HBox hbox;

    public SettingsPane(){
        super();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        {

        }

        hbox = new HBox(gridPane);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        gridPane.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(gridPane, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
    }
}
