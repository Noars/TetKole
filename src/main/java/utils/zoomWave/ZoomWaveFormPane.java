package utils.zoomWave;

import application.Main;
import application.ui.pane.ButtonsPane;
import application.ui.pane.ButtonsZoomPane;
import application.ui.pane.ZoomPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ZoomWaveFormPane extends ResizableZoomCanvas {

	private final float[] defaultZoomWave;
	private float[] waveZoomData;
	private Color backgroundColor;
	private Color foregroundColor;
	private Color transparentForeground;
	protected int width;
	protected int height;
	private double timerXPosition;
	private Main main;
	private ZoomPane wavePane;
	private ZoomWaveFormService zoomWaveFormService;
	private ButtonsZoomPane buttonsZoomPane;
	private Stage primaryStage;

	private double leftBorder;
	private double rightBorder;
	private final int sizeBorder = 10;

	public ZoomWaveFormPane(ButtonsZoomPane buttonsZoomPane, Stage primaryStage, int width, int height) {
		this.defaultZoomWave = new float[width];
		this.buttonsZoomPane = buttonsZoomPane;
		this.primaryStage = primaryStage;
		this.width = width;
		this.height = height;
		this.setWidth(width);
		this.setHeight(height);
		this.leftBorder = primaryStage.getWidth() / 3.0;
		this.rightBorder = (2 * primaryStage.getWidth()) / 3.0;
		this.timerXPosition = this.leftBorder;

		for (int i = 0; i < width; i++){
			defaultZoomWave[i] = 0.28802148f;
		}
		waveZoomData = defaultZoomWave;

		backgroundColor = Color.web("#252525");
		setForeground(Color.ORANGE);
	}

	public void setForeground(Color color) {
		this.foregroundColor = color;
		transparentForeground = Color.rgb((int) ( foregroundColor.getRed() * 255 ), (int) ( foregroundColor.getGreen() * 255 ), (int) ( foregroundColor.getBlue() * 255 ), 0.3);
	}

	public void setupMediaPlayer(){
		zoomWaveFormService.startTimeMediaPlayer(this.getCurrentTime());
	}

	public double getTimerXPosition() {
		return timerXPosition;
	}

	public void setTimerXPosition(double timerXPosition) {
		this.timerXPosition = timerXPosition;
		if (this.timerXPosition > this.rightBorder){
			this.timerXPosition = this.leftBorder;
			this.buttonsZoomPane.stopMusic();
			this.zoomWaveFormService.playStopMediaPlayer("stop");
			this.zoomWaveFormService.startTimeMediaPlayer(this.getCurrentTime());
		}
	}

	public void clear() {
		waveZoomData = defaultZoomWave;

		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, width, height);

		gc.setStroke(foregroundColor);
		gc.strokeLine(0, height / 2.0, width, height / 2.0);
	}

	public void paintWaveZoomForm() {

		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, width, height);

		gc.setStroke(foregroundColor);
		if (waveZoomData != null){
			for (int i = 0; i < waveZoomData.length; i++) {
				if (!wavePane.getAnimationService().isRunning()) {
					clear();
					break;
				}
				int value = (int) ( waveZoomData[i] * height );
				int y1 = ( height - 2 * value ) / 2;
				int y2 = y1 + 2 * value;
				gc.strokeLine(i, y1, i, y2);
			}
		}

		gc.setFill(Color.WHITE);
		gc.fillRect(this.leftBorder, 0, this.sizeBorder, height);
		gc.strokeText(this.calculTimeLeftBorder(), this.posLeftStrokeText(), this.adjustPosYStrokeText(), 500);
		gc.fillRect(this.rightBorder, 0, this.sizeBorder, height);
		gc.strokeText(this.calculTimeRightBorder(), this.posRightStrokeText(), 15, 500);

		gc.setFill(Color.RED);
		gc.fillOval(timerXPosition, 0, 1, height);

		gc.setFill(transparentForeground);
		gc.fillRect(0, 0, this.leftBorder, height);
		gc.fillRect((this.rightBorder + this.sizeBorder), 0, width, height);

	}

	public double posLeftStrokeText(){
		if (this.leftBorder < (primaryStage.getWidth() / 4)){
			return this.leftBorder + 5;
		}else {
			return this.leftBorder - 110;
		}
	}

	public double posRightStrokeText(){
		if (this.rightBorder > ((primaryStage.getWidth() / 4) * 3)){
			return this.rightBorder - 110;
		}else {
			return this.rightBorder + 25;
		}
	}

	public int adjustPosYStrokeText(){
		if ((this.rightBorder - this.leftBorder) < 100.0){
			return 30;
		}else {
			return 15;
		}
	}

	public String calculTimeLeftBorder(){
		int time = (int) (this.leftBorder / zoomWaveFormService.getRatioAudio()) + main.getWavePane().getStartTimeChoose();
		double milliTime = Math.round((this.leftBorder / zoomWaveFormService.getRatioAudio()) * 100.0) / 100.0;

		int hoursLeftBorderTime = time / 3600;
		int minutesLeftBorderTime = (time % 3600) / 60;
		int secondsLeftBorderTime = time % 60;
		String milliSeconds = String.valueOf(milliTime);
		String onlyMilliSeconds = milliSeconds.substring(milliSeconds.indexOf(".")).substring(1);

		return hoursLeftBorderTime + "h:" + minutesLeftBorderTime + "min:" + secondsLeftBorderTime + "s:" + onlyMilliSeconds + "ms";
	}

	public String calculTimeRightBorder(){
		int time = (int) (this.rightBorder / zoomWaveFormService.getRatioAudio()) + main.getWavePane().getStartTimeChoose();
		double milliTime = Math.round((this.rightBorder / zoomWaveFormService.getRatioAudio()) * 100.0) / 100.0;

		int hoursRightBorderTime = time / 3600;
		int minutesRightBorderTime = (time % 3600) / 60;
		int secondsRightBorderTime = time % 60;
		String milliSeconds = String.valueOf(milliTime);
		String onlyMilliSeconds = milliSeconds.substring(milliSeconds.indexOf(".")).substring(1);

		return hoursRightBorderTime + "h:" + minutesRightBorderTime + "min:" + secondsRightBorderTime + "s:" + onlyMilliSeconds + "ms";
	}

	public double getCurrentTime(){
		return (Math.round((this.leftBorder / zoomWaveFormService.getRatioAudio()) * 100.0) / 100.0);
	}

	public void resetBorders(){
		this.leftBorder = primaryStage.getWidth() / 3.0;
		this.rightBorder = (2 * primaryStage.getWidth()) / 3.0;
		this.timerXPosition = this.leftBorder;
	}

	public void setWaveZoomVisualization(ZoomPane wavePane) {
		this.wavePane = wavePane;
	}

	public double getLeftBorder(){
		return this.leftBorder;
	}

	public void setLeftBorder(double value){
		if (value < (this.rightBorder - this.sizeBorder - 1)){
			this.leftBorder = value;
			this.timerXPosition = value;
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

	public void sendMain(Main main){
		this.main = main;
	}

	public void sendWaveZoomService(ZoomWaveFormService waveFormService){
		this.zoomWaveFormService = waveFormService;
	}

	public void setWaveZoomData(float[] waveData) {
		this.waveZoomData = waveData;
	}
}
