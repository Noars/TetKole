package utils.files;

import application.Main;
import application.ui.pane.WavePane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CreateJson {

    Main main;
    WavePane wavePane;

    String pathFolder;

    public CreateJson(Main main, String pathFolder){
        super();
        this.main = main;
        this.pathFolder = pathFolder;
    }

    public void createJson(String nameRecordAudio){
        this.wavePane = main.getWavePane();

        JSONObject jsonAudioFile = new JSONObject();
        JSONObject jsonRecordFile = new JSONObject();
        JSONObject jsonStartTime = new JSONObject();
        JSONObject jsonEndTime = new JSONObject();

        jsonAudioFile.put("Nom du fichier audio", wavePane.getWaveService().audioFileName);
        jsonRecordFile.put("Nom du fichier audio enregistrer", nameRecordAudio + ".wav");
        jsonStartTime.put("Debut de l'intervalle", wavePane.calculTimeLeftBorder());
        jsonEndTime.put("Fin de l'intervalle", wavePane.calculTimeRightBorder());

        JSONArray json = new JSONArray();
        json.add(jsonAudioFile);
        json.add(jsonRecordFile);
        json.add(jsonStartTime);
        json.add(jsonEndTime);

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
}
