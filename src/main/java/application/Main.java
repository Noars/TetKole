package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.setTitle("TranslateAudioFiles");
        primaryStage.show();
    }
}
