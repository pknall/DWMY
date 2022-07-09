package com.ccgautomation.utilities;

import com.ccgautomation.data.MeterReport;
import com.ccgautomation.data.MeterReports;
import com.ccgautomation.data.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ccgautomation.utilities.DateTools.isTheNextDay;

// Until I think of a better name
public class Calculator {


    protected static List<Point> calculateDailyTotalsFromRawList(List<Point> data) {
        List<Point> results = new ArrayList<>();

        if (data == null) return null;
        if (data.size() < 2) return null;

        Date currentDate = new Date(0);
        Date previousDate = new Date(0);
        Float previousValue = 0f;
        Float currentValue = 0f;
        boolean first = true;

        for(Point p : data) {

            currentDate = p.getDate();
            currentValue = p.getValue();

            if (currentValue == 0) continue;

            if (first) {
                first = false;
                previousDate = currentDate;
                previousValue = currentValue;
                continue;
            }

            if (isTheNextDay(previousDate, currentDate)) {
                Float newValue = currentValue - previousValue;
                try {
                    Point newPoint = new Point(DateTools.getThisMidnight(previousDate), newValue);
                    results.add(newPoint);
                }
                catch (Exception ex) {
                    // TODO: Fix Logger
                    Logger.log(ex.getMessage());
                }

                previousDate = currentDate;
                previousValue = currentValue;
            }
        }

        Float newValue = currentValue - previousValue;
        try {
            Point newPoint = new Point(DateTools.getThisMidnight(previousDate), newValue);
            results.add(newPoint);
        }
        catch (Exception ex) {
            // TODO: Fix Logger
            Logger.log(ex.getMessage());
        }

        return results;
    }


    /*
     * 1) The meter report being recorded has data to add to a row that already exists in the Results list
     * 2) The meter report being recorded has data that DOES NOT exist in the Results list (add new item to list wr2 previous reports)
     * 3) The meter report being recorded DOES NOT have data for pre-existing items int eh Results list
     */
    public static List<MeterReport> processMonthlyMeterTotals(List<Point> data, List<Date> meterRecordDates) {
        List<MeterReport> results = new ArrayList<>();
        Date currentMeterDate = meterRecordDates.remove(0);
        Date previousMeterDate = new Date(0);

        if (data == null) return null;
        if (data.size() < 2) return null;

        Date currentDate = new Date(0);
        Date previousDate = new Date(0);
        Float previousValue = 0f;
        Float currentValue = 0f;
        boolean first = true;
        boolean firstMeterRead = true;

        //TODO : Fix break hack
        boolean done = false;
        MPL: for(Point p : data) {

            currentDate = p.getDate();
            currentValue = p.getValue();
            if (currentValue == 0) continue;

            if (first) {
                first = false;
                while (currentMeterDate.before(currentDate)) {
                    if (meterRecordDates.size() == 0) {
                        done = true;
                        break;
                    }
                    previousMeterDate = currentMeterDate;
                    currentMeterDate = meterRecordDates.remove(0);
                }
                if (done) break;

                previousDate = currentDate;
                previousValue = currentValue;
                continue;
            }

            if (currentDate.after(currentMeterDate)) {
                Float newValue = currentValue - previousValue;
                MeterReport newPoint = new MeterReport(previousMeterDate, DateTools.incrementDate(currentMeterDate, -1), newValue);

                //TODO:  Fix this hack
                if (!firstMeterRead) results.add(newPoint);
                firstMeterRead = false;

                previousDate = currentDate;
                previousValue = currentValue;

                if (meterRecordDates.size() == 0) break;
                previousMeterDate = currentMeterDate;
                currentMeterDate = meterRecordDates.remove(0);
            }
        }

        Float newValue = currentValue - previousValue;
        MeterReport newPoint = new MeterReport(previousMeterDate, currentMeterDate, newValue);
        results.add(newPoint);

        return results;
    }



