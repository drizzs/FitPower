package com.fitgroup.fitpower.objects;

import com.fitgroup.fitpower.objects.base.BaseObject;

public class DailyWorkout extends BaseObject {

    private Workout workout;
    private int workoutRoutineSID;
    private String workoutRoutineLabel;

    private int workoutOrder;

    private final Set[] workingSets;

    private final String date;

    private int weightPerRep;
    private int weightUnitSID;
    private String weightUnitLabel;

    private String superSetUUID;

    public DailyWorkout(int dailyWorkoutSID, Workout workout, int workoutRoutineSID, String workoutRoutineLabel,
                        int workoutOrder, Set[] setSets, String date, int weightPerRep,
                        int weightUnitSID, String weightUnitLabel, String superSetUUID){
        super(dailyWorkoutSID);
        this.workout = workout;
        this.workoutRoutineSID = workoutRoutineSID;
        this.workoutRoutineLabel = workoutRoutineLabel;
        this.workoutOrder = workoutOrder;
        this.workingSets = setSets;
        this.date = date;
        this.weightPerRep = weightPerRep;
        this.weightUnitSID = weightUnitSID;
        this.weightUnitLabel = weightUnitLabel;
        this.superSetUUID = superSetUUID;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public int getWorkoutRoutineSID() {
        return workoutRoutineSID;
    }

    public void setWorkoutRoutineSID(int workoutRoutineSID) {
        this.workoutRoutineSID = workoutRoutineSID;
    }

    public String getWorkoutRoutineLabel() {
        return workoutRoutineLabel;
    }

    public void setWorkoutRoutineLabel(String workoutRoutineLabel) {
        this.workoutRoutineLabel = workoutRoutineLabel;
    }

    public int getWorkoutOrder() {
        return workoutOrder;
    }

    public void setWorkoutOrder(int workoutOrder) {
        this.workoutOrder = workoutOrder;
    }

    public Set getSet(int number) {
        return workingSets[number];
    }

    public String getDate() {
        return date;
    }

    public int getWeightPerRep() {
        return weightPerRep;
    }

    public void setWeightPerRep(int weightPerRep) {
        this.weightPerRep = weightPerRep;
    }

    public int getWeightUnitSID() {
        return weightUnitSID;
    }

    public void setWeightUnitSID(int weightUnitSID) {
        this.weightUnitSID = weightUnitSID;
    }

    public String getWeightUnitLabel() {
        return weightUnitLabel;
    }

    public void setWeightUnitLabel(String weightUnitLabel) {
        this.weightUnitLabel = weightUnitLabel;
    }

    public String getSuperSetUUID() {
        return superSetUUID;
    }

    public void setSuperSetUUID(String superSetUUID) {
        this.superSetUUID = superSetUUID;
    }
}
