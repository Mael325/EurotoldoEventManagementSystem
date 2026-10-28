package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import ec.edu.espe.eurotoldo.model.Equipment;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.JsonParseException;
import java.nio.file.NoSuchFileException;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EquipmentFileManager {

    public boolean saveEquipment(ArrayList<Equipment> equipmentList) {

        if (equipmentList == null) {
            return false;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(equipmentList);

        try {
            Files.write(
                    Paths.get("equipment.json"),
                    json.getBytes(StandardCharsets.UTF_8)
            );

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Equipment> loadEquipment() {

        try {
            String json = new String(
                    Files.readAllBytes(Paths.get("equipment.json")),
                    StandardCharsets.UTF_8
            );

            Gson gson = new Gson();
            ArrayList<Equipment> equipmentList = gson.fromJson(json,
                    new TypeToken<ArrayList<Equipment>>() {}.getType());

            if (equipmentList == null) {
                return null;
            }

            return equipmentList;

        } catch (NoSuchFileException e) {
            return new ArrayList<>();

        } catch (IOException | JsonParseException e) {
            return null;
        }
    }
}
