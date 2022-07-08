package application.ui.pane;

import application.Main;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import utils.wave.WaveFormPane;
import utils.wave.WaveFormService;

public class WavePane extends WaveFormPane {

	Main main;
	Stage primaryStage;

	private final PaintService animationService;
	private final WaveFormService waveService;

	private boolean recalculateWaveData;
	private double stepPixel = 0;

	private boolean isLeftBorder = false;
	private boolean isRightBorder = false;

	/**
	 * Initialize the constructor of this class
	 * And create all the event for the mouse
	 *
	 * @param main
	 * @param buttonsPane
	 * @param primaryStage
	 * @param width
	 * @param height
	 */
	public WavePane(Main main, ButtonsPane buttonsPane, Stage primaryStage, int width, int height) {
		super(buttonsPane, primaryStage, width, height);
		super.setWaveVisualization(this);
		this.main = main;
		this.primaryStage = primaryStage;
		waveService = new WaveFormService(this, main, primaryStage);
		animationService = new PaintService();
		super.sendWaveService(this.waveService);

		widthProperty().addListener((observable , oldValue , newValue) -> {
			super.resetBorders();
			this.width = Math.round(newValue.floatValue());
			recalculateWaveData = true;
			clear();
		});

		heightProperty().addListener((observable , oldValue , newValue) -> {
			this.height = Math.round(newValue.floatValue());
			recalculateWaveData = true;
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
			buttonsPane.stopMusic();
			waveService.playStopMediaPlayer("stop");
			if (this.isLeftBorder){
				super.setLeftBorder(event.getX() - (super.getSizeBorder() / 2.0));
			}else if (this.isRightBorder) {
				super.setRightBorder(event.getX() - (super.getSizeBorder() / 2.0));
			}
			waveService.startTimeMediaPlayer(super.getCurrentTime());
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

	/**
	 * Function that move the cursor of the music when playing
	 *
	 * @param value
	 */
	public void setStep(boolean value){
		if (value){
            this.stepPixel = waveService.getRatioAudio();
        }else {
            this.stepPixel = 0.0;
        }
	}

	/**
	 * Function that give the instance of "waveService"
	 *
	 * @return "waveService" instance
	 */
	public WaveFormService getWaveService() {
		return waveService;
	}

	/**
	 * Function that give the instance of "animationService"
	 *
	 * @return "animationService" instance
	 */
	public PaintService getAnimationService() {
		return animationService;
	}

	/**
	 * Function that start the painter service
	 */
	public void startPainterService() {
		animationService.start();
		main.setNewWavePane(primaryStage);
		main.getButtonsPane().enableButton();
		main.getLoadingPane().resetLoading();
	}

	/**
	 * Function that stop the painter service
	 */
	public void stopPainterService() {
		animationService.stop();
		clear();
	}

	/**
	 * Class for the paint the wave form
	 */
	public class PaintService extends AnimationTimer {

		private volatile SimpleBooleanProperty running = new SimpleBooleanProperty(false);
		private long previousNanos = 0;

		@Override
		public void start() {
			if (width <= 0 || height <= 0)
				width = height = 1;

			super.start();
			running.set(true);
		}

		@Override
		public void handle(long nanos) {

			if (nanos >= (previousNanos + 999999999)) {
				previousNanos = nanos;
				setTimerXPosition(getTimerXPosition() + stepPixel);
			}

			if (getWaveService().getResultingWaveform() == null || recalculateWaveData) {

				getWaveService().startService(getWaveService().getFileAbsolutePath(), WaveFormService.WaveFormJob.WAVEFORM);
				recalculateWaveData = false;

				return;
			}

			paintWaveForm();
		}

		@Override
		public void stop() {
			super.stop();
			running.set(false);
		}

		public boolean isRunning() {
			return running.get();
		}

	}

}
