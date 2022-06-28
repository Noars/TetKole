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

    public CutAudio(Main main){
        super();
        this.main = main;
        this.setupDestinationFilesName();
    }

    public void setupDestinationFilesName(){
        String path = main.getSaveFolder().getRecordPath();
        this.destinationFileName = path + "/audioFileCut.wav";
        this.destinationTranscodedFileName = path + "/transcodedFile.wav";
    }

    public void cutAudio(String sourceFileName, int startTime, int endTime){

        int duration = endTime - startTime;
        int bytesPerSecond = 0;
        long framesOfAudioToCopy = 0;

        AudioInputStream inputStream = null;
        AudioInputStream exitStream = null;

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
        } finally {
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
}
