package utils.files;

import application.Main;
import application.ui.pane.WavePane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class CreateJson {

    Main main;
    WavePane wavePane;

    String folderPath;

    public CreateJson(Main main, String folderPath){
        super();
        this.main = main;
        this.wavePane = main.getWavePane();
        this.folderPath = folderPath;
    }

    public void createJson(String nameRecordAudio, String nameJson){
        JSONObject jsonDetails = new JSONObject();
        jsonDetails.put("Nom du fichier audio", "test");
        jsonDetails.put("Nom du fichier audio enregistrer", nameRecordAudio);
        jsonDetails.put("Début de l'intervalle ", "0h:10m:30s");
        jsonDetails.put("Fin de l'intervalle", "0h:12m:30s");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TranslateAudioFile", jsonDetails);

        JSONArray json = new JSONArray();
        json.add(jsonObject);

        try(FileWriter jsonFile = new FileWriter(folderPath + "//JsonFiles//" + nameJson + ".json")){
            jsonFile.write(json.toJSONString());
            jsonFile.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
