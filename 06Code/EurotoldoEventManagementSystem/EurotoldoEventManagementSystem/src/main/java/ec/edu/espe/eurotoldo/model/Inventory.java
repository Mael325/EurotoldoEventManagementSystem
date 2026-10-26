
package ec.edu.espe.eurotoldo.model;

import java.util.List;

/**
 *
 * @author Alvarez Juan,Anthony Arévalo,Dylan Cumbajin, MosQ, @ESPE
 */
public class Inventory {
    private List<Equipment> equipmentList;

    public Inventory(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    @Override
    public String toString() {
        return "Inventory{" + "equipmentList=" + equipmentList + '}';
    }
    
    
    public boolean addEquipment(Equipment equipment){
        
        if(equipment == null){
            return false;
        }
        
        if(equipment.getTotalQuantity() < 0){
            return false;
        }
        
        for (Equipment registeredEquipment : equipmentList){
            if (registeredEquipment.getId() == equipment.getId()){
                return false;
            }
        }
        
        equipmentList.add(equipment);
        
        return true;
    }
    
    
    public Equipment findEquipmentById(int id){
        
        for (Equipment equipment : equipmentList){
            if (equipment.getId() == id){
                return equipment;
            }
        }
        
        return null;
    }
  
}
