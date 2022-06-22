package utils.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveFolder {

    String os = "";

    String folderPath = "";
    String jsonPath = "";
    String recordPath = "";

    public SaveFolder(String os){
        super();
        this.os = os;
        this.createSaveFolder();
    }

    public void createSaveFolder() {
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
        if (this.os.contains("nux") || this.os.contains("mac")){
            this.folderPath = "/home/" + userName + "/TètKole";
            this.jsonPath = "/home/" + userName + "/TètKole/JsonFiles";
            this.recordPath = "/home/" + userName + "/TètKole/RecordFiles";
        }else {
            this.folderPath = "C:\\Users\\" + userName + "\\Documents\\TètKole";
            this.jsonPath = "C:\\Users\\" + userName + "\\Documents\\TètKole\\JsonFiles";
            this.recordPath = "C:\\Users\\" + userName + "\\Documents\\TètKole\\RecordFiles";
        }
    }

    public String getFolderPath(){
        return this.folderPath;
    }

    public String getJsonPath(){
        return this.jsonPath;
    }
}
