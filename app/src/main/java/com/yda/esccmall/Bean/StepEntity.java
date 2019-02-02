package com.yda.esccmall.Bean;

public class StepEntity {

    private String id;
    private String curDate; //当天的日期
    private String steps;//当天的步数
    private String totalStepsKm;
    private String totalStepsKa;
    private String longitude;
    private String latitude;

    public StepEntity() {
    }

    public StepEntity(String curDate, String steps, String totalStepsKm, String totalStepsKa) {
        this.curDate = curDate;
        this.steps = steps;
        this.totalStepsKm=totalStepsKm;
        this.totalStepsKa=totalStepsKa;
    }

    public StepEntity(String id, String curDate, String steps, String totalStepsKm, String totalStepsKa, String longitude, String latitude) {
        this.id = id;
        this.curDate = curDate;
        this.steps = steps;
        this.totalStepsKm = totalStepsKm;
        this.totalStepsKa = totalStepsKa;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getTotalStepsKm() {
        return totalStepsKm;
    }

    public void setTotalStepsKm(String totalStepsKm) {
        this.totalStepsKm = totalStepsKm;
    }

    public String getTotalStepsKa() {
        return totalStepsKa;
    }

    public void setTotalStepsKa(String totalStepsKa) {
        this.totalStepsKa = totalStepsKa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "StepEntity{" +
                "curDate='" + curDate + '\'' +
                ", steps=" + steps +
                '}';
    }
}
