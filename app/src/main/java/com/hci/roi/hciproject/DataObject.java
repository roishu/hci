package com.hci.roi.hciproject;

/**
 * Created by Roi on 14/06/2017.
 * for each view card we can add lable and description text this is been added to the semi-views of the project.
 */



public class DataObject {
    private String mText1; //lable
    private String mText2; //description

    DataObject (String text1, String text2){
        mText1 = text1;
        mText2 = text2;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}