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
    /*
    public List<Point> convertListOfPointsToListOfPeriodicValues(List<Point> pointList, Integer duration, Date startDate, Date endDate) {
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
                value = createPeriodicValue(previousPoint, currentPoint);
                results.add(new Point(currentPointAtMidnight, value));
                previousPoint = currentPoint;
                previousPointAtMidnight = currentPointAtMidnight;
            }
            currentIndex++;
        }

        return results;
    }

     */

    // Assume Daily Totals if startDate and duration are not provided
    public static List<Point> convertListOfPointsToListOfPeriodicValues(List<Point> data) {
        if ((data == null) || (data.size() < 2)) return new ArrayList<>();
        Date partitionStartDate = DateTools.getThisMidnight(data.get(0).getDate());
        return convertListOfPointsToListOfPeriodicValues(data, partitionStartDate, Configuration.MILLISECONDS_IN_A_DAY);
    }


    /*
        Need to normalize the points across partitions
        If the Current Point is AT the Data Partition, RECORD CURRENT and INCREMENT the Data Partition
        If the Current Point is BEFORE the Data Partition, then
            If the Next Point is BEFORE or AT the Next Data Partition, then proceed to next point
            (Straddle) If the Next Point is AFTER the next Partition, the LINEARIZE and RECORD
        If the Current Point is AFTER the Data Partition, then INCREMENT the Data Partition

     */
    public static List<Point> convertListOfPointsToListOfPeriodicValues(List<Point> data, Date partitionStartDate, Integer duration) {

        if ((data == null) || (data.size() < 2) || (partitionStartDate == null) || (duration == null) || (duration <= 0))
            return new ArrayList<>();

        List<Point> results = new ArrayList<>();

        int currentPointIndex = 0;

        Point currentPoint;

        // Initialize Current Partition Date
        Date currentPartitionDate = partitionStartDate;
        if (currentPartitionDate.before(data.get(0).getDate())) {
            currentPartitionDate = DateTools.incrementDate(currentPartitionDate,1);
        }
        Date nextPartitionDate = DateTools.incrementMS(currentPartitionDate, duration);

        while (currentPointIndex < data.size()) {
            currentPoint = data.get(currentPointIndex);

            if (areTheSameTime(currentPoint.getDate(), currentPartitionDate, Configuration.ONE_MINUTE)) {
                createPeriodicPoint(results, currentPartitionDate, currentPoint.getValue());
                currentPartitionDate = nextPartitionDate;
                nextPartitionDate = DateTools.incrementMS(currentPartitionDate, duration);
            }
            else {
                if ((data.size() - currentPointIndex) > 0) {
                    if  (currentPoint.getDate().before(currentPartitionDate)){
                        Point nextPoint = data.get(currentPointIndex + 1);
                        if (nextPoint.getDate().after(nextPartitionDate)) {
                            Float newValue = createPeriodicValue(currentPoint, nextPoint);
                            createPeriodicPoint(results, currentPartitionDate, newValue);
                            currentPartitionDate = nextPartitionDate;
                            nextPartitionDate = DateTools.incrementMS(currentPartitionDate, duration);
                        } else {
                            // Only increment if currentPoint if before currentPartition and nextPoint is
                            // at or before the nextPartition
                            currentPointIndex++;
                        }
                    } else {
                        // Increment the partition if the current point is at or after the current Partition
                        currentPartitionDate = nextPartitionDate;
                        nextPartitionDate = DateTools.incrementMS(currentPartitionDate, duration);
                    }
                } else {
                    // Do Nothing
                }
                /*
                if ((data.size() - currentPointIndex) > 0){
                    // Current Point is at or after the next partition time
                    Point nextPoint = data.get(currentPointIndex + 1);
                    if (!nextPoint.getDate().before(nextPartitionDate)) {
                        Float newValue = createPeriodicValue(currentPoint, nextPoint);
                        createPeriodicPoint(results, currentPartitionDate, newValue);
                        currentPartitionDate = nextPartitionDate;
                        nextPartitionDate = DateTools.incrementMS(currentPartitionDate, duration);
                    }
                }
                else {
                }

                 */
            }
            currentPointIndex++;
        }
        return results;
    }

    private static boolean areTheSameTime(Date date1, Date date2, Long tolerance) {
        return (Math.abs(date1.getTime() - date2.getTime()) < tolerance);
    }

    private static void createPeriodicPoint(List<Point> results, Date currentPartitionDate, Float newValue) {
        try {
            results.add( new Point(currentPartitionDate, newValue));
        }
        catch (Exception ex) {
            // TODO: Fix Logger
            Logger.log(ex.getMessage());
        }
    }

    private static Float createPeriodicValue(Point previousPoint, Point currentPoint) {
        try {
            return calculatePeriodicValue(previousPoint, currentPoint);
            // Need to use this for the next partition's calculation?  No.  Just calculation partition values here
        }
        catch (Exception ex) {
            // More than one partition apart ??
        }
        return 0f;
    }

    /*
        Calculates the value that occurs between (previousPoint, currentPoint] when the dates of those points
        straddle the reporting frame.
        If previousPoint and currentPoint occur before the report time, the difference between previousPoint and
        currentPoint is returned.
/g
        This method assumes that the currentPoint date immediately proceeds previousPoint date.
     */
    protected static Float calculatePeriodicValue(Point previousPoint, Point currentPoint, Date partitionTime) throws Exception {
        if ( (currentPoint.getDate().getTime() - previousPoint.getDate().getTime()) > Configuration.MILLISECONDS_IN_A_DAY) {
            throw new Exception("Previous and Current Dates are more than 1 day apart.");
        }
        if (previousPoint.getDate().after(currentPoint.getDate())) {
            throw new Exception("Previous Date is after Current Date.");
        }
        Long currentPointTimeLong = currentPoint.getDate().getTime();
        Long previousPointTimeLong = previousPoint.getDate().getTime();
        Long partitionTimeLong = partitionTime.getTime();

        Long period = currentPointTimeLong - previousPointTimeLong;
        Long resultTimeLong = (partitionTime.after(currentPoint.getDate())) ? period : partitionTimeLong - previousPointTimeLong;

        Float difference = currentPoint.getValue() - previousPoint.getValue();
        Float factor = ((float)resultTimeLong / (float)period);

        return difference * factor;
    }

    /*
        If not specified, assume a midnight partitionTime.
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
