package utils.save;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveFolder {

    String folderPath = "";
    String jsonPath = "";
    String recordPath = "";

    public SaveFolder(){
        super();
    }

    public void createSaveFolderWindows() {
        this.createPaths();
        try {
            Files.createDirectories(Path.of(this.folderPath));
            Files.createDirectories(Path.of(this.jsonPath));
            Files.createDirectories(Path.of(this.recordPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPaths(){
        String userName = System.getProperty("user.name");
        this.folderPath = "C://Users//" + userName + "//Documents//translateAudioFiles";
        this.jsonPath = "C://Users//" + userName + "//Documents//translateAudioFiles//JsonFiles";
        this.recordPath = "C://Users//" + userName + "//Documents//translateAudioFiles//RecordFiles";
    }

    public String getFolderPath(){
        return this.folderPath;
    }
}
