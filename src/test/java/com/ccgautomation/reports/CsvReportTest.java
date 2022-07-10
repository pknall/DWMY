package com.ccgautomation.reports;

import com.ccgautomation.utilities.Configuration;
import com.ccgautomation.utilities.DateTools;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsvReportTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testAddMeterReport() {
    }

    public void testTestToString() {
    }

    /*  public CsvReportRecord() */

    public void testCsvReportConstructor() {
        CsvReport csvReportObject = new CsvReport();
        assertEquals(1, csvReportObject.csvReport.size());
        assertEquals(2, csvReportObject.csvReport.get(0).size());
        assertEquals("Start Date", csvReportObject.csvReport.get(0).get(0));
        assertEquals("Stop Date", csvReportObject.csvReport.get(0).get(1));
    }

    /* public void addReport(List<ReportRecord> reports, String name) */

    /* protected void addReportToCsvReportByAddingANewDateToCsvReport(ReportRecord report) */

    public void testAddReportToCsvReportByAddingANewDateToCsvReport_NewReportAddedToEmptyList() {
        CsvReport csvReportObject = new CsvReport();
        csvReportObject.csvReport.get(0).add("one");
        Date startDate = DateTools.convertStringToDateWithFormat("01/01/1970", "MM/dd/yyyy");
        Date stopDate = DateTools.convertStringToDateWithFormat("01/02/1970", "MM/dd/yyyy");

        ReportRecord reportRecord = new MeterReportRecord(startDate, stopDate, 1f);

        csvReportObject.addReportToCsvReportByAddingANewDateToCsvReport(reportRecord);

        assertEquals(2, csvReportObject.csvReport.size());
        assertEquals(3, csvReportObject.csvReport.get(1).size());
        assertEquals("01/01/1970", csvReportObject.csvReport.get(1).get(0));
        assertEquals("01/02/1970", csvReportObject.csvReport.get(1).get(1));
        assertEquals("1.0", csvReportObject.csvReport.get(1).get(2));
    }

    public void testAddReportToCsvReportByAddingANewDateToCsvReport_NewReportAddedToPopulatedListInOrder() {
        CsvReport csvReportObject = new CsvReport();
        /*
        csvReportObject.csvReport.get(0).add("one");
        Date startDate = DateTools.convertStringToDateWithFormat("01/01/1970", "MM/dd/yyyy");
        Date stopDate = DateTools.convertStringToDateWithFormat("01/02/1970", "MM/dd/yyyy");

        ReportRecord reportRecord = new MeterReportRecord(startDate, stopDate, 1f);

        csvReportObject.addReportToCsvReportByAddingANewDateToCsvReport(reportRecord);

        assertEquals(2, csvReportObject.csvReport.size());
        assertEquals(3, csvReportObject.csvReport.get(1).size());
        assertEquals("01/01/1970", csvReportObject.csvReport.get(1).get(0));
        assertEquals("01/02/1970", csvReportObject.csvReport.get(1).get(1));
        assertEquals("1.0", csvReportObject.csvReport.get(1).get(2));

         */
    }

    /* protected void addZerosForCsvReportDatesTheNewReportDidNotHaveValuesFor() */

    public void testAddZerosForCsvReportDatesTheNewReportDidNotHaveValuesFor_ZeroNeedsToBeAdded() {
        CsvReport csvReportObject = new CsvReport();
        csvReportObject.csvReport.get(0).add("one");
        csvReportObject.csvReport.get(0).add("two");
        ArrayList<String> testReport = new ArrayList<>();
        testReport.add("01/01/1970");
        testReport.add("01/02/1970");
        testReport.add("1");
        csvReportObject.csvReport.add(testReport);

        csvReportObject.addZerosForCsvReportDatesTheNewReportDidNotHaveValuesFor();

        assertEquals(4,csvReportObject.csvReport.get(1).size());
        assertEquals("01/01/1970", csvReportObject.csvReport.get(1).get(0));
        assertEquals("01/02/1970", csvReportObject.csvReport.get(1).get(1));
        assertEquals("1", csvReportObject.csvReport.get(1).get(2));
        assertEquals(csvReportObject.VALUE_TO_USE_WHEN_THERE_IS_NO_VALUE_AVAILABLE, csvReportObject.csvReport.get(1).get(3));
    }

    public void testAddZerosForCsvReportDatesTheNewReportDidNotHaveValuesFor_ZeroDoesNotNeedToBeAdded() {
        CsvReport csvReportObject = new CsvReport();
        csvReportObject.csvReport.get(0).add("one");
        csvReportObject.csvReport.get(0).add("two");
        ArrayList<String> testReport = new ArrayList<>();
        testReport.add("01/01/1970");
        testReport.add("01/02/1970");
        testReport.add("1");
        testReport.add("2");
        csvReportObject.csvReport.add(testReport);

        csvReportObject.addZerosForCsvReportDatesTheNewReportDidNotHaveValuesFor();

        assertEquals(4,csvReportObject.csvReport.get(1).size());
        assertEquals("01/01/1970", csvReportObject.csvReport.get(1).get(0));
        assertEquals("01/02/1970", csvReportObject.csvReport.get(1).get(1));
        assertEquals("1", csvReportObject.csvReport.get(1).get(2));
        assertEquals("2", csvReportObject.csvReport.get(1).get(3));
    }

    /* protected int getCsvReportHeaderSize() */

    public void testGetCsvReportHeaderSize() {
        CsvReport csvReportObject = new CsvReport();
        assertEquals(2, csvReportObject.csvReport.get(0).size());
    }
}