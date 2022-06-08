package application.ui.pane;

import application.Main;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import utils.zoomWave.ZoomWaveFormPane;

public class ZoomPane extends ZoomWaveFormPane {

    private final PaintZoomService animationService;
    private boolean isLeftBorder = false;
    private boolean isRightBorder = false;

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
    Stage primaryStage;

    public ZoomPane(Main main, Stage primaryStage, int width, int height){
        super(primaryStage, width, height);
        super.setWaveVisualization(this);
        animationService = new PaintZoomService();
        this.main = main;
        this.primaryStage = primaryStage;

        widthProperty().addListener((observable , oldValue , newValue) -> {
            this.setWaveZoomData();
            clear();
        });

        heightProperty().addListener((observable , oldValue , newValue) -> {
            this.height = Math.round(newValue.floatValue());
            clear();
        });

        setOnMouseMoved(event -> {
            if ((event.getX() >= (super.getLeftBorder())) && (event.getX() <= (super.getLeftBorder() + super.getSizeBorder()))){
                this.isLeftBorder = true;
            }else if ((event.getX() >= super.getRightBorder()) && (event.getX() <= (super.getRightBorder() + super.getSizeBorder()))) {
                this.isRightBorder = true;
            }else {
                this.isLeftBorder = false;
                this.isRightBorder = false;
            }
        });
        setOnMouseDragged(event -> {
            if (this.isLeftBorder){
                super.setLeftBorder(event.getX() - (super.getSizeBorder() / 2.0));
            }else if (this.isRightBorder) {
                super.setRightBorder(event.getX() - (super.getSizeBorder() / 2.0));
            }
        });
        setOnMouseDragReleased(event -> {
            this.isLeftBorder = false;
            this.isRightBorder = false;
        });
        setOnMouseReleased(event -> {
            this.isLeftBorder = false;
            this.isRightBorder = false;
        });
        setOnMouseExited(event -> {
            this.isLeftBorder = false;
            this.isRightBorder = false;
        });
    }

    public void setWaveZoomData(){

        this.waveData = main.getWavePane().getWaveData();
        this.leftBorder = (int) main.getWavePane().getLeftBorder();
        this.rightBorder = (int) main.getWavePane().getRightBorder();
        this.width = (int) primaryStage.getWidth();
        this.interval = rightBorder - leftBorder;
        this.ratio = width / interval;
        this.rest = width % interval;
        this.waveZoomData = new float[this.width];
        this.index = 0;

        this.getStartAndStopValue();
        this.startValue();
        this.valueTab();
        this.endValue();

        this.stopPainterService();
        this.startPainterService();
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

    public void startPainterService() {
        animationService.start();
    }

    public void stopPainterService() {
        animationService.stop();
        clear();
    }

    public class PaintZoomService extends AnimationTimer {

        private volatile SimpleBooleanProperty running = new SimpleBooleanProperty(false);

        @Override
        public void start() {
            if (width <= 0 || height <= 0)
                width = height = 1;

            super.start();
            running.set(true);
        }

        @Override
        public void handle(long nanos) {

            paintZoomWaveForm(waveZoomData);
        }

        @Override
        public void stop() {
            super.stop();
            running.set(false);
        }

    }
}
