package com.ccgautomation.utilities;

import com.ccgautomation.data.Point;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// Until I think of a better name
public class Calculator {


    /*
        Use Case:  Convert a list of arbitrary points to a list of readings starting from startDate to endDate
        at regular intervals of duration (ms).
     */
    public List<Point> convertListOfPointsToPeriodicValues(List<Point> pointList, Integer duration, Date startDate, Date endDate) {
        List<Point> results = new ArrayList<>();
        if (pointList == null) return results;
        if (pointList.size() < 2) return results;

        // TODO: Periodic Readings
        // Date nextPartition = DateTools.incrementMS(startDate, duration);

        Integer currentIndex = 0;

        Point previousPoint = pointList.get(currentIndex++);
        Date previousPointAtMidnight = DateTools.getThisMidnight(previousPoint.getDate());
        results.add(new Point(previousPointAtMidnight, previousPoint.getValue()));

        Point currentPoint;
        Date currentPointAtMidnight;

        while (currentIndex <= pointList.size()) {
            currentPoint = pointList.get(currentIndex);
            currentPointAtMidnight = DateTools.getThisMidnight(currentPoint.getDate());     // Does this occur during the same partition/day...if so, skip
            if (currentPointAtMidnight.after(previousPointAtMidnight)) {
                Float value = 0f;
                try {
                    value = calculatePeriodicValue(previousPoint, currentPoint);
                }
                catch (Exception ex) {
                    // previous and current are more than 1 day apart
                }
                results.add(new Point(currentPointAtMidnight, value));
                previousPoint = currentPoint;
                previousPointAtMidnight = currentPointAtMidnight;
            }
            currentIndex++;
        }

        return results;
    }


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

            Float newValue = 0f;
            if (currentPoint.getDate().after(midnightTomorrow)) {
                try {
                    newValue = calculatePeriodicValue(previousPoint, currentPoint);
                }
                catch (Exception ex) {
                    // More than one day apart
                }
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
        Calculates the value that occurs between (previousPoint, currentPoint] when the dates of those points
        straddle the reporting frame.

        Interpolates between two values where previousPoint<reportTime<=currentPoint

        If previousPoint < currentPoint < reportTime

        This method assumes that the currentPoint date immediately proceeds previousPoint date.
     */
    protected static Float calculatePeriodicValue(Point previousPoint, Point currentPoint, Date reportTime) throws Exception {
        Float result = 0f;

        if (currentPoint.getDate().getTime() - previousPoint.getDate().getTime() > Configuration.MILLISECONDS_IN_A_DAY) {
            throw new Exception("Previous and Current Dates are more than 1 day apart.");
        }
        if (previousPoint.getDate().after(currentPoint.getDate())) {
            throw new Exception("Previous Date is after Current Date.");
        }

        Long currentPointTimeLong = currentPoint.getDate().getTime();
        Long previousPointTimeLong = previousPoint.getDate().getTime();
        Long reportTimeLong = reportTime.getTime();

        Long period = currentPointTimeLong - previousPointTimeLong;
        //Long midnightPeriod = reportTimeLong - previousPointTimeLong;
        Long midnightPeriod = (reportTime.getTime() > currentPoint.getDate().getTime()) ? period : reportTimeLong - previousPointTimeLong;

        Float difference = currentPoint.getValue() - previousPoint.getValue();
        Float factor = ((float)midnightPeriod / (float)period);
        result +=(difference * factor);

        return result;
    }

    /*
        If not specified, assume daily.
     */
    protected static Float calculatePeriodicValue(Point previousPoint, Point currentPoint) throws Exception {
        return calculatePeriodicValue(previousPoint, currentPoint, DateTools.getThisMidnight(currentPoint.getDate()));
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
