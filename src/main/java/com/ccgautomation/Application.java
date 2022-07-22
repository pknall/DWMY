package com.ccgautomation;

import com.ccgautomation.data.Point;
import com.ccgautomation.utilities.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Application {


    public static void main(String[] args) {

        List<String> files = getFileList();
        List<Date> meterRecordDates = getDateList();

        //CsvReport meterReports = new CsvReport();
        String path = "C:\\";
        for (String file : files) {
            System.out.println(file);
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
            List<String> data = FileUtilities.readWebCtrlTrendCSVFileIntoAListOfStrings(file);
            FileUtilities.writeTextFileAsString(StringTools.convertStringListToString(data), newPath + "data\\" + newFileName + "_step1.csv");

            List<Point> points = Point.convertListOfStringsToListOfPoints(data);
            FileUtilities.writeTextFileAsString(StringTools.convertStringListToString(Point.convertListOfPointsToListOfStrings(points)), newPath + "data\\" + newFileName + "_step2.csv");

            List<Point> dailyTotals = Calculator.convertListOfPointsToListOfPeriodicValues(points);
            FileUtilities.writeTextFileAsString(StringTools.convertStringListToString(Point.convertListOfPointsToListOfStrings(dailyTotals)), newPath + "data\\" + newFileName + "_step3.csv");

            //List<ReportRecord> meterTotals = Calculator.calculateMonthlyTotalsFromDailyTotals(dailyTotals, meterRecordDates);
            //FileUtilities.writeTextFileAsString(StringTools.convertStringListToString(Point.convertPointListToStringList(dailyTotals)), newPath + "data\\" + newFileName + "_step4.csv");
            //meterReports.addReport(meterTotals, newFileName.substring(0,newFileName.indexOf(".")));

        }
        //System.out.println(meterReports);
        //FileUtilities.writeTextFileAsString(meterReports.toString(), path +  "_totals.csv");
    }

    @NotNull
    private static List<String> getFileList() {
        List<String> files = new ArrayList<>();
        files.add("C:\\data\\kwh.csv");
        /*
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

         */
        return files;
    }

    @NotNull
    private static List<Date> getDateList() {
        List<Date> meterRecordDates = new ArrayList<>();
        /*
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

         */
        return meterRecordDates;
    }


}
