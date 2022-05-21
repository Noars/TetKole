package application.ui.pane;

import application.Main;
import application.ui.buttons.Buttons;
import utils.ImageButton;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MainPane extends BorderPane {

    HBox hbox;

    public MainPane(Main main, int widthScreen) {
        super();
        this.setWidth(widthScreen);
        this.setHeight(200);

        Button playStopAudioFile = createPlayStopAudioFileButton(main);

        hbox = new HBox(playStopAudioFile);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
    }

    public Button createPlayStopAudioFileButton(Main main) {
        Button playStopAudioFile = new Buttons("Ajouter un fichier audio");
        playStopAudioFile.setGraphic(ImageButton.createAddButtonImageView("images/add.png"));
        playStopAudioFile.getStyleClass().add("noColor");
        playStopAudioFile.setContentDisplay(ContentDisplay.TOP);
        playStopAudioFile.setPrefHeight(250);
        playStopAudioFile.setPrefWidth(250);
        playStopAudioFile.setOnAction((e) -> {
            playStopAudioFile.visibleProperty().set(false);
        });
        return playStopAudioFile;
    }
}
