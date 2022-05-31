package utils.files;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RecordVoice {

    String pathFolder;
    File wavOutputFile;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine targetLine;
    Thread audioRecordThread;
    Media audioFile;
    MediaPlayer mediaPlayer;

    public RecordVoice(String pathFolder){
        super();
        this.pathFolder = pathFolder;
    }

    public void startRecording(){

        this.deleteTempAudioFile();

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
                    wavOutputFile = new File(pathFolder + "//RecordFiles//TempAudio.wav");
                    try {
                        AudioSystem.write(recordStream, fileType, wavOutputFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        this.setupMediaPlayer();
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

    public void deleteTempAudioFile(){
        File audioTempFile = new File(pathFolder + "//RecordFiles//TempAudio.wav");
        if (audioTempFile.delete()){
            System.out.println("Temp file deleted !");
        }
    }

    public void renameTempAudioFile(String newName) {
        Path audioTempFilePath = Paths.get(pathFolder + "//RecordFiles//TempAudio.wav");
        Path audioFilePath = Paths.get(pathFolder + "//RecordFiles//" + newName + ".wav");
        try{
            Files.copy(audioTempFilePath, audioFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupMediaPlayer(){
        audioFile = new Media(new File(pathFolder + "//RecordFiles//TempAudio.wav").toURI().toString());
        mediaPlayer = new MediaPlayer(audioFile);
    }

    public void playStopMediaPlayer(String status){
        if (audioFile != null){
            switch (status){

                case "play":
                    mediaPlayer.play();
                    break;

                case "pause":
                    mediaPlayer.pause();
                    break;

                case "stop":
                    mediaPlayer.stop();
                    break;

                default:
                    break;
            }
        }
    }
}
