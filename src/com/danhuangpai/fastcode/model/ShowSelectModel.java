package com.danhuangpai.fastcode.model;

public class ShowSelectModel {
    private String mFieldName;
    private String Type;

    public ShowSelectModel() {
    }

    public ShowSelectModel(String mFieldName, String type) {
        this.mFieldName = mFieldName;
        Type = type;
    }

    public String getmFieldName() {
        return mFieldName;
    }

    public void setmFieldName(String mFieldName) {
        this.mFieldName = mFieldName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
