package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.buttons.Buttons;
import utils.buttons.ImageButton;

public class ButtonsZoomPane extends BorderPane {

    HBox hbox;

    boolean runningAudio = false;

    public ButtonsZoomPane(Main main, Stage primaryStage){
        super();

        Button zoom = createZoomButton(main, primaryStage);
        Button playStop = createPlayStopAudioFileButton(main);

        hbox = new HBox(zoom, playStop);
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
                main.getZoomPane().getWaveZoomService().playStopMediaPlayer("pause");
                main.getZoomPane().setStep(false);
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/play.png"));
            } else {
                runningAudio = true;
                main.getZoomPane().getWaveZoomService().playStopMediaPlayer("play");
                main.getZoomPane().setStep(true);
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/pause.png"));
            }
        });
        return playStopAudioFile;
    }
}
