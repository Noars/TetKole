package application.ui.pane;

import application.Main;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import utils.zoomWave.ZoomWaveFormPane;
import utils.zoomWave.ZoomWaveFormService;

public class ZoomPane extends ZoomWaveFormPane {

	Main main;
	Stage primaryStage;

	private final PaintZoomService animationZoomService;
	private final ZoomWaveFormService zoomWaveService;

	private boolean recalculateWaveZoomData;
	private double stepPixel = 0;

	private boolean isLeftBorder = false;
	private boolean isRightBorder = false;

	public ZoomPane(Main main, ButtonsPane buttonsPane, Stage primaryStage, int width, int height) {
		super(buttonsPane, primaryStage, width, height);
		super.setWaveZoomVisualization(this);
		this.main = main;
		this.primaryStage = primaryStage;
		zoomWaveService = new ZoomWaveFormService(this, main, primaryStage);
		animationZoomService = new PaintZoomService();
		super.sendMain(main);
		super.sendWaveZoomService(this.zoomWaveService);

		widthProperty().addListener((observable , oldValue , newValue) -> {
			super.resetBorders();
			this.width = Math.round(newValue.floatValue());
			recalculateWaveZoomData = true;
			clear();
		});

		heightProperty().addListener((observable , oldValue , newValue) -> {
			this.height = Math.round(newValue.floatValue());
			recalculateWaveZoomData = true;
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
			zoomWaveService.playStopMediaPlayer("stop");
			if (this.isLeftBorder){
				super.setLeftBorder(event.getX() - (super.getSizeBorder() / 2.0));
			}else if (this.isRightBorder) {
				super.setRightBorder(event.getX() - (super.getSizeBorder() / 2.0));
			}
			zoomWaveService.startTimeMediaPlayer(super.getCurrentTime());
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

	public void setStep(boolean value){
		if (value){
            this.stepPixel = zoomWaveService.getRatioAudio();
        }else {
            this.stepPixel = 0.0;
        }
	}

	public ZoomWaveFormService getWaveZoomService() {
		return zoomWaveService;
	}

	public PaintZoomService getAnimationService() {
		return animationZoomService;
	}

	public void startPainterService() {
		animationZoomService.start();
		main.setNewZoomWavePane(primaryStage);
		main.getButtonsPane().enableButton();
		main.getLoadingPane().resetLoading();
	}

	public void stopPainterService() {
		animationZoomService.stop();
		clear();
	}

	public class PaintZoomService extends AnimationTimer {

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

			if (getWaveZoomService().getResultingWaveform() == null || recalculateWaveZoomData) {

				getWaveZoomService().startService(getWaveZoomService().getFileAbsolutePath(), ZoomWaveFormService.WaveZoomFormJob.WAVEFORM);
				recalculateWaveZoomData = false;

				return;
			}

			paintWaveZoomForm();
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
