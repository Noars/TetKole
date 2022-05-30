package application.ui.pane;

import application.Main;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.buttons.Buttons;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import utils.buttons.ImageButton;
import utils.wave.WaveFormService;

import java.io.File;
import java.io.IOException;

public class ButtonsPane extends BorderPane {

    boolean runningAudio = false;

    HBox hbox;
    FileChooser fileChooser = new FileChooser();

    public ButtonsPane(Main main, Stage primaryStage){
        super();

        Button newAudioFile = createNewAudioFileButton(main, primaryStage);
        Button playStopAudioFile = createPlayStopAudioFileButton(main);
        Button record = createRecordButton(main, primaryStage);
        Button seeJsonFolder = createSeeJsonFolder();

        hbox = new HBox(newAudioFile, playStopAudioFile, record, seeJsonFolder);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
        this.extensionAudioFiles();
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
                main.getWavePane().getWaveService().playStopMediaPlayer(false);
                main.getWavePane().setStep(false);
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/play.png"));
            } else {
                runningAudio = true;
                main.getWavePane().getWaveService().playStopMediaPlayer(true);
                main.getWavePane().setStep(true);
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/pause.png"));
            }
        });
        return playStopAudioFile;
    }

    public Button createRecordButton(Main main, Stage primaryStage){
        Button record = new Buttons();
        record.setGraphic(ImageButton.createButtonImageView("images/record.png"));
        record.getStyleClass().add("blue");
        record.setContentDisplay(ContentDisplay.TOP);
        record.setPrefHeight(50);
        record.setPrefWidth(300);
        record.setOnAction((e) -> {
            main.goToRecord(primaryStage);
        });
        return record;
    }

    public Button createNewAudioFileButton(Main main, Stage primaryStage){
        Button newAudioFile = new Buttons();
        newAudioFile.setGraphic(ImageButton.createButtonImageView("images/add.png"));
        newAudioFile.getStyleClass().add("blue");
        newAudioFile.setContentDisplay(ContentDisplay.TOP);
        newAudioFile.setPrefHeight(50);
        newAudioFile.setPrefWidth(300);
        newAudioFile.setOnAction((e) -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null){
                main.setNewWavePane(primaryStage);
                main.getWavePane().getWaveService().startService(String.valueOf(file), WaveFormService.WaveFormJob.AMPLITUDES_AND_WAVEFORM);
                main.getWavePane().getWaveService().setupMediaPlayer(String.valueOf(file));
            }
        });
        return  newAudioFile;
    }

    public Button createSeeJsonFolder(){
        Button seeJsonFolder = new Buttons();
        seeJsonFolder.setGraphic(ImageButton.createButtonImageView("images/folder.png"));
        seeJsonFolder.getStyleClass().add("blue");
        seeJsonFolder.setContentDisplay(ContentDisplay.TOP);
        seeJsonFolder.setPrefHeight(50);
        seeJsonFolder.setPrefWidth(300);
        seeJsonFolder.setOnAction((e) -> {
            try {
                String pathFolder = "C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\translateAudioFiles\\JsonFiles";
                Runtime.getRuntime().exec("explorer.exe /select," + pathFolder);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        return  seeJsonFolder;
    }

    public void extensionAudioFiles(){
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );
    }
}
