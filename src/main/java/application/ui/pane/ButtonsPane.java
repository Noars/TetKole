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

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ButtonsPane extends BorderPane {

    boolean runningAudio = false;

    Button record;
    Button playStopAudioFile;
    Button zoom;

    HBox hbox;
    FileChooser fileChooser = new FileChooser();

    public ButtonsPane(Main main, Stage primaryStage){
        super();

        Button newAudioFile = createNewAudioFileButton(main, primaryStage);
        playStopAudioFile = createPlayStopAudioFileButton(main);
        record = createRecordButton(main, primaryStage);
        Button seeJsonFolder = createSeeJsonFolder(main);
        zoom = createZoomButton(main, primaryStage);

        hbox = new HBox(newAudioFile, zoom, playStopAudioFile, record, seeJsonFolder);
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
                main.getWavePane().getWaveService().playStopMediaPlayer("pause");
                main.getWavePane().setStep(false);
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/play.png"));
            } else {
                runningAudio = true;
                main.getWavePane().getWaveService().playStopMediaPlayer("play");
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
            this.stopMusic();
            main.getRecordPane().generateNameFile(main);
            main.goToRecord(primaryStage);
        });
        record.setDisable(true);
        return record;
    }

    public Button createZoomButton(Main main, Stage primaryStage){
        Button zoom = new Button();
        zoom.setGraphic(ImageButton.createButtonImageView("images/zoom+.png"));
        zoom.getStyleClass().add("blue");
        zoom.setContentDisplay(ContentDisplay.TOP);
        zoom.setPrefHeight(50);
        zoom.setPrefWidth(300);
        zoom.setOnAction((e) -> {
            this.stopMusic();
            main.setNewZoomWavePane(primaryStage);
            main.getZoomPane().setWaveZoomData();
            main.goToZoom(primaryStage);
        });
        zoom.setDisable(true);
        return zoom;
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
                main.setLoadingPane(main, primaryStage);
                main.getWavePane().getWaveService().startService(String.valueOf(file), WaveFormService.WaveFormJob.AMPLITUDES_AND_WAVEFORM);
                main.getWavePane().getWaveService().setupMediaPlayer(String.valueOf(file));
                record.setDisable(false);
                zoom.setDisable(false);
            }
        });
        return  newAudioFile;
    }

    public Button createSeeJsonFolder(Main main){
        Button seeJsonFolder = new Buttons();
        seeJsonFolder.setGraphic(ImageButton.createButtonImageView("images/folder.png"));
        seeJsonFolder.getStyleClass().add("blue");
        seeJsonFolder.setContentDisplay(ContentDisplay.TOP);
        seeJsonFolder.setPrefHeight(50);
        seeJsonFolder.setPrefWidth(300);
        seeJsonFolder.setOnAction((e) -> {
            try {
                if (main.getOs().contains("nux") || main.getOs().contains("mac")){
                    new Thread(() -> {
                        String pathFolder = "/home/" + System.getProperty("user.name") + "/TètKole/";
                        try {
                            Desktop.getDesktop().open(new File(pathFolder));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }else {
                    String pathFolder = System.getProperty("user.home") + "\\Documents\\TètKole\\JsonFiles\\";
                    Runtime.getRuntime().exec("explorer.exe /select," + pathFolder);
                }
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

    public void stopMusic(){
        if (runningAudio){
            playStopAudioFile.fire();
        }
    }
}
