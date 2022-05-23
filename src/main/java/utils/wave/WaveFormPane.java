package utils.wave;

import application.ui.pane.WavePane;
import javafx.scene.paint.Color;

public class WaveFormPane extends ResizableCanvas {

	private final float[] defaultWave;
	private float[] waveData;
	private Color backgroundColor;
	private Color foregroundColor;
	private Color transparentForeground;
	private Color mouseXColor = Color.rgb(255, 255, 255, 0.7);
	protected int width;
	protected int height;
	private int timerXPosition = 0;
	private int mouseXPosition = -1;
	private WavePane wavePane;

	public WaveFormPane(int width, int height) {
		defaultWave = new float[width];
		this.width = width;
		this.height = height;
		this.setWidth(width);
		this.setHeight(height);

		for (int i = 0; i < width; i++)
			defaultWave[i] = 0.28802148f;
		waveData = defaultWave;

		backgroundColor = Color.web("#252525");
		setForeground(Color.ORANGE);

	}

	public void setWaveData(float[] waveData) {
		this.waveData = waveData;
	}

	public void setForeground(Color color) {
		this.foregroundColor = color;
		transparentForeground = Color.rgb((int) ( foregroundColor.getRed() * 255 ), (int) ( foregroundColor.getGreen() * 255 ), (int) ( foregroundColor.getBlue() * 255 ), 0.3);
	}

	public int getTimerXPosition() {
		return timerXPosition;
	}

	public void setTimerXPosition(int timerXPosition) {
		this.timerXPosition = timerXPosition;
	}

	public void setMouseXPosition(int mouseXPosition) {
		this.mouseXPosition = mouseXPosition;
	}

	public void clear() {
		waveData = defaultWave;

		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, width, height);

		gc.setStroke(foregroundColor);
		gc.strokeLine(0, height / 2, width, height / 2);
	}

	public void paintWaveForm() {

		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, width, height);

		gc.setStroke(foregroundColor);
		if (waveData != null)
			for (int i = 0; i < waveData.length; i++) {
				if (!wavePane.getAnimationService().isRunning()) {
					clear();
					break;
				}
				int value = (int) ( waveData[i] * height );
				int y1 = ( height - 2 * value ) / 2;
				int y2 = y1 + 2 * value;
				gc.strokeLine(i, y1, i, y2);
			}

		gc.setFill(transparentForeground);
		gc.fillRect(0, 0, timerXPosition, height);

		gc.setFill(Color.WHITE);
		gc.fillOval(timerXPosition, 0, 1, height);

		if (mouseXPosition != -1) {
			gc.setFill(mouseXColor);
			gc.fillRect(mouseXPosition, 0, 3, height);
		}
	}

	public void setWaveVisualization(WavePane wavePane) {
		this.wavePane = wavePane;
	}

}
