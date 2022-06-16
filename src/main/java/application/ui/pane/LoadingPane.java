package application.ui.pane;

import javafx.scene.layout.BorderPane;

public class LoadingPane extends BorderPane {

    public LoadingPane(){
        super();

        this.setStyle("-fx-background-image: url('images/loading.gif'); -fx-background-repeat: no-repeat; -fx-background-position: center center");
    }

}
