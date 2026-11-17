package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import ec.edu.espe.eurotoldo.model.Equipment;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EquipmentFileManager {

    private final Path filePath
            = Path.of("equipment.json");

    private final Gson gson
            = GsonFactory.createGson();

    private final EquipmentJsonConverter converter
            = new EquipmentJsonConverter(gson);

    public boolean saveEquipment(
            ArrayList<Equipment> equipmentList) {

        if (equipmentList == null) {
            return false;
        }

        JsonArray equipmentArray = new JsonArray();

        for (Equipment equipment : equipmentList) {

            equipmentArray.add(
                    converter.equipmentToJson(equipment)
            );
        }

        try {
            Files.writeString(
                    filePath,
                    gson.toJson(equipmentArray),
                    StandardCharsets.UTF_8
            );

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Equipment> loadEquipment() {

        try {
            String json = Files.readString(
                    filePath,
                    StandardCharsets.UTF_8
            );

            JsonArray equipmentArray = JsonParser
                    .parseString(json)
                    .getAsJsonArray();

            ArrayList<Equipment> equipmentList
                    = new ArrayList<>();

            for (JsonElement element : equipmentArray) {

                JsonObject equipmentJson
                        = element.getAsJsonObject();

                Equipment equipment
                        = converter.equipmentFromJson(
                                equipmentJson
                        );

                equipmentList.add(equipment);
            }

            return equipmentList;

        } catch (NoSuchFileException e) {
            return new ArrayList<>();

        } catch (IOException
                | JsonParseException
                | IllegalStateException e) {

            return null;
        }
    }
}