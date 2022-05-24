package application.ui.pane;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import utils.wave.WaveFormPane;
import utils.wave.WaveFormService;

public class WavePane extends WaveFormPane {

	private final PaintService animationService;
	private final WaveFormService waveService;

	private boolean recalculateWaveData;
	private int step = 0;

	public WavePane(int width, int height) {
		super(width, height);
		super.setWaveVisualization(this);
		waveService = new WaveFormService(this);
		animationService = new PaintService();

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

		setOnMouseMoved(m -> this.setMouseXPosition((int) m.getX()));
		setOnMouseDragged(m -> this.setMouseXPosition((int) m.getX()));
		setOnMouseExited(m -> this.setMouseXPosition(-1));

	}

	public int timeAudio(){
		return (int) (getTimerXPosition() * waveService.getRatioAudio());
	}

	public void setStep(int value){
		this.step = value;
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

			if (nanos >= previousNanos + 100000 * 1000) {
				previousNanos = nanos;
				setTimerXPosition(getTimerXPosition() + step);
				setTimerAudio(timeAudio());
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
