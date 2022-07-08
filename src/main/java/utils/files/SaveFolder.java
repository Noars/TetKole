package utils.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveFolder {

    String os = "";

    String folderPath = "";
    String jsonPath = "";
    String recordPath = "";

    /**
     * Initialize the constructor of this class
     *
     * @param os -> operating system of the user
     */
    public SaveFolder(String os){
        super();
        this.os = os;
        this.createSaveFolder();
    }

    /**
     * Create all the folder needed if they don't exist
     * 1 folder "TètKole" who contains all other folder
     * 1 folder "JsonFiles" who contain all json files
     * 1 folder "RecordFiles" who contain all recorded file in wav format
     */
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

    /**
     * Check the current operating system used by the user
     * And create path for all folder according to the operating system
     */
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

    /**
     * Function that return the path of the folder "TètKole"
     *
     * @return variable folderPath
     */
    public String getFolderPath(){
        return this.folderPath;
    }

    /**
     * Function that return the path of the folder "JsonFiles"
     *
     * @return variable jsonPath
     */
    public String getJsonPath(){
        return this.jsonPath;
    }

    /**
     * Function that return the path of the folder "RecordFiles"
     *
     * @return variable recordPath
     */
    public String getRecordPath(){
        return this.recordPath;
    }
}
