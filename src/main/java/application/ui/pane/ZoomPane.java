package application.ui.pane;

import application.Main;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ZoomPane extends BorderPane {

    WavePane waveZoomPane;
    float[] waveZoomData;
    float[] waveData;

    int leftBorder;
    int rightBorder;
    int width;
    int interval;
    int ratio;
    int rest;
    int startValue;
    int endValue;
    int index;

    Main main;

    public ZoomPane(Main main, Stage primaryStage, int width, int height){
        super();

        this.main = main;
        this.waveZoomPane = new WavePane(main.getButtonsPane(), primaryStage, width, height);

        this.setStyle("-fx-background-color: #535e65");
    }

    public void setWaveZoomData(){

        this.waveData = main.getWavePane().getWaveData();
        this.leftBorder = (int) main.getWavePane().getLeftBorder();
        this.rightBorder = (int) main.getWavePane().getRightBorder();
        this.width = (int) main.getWavePane().getWidth();
        this.interval = rightBorder - leftBorder;
        this.ratio = width / interval;
        this.rest = width % interval;
        this.waveZoomData = new float[this.width];
        this.index = 0;

        this.getStartAndStopValue();
        this.startValue();
        this.valueTab();
        this.endValue();

        this.waveZoomPane.paintZoomWaveForm(this.waveZoomData);
    }

    public void getStartAndStopValue(){
        if ((this.rest % 2) != 0){
            int surplus = this.rest % 2;
            this.startValue = this.rest / 2;
            this.endValue = (this.rest / 2) + surplus;
        }else {
            this.startValue = this.rest / 2;
            this.endValue = this.rest / 2;
        }
    }

    public void startValue(){
        for (int i=0; i<this.startValue; i++){
            this.waveZoomData[this.index] = 0;
            this.index++;
        }
    }

    public void valueTab(){
        for (int i=leftBorder; i<rightBorder; i++){
            for (int j = 0; j < this.ratio; j++){
                this.waveZoomData[index] = this.waveData[i];
                index++;
            }
        }
    }

    public void endValue(){
        for (int i=0; i<this.endValue; i++){
            this.waveZoomData[this.index] = 0;
            this.index++;
        }
    }

    public WavePane getWaveZoomPane(){
        return this.waveZoomPane;
    }
}
