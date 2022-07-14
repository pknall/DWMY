package com.ccgautomation.utilities;

import com.ccgautomation.data.Point;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// Until I think of a better name
public class Calculator {


    /*
        Possible Conditions:
        More than one day may pass between Previous and Current Date Trends.
        The VALUE of the current point may be less than the VALUE of the previous point
        There may be several thousand data points per day
        Maybe do both ways?

        the first day should total value from the first trend point to the next day's midnight
        each midnight point should be calculated and recorded in the Midnight Log, which is used for the totals
        → The individual data points are used for the day-to-day calculation
        → The individual values are still used for the running total
     */
    public static List<Point> calculateMeterDailyTotalsFromListOfPoints(List<Point> data) {

        List<Point> results = new ArrayList<>();
        List<Point> midnightPointLog = new ArrayList<>();
        if (data == null) return results;
        if (data.size() < 2) return results;

        int currentPointIndex = 1;
        int previousPointIndex = 0;
        int dataSize = data.size();
        Point previousPoint = data.get(0);
        Point currentPoint;
        Point previousMidnightPoint = new Point(previousPoint.getDate(), previousPoint.getValue());
        midnightPointLog.add(previousMidnightPoint);
        Point currentMidnightPoint;
        Float runningTotal = 0f;

        Date midnightTomorrow = DateTools.incrementDate(DateTools.getThisMidnight(data.get(previousPointIndex).getDate()), 1);

        while (currentPointIndex <= dataSize) {
            previousPoint = data.get(previousPointIndex);
            currentPoint = data.get(currentPointIndex);

            boolean resetPrevious = false;
            while (previousPoint.getDate().after(midnightTomorrow)) {
                results.add(new Point(midnightTomorrow, 0f));
                midnightTomorrow = DateTools.incrementDate(midnightTomorrow, 1);
                resetPrevious = true;
            }
            if (resetPrevious) {
                midnightTomorrow = DateTools.incrementDate(DateTools.getThisMidnight(data.get(previousPointIndex).getDate()), 1);
            }

            if (currentPoint.getDate().after(midnightTomorrow)) {
                Float newValue = calculateMidnightPointBetween(previousPoint, currentPoint);
                currentMidnightPoint = new Point(midnightTomorrow, newValue);
                try {
                    Point newPoint = new Point(DateTools.getThisMidnight(previousPoint.getDate()), newValue);
                    results.add(newPoint);
                }
                catch (Exception ex) {
                    // TODO: Fix Logger
                    Logger.log(ex.getMessage());
                }
                //TODO:  Change this such that it will shift to the previous midnight value
                previousPointIndex = currentPointIndex;  // TODO: Wonky
            }
            currentPointIndex++;
        }

        return results;
    }

    /*
      This method assumes that the currentPoint date immediately proceeds previousPoint date.
     */
    private static Float calculateMidnightPointBetween(Point previousPoint, Point currentPoint) {
        Float result = 0f;

        if (currentPoint.getDate().getTime() - previousPoint.getDate().getTime() > Configuration.MILLISECONDS_IN_A_DAY) {
            // Something bad has happened
        }

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
