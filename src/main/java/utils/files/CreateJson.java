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

    String pathFolder;

    public CreateJson(Main main, String pathFolder){
        super();
        this.main = main;
        this.pathFolder = pathFolder;
    }

    public void createJson(String nameRecordAudio, String nameJson){
        this.wavePane = main.getWavePane();

        JSONObject jsonAudioFile = new JSONObject();
        JSONObject jsonRecordFile = new JSONObject();
        JSONObject jsonStartTime = new JSONObject();
        JSONObject jsonEndTime = new JSONObject();

        jsonAudioFile.put("Nom du fichier audio", wavePane.getWaveService().audioFileName);
        jsonRecordFile.put("Nom du fichier audio enregistrer", nameRecordAudio + ".wav");
        jsonStartTime.put("Début de l'intervalle", wavePane.calculTimeLeftBorder());
        jsonEndTime.put("Fin de l'intervalle", wavePane.calculTimeRightBorder());

        JSONArray json = new JSONArray();
        json.add(jsonAudioFile);
        json.add(jsonRecordFile);
        json.add(jsonStartTime);
        json.add(jsonEndTime);

        try(FileWriter jsonFile = new FileWriter(pathFolder + "//JsonFiles//" + nameJson + ".json")){
            jsonFile.write(json.toJSONString());
            jsonFile.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