    public static void main(String[] args) {

        List<String> files = getFileList();
        MeterReports meterReports = new MeterReports();
        String path = "C:\\";
        for (String file : files) {
            System.out.println(file);
            List<Date> meterRecordDates = getDateList();
            String newPath = "C:\\";
            String newFileName = "_new_";
            try {
                newPath = file.substring(0, file.lastIndexOf("\\")+1);
                path = newPath;
                newFileName = file.substring(file.lastIndexOf("\\")+1);
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            List<String> data = FileUtilities.readCSVFileAsListOfString(file);
            FileUtilities.writeTextFileAsString(StringTools.convertStringListToString(data), newPath + "data\\" + newFileName + "_kwh.csv");

            List<Point> points = new WebCTRLCSVTrendFileTools().preprocessMeterData(data);
            FileUtilities.writeTextFileAsString(StringTools.convertStringListToString(Point.convertPointListToStringList(points)), newPath + "data\\" + newFileName + "_kwh_points.csv");

            List<Point> dailyTotals = calculateDailyTotalsFromRawList(points);
            FileUtilities.writeTextFileAsString(StringTools.convertStringListToString(Point.convertPointListToStringList(dailyTotals)), newPath + "data\\" + newFileName + "_kwh_daily.csv");

            List<MeterReport> meterTotals = processMonthlyMeterTotals(points, meterRecordDates);
            FileUtilities.writeTextFileAsString(StringTools.convertStringListToString(Point.convertPointListToStringList(dailyTotals)), newPath + "data\\" + newFileName + "_kwh_meter_totals.csv");
            meterReports.addMeterReport(meterTotals, newFileName.substring(0,newFileName.indexOf(".")));

        }
        System.out.println(meterReports);
        FileUtilities.writeTextFileAsString(meterReports.toString(), path +  "_totals.csv");
    }

    @NotNull
    private static List<String> getFileList() {
        List<String> files = new ArrayList<>();
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub3.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub4.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub5.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub6.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub7.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub8.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub9.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub10.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub11.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub12.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\sub13.csv");

        files.add("C:\\Users\\pknall\\Desktop\\Meters\\cbv2.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\cbv3.csv");

        files.add("C:\\Users\\pknall\\Desktop\\Meters\\kjp_pa.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\kjp_pb.csv");

        files.add("C:\\Users\\pknall\\Desktop\\Meters\\lb_t1.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\lb_t2.csv");

        files.add("C:\\Users\\pknall\\Desktop\\Meters\\rmh.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\pd1.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\epd-mdp.csv");
        files.add("C:\\Users\\pknall\\Desktop\\Meters\\mdp.csv");


        files.add("C:\\Users\\pknall\\Desktop\\Meters\\campus_total.csv");
        return files;
    }

    @NotNull
    private static List<Date> getDateList() {
        List<Date> meterRecordDates = new ArrayList<>();
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("03/14/2017", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("04/13/2017", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("05/12/2017", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("06/13/2017", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("07/13/2017", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("08/11/2017", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("09/12/2017", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("10/13/2017", "MM/dd/yyyy"));

        meterRecordDates.add(DateTools.convertStringToDateWithFormat("11/11/2017", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("12/13/2017", "MM/dd/yyyy"));

        meterRecordDates.add(DateTools.convertStringToDateWithFormat("01/15/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("02/14/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("03/14/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("04/13/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("05/12/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("06/13/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("07/14/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("08/14/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("09/12/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("10/11/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("11/10/2018", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("12/12/2018", "MM/dd/yyyy"));

        meterRecordDates.add(DateTools.convertStringToDateWithFormat("01/12/2019", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("02/13/2019", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("03/14/2019", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("04/12/2019", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("05/14/2019", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("06/13/2019", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("07/13/2019", "MM/dd/yyyy"));
        meterRecordDates.add(DateTools.convertStringToDateWithFormat("08/13/2019", "MM/dd/yyyy"));
        return meterRecordDates;
    }
}
