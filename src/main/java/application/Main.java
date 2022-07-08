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
import utils.zoomWave.CutAudio;

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
    ListenPane listenPane;
    CutAudio cutAudio;

    int widthScreen, heightScreen;
    String lastPane = "home";
    String os = "";

    /**
     * Launch the application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main function of this application
     * Create all the instance
     * Create the scene
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        this.os = System.getProperty("os.name").toLowerCase();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.widthScreen = (int) dimension.getWidth();
        this.heightScreen = (int) dimension.getHeight();

        primaryStage.setWidth(this.widthScreen);
        primaryStage.setHeight(this.heightScreen);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("TètKole");

        saveFolder = new SaveFolder(this.os);
        cutAudio = new CutAudio(this);
        settingsPane = new SettingsPane(this);
        buttonsPane = new ButtonsPane(this, primaryStage);
        buttonsZoomPane = new ButtonsZoomPane(this, primaryStage);
        wavePane = new WavePane(this, this.buttonsPane, primaryStage, this.widthScreen, this.heightScreen);
        zoomPane = new ZoomPane(this, this.buttonsZoomPane, primaryStage, this.widthScreen, this.heightScreen);
        recordPane = new RecordPane(this, primaryStage, saveFolder.getFolderPath(), settingsPane.getLanguage());
        loadingPane = new LoadingPane(this.widthScreen);
        listenPane = new ListenPane(this, primaryStage, settingsPane.getLanguage());
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

    /**
     * Function that permit going on the settings page
     *
     * @param primaryStage
     */
    public void goToOption(Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(settingsPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(emptyPane);
    }

    /**
     * Function that permit going on the home page
     *
     * @param primaryStage
     */
    public void goToHome(Stage primaryStage){
        this.lastPane = "home";
        primaryStage.getScene().setRoot(decoratedPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(wavePane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(buttonsPane);
    }

    /**
     * Function that permit going on the record page
     *
     * @param primaryStage
     */
    public void goToRecord(Stage primaryStage){
        this.lastPane = "record";
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(recordPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(emptyPane);
    }

    /**
     * Function that permit going on the zoom page
     *
     * @param primaryStage
     */
    public void goToZoom(Stage primaryStage){
        this.lastPane = "zoom";
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(zoomPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(buttonsZoomPane);
    }

    /**
     * Function that permit going on the listen page
     *
     * @param primaryStage
     */
    public void goToListen(Stage primaryStage){
        this.lastPane = "listen";
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(listenPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(emptyPane);
    }

    /**
     * Function that permit to come back to the previous page
     *
     * @param primaryStage
     */
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

            case "listen":
                this.goToListen(primaryStage);
                break;

            default:
                break;
        }
    }

    /**
     * Function that permit going on the wave page
     *
     * @param primaryStage
     */
    public void setNewWavePane(Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(wavePane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(buttonsPane);
    }

    /**
     * Function that permit going on the zoom page
     *
     * @param primaryStage
     */
    public void setNewZoomWavePane(Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(zoomPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(buttonsZoomPane);
    }

    /**
     * Function that create a new instance of wave page
     *
     * @param main
     * @param primaryStage
     */
    public void setLoadingPane(Main main, Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(loadingPane);
        wavePane = new WavePane(main, this.buttonsPane, primaryStage, this.widthScreen, this.heightScreen);
    }

    /**
     * Function that create a new instance of zoom page
     *
     * @param main
     * @param primaryStage
     */
    public void setLoadingZoomPane(Main main, Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(loadingPane);
        zoomPane = new ZoomPane(main, this.buttonsZoomPane, primaryStage, this.widthScreen, this.heightScreen);
    }

    // ------------------- All the getter -------------------------------- //

    public WavePane getWavePane(){
        return this.wavePane;
    }

    public ZoomPane getZoomPane(){
        return this.zoomPane;
    }

    public RecordPane getRecordPane(){
        return this.recordPane;
    }

    public ButtonsPane getButtonsPane(){
        return this.buttonsPane;
    }

    public ButtonsZoomPane getButtonsZoomPane(){
        return this.buttonsZoomPane;
    }

    public SaveFolder getSaveFolder(){
        return this.saveFolder;
    }

    public ListenPane getListenPane(){
        return this.listenPane;
    }

    public LoadingPane getLoadingPane(){
        return this.loadingPane;
    }

    public CutAudio getCutAudio(){
        return this.cutAudio;
    }

    public String getOs(){
        return this.os;
    }
}
