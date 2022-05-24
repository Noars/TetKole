package application.ui.pane;

import application.Main;
import application.ui.files.RecordVoice;
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
    boolean runningRecord = false;

    HBox hbox;
    FileChooser fileChooser = new FileChooser();
    RecordVoice recordVoice;

    public ButtonsPane(Main main, Stage primaryStage, WavePane wavePane, int widthScreen, String folderPath){
        super();
        this.setWidth(widthScreen);

        recordVoice = new RecordVoice(folderPath);

        Button newAudioFile = createNewAudioFileButton(wavePane, primaryStage);
        Button returnBack = createReturnBackButton(main);
        Button playStopAudioFile = createPlayStopAudioFileButton(wavePane);
        Button record = createRecordButton();
        Button seeJsonFolder = createSeeJsonFolder();

        hbox = new HBox(newAudioFile, returnBack, playStopAudioFile, record, seeJsonFolder);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
        this.extensionAudioFiles();
    }

    public Button createPlayStopAudioFileButton(WavePane wavePane) {
        Button playStopAudioFile = new Buttons();
        playStopAudioFile.setGraphic(ImageButton.createButtonImageView("images/play.png"));
        playStopAudioFile.getStyleClass().add("blue");
        playStopAudioFile.setContentDisplay(ContentDisplay.TOP);
        playStopAudioFile.setPrefHeight(50);
        playStopAudioFile.setPrefWidth(300);
        playStopAudioFile.setOnAction((e) -> {
            if (runningAudio) {
                runningAudio = false;
                wavePane.getWaveService().playStopMediaPlayer(false);
                wavePane.setStep(0);
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/play.png"));
            } else {
                runningAudio = true;
                wavePane.getWaveService().playStopMediaPlayer(true);
                wavePane.setStep(1);
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/pause.png"));
            }
        });
        return playStopAudioFile;
    }

    public Button createRecordButton(){
        Button record = new Buttons();
        record.setGraphic(ImageButton.createButtonImageView("images/record.png"));
        record.getStyleClass().add("blue");
        record.setContentDisplay(ContentDisplay.TOP);
        record.setPrefHeight(50);
        record.setPrefWidth(300);
        record.setOnAction((e) -> {
            if (runningRecord) {
                runningRecord = false;
                recordVoice.stopRecording();
                ((ImageView) record.getGraphic()).setImage(new Image("images/record.png"));
            } else {
                runningRecord = true;
                recordVoice.startRecording();
                ((ImageView) record.getGraphic()).setImage(new Image("images/stopRecord.png"));
            }
        });
        return record;
    }

    public Button createNewAudioFileButton(WavePane wavePane, Stage primaryStage){
        Button newAudioFile = new Buttons();
        newAudioFile.setGraphic(ImageButton.createButtonImageView("images/add.png"));
        newAudioFile.getStyleClass().add("blue");
        newAudioFile.setContentDisplay(ContentDisplay.TOP);
        newAudioFile.setPrefHeight(50);
        newAudioFile.setPrefWidth(300);
        newAudioFile.setOnAction((e) -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null){
                wavePane.getWaveService().startService(String.valueOf(file), WaveFormService.WaveFormJob.AMPLITUDES_AND_WAVEFORM);
                wavePane.getWaveService().setupMediaPlayer(String.valueOf(file));
            }
        });
        return  newAudioFile;
    }

    public Button createReturnBackButton(Main main){
        Button returnBack = new Buttons();
        returnBack.setGraphic(ImageButton.createButtonImageView("images/back.png"));
        returnBack.getStyleClass().add("blue");
        returnBack.setContentDisplay(ContentDisplay.TOP);
        returnBack.setPrefHeight(50);
        returnBack.setPrefWidth(300);
        returnBack.setOnAction((e) -> {

        });
        return  returnBack;
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
