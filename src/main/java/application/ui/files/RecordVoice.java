package application.ui.files;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RecordVoice {

    String pathFolder;
    File wavOutputFile;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine targetLine;
    Thread audioRecordThread;

    public RecordVoice(String pathFolder){
        super();
        this.pathFolder = pathFolder;
    }

    public void startRecording(){

        try {
            AudioFormat audioFormat = getAudioFormat();
            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

            if (!AudioSystem.isLineSupported(dataInfo)){
                System.out.println("Not supported");
            }else {
                targetLine = (TargetDataLine)AudioSystem.getLine(dataInfo);
                targetLine.open(audioFormat);
                targetLine.start();

                audioRecordThread = new Thread(() -> {
                    AudioInputStream recordStream = new AudioInputStream(targetLine);
                    wavOutputFile = new File(pathFolder + "//RecordFiles//TestAudio.wav");
                    try {
                        AudioSystem.write(recordStream, fileType, wavOutputFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                audioRecordThread.start();
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording(){
        audioRecordThread.stop();
        targetLine.stop();
        targetLine.close();
    }

    public AudioFormat getAudioFormat(){
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
