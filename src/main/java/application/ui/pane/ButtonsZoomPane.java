package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.buttons.ImageButton;

public class ButtonsZoomPane extends BorderPane {

    HBox hbox;

    public ButtonsZoomPane(Main main, Stage primaryStage){
        super();

        Button zoom = createZoomButton(main, primaryStage);

        hbox = new HBox(zoom);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
    }

    public Button createZoomButton(Main main, Stage primaryStage){
        Button zoom = new Button();
        zoom.setGraphic(ImageButton.createButtonImageView("images/zoom-.png"));
        zoom.getStyleClass().add("blue");
        zoom.setContentDisplay(ContentDisplay.TOP);
        zoom.setPrefHeight(50);
        zoom.setPrefWidth(300);
        zoom.setOnAction((e) -> {
            main.goToHome(primaryStage);
        });
        return zoom;
    }
}
