package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import ec.edu.espe.eurotoldo.model.Equipment;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo,
 * Dylan Cumbajin, MosQ, @ESPE
 */
public class EquipmentApiClient extends ApiClient {

    private static final String EQUIPMENT_URL
            = "http://localhost:3000/api/equipment";

    private final Gson gson;
    private final EquipmentJsonConverter converter;

    public EquipmentApiClient() {

        gson = GsonFactory.createGson();
        converter = new EquipmentJsonConverter(gson);
    }

    public ArrayList<Equipment> getEquipment() {

        ApiResponse response = sendRequest(
                "GET", EQUIPMENT_URL, null
        );

        if (response == null
                || response.getStatusCode() != 200) {
            return null;
        }

        try {
            JsonArray equipmentArray = gson.fromJson(
                    response.getBody(),
                    JsonArray.class
            );

            ArrayList<Equipment> equipmentList
                    = new ArrayList<>();

            if (equipmentArray == null) {
                return equipmentList;
            }

            for (JsonElement element : equipmentArray) {

                if (!element.isJsonObject()) {
                    return null;
                }

                Equipment equipment
                        = converter.equipmentFromJson(
                                element.getAsJsonObject()
                        );

                equipmentList.add(equipment);
            }

            return equipmentList;

        } catch (JsonParseException
                | IllegalArgumentException exception) {

            return null;
        }
    }

    public Equipment getEquipmentById(int id) {

        ApiResponse response = sendRequest(
                "GET",
                EQUIPMENT_URL + "/" + id,
                null
        );

        if (response == null
                || response.getStatusCode() != 200) {
            return null;
        }

        try {
            JsonObject equipmentJson = gson.fromJson(
                    response.getBody(),
                    JsonObject.class
            );

            return converter.equipmentFromJson(
                    equipmentJson
            );

        } catch (JsonParseException
                | IllegalArgumentException exception) {

            return null;
        }
    }

    public boolean addEquipment(Equipment equipment) {

        if (equipment == null) {
            return false;
        }

        JsonObject equipmentJson
                = converter.equipmentToJson(equipment);

        ApiResponse response = sendRequest(
                "POST",
                EQUIPMENT_URL,
                equipmentJson.toString()
        );

        return response != null
                && response.getStatusCode() == 201;
    }

    public boolean updateEquipment(Equipment equipment) {

        if (equipment == null) {
            return false;
        }

        JsonObject equipmentJson
                = converter.equipmentToJson(equipment);

        ApiResponse response = sendRequest(
                "PUT",
                EQUIPMENT_URL + "/" + equipment.getId(),
                equipmentJson.toString()
        );

        return response != null
                && response.getStatusCode() == 200;
    }

    public boolean deleteEquipment(int id) {

        ApiResponse response = sendRequest(
                "DELETE",
                EQUIPMENT_URL + "/" + id,
                null
        );

        return response != null
                && response.getStatusCode() == 200;
    }
}