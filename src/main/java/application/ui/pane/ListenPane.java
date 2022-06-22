package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
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
    HBox hbox;
    GridPane gridPane;

    String jsonPath;
    String recordPath;
    String pathAudioFile;

    String[] listFiles;
    JSONArray[] listFilesCorrespondingToAudioFile;
    MediaPlayer[] listMediaPlayerRecordFiles;
    MediaPlayer[] listMediaPlayerAudioFile;
    Label[] listLabels;

    int nbCorrespondingFile = 0;

    public ListenPane(Main main, Stage primaryStage){
        super();

        this.main = main;

        Button returnBack = createReturnBackButton(main, primaryStage);
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        {
            gridPane.add(returnBack, 1, 10);
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

    public void setupListenPane(){
        this.nbCorrespondingFile = 0;
        this.jsonPath = main.getSaveFolder().getJsonPath();
        this.recordPath = main.getSaveFolder().getRecordPath();
        this.getAllJsonFile();
        this.getJsonFileCorrespondingToAudioFile(main);
        this.createMediaPlayerRecordFiles(main);
        this.createLabel();
    }

    public Button createReturnBackButton(Main main, Stage primaryStage){
        Button returnBack = new Buttons();
        returnBack.setGraphic(ImageButton.createButtonImageView("images/back.png"));
        returnBack.getStyleClass().add("blue");
        returnBack.setContentDisplay(ContentDisplay.TOP);
        returnBack.setPrefHeight(50);
        returnBack.setPrefWidth(300);
        returnBack.setOnAction((e) -> {
            main.goToHome(primaryStage);
        });
        return  returnBack;
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
        for (int i = 0; i < this.nbCorrespondingFile; i++){
            Label audioLabel = new Label("Fichier audio");
            audioLabel.getStyleClass().add("textLabel");

            Label recordLabel = new Label("Enregistrement audio");
            recordLabel.getStyleClass().add("textLabel");

            this.gridPane.add(audioLabel,0,i);
            this.gridPane.add(recordLabel,1,i);
        }
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
