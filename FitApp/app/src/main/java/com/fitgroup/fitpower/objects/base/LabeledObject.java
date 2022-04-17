package com.fitgroup.fitpower.objects.base;

public class LabeledObject extends BaseObject {

    private final String label;

    public LabeledObject(int SID,String label) {
        super(SID);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
