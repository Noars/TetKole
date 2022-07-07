package utils.zoomWave;

import application.Main;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.progress.EncoderProgressListener;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class CutAudio {

    Main main;

    String destinationFileName = "";
    String destinationTranscodedFileName = "";

    Encoder encoder;
    ConvertProgressListener listener = new ConvertProgressListener();

    /**
     * Initialize the constructor of this class
     * And setup all path for files
     *
     * @param main
     */
    public CutAudio(Main main){
        super();
        this.main = main;
        this.setupDestinationFilesName();
    }

    /**
     * Function that create the path for the future temporary files
     */
    public void setupDestinationFilesName(){
        String path = main.getSaveFolder().getRecordPath();
        this.destinationFileName = path + "/audioFileCut.wav";
        this.destinationTranscodedFileName = path + "/transcodedFile.wav";
    }

    /**
     * Function that extract a portion of the audio file
     *
     * @param sourceFileName -> original audio file
     * @param startTime
     * @param endTime
     */
    public void cutAudio(String sourceFileName, int startTime, int endTime){

        int duration = endTime - startTime;
        int bytesPerSecond = 0;
        long framesOfAudioToCopy = 0;

        AudioInputStream inputStream = null;
        AudioInputStream exitStream = null;

        // try to extract audio but if the audio file have a wrong extension, then i transcode him in Wav
        try {
            File file = new File(sourceFileName);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat audioFormat = fileFormat.getFormat();
            inputStream = AudioSystem.getAudioInputStream(file);
            bytesPerSecond = audioFormat.getFrameSize() * (int)audioFormat.getFrameRate();
            inputStream.skip(startTime * bytesPerSecond);
            framesOfAudioToCopy = duration * (int)audioFormat.getFrameRate();
            exitStream = new AudioInputStream(inputStream, audioFormat, framesOfAudioToCopy);
            File destinationFile = new File(this.destinationFileName);
            AudioSystem.write(exitStream, fileFormat.getType(), destinationFile);
        } catch (UnsupportedAudioFileException e) {
            this.transcodeToWav(sourceFileName, startTime, endTime);
        } catch (IOException e) {
            e.printStackTrace();
        } finally { // At the end close all stream open
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (exitStream != null){
                try {
                    exitStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Transcode the audio file passed in parameter to Wav
     * The "startTime" and "endTime" passed in parameter only serve for the "cutAudio" function
     * It's a recursive call
     *
     * @param audioFile -> file we convert in wav
     * @param startTime
     * @param endTime
     */
    public void transcodeToWav(String audioFile, int startTime, int endTime) {
        try {

            File sourceFile = new File(audioFile);
            File destinationFile = new File(this.destinationTranscodedFileName);

            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("pcm_s16le");
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            EncodingAttributes attributes = new EncodingAttributes();
            attributes.setOutputFormat("wav");
            attributes.setAudioAttributes(audio);

            encoder = encoder != null ? encoder : new Encoder();
            encoder.encode(new MultimediaObject(sourceFile), destinationFile, attributes, listener);

            this.cutAudio(this.destinationTranscodedFileName, startTime, endTime);

            destinationFile.delete();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Methode needed for transcode function
     */
    public class ConvertProgressListener implements EncoderProgressListener {

        public ConvertProgressListener() {
        }

        public void message(String m) {
        }

        public void progress(int p) {
            double progress = p / 1000.00;
        }

        public void sourceInfo(MultimediaInfo m) {
        }
    }

    /**
     * Function that give the path of the audio file cut
     *
     * @return the destinationFileName variable
     */
    public String getPathAudioCut(){
        return this.destinationFileName;
    }


    /**
     * Function that delete al the temporary files created
     */
    public void deleteTempFiles(){
        File file1 = new File(this.destinationFileName);
        File file2 = new File(this.destinationTranscodedFileName);

        file1.delete();
        file2.delete();
    }
}
