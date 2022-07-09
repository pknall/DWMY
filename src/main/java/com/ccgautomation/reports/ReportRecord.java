package com.ccgautomation.reports;

import java.util.Date;

/* Superclass */

public class ReportRecord {
    protected Date startDate;
    protected Date stopDate;

    public Date getStartDate() { return startDate; }
    public Date getStopDate() { return stopDate; }
    public String getValue(){ return "Not Assigned."; }

}
