package application;

import application.ui.pane.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.awt.*;

public class Main extends Application {

    @Getter
    DecoratedPane decoratedPane;
    @Getter
    MainPane mainPane;
    @Getter
    ButtonsPane buttonsPane;
    @Getter
    OptionsPane optionsPane;
    @Getter
    EmptyPane emptyPane;

    int widthScreen, heightScreen;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.widthScreen = (int) dimension.getWidth();
        this.heightScreen = (int) dimension.getHeight();

        primaryStage.setWidth(this.widthScreen / 1.5);
        primaryStage.setHeight(this.heightScreen / 1.5);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("TranslateAudioFiles");

        optionsPane = new OptionsPane(this, primaryStage);
        mainPane = new MainPane(this, this.widthScreen);
        buttonsPane = new ButtonsPane(this, this.widthScreen);

        decoratedPane = new DecoratedPane(this, primaryStage);
        decoratedPane.setCenter(mainPane);
        decoratedPane.setBottom(buttonsPane);

        Scene scene = new Scene(decoratedPane, primaryStage.getWidth(), primaryStage.getHeight());
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.show();
    }

    public void goToOption(Stage primaryStage){
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(optionsPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(emptyPane);
    }

    public void goToMain(Stage primaryStage){
        primaryStage.getScene().setRoot(decoratedPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(mainPane);
        ((BorderPane) primaryStage.getScene().getRoot()).setBottom(buttonsPane);
    }
}
