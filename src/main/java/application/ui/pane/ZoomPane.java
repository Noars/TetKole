package application.ui.pane;

import application.Main;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ZoomPane extends BorderPane {

    WavePane wavePane;

    public ZoomPane(Main main, Stage primaryStage, int width, int height){
        super();

        this.wavePane = new WavePane(main.getButtonsPane(), primaryStage, width, height);

        this.setStyle("-fx-background-color: #535e65");
    }

    public WavePane getWavePane(){
        return this.wavePane;
    }
}
