package application.ui.pane;

import application.Main;
import application.ui.buttons.Buttons;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import utils.ImageButton;

public class ButtonsPane extends BorderPane {

    boolean runningAudio = false;
    boolean runningRecord = false;

    HBox hbox;

    public ButtonsPane(Main main, int widthScreen){
        super();
        this.setWidth(widthScreen);

        Button playStopAudioFile = createPlayStopAudioFileButton(main);
        Button record = createRecordButton(main);

        hbox = new HBox(playStopAudioFile, record);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
    }

    public Button createPlayStopAudioFileButton(Main main) {
        Button playStopAudioFile = new Buttons();
        playStopAudioFile.setGraphic(ImageButton.createButtonImageView("images/play.png"));
        playStopAudioFile.getStyleClass().add("blue");
        playStopAudioFile.setContentDisplay(ContentDisplay.TOP);
        playStopAudioFile.setPrefHeight(50);
        playStopAudioFile.setPrefWidth(300);
        playStopAudioFile.setOnAction((e) -> {
            if (runningAudio) {
                runningAudio = false;
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/play.png"));
            } else {
                runningAudio = true;
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/pause.png"));
            }
        });
        return playStopAudioFile;
    }

    public Button createRecordButton(Main main){
        Button record = new Buttons();
        record.setGraphic(ImageButton.createButtonImageView("images/record.png"));
        record.getStyleClass().add("blue");
        record.setContentDisplay(ContentDisplay.TOP);
        record.setPrefHeight(50);
        record.setPrefWidth(300);
        record.setOnAction((e) -> {
            if (runningRecord) {
                runningRecord = false;
                ((ImageView) record.getGraphic()).setImage(new Image("images/record.png"));
            } else {
                runningRecord = true;
                ((ImageView) record.getGraphic()).setImage(new Image("images/stopRecord.png"));
            }
        });
        return record;
    }
}
