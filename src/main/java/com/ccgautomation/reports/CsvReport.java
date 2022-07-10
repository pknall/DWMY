package com.ccgautomation.reports;
import com.ccgautomation.utilities.DateTools;

import java.util.ArrayList;
import java.util.List;

/*
    A Report consisting of a 2D array of Strings based on the ReportRecordObject and its subclasses.
    This report is not a sparse or jagged 2D array.  All cells wil be populated.

    Three possible results to adding a report to the list:
    1) The date the data occurred exists in the results list and needs to be added to an
       existing results  List<String>
    2) The date the data occurred on does not exist and a new List<String> needs to be added to results
    3) The results List<String> contains dates that do not occur in the report and need to be zeroed
 */

public class CsvReport {

    private final int REPORT_ROW_START_DATE_INDEX = 0;
    private final int CSV_REPORT_HEADER_ROW_INDEX = 0;
    protected final String VALUE_TO_USE_WHEN_THERE_IS_NO_VALUE_AVAILABLE = "0";
    protected ArrayList<ArrayList<String>> csvReport = new ArrayList<>();
    
    public CsvReport() {
        ArrayList<String> header = new ArrayList<>();
        header.add("Start Date");
        header.add("Stop Date");
        csvReport.add(header);
    }

    public void addReport(List<ReportRecord> reports, String name) {
        addReportNameToHeader(name);
        for (ReportRecord report : reports) { addReportToReportList(report); }
    }

    /*
        Adds a single Report Record (startDate, stopDate, Value) to the CSV List.
        The method will maintain the csvReport as a fully populated matrix.
     */
    private void addReportToReportList(ReportRecord report) {
        boolean reportAdded = addReportToCsvReportIfDateAlreadyExistsInCsvReport(report);
        if (!reportAdded) {
            addReportToCsvReportByAddingANewDateToCsvReport(report);
        }
        addZerosForCsvReportDatesTheNewReportDidNotHaveValuesFor();
    }

    private boolean addReportToCsvReportIfDateAlreadyExistsInCsvReport(ReportRecord report) {
        boolean added = false;
        boolean skipHeaderInCsvReport = true;
        for (List<String> csvReportRow : csvReport) {
            if (skipHeaderInCsvReport) {
                skipHeaderInCsvReport = false;
                continue;
            }
            String reportDateString = DateTools.convertDateToStringWithFormat(report.getStartDate(), "MM/dd/yyyy");
            String csvReportDateString = csvReportRow.get(REPORT_ROW_START_DATE_INDEX);
            if (csvReportDateString.contains(reportDateString)) {
                csvReportRow.add(report.getValue());
                added = true;
            }
            if (added) break;
        }
        return added;
    }

    // TODO:  This may add the report out of date order
    protected void addReportToCsvReportByAddingANewDateToCsvReport(ReportRecord report) {
        ArrayList<String> newRow = new ArrayList<>();
        newRow.add(DateTools.convertDateToStringWithFormat(report.getStartDate(), "MM/dd/yyyy"));
        newRow.add(DateTools.convertDateToStringWithFormat(report.getStopDate(), "MM/dd/yyyy"));
        int count = getCsvReportHeaderSize() - 3;     // for added values startDate, stopDate and value
        for (int i = 0; i < count; i++) newRow.add(VALUE_TO_USE_WHEN_THERE_IS_NO_VALUE_AVAILABLE);
        newRow.add(report.getValue());
        csvReport.add(newRow);
    }

    protected void addZerosForCsvReportDatesTheNewReportDidNotHaveValuesFor() {
        int csvReportHeaderSize = getCsvReportHeaderSize();
        for (List<String> csvReportRow : csvReport) {
            if (csvReportHeaderSize - csvReportRow.size() > 1) {
                //TODO: Add Handler
                System.out.println("ERROR");
            }
            if (csvReportHeaderSize > csvReportRow.size()) {
                csvReportRow.add(VALUE_TO_USE_WHEN_THERE_IS_NO_VALUE_AVAILABLE);
            }
        }
    }

    protected int getCsvReportHeaderSize() {
        return csvReport.get(CSV_REPORT_HEADER_ROW_INDEX).size();
    }

    private void addReportNameToHeader(String name) {
        csvReport.get(CSV_REPORT_HEADER_ROW_INDEX).add(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (List<String> reportRow : csvReport) {
            boolean first = true;
            for (String s : reportRow) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(s);
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
