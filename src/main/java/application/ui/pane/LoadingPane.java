package application.ui.pane;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class LoadingPane extends BorderPane {

    HBox hbox;
    ProgressBar loading;
    double progress = 0.0;

    public LoadingPane(int widthScreen){
        super();

        loading = new ProgressBar(0.0);
        loading.setPrefSize((widthScreen / 2.0), 50);

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

    public void updateLoading(double value){
        this.progress += value;
        this.loading.setProgress(this.progress);
    }
}
