package com.ccgautomation.data;
import com.ccgautomation.utilities.DateTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeterReports {

    ArrayList<ArrayList<String>> results = new ArrayList<>();

    public void addMeterReport(List<MeterReport> meterReport, String name) {

        if (results.size() == 0) {
            ArrayList<String> header = new ArrayList<>();
            header.add("Start Date");
            header.add("Stop Date");
            results.add(header);
        }
        results.get(0).add(name);
        boolean first = true;
        for (MeterReport report : meterReport) {

            boolean added = false;
            for (List<String> reportRow : results) {
                // Skip Header Row
                if (first) {
                    first = false;
                    continue;
                }
                // Data exists in Report and a Date exists in Results
                if (reportRow.get(0).contains(DateTools.convertDateToStringWithFormat(report.startDate, "MM/dd/yyyy"))) {
                    reportRow.add(report.value.toString());
                    added = true;
                }
            }
            // Date does not exist in Results → Add A Date to the Rerport
            if (!added) {
                ArrayList<String> newRow = new ArrayList<>();
                newRow.add(DateTools.convertDateToStringWithFormat(report.startDate, "MM/dd/yyyy"));
                newRow.add(DateTools.convertDateToStringWithFormat(report.stopDate, "MM/dd/yyyy"));

                // Account for any reports that have already been entered by adding "No Data" in their place
                int count = results.get(0).size();
                count -= 3;
                for (int i = 0; i < count; i++) newRow.add("0");

                newRow.add(report.value.toString());
                results.add(newRow);
            }
            // The report did not have values to add to the Results matrix
            int count = results.get(0).size()-1;
            for (List<String> reportRow : results) {
                // Should never have to add more than 1 row
                if (count - reportRow.size() > 1) {
                    System.out.println("ERROR");
                }
                if (reportRow.size() < count) {
                    reportRow.add("0");
                }
            }

        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (List<String> reportRow : results) {
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
