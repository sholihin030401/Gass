package com.project.gemastik.reminder.impian;
public class dataDetailImpian {
    String misi, timeStamp;

    public dataDetailImpian(){

    }

    public dataDetailImpian(String misi, String timeStamp) {
        this.misi = misi;
        this.timeStamp = timeStamp;
    }

    public String getMisi() {
        return misi;
    }

    public void setMisi(String misi) {
        this.misi = misi;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
