package application.ui.pane;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DecoratedPane extends BorderPane {

    boolean inSettings = false;

    public DecoratedPane(Main main, Stage primaryStage) {

        Button exit = new Button("Fermer");
        ImageView exitImage = new ImageView(new Image("images/close.png"));
        exit.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        exit.getStyleClass().add("topBarButton");
        exitImage.setPreserveRatio(true);
        exitImage.setFitWidth(50);
        exit.setGraphic(exitImage);
        exit.setOnAction((e) -> {
            System.exit(0);
        });

        Button minimize = new Button("Minimiser");
        ImageView minimizeImage = new ImageView(new Image("images/minimize.png"));
        minimize.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        minimize.getStyleClass().add("topBarButton");
        minimizeImage.setPreserveRatio(true);
        minimizeImage.setFitWidth(50);
        minimize.setGraphic(minimizeImage);
        minimize.setOnAction((e) -> {
            primaryStage.setIconified(true);
        });

        Button settings = new Button("Paramètre");
        ImageView settingsImage = new ImageView(new Image("images/option.png"));
        settings.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        settings.getStyleClass().add("topBarButton");
        settingsImage.setPreserveRatio(true);
        settingsImage.setFitWidth(50);
        settings.setGraphic(settingsImage);
        settings.setOnAction((e) -> {
            if (this.inSettings){
                this.inSettings = false;
                main.goBack(primaryStage);
                ((ImageView) settings.getGraphic()).setImage(new Image("images/option.png"));
            }else {
                this.inSettings = true;
                main.goToOption(primaryStage);
                ((ImageView) settings.getGraphic()).setImage(new Image("images/back.png"));
            }
        });

        HBox topBar = new HBox(settings, minimize, exit);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        BorderPane.setAlignment(topBar, Pos.CENTER_RIGHT);
        this.setTop(topBar);
        this.setStyle("-fx-background-color: #282e35;");
    }
}
