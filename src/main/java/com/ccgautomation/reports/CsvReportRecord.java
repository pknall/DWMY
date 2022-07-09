package com.ccgautomation.reports;
import com.ccgautomation.utilities.DateTools;

import java.util.ArrayList;
import java.util.List;

/*
    A Report consisting of a 2D array of Strings based on the ReportRecordObject and its subclasses.
    This report is not a sparse or jagged 2D array.  All cells wil be populated.
 */

public class CsvReportRecord {

    ArrayList<ArrayList<String>> csvReport = new ArrayList<>();
    
    public CsvReportRecord() {
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
        Three possible results to adding a report to the list:
        1) The date the data occurred exists in the results list and needs to be added to an
           existing results  List<String>
        2) The date the data occurred on does not exist and a new List<String> needs to be added to results
        3) The results List<String> contains dates that do not occur in the report and need to be zeroed
     */
    private void addReportToReportList(ReportRecord report) {
        boolean added = false;

        //for (List<String> reportRow : results) {
        for (int i = 1; i < csvReport.size(); i++) {
            List<String> reportRow = csvReport.get(i);

            // Data exists in Report and a Date exists in Results
            if (reportRow.get(0).contains(DateTools.convertDateToStringWithFormat(report.getStartDate(), "MM/dd/yyyy"))) {
                reportRow.add(report.getValue());
                added = true;
            }
        }

        // Date does not exist in Results → Add A Date to the Rerport
        if (!added) {
            ArrayList<String> newRow = new ArrayList<>();
            newRow.add(DateTools.convertDateToStringWithFormat(report.getStopDate(), "MM/dd/yyyy"));
            newRow.add(DateTools.convertDateToStringWithFormat(report.getStopDate(), "MM/dd/yyyy"));

            // Account for any reports that have already been entered by adding "No Data" in their place
            int count = csvReport.get(0).size();
            count -= 3;
            for (int i = 0; i < count; i++) newRow.add("0");

            newRow.add(report.getValue());
            csvReport.add(newRow);
        }

        // The report did not have values to add to the Results matrix
        int count = csvReport.get(0).size()-1;
        for (List<String> reportRow : csvReport) {
            // Should never have to add more than 1 row
            if (count - reportRow.size() > 1) {
                System.out.println("ERROR");
            }
            if (reportRow.size() < count) {
                reportRow.add("0");
            }
        }
        
    }

    private void addReportNameToHeader(String name) {
        csvReport.get(0).add(name);
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
