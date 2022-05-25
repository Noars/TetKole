package application.ui.pane;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import utils.wave.WaveFormPane;
import utils.wave.WaveFormService;

public class WavePane extends WaveFormPane {

	private final PaintService animationService;
	private final WaveFormService waveService;

	private boolean recalculateWaveData;
	private double stepPixel = 0;

	private boolean isLeftBorder = false;
	private boolean isRightBorder = false;

	public WavePane(int width, int height) {
		super(width, height);
		super.setWaveVisualization(this);
		waveService = new WaveFormService(this);
		animationService = new PaintService();
		super.sendWaveService(this.waveService);

		widthProperty().addListener((observable , oldValue , newValue) -> {
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
			if ((event.getX() >= super.getLeftBorder()) && (event.getX() <= (super.getLeftBorder() + super.getSizeBorder()))){
				this.isLeftBorder = true;
			}else if ((event.getX() >= super.getRightBorder()) && (event.getX() <= (super.getRightBorder() + super.getSizeBorder()))) {
				this.isRightBorder = true;
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

	public void setStep(boolean value){
		if (value){
            this.stepPixel = waveService.getRatioAudio();
        }else {
            this.stepPixel = 0.0;
        }
	}

	public PaintService getAnimationService() {
		return animationService;
	}

	public WaveFormService getWaveService() {
		return waveService;
	}

	public void startPainterService() {
		animationService.start();
	}

	public void stopPainterService() {
		animationService.stop();
		clear();
	}

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
