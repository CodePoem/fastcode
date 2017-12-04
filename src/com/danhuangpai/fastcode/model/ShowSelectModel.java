package com.danhuangpai.fastcode.model;

/**
 * 选择属性字段实体类
 *
 * @author danhuangpai
 * @version 1.0.0 created at 2017/12/4 16:57
 */
public class ShowSelectModel {
    private String mFieldName;
    private String mType;

    public ShowSelectModel() {
    }

    public ShowSelectModel(String mFieldName, String type) {
        this.mFieldName = mFieldName;
        mType = type;
    }

    public String getmFieldName() {
        return mFieldName;
    }

    public void setmFieldName(String mFieldName) {
        this.mFieldName = mFieldName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
