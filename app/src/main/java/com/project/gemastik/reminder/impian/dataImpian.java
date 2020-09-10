package com.project.gemastik.reminder.impian;

public class dataImpian {
    String judul, waktu, timeStamp;

    public dataImpian(){

    }

    public dataImpian(String judul, String waktu, String timeStamp) {
        this.judul = judul;
        this.waktu = waktu;
        this.timeStamp = timeStamp;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
