package utils.wave;

import application.ui.pane.WavePane;
import javafx.scene.paint.Color;

public class WaveFormPane extends ResizableCanvas {

	private final float[] defaultWave;
	private float[] waveData;
	private Color backgroundColor;
	private Color foregroundColor;
	private Color transparentForeground;
	protected int width;
	protected int height;
	private double timerXPosition = 0;
	private WavePane wavePane;
	private WaveFormService waveFormService;

	private double leftBorder;
	private double rightBorder;
	private final int sizeBorder = 10;

	public WaveFormPane(int width, int height) {
		defaultWave = new float[width];
		this.width = width;
		this.height = height;
		this.setWidth(width);
		this.setHeight(height);
		this.leftBorder = width / 3.0;
		this.rightBorder = (2 * width) / 3.0;

		for (int i = 0; i < width; i++){
			defaultWave[i] = 0.28802148f;
		}
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

	public double getTimerXPosition() {
		return timerXPosition;
	}

	public void setTimerXPosition(double timerXPosition) {
		this.timerXPosition = timerXPosition;
	}

	public void clear() {
		waveData = defaultWave;

		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, width, height);

		gc.setStroke(foregroundColor);
		gc.strokeLine(0, height / 2.0, width, height / 2.0);
	}

	public void paintWaveForm() {

		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, width, height);

		gc.setStroke(foregroundColor);
		if (waveData != null){
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
		}

		gc.setFill(Color.WHITE);
		gc.fillOval(timerXPosition, 0, 1, height);

		gc.setFill(Color.WHITE);
		gc.fillRect(this.leftBorder, 0, this.sizeBorder, height);
		gc.strokeText(this.calculTimeLeftBorder(), this.leftBorder - 60, 15, 500);
		gc.fillRect(this.rightBorder, 0, this.sizeBorder, height);
		gc.strokeText(this.calculTimeRightBorder(), this.rightBorder + 15, 15, 500);

		gc.setFill(transparentForeground);
		gc.fillRect(0, 0, this.leftBorder, height);
		gc.fillRect((this.rightBorder + this.sizeBorder), 0, width, height);

	}

	public String calculTimeLeftBorder(){
		int time = (int) (this.leftBorder / waveFormService.getRatioAudio());
		int hoursLeftBorderTime = time / 3600;
		int minutesLeftBorderTime = (time % 3600) / 60;
		int secondsLeftBorderTime = time % 60;
		return hoursLeftBorderTime + "h:" + minutesLeftBorderTime + "min:" + secondsLeftBorderTime + "s";
	}

	public String calculTimeRightBorder(){
		int time = (int) (this.rightBorder / waveFormService.getRatioAudio());
		int hoursRightBorderTime = time / 3600;
		int minutesRightBorderTime = (time % 3600) / 60;
		int secondsRightBorderTime = time % 60;
		return hoursRightBorderTime + "h:" + minutesRightBorderTime + "min:" + secondsRightBorderTime + "s";
	}

	public void setWaveVisualization(WavePane wavePane) {
		this.wavePane = wavePane;
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

	public void sendWaveService(WaveFormService waveFormService){
		this.waveFormService = waveFormService;
	}

}
