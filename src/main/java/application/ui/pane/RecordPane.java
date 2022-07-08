package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utils.buttons.Buttons;
import utils.buttons.ImageButton;
import utils.files.CreateJson;
import utils.files.RecordVoice;

import java.io.File;
import java.util.Objects;
import java.util.ResourceBundle;

public class RecordPane extends BorderPane {

    boolean runningAudioFile = false;
    boolean runningAudioRecorded = false;
    boolean runningRecord = false;
    boolean recordDone = false;

    Button playStopAudioFile;
    Button record;
    Button listenAudioFile;

    Label statusAudioFile;
    Label audioFileNameLabel;
    Label errorStatusAudioLabel;
    Label errorAudioFileNameLabel;
    TextField audioFileNameText;
    ImageView statusImageView;

    HBox hbox;
    RecordVoice recordVoice;
    CreateJson createJson;

    String pathFolder;
    String generatedNameFile;

    ResourceBundle language;

    /**
     * Initialize the constructor of this class
     * And create the gridPane with all buttons
     *
     * @param main
     * @param primaryStage
     * @param pathFolder
     * @param language
     */
    public RecordPane(Main main, Stage primaryStage, String pathFolder, ResourceBundle language){
        super();

        this.language = language;

        recordVoice = new RecordVoice(main, pathFolder);
        createJson = new CreateJson(main, pathFolder);
        this.pathFolder = pathFolder;

        playStopAudioFile = createPlayStopAudioFileButton();
        record = createRecordButton();
        Button validate = createValidateRecordButton(main, primaryStage);
        Button returnBack = createReturnBackButton(main, primaryStage);
        listenAudioFile = createListenAudioFile(main);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        {
            statusAudioFile = new Label(language.getString("RecordAudio"));
            Image statusImage = new Image("images/error.png");
            statusImageView = new ImageView();
            statusImageView.setFitWidth(30);
            statusImageView.setFitHeight(30);
            statusImageView.setImage(statusImage);
            errorStatusAudioLabel = new Label();
            gridPane.add(statusAudioFile, 1, 0);
            gridPane.add(statusImageView, 2, 0);
            gridPane.add(errorStatusAudioLabel, 3, 0);
            statusAudioFile.getStyleClass().add("textLabel");
            errorStatusAudioLabel.getStyleClass().add("errorLabel");

            audioFileNameLabel = new Label(language.getString("NameAudioFile"));
            audioFileNameText = new TextField(this.generatedNameFile);
            errorAudioFileNameLabel = new Label();
            gridPane.add(audioFileNameLabel, 1, 1);
            gridPane.add(audioFileNameText, 2, 1);
            gridPane.add(errorAudioFileNameLabel, 3, 1);
            audioFileNameLabel.getStyleClass().add("textLabel");
            audioFileNameText.getStyleClass().add("textField");
            errorAudioFileNameLabel.getStyleClass().add("errorLabel");

            Label emptyLabel = new Label("");
            gridPane.add(emptyLabel, 0, 2);

            gridPane.add(returnBack, 0, 3);
            gridPane.add(listenAudioFile, 1, 3);
            gridPane.add(record, 2, 3);
            gridPane.add(playStopAudioFile, 3, 3);
            gridPane.add(validate, 4, 3);
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

    /**
     * Function that create the playStop button
     * This button permit listen or pause the audio file recorded
     *
     * @return the button "playStop"
     */
    public Button createPlayStopAudioFileButton() {
        Button playStopAudioFile = new Buttons();
        playStopAudioFile.setGraphic(ImageButton.createButtonImageView("images/play.png"));
        playStopAudioFile.getStyleClass().add("blue");
        playStopAudioFile.setContentDisplay(ContentDisplay.TOP);
        playStopAudioFile.setPrefHeight(50);
        playStopAudioFile.setPrefWidth(300);
        playStopAudioFile.setOnAction((e) -> {
            if (runningAudioRecorded) {
                runningAudioRecorded = false;
                recordVoice.playStopMediaPlayer("stop");
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/play.png"));
            } else {
                runningAudioRecorded = true;
                recordVoice.playStopMediaPlayer("play");
                ((ImageView) playStopAudioFile.getGraphic()).setImage(new Image("images/stop.png"));
            }
        });
        return playStopAudioFile;
    }

    /**
     * Function that create the record button
     * This button permit recording our voice
     *
     * @return the button "record"
     */
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
                recordDone = true;
                this.setStatusImageView(true);
                ((ImageView) record.getGraphic()).setImage(new Image("images/record.png"));
            } else {
                runningRecord = true;
                recordDone = true;
                this.setStatusImageView(false);
                recordVoice.startRecording();
                ((ImageView) record.getGraphic()).setImage(new Image("images/stopRecord.png"));
            }
        });
        return record;
    }

    /**
     * Function that create the record button
     * This button permit going on the home page or the zoom page
     *
     * @param main
     * @param primaryStage
     * @return
     */
    public Button createReturnBackButton(Main main, Stage primaryStage){
        Button returnBack = new Buttons();
        returnBack.setGraphic(ImageButton.createButtonImageView("images/back.png"));
        returnBack.getStyleClass().add("blue");
        returnBack.setContentDisplay(ContentDisplay.TOP);
        returnBack.setPrefHeight(50);
        returnBack.setPrefWidth(300);
        returnBack.setOnAction((e) -> {
            this.resetValue();
            this.resetButton();
            this.returnBack(main, primaryStage);
        });
        return  returnBack;
    }

    /**
     * Function that create the listenAudioFile button
     * This button permit listen or pause the audio file
     *
     * @param main
     * @return the button "listenAudioFile"
     */
    public Button createListenAudioFile(Main main){
        Button listenAudioFile = new Buttons();
        listenAudioFile.setGraphic(ImageButton.createButtonImageView("images/playAudioFile.png"));
        listenAudioFile.getStyleClass().add("blue");
        listenAudioFile.setContentDisplay(ContentDisplay.TOP);
        listenAudioFile.setPrefHeight(50);
        listenAudioFile.setPrefWidth(300);
        listenAudioFile.setOnAction((e) -> {
            if (runningAudioFile) {
                runningAudioFile = false;
                this.playGoodAudioFile(main);
                ((ImageView) listenAudioFile.getGraphic()).setImage(new Image("images/playAudioFile.png"));
            } else {
                runningAudioFile = true;
                this.playGoodAudioFile(main);
                ((ImageView) listenAudioFile.getGraphic()).setImage(new Image("images/stopAudioFile.png"));
            }
        });
        return  listenAudioFile;
    }

    /**
     * Function that play or stop the good audio
     * Audio original or Audio cut
     *
     * @param main
     */
    public void playGoodAudioFile(Main main){
        if (createJson.getIsZoom()){
            main.getButtonsZoomPane().playStop.fire();
        }else {
            main.getButtonsPane().playStopAudioFile.fire();
        }
    }

    /**
     * Function that validate our record
     * Check if :
     * - A record is done
     * - The name is valid or not already used
     *
     * @param main
     * @param primaryStage
     * @return the button "validateRecord"
     */
    public Button createValidateRecordButton(Main main, Stage primaryStage){
        Button validateRecord = new Button();
        validateRecord.setGraphic(ImageButton.createButtonImageView("images/validate.png"));
        validateRecord.getStyleClass().add("blue");
        validateRecord.setContentDisplay(ContentDisplay.TOP);
        validateRecord.setPrefHeight(50);
        validateRecord.setPrefWidth(300);
        validateRecord.setOnAction((e) -> {
            if (!recordDone){
                this.errorStatusAudioMessage();
            }else if (Objects.equals(audioFileNameText.getText(), "")){
                this.errorAudioMessage();
            }else {
                if (!this.checkNameAlreadyUse(audioFileNameText.getText())){
                    recordVoice.renameTempAudioFile(audioFileNameText.getText());
                    createJson.createJson(audioFileNameText.getText());
                    this.resetValue();
                    this.resetButton();
                    this.returnBack(main, primaryStage);
                }
            }
        });
        return validateRecord;
    }

    /**
     * Function that check if the name we want is not already used
     *
     * @param recordFileName
     * @return a boolean
     */
    public Boolean checkNameAlreadyUse(String recordFileName){
        File recordFile = new File(pathFolder + "//RecordFiles//" + recordFileName + ".wav");

        if (recordFile.exists()){
            this.errorAudioFileNameLabel.setText(language.getString("NameAlreadyUse"));
            return true;
        }else {
            return false;
        }
    }

    /**
     * Function that set no record audio done text in the label error
     */
    public void errorStatusAudioMessage(){
        this.errorStatusAudioLabel.setText(language.getString("ErrorRecordAudio"));
    }

    /**
     * Function that set error in name file text in the label error
     */
    public void errorAudioMessage(){
        this.errorStatusAudioLabel.setText("");
        this.errorAudioFileNameLabel.setText(language.getString("ErrorNameAudioFile"));
    }

    /**
     * Function that change the image view for the record
     *
     * @param status
     */
    public void setStatusImageView(boolean status){
        Image statusImage;

        if (status){
            statusImage = new Image("images/validate.png");
        }else {
            statusImage = new Image("images/error.png");
        }
        this.statusImageView.setImage(statusImage);
    }

    /**
     * Function that reset all value in this class
     */
    public void resetValue(){
        recordDone = false;

        Image statusImage = new Image("images/error.png");
        this.statusImageView.setImage(statusImage);

        this.errorStatusAudioLabel.setText("");
        this.errorAudioFileNameLabel.setText("");

        this.audioFileNameText.setText(generatedNameFile);
    }

    /**
     * Function that reset all label in this class
     */
    public void resetErrorLabel(){
        this.errorStatusAudioLabel.setText("");
        this.errorAudioFileNameLabel.setText("");
    }

    /**
     * Function that reset all button in this class
     */
    public void resetButton(){
        if (runningAudioRecorded){
            playStopAudioFile.fire();
        }
        if (runningRecord){
            record.fire();
        }
        if (runningAudioFile){
            listenAudioFile.fire();
        }
    }

    /**
     * Function that generate a name for the record file
     * It takes the name of the original audio file and add a "_translate"
     * And he adds a number
     * If the number is already used, increment the number
     * Until we find a valid one
     */
    public void generateNameFile(Main main){
        boolean findNewName = false;
        int number = 1;
        String newNameFile = "";
        File newNameFileGenerated;
        String nameFile = main.getWavePane().getWaveService().audioFileName;
        String[] nameFileWithoutExtension = nameFile.split("\\.");

        while (!findNewName){
            newNameFile = nameFileWithoutExtension[0] + "_Translate" + number;
            newNameFileGenerated = new File(pathFolder + "//RecordFiles//" + newNameFile + ".wav");
            if (newNameFileGenerated.exists()){
                number += 1;
            }else {
                findNewName = true;
            }
        }

        this.generatedNameFile = newNameFile;
        this.audioFileNameText.setText(generatedNameFile);
    }

    /**
     * Function that change the language of label
     *
     * @param languages
     */
    public void changeLabel(ResourceBundle languages){
        this.language = languages;
        this.resetErrorLabel();
        this.statusAudioFile.setText(languages.getString("RecordAudio"));
        this.audioFileNameLabel.setText(languages.getString("NameAudioFile"));
    }

    public void deleteTempFiles(){
        this.recordVoice.deleteTempFiles();
    }

    /**
     * Function that give the instance of createJson
     *
     * @return createJson instance
     */
    public CreateJson getCreateJson(){
        return this.createJson;
    }

    /**
     * Function that permit to return on the home page or zoom page
     *
     * @param main
     * @param primaryStage
     */
    public void returnBack(Main main, Stage primaryStage){
        if (createJson.getIsZoom()){
            main.goToZoom(primaryStage);
        }else {
            main.goToHome(primaryStage);
        }
    }
}
