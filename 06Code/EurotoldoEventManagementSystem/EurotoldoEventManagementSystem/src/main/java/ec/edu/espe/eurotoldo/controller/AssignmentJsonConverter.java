package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ec.edu.espe.eurotoldo.model.Assignment;
import ec.edu.espe.eurotoldo.model.Equipment;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class AssignmentJsonConverter {

    private final Gson gson;
    private final EquipmentJsonConverter equipmentConverter;

    public AssignmentJsonConverter(Gson gson) {

        this.gson = gson;

        equipmentConverter
                = new EquipmentJsonConverter(gson);
    }

    public JsonArray assignmentsToJson(
            ArrayList<Assignment> assignments) {

        JsonArray assignmentsJson = new JsonArray();

        if (assignments == null) {
            return assignmentsJson;
        }

        for (Assignment assignment : assignments) {

            JsonObject assignmentJson
                    = gson.toJsonTree(
                            assignment,
                            Assignment.class
                    ).getAsJsonObject();

            assignmentJson.add(
                    "equipment",
                    equipmentConverter.equipmentToJson(
                            assignment.getEquipment()
                    )
            );

            assignmentsJson.add(assignmentJson);
        }

        return assignmentsJson;
    }

    public ArrayList<Assignment> assignmentsFromJson(
            JsonArray assignmentsJson) {

        ArrayList<Assignment> assignments
                = new ArrayList<>();

        if (assignmentsJson == null) {
            return assignments;
        }

        for (JsonElement element : assignmentsJson) {

            JsonObject assignmentJson
                    = element.getAsJsonObject();

            int id = assignmentJson
                    .get("id")
                    .getAsInt();

            int quantity = assignmentJson
                    .get("quantity")
                    .getAsInt();

            JsonObject equipmentJson
                    = assignmentJson
                            .getAsJsonObject("equipment");

            Equipment equipment
                    = equipmentConverter.equipmentFromJson(
                            equipmentJson
                    );

            Assignment assignment = new Assignment(
                    id,
                    equipment,
                    quantity
            );

            assignments.add(assignment);
        }

        return assignments;
    }
}