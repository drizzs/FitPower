package com.fitgroup.fitpower.objects;

import com.fitgroup.fitpower.objects.base.DescribedObject;

public class Muscle extends DescribedObject {

    private final MuscleGroup group1;
    private final MuscleGroup group2;

    public Muscle(int SID, String label, String muscleDescription, MuscleGroup group1, MuscleGroup group2) {
        super(SID, label, muscleDescription);
        this.group1 = group1;
        this.group2 = group2;
    }

    public MuscleGroup getGroup1() {
        return group1;
    }

    public MuscleGroup getGroup2() {
        return group2;
    }
}
