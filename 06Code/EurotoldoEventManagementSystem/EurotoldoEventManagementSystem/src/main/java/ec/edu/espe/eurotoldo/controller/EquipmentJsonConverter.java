package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ec.edu.espe.eurotoldo.model.Chair;
import ec.edu.espe.eurotoldo.model.Chandelier;
import ec.edu.espe.eurotoldo.model.Equipment;
import ec.edu.espe.eurotoldo.model.Table;
import ec.edu.espe.eurotoldo.model.Tent;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class EquipmentJsonConverter {

    private final Gson gson;

    public EquipmentJsonConverter(Gson gson) {
        this.gson = gson;
    }

    public JsonObject equipmentToJson(
            Equipment equipment) {

        JsonObject equipmentJson = gson.toJsonTree(
                equipment,
                equipment.getClass()
        ).getAsJsonObject();

        equipmentJson.addProperty(
                "equipmentType",
                equipment.getEquipmentType()
        );

        return equipmentJson;
    }

    public Equipment equipmentFromJson(
            JsonObject equipmentJson) {

        String equipmentType = "General Equipment";

        if (equipmentJson.has("equipmentType")) {

            equipmentType = equipmentJson
                    .get("equipmentType")
                    .getAsString();
        }

        Equipment equipment;

        if ("Tent".equals(equipmentType)) {

            equipment = gson.fromJson(
                    equipmentJson,
                    Tent.class
            );

        } else if ("Chair".equals(equipmentType)) {

            equipment = gson.fromJson(
                    equipmentJson,
                    Chair.class
            );

        } else if ("Table".equals(equipmentType)) {

            equipment = gson.fromJson(
                    equipmentJson,
                    Table.class
            );

        } else if ("Chandelier".equals(equipmentType)) {

            equipment = gson.fromJson(
                    equipmentJson,
                    Chandelier.class
            );

        } else {

            equipment = gson.fromJson(
                    equipmentJson,
                    Equipment.class
            );
        }

        if (!equipmentJson.has("availableQuantity")) {

            equipment.setAvailableQuantity(
                    equipment.getTotalQuantity()
            );
        }

        return equipment;
    }
}