package com.ccgautomation.reports;

import com.ccgautomation.reports.MeterReportRecord;
import junit.framework.TestCase;

import java.util.Date;

public class MeterReportRecordTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    /* MeterReportRecord(Date startDate, Date stopDate, Float value) throws NullPointerException */

    public void test_Meter_Report_Record_With_Null_Start_Date_Returns_NullPointerException() {
        try {
            new MeterReportRecord(null, new Date(0), 0f);
            assert(false);
        }
        catch(NullPointerException ex) {
            assertEquals("startDate cannot be null.", ex.getMessage());
        }
    }

    public void test_Meter_Report_Record_With_Null_Stop_Date_Returns_NullPointerException() {
        try {
            new MeterReportRecord(new Date(0), null,0f);
            assert(false);
        }
        catch(NullPointerException ex) {
            assertEquals("stopDate cannot be null.", ex.getMessage());
        }
    }

    public void test_Meter_Report_Record_With_Null_Value_Returns_NullPointerException() {
        try {
            new MeterReportRecord(new Date(0), new Date(0),null);
            assert(false);
        }
        catch(NullPointerException ex) {
            assertEquals("Value cannot be null.", ex.getMessage());
        }
    }

    /* String toString() */

    public void testTestToString() {
        String meterReportString = new MeterReportRecord(new Date(0), new Date(86400000), 0f).toString();
        assertEquals("12/31/1969,01/01/1970,0.0", meterReportString);
    }

    /* String getValue() */

    public void testGetValue() {
        MeterReportRecord meterReportRecord = new MeterReportRecord(new Date(0), new Date(86400000), 8675309f);
        assertEquals("8675309.0", meterReportRecord.getValue());
    }
}