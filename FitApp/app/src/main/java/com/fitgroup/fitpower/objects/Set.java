package com.fitgroup.fitpower.objects;

import com.fitgroup.fitpower.objects.base.BaseObject;

public class Set extends BaseObject {

    private final int setNumber;
    private int completedReps;
    private int completedWeight;

    public Set(int setSID, int setNumber, int completedReps, int completedWeight){
        super(setSID);
        this.setNumber = setNumber;
        this.completedReps = completedReps;
        this.completedWeight = completedWeight;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public int getCompletedReps() {
        return completedReps;
    }

    public void setCompletedReps(int completedReps) {
        this.completedReps = completedReps;
    }

    public int getCompletedWeight() {
        return completedWeight;
    }

    public void setCompletedWeight(int completedWeight) {
        this.completedWeight = completedWeight;
    }
}
