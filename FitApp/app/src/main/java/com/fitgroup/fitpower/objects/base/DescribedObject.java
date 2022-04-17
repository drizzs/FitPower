package com.fitgroup.fitpower.objects.base;

public class DescribedObject extends LabeledObject{

    private final String description;

    public DescribedObject(int SID, String label, String description) {
        super(SID, label);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
