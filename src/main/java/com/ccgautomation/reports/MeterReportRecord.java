package com.ccgautomation.reports;

import com.ccgautomation.utilities.DateTools;

import java.util.Date;

/*
    Construction of this object guarantees that all fields are valid objects of the required type.
    If any fields are not valid during construction, the constructor throws the appropriate exception.
 */

public class MeterReportRecord extends ReportRecord {

    public Float value;

    public MeterReportRecord(Date startDate, Date stopDate, Float value) throws NullPointerException {
        if (startDate == null) throw new NullPointerException("startDate cannot be null.");
        if (stopDate == null) throw new NullPointerException("stopDate cannot be null.");
        if (value == null) throw new NullPointerException("Value cannot be null.");
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

    @Override
    public String getValue() {
        return value.toString();
    }
}
