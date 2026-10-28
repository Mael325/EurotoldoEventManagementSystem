
package ec.edu.espe.eurotoldo.controller;

import ec.edu.espe.eurotoldo.model.Equipment;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */

public class CsvReportManager {

    public boolean exportEquipmentReport(ArrayList<Equipment> equipmentList, String filePath) {

        if (equipmentList == null || filePath == null
                || filePath.isBlank()) {
            return false;
        }

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("ID", "Name", "Category", "TotalQuantity")
                .get();

        try (BufferedWriter writer = Files.newBufferedWriter(
                Path.of(filePath), StandardCharsets.UTF_8);
                CSVPrinter printer = new CSVPrinter(writer, format)) {

            for (Equipment equipment : equipmentList) {

                String categoryName = "";

                if (equipment.getCategory() != null) {
                    categoryName = equipment.getCategory().getName();
                }

                printer.printRecord(
                        equipment.getId(),
                        equipment.getName(),
                        categoryName,
                        equipment.getTotalQuantity()
                );
            }

        } catch (IOException e) {
            return false;
        }

        return true;
    }
}