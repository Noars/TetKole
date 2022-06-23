package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.buttons.Buttons;
import utils.buttons.ImageButton;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ListenPane extends BorderPane {

    Main main;
    Stage primaryStage;

    HBox hbox;
    GridPane gridPane;

    String jsonPath;
    String recordPath;
    String pathAudioFile;

    String[] listFiles;
    JSONArray[] listFilesCorrespondingToAudioFile;
    MediaPlayer[] listMediaPlayerRecordFiles;
    MediaPlayer[] listMediaPlayerAudioFile;

    int nbCorrespondingFile = 0;

    public ListenPane(Main main, Stage primaryStage){
        super();

        this.main = main;
        this.primaryStage = primaryStage;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        hbox = new HBox(gridPane);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        gridPane.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(gridPane, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65");
    }

    public void setupListenPane(){
        this.nbCorrespondingFile = 0;
        this.jsonPath = main.getSaveFolder().getJsonPath();
        this.recordPath = main.getSaveFolder().getRecordPath();
        this.getAllJsonFile();
        this.getJsonFileCorrespondingToAudioFile(main);
        this.createMediaPlayerRecordFiles(main);
        this.createLabel();
        this.createReturnBackButton();
    }

    public void createReturnBackButton(){
        Button returnBack = new Buttons();
        returnBack.setGraphic(ImageButton.createButtonImageView("images/back.png"));
        returnBack.getStyleClass().add("blue");
        returnBack.setContentDisplay(ContentDisplay.TOP);
        returnBack.setPrefHeight(50);
        returnBack.setPrefWidth(300);
        returnBack.setOnAction((e) -> {
            main.goToHome(primaryStage);
        });
        this.gridPane.add(returnBack,1,(this.nbCorrespondingFile * 2) + 1);
    }

    public void getAllJsonFile(){
        File folder = new File(jsonPath);
        listFiles = folder.list();
        listFilesCorrespondingToAudioFile = new JSONArray[listFiles.length];
    }

    public void getJsonFileCorrespondingToAudioFile(Main main){
        for (int i = 0; i < listFiles.length; i++){
            String jsonPath = getJsonPathForActualOS(main, i);
            try (FileReader reader = new FileReader(jsonPath)){
                JSONArray jsonArray = (JSONArray) new JSONParser().parse(reader);
                JSONObject id = (JSONObject) jsonArray.get(0);
                if (id.get("Nom du fichier audio").equals(main.getWavePane().getWaveService().audioFileName)){
                    listFilesCorrespondingToAudioFile[i] = jsonArray;
                    this.nbCorrespondingFile += 1;
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        this.listMediaPlayerAudioFile = new MediaPlayer[this.nbCorrespondingFile];
        this.listMediaPlayerRecordFiles = new MediaPlayer[this.nbCorrespondingFile];
    }

    public void createMediaPlayerRecordFiles(Main main){
        int index = 0;
        for (JSONArray item: listFilesCorrespondingToAudioFile){
            if (item != null){
                JSONObject nameRecordFile = (JSONObject) item.get(1);
                Media recordFile = new Media(new File(getRecordPathForActualOS(main, (String) nameRecordFile.get("Nom du fichier audio enregistrer"))).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(recordFile);
                this.listMediaPlayerRecordFiles[index] = this.setupRecordMediaPlayer(mediaPlayer, item, index);
                index++;
            }
        }
    }

    public MediaPlayer setupRecordMediaPlayer(MediaPlayer mediaPlayer, JSONArray item, int index){
        JSONObject startTimeObj = (JSONObject) item.get(2);
        JSONObject endTimeObj = (JSONObject) item.get(3);

        String startTime = (String) startTimeObj.get("Debut de l'intervalle");
        startTime = startTime.replaceAll("[a-zA-z]", "");
        String endTime = (String) endTimeObj.get("Fin de l'intervalle");
        endTime = endTime.replaceAll("[a-zA-z]", "");

        String[] startTimeSplit = startTime.split(":");
        String[] endTimeSplit = endTime.split(":");

        int startTimeValue = (Integer.parseInt(startTimeSplit[0]) * 3600000) + (Integer.parseInt(startTimeSplit[1]) * 60000) + (Integer.parseInt(startTimeSplit[2]) * 1000) + Integer.parseInt(startTimeSplit[3]);
        int endTimeValue = (Integer.parseInt(endTimeSplit[0]) * 3600000) + (Integer.parseInt(endTimeSplit[1]) * 60000) + (Integer.parseInt(endTimeSplit[2]) * 1000) + Integer.parseInt(endTimeSplit[3]);

        mediaPlayer.setStartTime(new Duration(startTimeValue));
        mediaPlayer.setStopTime(new Duration(endTimeValue));

        this.setupAudioFileMediaPlayer(startTimeValue, endTimeValue, index);

        return mediaPlayer;
    }

    public void setupAudioFileMediaPlayer(int startTime, int endTime, int index){
        Media audioFile = new Media(new File(this.pathAudioFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(audioFile);

        mediaPlayer.setStartTime(new Duration(startTime));
        mediaPlayer.setStopTime(new Duration(endTime));

        this.listMediaPlayerAudioFile[index] = mediaPlayer;
    }

    public void createLabel(){
        int index = 0;
        for (int i = 0; i < this.nbCorrespondingFile; i++){
            Label audioLabel = new Label("Fichier audio");
            audioLabel.getStyleClass().add("textLabel");

            Label emptyLabel = new Label("");

            Label recordLabel = new Label("Enregistrement audio");
            recordLabel.getStyleClass().add("textLabel");

            this.gridPane.add(audioLabel,0,index);
            gridPane.add(emptyLabel, 1, index);
            this.gridPane.add(recordLabel,2,index);

            this.createMediaPlayerAudioButton(index);
            this.createMediaPlayerRecordButton(index);
            index += 2;
        }
    }

    public void createMediaPlayerAudioButton(int index){
        Button playStopMediaPlayerAudio = new Buttons();
        playStopMediaPlayerAudio.setGraphic(ImageButton.createButtonImageView("images/play.png"));
        playStopMediaPlayerAudio.getStyleClass().add("blue");
        playStopMediaPlayerAudio.setContentDisplay(ContentDisplay.TOP);
        playStopMediaPlayerAudio.setPrefHeight(50);
        playStopMediaPlayerAudio.setPrefWidth(300);
        playStopMediaPlayerAudio.setOnAction((e) -> {
            listMediaPlayerAudioFile[index].stop();
            listMediaPlayerAudioFile[index].play();
        });

        this.gridPane.add(playStopMediaPlayerAudio, 0,index+1);
    }

    public void createMediaPlayerRecordButton(int index){
        Button playStopMediaPlayerRecord = new Buttons();
        playStopMediaPlayerRecord.setGraphic(ImageButton.createButtonImageView("images/play.png"));
        playStopMediaPlayerRecord.getStyleClass().add("blue");
        playStopMediaPlayerRecord.setContentDisplay(ContentDisplay.TOP);
        playStopMediaPlayerRecord.setPrefHeight(50);
        playStopMediaPlayerRecord.setPrefWidth(300);
        playStopMediaPlayerRecord.setOnAction((e) -> {
            listMediaPlayerRecordFiles[index].stop();
            listMediaPlayerRecordFiles[index].play();
        });

        this.gridPane.add(playStopMediaPlayerRecord, 2,index+1);
    }

    public String getJsonPathForActualOS(Main main, int i){
        if (main.getOs().contains("nux") || main.getOs().contains("mac")){
            return jsonPath + "/" + listFiles[i];
        }else {
            return jsonPath + "\\" + listFiles[i];
        }
    }

    public String getRecordPathForActualOS(Main main, String nameFile){
        if (main.getOs().contains("nux") || main.getOs().contains("mac")){
            return recordPath + "/" + nameFile;
        }else {
            return recordPath + "\\" + nameFile;
        }
    }

    public void setPath(String path){
        this.pathAudioFile = path;
    }
}
