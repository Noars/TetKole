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
import java.util.ResourceBundle;

public class ListenPane extends BorderPane {

    Main main;
    Stage primaryStage;
    ResourceBundle language;

    HBox hbox;
    GridPane gridPane;

    String jsonPath;
    String recordPath;
    String pathAudioFile;

    Label audioLabel;
    Label timeLabel;

    String[] listFiles;
    JSONArray[] listFilesCorrespondingToAudioFile;
    String[] listNameFilesCorrespondingToAudioFile;
    String[] listStartTimeAudioAndRecordFiles;
    String[] listEndTimeAudioAndRecordFiles;
    MediaPlayer[] listMediaPlayerRecordFiles;
    MediaPlayer[] listMediaPlayerAudioFile;
    Boolean[] listStatusMediaPlayerRecordFiles;
    Boolean[] listStatusMediaPlayerAudioFile;

    int nbCorrespondingFile = 0;

    public ListenPane(Main main, Stage primaryStage, ResourceBundle language){
        super();

        this.main = main;
        this.primaryStage = primaryStage;
        this.language = language;

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
        this.gridPane.add(returnBack,1,(this.nbCorrespondingFile * 3) + 1);
    }

    public void getAllJsonFile(){
        File folder = new File(jsonPath);
        listFiles = folder.list();
        listFilesCorrespondingToAudioFile = new JSONArray[listFiles.length];
        listNameFilesCorrespondingToAudioFile = new String[listFiles.length];
    }

