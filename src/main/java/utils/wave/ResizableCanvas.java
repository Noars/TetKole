package utils.wave;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ResizableCanvas extends Canvas {

    public final GraphicsContext gc = getGraphicsContext2D();

    @Override
    public double minHeight(double width) {
        return 1;
    }

    @Override
    public double minWidth(double height) {
        return 1;
    }

    @Override
    public double prefWidth(double width) {
        return minWidth(width);
    }

    @Override
    public double prefHeight(double width) {
        return minHeight(width);
    }

    @Override
    public double maxWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override
    public double maxHeight(double width) {
        return Double.MAX_VALUE;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
    }
}