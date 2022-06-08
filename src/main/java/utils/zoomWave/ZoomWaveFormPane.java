package utils.zoomWave;

import application.ui.pane.ZoomPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ZoomWaveFormPane extends ResizableZoomCanvas{

    private final Color backgroundColor;
    private Color foregroundColor;
    private Color transparentForeground;
    private ZoomPane zoomPane;
    private Stage primaryStage;
    protected int width;
    protected int height;

    private double leftBorder;
    private double rightBorder;
    private final int sizeBorder = 5;

    public ZoomWaveFormPane(Stage primaryStage, int width, int height) {
        this.primaryStage = primaryStage;
        this.width = width;
        this.height = height;
        this.setWidth(width);
        this.setHeight(height);
        this.leftBorder = primaryStage.getWidth() / 3.0;
        this.rightBorder = (2 * primaryStage.getWidth()) / 3.0;

        backgroundColor = Color.web("#252525");
        setForeground(Color.ORANGE);
    }

    public void setForeground(Color color) {
        this.foregroundColor = color;
        transparentForeground = Color.rgb((int) ( foregroundColor.getRed() * 255 ), (int) ( foregroundColor.getGreen() * 255 ), (int) ( foregroundColor.getBlue() * 255 ), 0.3);
    }

    public void clear() {

        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, width, height);

        gc.setStroke(foregroundColor);
        gc.strokeLine(0, height / 2.0, width, height / 2.0);
    }

    public void paintZoomWaveForm(float[] zoomValue) {

        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, width, height);

        gc.setStroke(foregroundColor);
        if (zoomValue != null){
            for (int i = 0; i < zoomValue.length; i++) {
                int value = (int) ( zoomValue[i] * height );
                int y1 = ( height - 2 * value ) / 2;
                int y2 = y1 + 2 * value;
                gc.strokeLine(i, y1, i, y2);
            }
        }

        gc.setFill(Color.WHITE);
        gc.fillRect(this.leftBorder, 0, this.sizeBorder, height);
        gc.fillRect(this.rightBorder, 0, this.sizeBorder, height);

        gc.setFill(transparentForeground);
        gc.fillRect(0, 0, this.leftBorder, height);
        gc.fillRect((this.rightBorder + this.sizeBorder), 0, width, height);
    }

    public double getLeftBorder(){
        return this.leftBorder;
    }

    public void setLeftBorder(double value){
        if (value < (this.rightBorder - this.sizeBorder - 1)){
            this.leftBorder = value;
        }
    }

    public double getRightBorder(){
        return this.rightBorder;
    }

    public void setRightBorder(double value){
        if (value > (this.leftBorder + this.sizeBorder)){
            this.rightBorder = value;
        }
    }

    public int getSizeBorder(){
        return this.sizeBorder;
    }

    public void setWaveVisualization(ZoomPane zoomPane) {
        this.zoomPane = zoomPane;
    }
}
