package application.ui.pane;

import application.Main;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DecoratedPane extends BorderPane {

    private double xOffset = 0;
    private double yOffset = 0;

    boolean inSettings = false;
    boolean isReduce = false;

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

        Button reduceEnlarge = new Button("Réduire/Agrandir");
        ImageView reduceEnlargeImage = new ImageView(new Image("images/reduce.png"));
        reduceEnlarge.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        reduceEnlarge.getStyleClass().add("topBarButton");
        reduceEnlargeImage.setPreserveRatio(true);
        reduceEnlargeImage.setFitWidth(50);
        reduceEnlarge.setGraphic(reduceEnlargeImage);
        reduceEnlarge.setOnAction((e) -> {
            this.reduceEnlargeStage(primaryStage, reduceEnlarge);
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

        HBox topBar = new HBox(settings, reduceEnlarge, minimize, exit);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        BorderPane.setAlignment(topBar, Pos.CENTER_RIGHT);
        this.setTop(topBar);
        this.setStyle("-fx-background-color: #282e35;");

        topBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        topBar.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2){
                this.reduceEnlargeStage(primaryStage, reduceEnlarge);
            }
        });
        topBar.setOnMouseDragged(event -> {
            this.moveStage(event, primaryStage, reduceEnlarge);
        });
    }

    public void reduceEnlargeStage(Stage primaryStage, Button reduceEnlarge){
        if (this.isReduce){
            this.isReduce = false;
            ((ImageView) reduceEnlarge.getGraphic()).setImage(new Image("images/reduce.png"));
            primaryStage.setFullScreen(true);
        }else {
            this.isReduce = true;
            ((ImageView) reduceEnlarge.getGraphic()).setImage(new Image("images/enlarge.png"));
            primaryStage.setFullScreen(false);
        }
    }

    public void reduceStage(Stage primaryStage, Button reduceEnlarge){
        this.isReduce = true;
        ((ImageView) reduceEnlarge.getGraphic()).setImage(new Image("images/enlarge.png"));
        primaryStage.setFullScreen(false);
    }

    public void moveStage(MouseEvent event, Stage primaryStage, Button reduceEnlarge){
        double tempX, tempY;
        ObservableList<Screen> screens = Screen.getScreensForRectangle(primaryStage.getX(),primaryStage.getY(), primaryStage.getWidth(),primaryStage.getHeight());
        Rectangle2D primaryScreenBounds = screens.get(0).getVisualBounds();

        tempX = event.getScreenX() - xOffset;

        if (event.getScreenY() - yOffset < 0) {
            tempY = 0;
        } else
            tempY = Math.min(event.getScreenY() - yOffset, primaryScreenBounds.getHeight() - primaryStage.getHeight() / 3);

        this.reduceStage(primaryStage, reduceEnlarge);
        primaryStage.setX(tempX);
        primaryStage.setY(tempY);
    }
}
