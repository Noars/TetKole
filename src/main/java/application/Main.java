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

public class Main extends Application {

    DecoratedPane decoratedPane;
    ButtonsPane buttonsPane;
    SettingsPane settingsPane;
    EmptyPane emptyPane;
    WavePane wavePane;
    RecordPane recordPane;
    ZoomPane zoomPane;
    ButtonsZoomPane buttonsZoomPane;
    SaveFolder saveFolder;
    LoadingPane loadingPane;

    int widthScreen, heightScreen;
    String lastPane = "home";
    String os = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.os = System.getProperty("os.name").toLowerCase();

        saveFolder = new SaveFolder(this.os);
        saveFolder.createSaveFolderWindows();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.widthScreen = (int) dimension.getWidth();
        this.heightScreen = (int) dimension.getHeight();

        primaryStage.setWidth(this.widthScreen);
        primaryStage.setHeight(this.heightScreen);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("TètKole");

        settingsPane = new SettingsPane();
        buttonsPane = new ButtonsPane(this, primaryStage);
        wavePane = new WavePane(this, this.buttonsPane, primaryStage, this.widthScreen, this.heightScreen);
        zoomPane = new ZoomPane(this, primaryStage, this.widthScreen, this.heightScreen);
        recordPane = new RecordPane(this, primaryStage, saveFolder.getFolderPath());
        buttonsZoomPane = new ButtonsZoomPane(this, primaryStage);
        loadingPane = new LoadingPane();
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

    public void goToZoom(Stage primaryStage){
        this.lastPane = "zoom";
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(zoomPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(buttonsZoomPane);
    }

    public void goBack(Stage primaryStage){
        switch (this.lastPane){

            case "home":
                this.goToHome(primaryStage);
                break;

            case "record":
                this.goToRecord(primaryStage);
                break;

            case "zoom":
                this.goToZoom(primaryStage);
                break;

            default:
                break;
        }
    }

    public void setNewWavePane(Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(wavePane);
    }

    public void setNewZoomWavePane(Stage primaryStage){
        zoomPane = new ZoomPane(this, primaryStage, this.widthScreen, this.heightScreen);
    }

    public void setLoadingPane(Main main, Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(loadingPane);
        wavePane = new WavePane(main, this.buttonsPane, primaryStage, this.widthScreen, this.heightScreen);
    }

    public WavePane getWavePane(){
        return this.wavePane;
    }

    public RecordPane getRecordPane(){
        return this.recordPane;
    }

    public ButtonsPane getButtonsPane(){
        return this.buttonsPane;
    }

    public ZoomPane getZoomPane(){
        return this.zoomPane;
    }

    public String getOs(){
        return this.os;
    }
}
