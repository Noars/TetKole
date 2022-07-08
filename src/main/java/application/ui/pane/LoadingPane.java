package application.ui.pane;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class LoadingPane extends BorderPane {

    HBox hbox;
    ProgressBar loading;

    /**
     * Initialize the constructor of this class
     * Contains the loading bar when we zoom or import audio file
     *
     * @param widthScreen
     */
    public LoadingPane(int widthScreen){
        super();

        loading = new ProgressBar();
        loading.setPrefSize((widthScreen / 2.0), 50);
        loading.setProgress(0.0);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        {
            gridPane.add(loading, 0, 0);
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
     * Function that advance the loading bar according to the value passed in parameter
     *
     * @param value -> a number between 0.0 and 1.0
     */
    public void updateLoading(double value){
        this.loading.setProgress(value);
    }

    /**
     * Function that reset the loading bar
     */
    public void resetLoading(){
        this.loading.setProgress(0.0);
    }
}
