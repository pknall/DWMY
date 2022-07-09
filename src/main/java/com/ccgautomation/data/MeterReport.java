package com.ccgautomation.data;

import com.ccgautomation.utilities.DateTools;

import java.util.Date;

public class MeterReport {
    public Date startDate;
    public Date stopDate;
    public Float value;

    public MeterReport(Date startDate, Date stopDate, Float value) {
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.value = value;
    }

    @Override
    public String toString() {
        String startDateString = new DateTools().convertDateToStringWithFormat(startDate, "MM/dd/yyyy");
        String stopDateString = new DateTools().convertDateToStringWithFormat(stopDate, "MM/dd/yyyy");
        return startDateString + "," + stopDateString + "," + value.toString();
    }
}
