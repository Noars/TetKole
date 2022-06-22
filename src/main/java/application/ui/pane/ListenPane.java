package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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

    String jsonPath;
    String[] listFiles;
    JSONArray[] listFilesCorrespondingToAudioFile;

    public ListenPane(Main main, Stage primaryStage){
        super();

        this.main = main;

        Button returnBack = createReturnBackButton(main, primaryStage);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        {
            gridPane.add(returnBack, 0, 0);
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
        this.jsonPath = main.getSaveFolder().getJsonPath();
        this.getAllJsonFile();
        this.getJsonFileCorrespondingToAudioFile(main);
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
            String filePath = getFilePathForActualOS(main, i);
            try (FileReader reader = new FileReader(filePath)){
                JSONArray jsonArray = (JSONArray) new JSONParser().parse(reader);
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                if (jsonObject.get("Nom du fichier audio").equals(main.getWavePane().getWaveService().audioFileName)){
                    listFilesCorrespondingToAudioFile[i] = jsonArray;
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilePathForActualOS(Main main, int i){
        if (main.getOs().contains("nux") || main.getOs().contains("mac")){
            return jsonPath + "/" + listFiles[i];
        }else {
            return jsonPath + "\\" + listFiles[i];
        }
    }
}
