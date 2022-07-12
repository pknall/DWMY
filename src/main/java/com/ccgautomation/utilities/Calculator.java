package com.ccgautomation.utilities;

import com.ccgautomation.data.Point;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;


// Until I think of a better name
public class Calculator {


    public static List<Point> calculateMeterDailyTotalsFromListOfPoints(List<Point> data) {
        List<Point> results = new ArrayList<>();

        if (data == null) return results;
        if (data.size() < 2) return results;

        Point currentPoint = data.get(0);
        Point previousPoint = data.get(0);

        boolean first = true;

        for(Point p : data) {
            currentPoint = p;

            if (first) {
                first = false;
                previousPoint = currentPoint;
                continue;
            }

            if (DateTools.isTheNextDay(previousPoint.getDate(), currentPoint.getDate())) {
                //TODO: Make this return a midnight point instead of a value;
                Float newValue = interpolateValue(previousPoint, currentPoint);

                try {
                    Point newPoint = new Point(DateTools.getThisMidnight(previousPoint.getDate()), newValue);
                    results.add(newPoint);
                }
                catch (Exception ex) {
                    // TODO: Fix Logger
                    Logger.log(ex.getMessage());
                }
                //TODO:  Change this such that it will shift to the previous midnight value
                previousPoint = currentPoint;
            }
        }

        /*
        Float newValue = currentPoint.getValue() - previousPoint.getValue();
        try {
            Point newPoint = new Point(DateTools.getThisMidnight(previousPoint.getDate()), newValue);
            results.add(newPoint);
        }
        catch (Exception ex) {
            // TODO: Fix Logger
            Logger.log(ex.getMessage());
        }
        */

        return results;
    }

    private static Float interpolateValue(Point previousPoint, Point currentPoint) {
        Float result = 0f;
        Date midnight = DateTools.getThisMidnight(currentPoint.getDate());

        Long currentPointTimeLong = currentPoint.getDate().getTime();
        Long previousPointTimeLong = previousPoint.getDate().getTime();
        Long midnightTimeLong = midnight.getTime();

        Long period = currentPointTimeLong - previousPointTimeLong;
        Long midnightPeriod = midnightTimeLong - previousPointTimeLong;

        Float difference = currentPoint.getValue() - previousPoint.getValue();
        Float factor = ((float)midnightPeriod / (float)period);
        result += difference * factor;
        if (result < 0) result = 0f;

        return result;
    }


    /*
     * 1) The meter report being recorded has data to add to a row that already exists in the Results list
     * 2) The meter report being recorded has data that DOES NOT exist in the Results list (add new item to list wr2 previous reports)
     * 3) The meter report being recorded DOES NOT have data for pre-existing items int eh Results list
     */
    /*
    public static List<ReportRecord> calculateMonthlyTotalsFromDailyTotals(List<Point> data, List<Date> meterRecordDates) {
        List<ReportRecord> results = new ArrayList<>();
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
                try {
                    MeterReportRecord newPoint = new MeterReportRecord(previousMeterDate, DateTools.incrementDate(currentMeterDate, -1), newValue);

                    //TODO:  Fix this hack
                    if (!firstMeterRead) results.add(newPoint);
                    firstMeterRead = false;
                }
                catch (NullPointerException ex) {
                    //TODO: Fix Logger
                    Logger.log(ex.getMessage());
                }

                previousDate = currentDate;
                previousValue = currentValue;

                if (meterRecordDates.size() == 0) break;
                previousMeterDate = currentMeterDate;
                currentMeterDate = meterRecordDates.remove(0);
            }
        }

        Float newValue = currentValue - previousValue;
        try {
            MeterReportRecord newPoint = new MeterReportRecord(previousMeterDate, currentMeterDate, newValue);
            results.add(newPoint);
        }
        catch (NullPointerException ex) {
            //TODO: Fix Logger
            Logger.log(ex.getMessage());
        }

        return results;
    }

*/

}
