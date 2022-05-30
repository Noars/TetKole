package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SettingsPane extends BorderPane {

    HBox hbox;

    public SettingsPane(Main main, Stage primaryStage){
        super();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        {
            Label test1 = new Label("Test 1 : ");
            TextField test1Text = new TextField("");
            gridPane.add(test1, 0, 0);
            gridPane.add(test1Text, 1, 0);
            test1.getStyleClass().add("text");
            test1Text.getStyleClass().add("text");

            Label test2 = new Label("Test 2 : ");
            TextField test2Text = new TextField("");
            gridPane.add(test2, 0, 1);
            gridPane.add(test2Text, 1, 1);
            test2.getStyleClass().add("text");
            test2Text.getStyleClass().add("text");

            Label test3 = new Label("Test 3 : ");
            TextField test3Text = new TextField("");
            gridPane.add(test3, 0, 2);
            gridPane.add(test3Text, 1, 2);
            test3.getStyleClass().add("text");
            test3Text.getStyleClass().add("text");
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