    public void getJsonFileCorrespondingToAudioFile(Main main){
        for (int i = 0; i < listFiles.length; i++){
            String jsonPath = getJsonPathForActualOS(main, i);
            try (FileReader reader = new FileReader(jsonPath)){
                JSONArray jsonArray = (JSONArray) new JSONParser().parse(reader);
                JSONObject id = (JSONObject) jsonArray.get(0);
                if (id.get("Nom du fichier audio").equals(main.getWavePane().getWaveService().audioFileName)){
                    listFilesCorrespondingToAudioFile[i] = jsonArray;
                    listNameFilesCorrespondingToAudioFile[i] = listFiles[i];
                    this.nbCorrespondingFile += 1;
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        this.listMediaPlayerAudioFile = new MediaPlayer[this.nbCorrespondingFile];
        this.listStatusMediaPlayerAudioFile = new Boolean[this.nbCorrespondingFile];
        this.listMediaPlayerRecordFiles = new MediaPlayer[this.nbCorrespondingFile];
        this.listStatusMediaPlayerRecordFiles = new Boolean[this.nbCorrespondingFile];
        this.listStartTimeAudioAndRecordFiles = new String[this.nbCorrespondingFile];
        this.listEndTimeAudioAndRecordFiles = new String[this.nbCorrespondingFile];

        this.filterNameList();
        this.initBooleanTab();
    }

    public void filterNameList(){
        String[] temp = new String[nbCorrespondingFile];
        int index = 0;
        for (String value: this.listNameFilesCorrespondingToAudioFile){
            if (value != null){
                temp[index] = value;
                index++;
            }
        }
        this.listNameFilesCorrespondingToAudioFile = temp;
    }

    public void initBooleanTab(){
        for (int i = 0; i < this.nbCorrespondingFile; i++){
            this.listStatusMediaPlayerAudioFile[i] = false;
            this.listStatusMediaPlayerRecordFiles[i] = false;
        }
    }

    public void createMediaPlayerRecordFiles(Main main){
        int index = 0;
        for (JSONArray item: listFilesCorrespondingToAudioFile){
            if (item != null){
                JSONObject nameRecordFile = (JSONObject) item.get(1);
                Media recordFile = new Media(new File(getRecordPathForActualOS(main, (String) nameRecordFile.get("Nom du fichier audio enregistrer"))).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(recordFile);
                this.listMediaPlayerRecordFiles[index] = mediaPlayer;
                this.setupAudioFileMediaPlayer(item, index);
                index++;
            }
        }
    }

    public void setupAudioFileMediaPlayer(JSONArray item, int index){
        JSONObject startTimeObj = (JSONObject) item.get(2);
        JSONObject endTimeObj = (JSONObject) item.get(3);

        String startTime = (String) startTimeObj.get("Debut de l'intervalle");
        this.listStartTimeAudioAndRecordFiles[index] = startTime;
        startTime = startTime.replaceAll("[a-zA-z]", "");
        String endTime = (String) endTimeObj.get("Fin de l'intervalle");
        this.listEndTimeAudioAndRecordFiles[index] = endTime;
        endTime = endTime.replaceAll("[a-zA-z]", "");

        String[] startTimeSplit = startTime.split(":");
        String[] endTimeSplit = endTime.split(":");

        int startTimeValue = (Integer.parseInt(startTimeSplit[0]) * 3600000) + (Integer.parseInt(startTimeSplit[1]) * 60000) + (Integer.parseInt(startTimeSplit[2]) * 1000) + Integer.parseInt(startTimeSplit[3]);
        int endTimeValue = (Integer.parseInt(endTimeSplit[0]) * 3600000) + (Integer.parseInt(endTimeSplit[1]) * 60000) + (Integer.parseInt(endTimeSplit[2]) * 1000) + Integer.parseInt(endTimeSplit[3]);

        Media audioFile = new Media(new File(this.pathAudioFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(audioFile);

        mediaPlayer.setStartTime(new Duration(startTimeValue));
        mediaPlayer.setStopTime(new Duration(endTimeValue));

        this.listMediaPlayerAudioFile[index] = mediaPlayer;
    }

    public void createLabel(){
        int index = 0;

        audioLabel = new Label(language.getString("AudioFile"));
        audioLabel.getStyleClass().add("textLabel");
        this.gridPane.add(audioLabel,0,index);

        timeLabel = new Label(language.getString("Time"));
        timeLabel.getStyleClass().add("textLabel");
        this.gridPane.add(timeLabel, 4, index);

        for (int i = 0; i < this.nbCorrespondingFile; i++){

            Label recordLabel = new Label(listNameFilesCorrespondingToAudioFile[i]);
            recordLabel.getStyleClass().add("textLabel");

            Label emptyLabel = new Label("");
            Label emptyLabel2 = new Label("");
            Label emptyLabel3 = new Label("");


            this.gridPane.add(emptyLabel, 1, index);
            this.gridPane.add(recordLabel,2,index);
            this.gridPane.add(emptyLabel2, 3, index);

            this.createMediaPlayerAudioButton(i, index);
            this.createMediaPlayerRecordButton(i, index);
            this.displayTimeValue(i, index);

            this.gridPane.add(emptyLabel3, 0, index+2);

            index += 3;
        }
    }

    public void createMediaPlayerAudioButton(int indexTab, int indexGridPane){
        Button playStopMediaPlayerAudio = new Buttons();
        playStopMediaPlayerAudio.setGraphic(ImageButton.createButtonImageView("images/play.png"));
        playStopMediaPlayerAudio.getStyleClass().add("blue");
        playStopMediaPlayerAudio.setContentDisplay(ContentDisplay.TOP);
        playStopMediaPlayerAudio.setPrefHeight(50);
        playStopMediaPlayerAudio.setPrefWidth(300);
        playStopMediaPlayerAudio.setOnAction((e) -> {
            if (this.listStatusMediaPlayerAudioFile[indexTab]){
                this.listStatusMediaPlayerAudioFile[indexTab] = false;
                listMediaPlayerAudioFile[indexTab].stop();
                ((ImageView) playStopMediaPlayerAudio.getGraphic()).setImage(new Image("images/play.png"));
            }else {
                this.listStatusMediaPlayerAudioFile[indexTab] = true;
                listMediaPlayerAudioFile[indexTab].play();
                ((ImageView) playStopMediaPlayerAudio.getGraphic()).setImage(new Image("images/stop.png"));
            }
        });
        this.gridPane.add(playStopMediaPlayerAudio, 0,indexGridPane+1);
    }

    public void createMediaPlayerRecordButton(int indexTab, int indexGridPane){
        Button playStopMediaPlayerRecord = new Buttons();
        playStopMediaPlayerRecord.setGraphic(ImageButton.createButtonImageView("images/play.png"));
        playStopMediaPlayerRecord.getStyleClass().add("blue");
        playStopMediaPlayerRecord.setContentDisplay(ContentDisplay.TOP);
        playStopMediaPlayerRecord.setPrefHeight(50);
        playStopMediaPlayerRecord.setPrefWidth(300);
        playStopMediaPlayerRecord.setOnAction((e) -> {
            if (this.listStatusMediaPlayerRecordFiles[indexTab]){
                this.listStatusMediaPlayerRecordFiles[indexTab] = false;
                listMediaPlayerRecordFiles[indexTab].stop();
                ((ImageView) playStopMediaPlayerRecord.getGraphic()).setImage(new Image("images/play.png"));
            }else {
                this.listStatusMediaPlayerRecordFiles[indexTab] = true;
                listMediaPlayerRecordFiles[indexTab].play();
                ((ImageView) playStopMediaPlayerRecord.getGraphic()).setImage(new Image("images/stop.png"));
            }
        });
        this.gridPane.add(playStopMediaPlayerRecord, 2,indexGridPane+1);
    }

    public void displayTimeValue(int indexTab, int indexGridPane){
        Label timeValueLabel = new Label(this.listStartTimeAudioAndRecordFiles[indexTab] + " - " + this.listEndTimeAudioAndRecordFiles[indexTab]);
        timeValueLabel.getStyleClass().add("textLabel");
        timeValueLabel.setAlignment(Pos.CENTER);
        this.gridPane.add(timeValueLabel, 4, indexGridPane+1);
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

    public void changeLabel(ResourceBundle languages){
        this.language = languages;
        this.audioLabel.setText(languages.getString("AudioFile"));
        this.timeLabel.setText(languages.getString("Time"));
    }
}
