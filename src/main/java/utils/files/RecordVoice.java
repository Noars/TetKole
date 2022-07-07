package utils.files;

import application.Main;
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
    Main main;

    Path audioTempFilePath;
    Path audioFilePath;

    /**
     * Initialize the constructor of this class
     *
     * @param main -> the main class
     * @param pathFolder -> the path to the "TètKole" folder according to the user's operating system
     */
    public RecordVoice(Main main, String pathFolder){
        super();
        this.main = main;
        this.pathFolder = pathFolder;
    }

    /**
     * Function that record our voice
     */
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
                    this.getWavOutputFile();
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

    /**
     * Function that create the temporary record file according to the user's operating system
     * The name of this temporary file is "TempAudio.wav"
     */
    public void getWavOutputFile(){
        if (this.main.getOs().contains("nux") || this.main.getOs().contains("mac")){
            wavOutputFile = new File(pathFolder + "/RecordFiles/TempAudio.wav");
        }else{
            wavOutputFile = new File(pathFolder + "\\RecordFiles\\TempAudio.wav");
        }
    }

    /**
     * Function that stop the record of our voice
     */
    public void stopRecording(){
        audioRecordThread.stop();
        targetLine.stop();
        targetLine.close();
    }

    /**
     * Function that set the format of our audio recorded file
     *
     * @return format for our audio recorded file
     */
    public AudioFormat getAudioFormat(){
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    /**
     * Function that delete the temporary audio file
     */
    public void deleteTempAudioFile(){
        File audioTempFile;
        if (this.main.getOs().contains("nux") || this.main.getOs().contains("mac")){
            audioTempFile = new File(pathFolder + "/RecordFiles/TempAudio.wav");
        }else {
            audioTempFile = new File(pathFolder + "\\RecordFiles\\TempAudio.wav");
        }
        if (audioTempFile.delete()){
            System.out.println("Temp file deleted !");
        }
    }

    /**
     * This function :
     * - Copy the temporary recorded file
     * - Rename the copied file with the name given by the user
     *
     * @param newName -> name given by the user
     */
    public void renameTempAudioFile(String newName) {
        if (this.main.getOs().contains("nux") || this.main.getOs().contains("mac")){
            audioTempFilePath = Paths.get(pathFolder + "/RecordFiles/TempAudio.wav");
            audioFilePath = Paths.get(pathFolder + "/RecordFiles/" + newName + ".wav");
        }else {
            audioTempFilePath = Paths.get(pathFolder + "\\RecordFiles\\TempAudio.wav");
            audioFilePath = Paths.get(pathFolder + "\\RecordFiles\\" + newName + ".wav");
        }
        try{
            Files.copy(audioTempFilePath, audioFilePath, StandardCopyOption.REPLACE_EXISTING);
            this.deleteTempFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that delete all temporary file created here
     */
    public void deleteTempFiles(){
        File audioTempFile = new File(String.valueOf(audioTempFilePath));
        audioTempFile.delete();
    }

    /**
     * Function that set the Media Player with the created recorded file
     */
    public void setupMediaPlayer(){
        if (this.main.getOs().contains("nux") || this.main.getOs().contains("mac")){
            audioFile = new Media(new File(pathFolder + "/RecordFiles/TempAudio.wav").toURI().toString());
        }else {
            audioFile = new Media(new File(pathFolder + "\\RecordFiles\\TempAudio.wav").toURI().toString());
        }
        mediaPlayer = new MediaPlayer(audioFile);
    }

    /**
     * Function that manage the Media Player
     *
     * @param status -> the order to give to the Media Player
     */
    public void playStopMediaPlayer(String status){
        if (audioFile != null){
            switch (status){

                case "play":
                    mediaPlayer.play();
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
