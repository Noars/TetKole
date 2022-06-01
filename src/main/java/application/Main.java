package application;

import application.ui.pane.*;
import utils.files.SaveFolder;
import application.ui.pane.WavePane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.util.Objects;

public class Main extends Application {

    DecoratedPane decoratedPane;
    ButtonsPane buttonsPane;
    SettingsPane settingsPane;
    EmptyPane emptyPane;
    WavePane wavePane;
    RecordPane recordPane;
    SaveFolder saveFolder = new SaveFolder();

    int widthScreen, heightScreen;
    String lastPane = "home";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        saveFolder.createSaveFolderWindows();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.widthScreen = (int) dimension.getWidth();
        this.heightScreen = (int) dimension.getHeight();

        primaryStage.setWidth(this.widthScreen * 0.8);
        primaryStage.setHeight(this.heightScreen * 0.8);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("TranslateAudioFiles");

        recordPane = new RecordPane(this, primaryStage, saveFolder.getFolderPath());
        settingsPane = new SettingsPane(this, primaryStage);
        buttonsPane = new ButtonsPane(this, primaryStage);
        wavePane = new WavePane(this.buttonsPane, primaryStage, this.widthScreen, this.heightScreen);
        emptyPane = new EmptyPane();

        decoratedPane = new DecoratedPane(this, primaryStage);
        decoratedPane.setCenter(wavePane);
        decoratedPane.setBottom(buttonsPane);

        Scene scene = new Scene(decoratedPane, primaryStage.getWidth(), primaryStage.getHeight());
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.show();
    }

    public void goToOption(Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(settingsPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(emptyPane);
    }

    public void goToHome(Stage primaryStage){
        this.lastPane = "home";
        primaryStage.getScene().setRoot(decoratedPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(wavePane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(buttonsPane);
    }

    public void goToRecord(Stage primaryStage){
        this.lastPane = "record";
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(recordPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(emptyPane);
    }

    public void goBack(Stage primaryStage){
        switch (this.lastPane){

            case "home":
                this.goToHome(primaryStage);
                break;

            case "record":
                this.goToRecord(primaryStage);
                break;

            default:
                break;
        }
    }

    public void setNewWavePane(Stage primaryStage){
        wavePane = new WavePane(this.buttonsPane, primaryStage, this.widthScreen, this.heightScreen);
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(wavePane);
    }

    public WavePane getWavePane(){
        return this.wavePane;
    }

}
