package utils.files;

import application.Main;
import application.ui.pane.WavePane;
import application.ui.pane.ZoomPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CreateJson {

    Main main;
    WavePane wavePane;
    ZoomPane zoomPane;

    String pathFolder;
    Boolean isZoom = false;

    /**
     * Initialize the constructor of this class
     *
     * @param main -> the main class
     * @param pathFolder -> the path to the "TètKole" folder according to the user's operating system
     */
    public CreateJson(Main main, String pathFolder){
        super();
        this.main = main;
        this.pathFolder = pathFolder;
    }

    /**
     * Create a .json that contain 4 values :
     * - The name of the audio file used
     * - The name of the recorded file the user choose
     * - The start time of the record
     * - The end time of the record
     *
     * @param nameRecordAudio -> the name of the audio file recorded the user choose
     */
    public void createJson(String nameRecordAudio){
        this.wavePane = main.getWavePane();
        this.zoomPane = main.getZoomPane();

        JSONObject jsonAudioFile = new JSONObject();
        JSONObject jsonRecordFile = new JSONObject();
        JSONObject jsonStartTime = new JSONObject();
        JSONObject jsonEndTime = new JSONObject();

        // Check if the record is done on a zoom or not
        if (this.isZoom){
            jsonAudioFile.put("Nom du fichier audio", wavePane.getWaveService().audioFileName);
            jsonRecordFile.put("Nom du fichier audio enregistrer", nameRecordAudio + ".wav");
            jsonStartTime.put("D\u00e9but de l'intervalle", zoomPane.calculTimeLeftBorder());
            jsonEndTime.put("Fin de l'intervalle", zoomPane.calculTimeRightBorder());
        }else {
            jsonAudioFile.put("Nom du fichier audio", wavePane.getWaveService().audioFileName);
            jsonRecordFile.put("Nom du fichier audio enregistrer", nameRecordAudio + ".wav");
            jsonStartTime.put("D\u00e9but de l'intervalle", wavePane.calculTimeLeftBorder());
            jsonEndTime.put("Fin de l'intervalle", wavePane.calculTimeRightBorder());
        }

        JSONArray json = new JSONArray();
        json.add(jsonAudioFile);
        json.add(jsonRecordFile);
        json.add(jsonStartTime);
        json.add(jsonEndTime);

        // Check the operating system of the user to choose the correct path
        if (this.main.getOs().contains("nux") || this.main.getOs().contains("mac")){
            try(FileWriter jsonFile = new FileWriter(pathFolder + "/JsonFiles/" + nameRecordAudio + ".json", StandardCharsets.UTF_8)){
                jsonFile.write(json.toJSONString());
                jsonFile.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            try(FileWriter jsonFile = new FileWriter(pathFolder + "\\JsonFiles\\" + nameRecordAudio + ".json", StandardCharsets.UTF_8)){
                jsonFile.write(json.toJSONString());
                jsonFile.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Allows to get the value of the variable "isZoom"
     *
     * @return -> the value of the variable "isZoom"
     */
    public boolean getIsZoom(){
        return this.isZoom;
    }

    /**
     * Allows to set the value passed as a parameter in the vraiable "isZoom"
     *
     * @param value -> set this value in the variable "isZoom"
     */
    public void setIsZoom(boolean value){
        this.isZoom = value;
    }
}
