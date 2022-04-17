package com.fitgroup.fitpower.objects;

import com.fitgroup.fitpower.objects.base.LabeledObject;

import java.util.ArrayList;
import java.util.List;

public class Gym  extends LabeledObject {

    private final List<Integer> equipment = new ArrayList<>();

    public Gym(int SID, String label) {
        super(SID, label);
    }

    public List<Integer> getEquipment() {
        return equipment;
    }

    public void addEquipment(int equipmentSID){
        equipment.add(equipmentSID);
    }

    public int getTotalEquipment(){
        return equipment.size();
    }

    public boolean hasEquipment(int equipmentSID){
        return equipment.contains(equipmentSID);
    }

}
