package com.fitgroup.fitpower.objects;

import com.fitgroup.fitpower.objects.base.DescribedObject;

import java.util.ArrayList;
import java.util.List;

public class Workout extends DescribedObject {

    private final List<Integer> muscles = new ArrayList<>();
    private final List<Integer> Equipment = new ArrayList<>();

    public Workout(int workoutSID,String workoutLabel,String workoutDescription){
        super(workoutSID,workoutLabel,workoutDescription);
    }

    public List<Integer> getEquipment() {
        return Equipment;
    }

    public void addEquipment(int equipmentSID){
        Equipment.add(equipmentSID);
    }

    public List<Integer> getMuscles() {
        return muscles;
    }

    public void addMuscle(int muscleSID){
        muscles.add(muscleSID);
    }

}
