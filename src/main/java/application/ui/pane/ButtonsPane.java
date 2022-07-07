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
import utils.zoomWave.ZoomWaveFormService;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ButtonsPane extends BorderPane {

    boolean runningAudio = false;

    Button newAudioFile;
    Button record;
    Button playStopAudioFile;
    Button zoom;
    Button listenAudioAndRecord;

    HBox hbox;
    FileChooser fileChooser = new FileChooser();

    /**
     * Initialize the constructor of this class
     * And create the gridPane with all buttons
     *
     * @param main
     * @param primaryStage
     */
    public ButtonsPane(Main main, Stage primaryStage){
        super();

        newAudioFile = createNewAudioFileButton(main, primaryStage);
        playStopAudioFile = createPlayStopAudioFileButton(main);
        record = createRecordButton(main, primaryStage);
        Button seeJsonFolder = createSeeJsonFolderButton(main);
        zoom = createZoomButton(main, primaryStage);
        listenAudioAndRecord = createListenAudioAndRecordButton(main, primaryStage);

        hbox = new HBox(newAudioFile, zoom, playStopAudioFile, record, listenAudioAndRecord, seeJsonFolder);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
        this.extensionAudioFiles();
    }

    /**
     * Function that create the playStop button
     * This button permit listen or pause the audio file
     *
     * @param main
     * @return the button "playStop"
     */
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
        playStopAudioFile.setDisable(true);
        return playStopAudioFile;
    }

    /**
     * Function that create the record button
     * This button permit going on the record page
     *
     * @param main
     * @param primaryStage
     * @return the button "record"
     */
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
            main.getRecordPane().getCreateJson().setIsZoom(false);
            main.goToRecord(primaryStage);
        });
        record.setDisable(true);
        return record;
    }

    /**
     * Function that create the zoom button
     * This button :
     *  - Create a temporary cut file audio who will be used for the zoom
     *  - Launch the loading page
     *  - Then go on the zoom page
     *
     * @param main
     * @param primaryStage
     * @return the button "zoom"
     */
    public Button createZoomButton(Main main, Stage primaryStage){
        Button zoom = new Button();
        zoom.setGraphic(ImageButton.createButtonImageView("images/zoom+.png"));
        zoom.getStyleClass().add("blue");
        zoom.setContentDisplay(ContentDisplay.TOP);
        zoom.setPrefHeight(50);
        zoom.setPrefWidth(300);
        zoom.setOnAction((e) -> {
            this.stopMusic();
            main.getCutAudio().cutAudio(main.getWavePane().getWaveService().pathAudioFile, main.getWavePane().getStartTimeChoose(), main.getWavePane().getEndTimeChoose());
            main.setLoadingZoomPane(main, primaryStage);
            main.getZoomPane().getWaveZoomService().startService(main.getCutAudio().getPathAudioCut(), ZoomWaveFormService.WaveZoomFormJob.AMPLITUDES_AND_WAVEFORM);
            main.getZoomPane().getWaveZoomService().setupMediaPlayer(main.getCutAudio().getPathAudioCut());
        });
        zoom.setDisable(true);
        return zoom;
    }

    /**
     * Function that create the newAudioFile button
     * This button permit to add the audio file who will be used to our record
     *
     * @param main
     * @param primaryStage
     * @return the button "newAudioFile"
     */
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
                newAudioFile.setDisable(true);
                main.setLoadingPane(main, primaryStage);
                main.getWavePane().getWaveService().startService(String.valueOf(file), WaveFormService.WaveFormJob.AMPLITUDES_AND_WAVEFORM);
                main.getWavePane().getWaveService().setupMediaPlayer(String.valueOf(file));
                main.getListenPane().setPath(String.valueOf(file));
            }
        });
        return  newAudioFile;
    }

    /**
     * Function that enable all the disable button
     * At the start of the application, since we don't have an audio file
     * I disable all buttons
     */
    public void enableButton(){
        newAudioFile.setDisable(false);
        record.setDisable(false);
        zoom.setDisable(false);
        listenAudioAndRecord.setDisable(false);
        playStopAudioFile.setDisable(false);
    }

    /**
     * Function that create the listenAudioAndRecord button
     * This button permit going on the Listen page
     *
     * @param main
     * @param primaryStage
     * @return the button "listenAudioAndRecord"
     */
    public Button createListenAudioAndRecordButton(Main main, Stage primaryStage){
        Button listenAudioAndRecord = new Buttons();
        listenAudioAndRecord.setGraphic(ImageButton.createButtonImageView("images/music.png"));
        listenAudioAndRecord.getStyleClass().add("blue");
        listenAudioAndRecord.setContentDisplay(ContentDisplay.TOP);
        listenAudioAndRecord.setPrefHeight(50);
        listenAudioAndRecord.setPrefWidth(300);
        listenAudioAndRecord.setOnAction((e) -> {
            main.getListenPane().setupListenPane();
            main.goToListen(primaryStage);
        });
        listenAudioAndRecord.setDisable(true);
        return  listenAudioAndRecord;
    }

    /**
     * Function that create the seeJsonFolder button
     * This button permit opening the file explorer according to the good operating system
     *
     * @param main
     * @return the button "seeJsonFolder"
     */
    public Button createSeeJsonFolderButton(Main main){
        Button seeJsonFolder = new Buttons();
        seeJsonFolder.setGraphic(ImageButton.createButtonImageView("images/folder.png"));
        seeJsonFolder.getStyleClass().add("blue");
        seeJsonFolder.setContentDisplay(ContentDisplay.TOP);
        seeJsonFolder.setPrefHeight(50);
        seeJsonFolder.setPrefWidth(300);
        seeJsonFolder.setOnAction((e) -> {
            this.deleteTempFiles(main);
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

    /**
     * Function that create the extension filter
     * With this filter actually only .mp3 & .wav files will be visible in the file explorer
     */
    public void extensionAudioFiles(){
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );
    }

    /**
     * Function that stop the music if she is running
     */
    public void stopMusic(){
        if (runningAudio){
            playStopAudioFile.fire();
        }
    }

    /**
     * Function that delete all temporary files created
     *
     * @param main
     */
    public void deleteTempFiles(Main main){
        main.getCutAudio().deleteTempFiles();
        main.getRecordPane().deleteTempFiles();
    }
}
