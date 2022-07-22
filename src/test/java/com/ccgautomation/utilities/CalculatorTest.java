package com.ccgautomation.utilities;

import com.ccgautomation.data.Point;
import junit.framework.TestCase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalculatorTest extends TestCase {

    /* public static List<Point> calculateMeterDailyTotalsFromListOfPoints(List<Point> data) */

    public void test_calculateMeterDailyTotalsFromListOfPoints_Returns_Empty_List_With_Null_Data() {
        List<Point> results = Calculator.calculatePeriodicPartitionValuesFromListOfPoints(null);
        assertEquals(0, results.size());
    }

    public void test_calculateMeterDailyTotalsFromListOfPoints_Returns_Empty_List_With_Empty_Data() {
        List<Point> results = Calculator.calculatePeriodicPartitionValuesFromListOfPoints(new ArrayList<>());
        assertEquals(0, results.size());
    }
    public void test_calculateMeterDailyTotalsFromListOfPoints_Returns_Empty_List_With_One_Item_Of_Data() {
        List<Point> results = new ArrayList<>();
        results.add(new Point(new Date(0), 1f));
        results = Calculator.calculatePeriodicPartitionValuesFromListOfPoints(new ArrayList<>());
        assertEquals(0, results.size());
    }

    // Starts at Midnight
    public void test_calculateMeterDailyTotals_From_List_Of_Two_Midnight_Points_Returns_Valid_Data_With_One_Midnight_Item() {
        List<Point> results;
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point("1/1/2022 00:00:00 AM EST", "0f"));
            pointList.add(new Point("1/2/2022 00:00:00 AM EST", "1f"));
        }
        catch (Exception ex) {}
        results = Calculator.calculatePeriodicPartitionValuesFromListOfPoints(pointList);
        assertEquals(1, results.size());
        assertEquals("Sat Jan 01 00:00:00 EST 2022", results.get(0).getDate().toString());
        assertEquals(1f, results.get(0).getValue());
    }

/*
    // Starts at Midnight
    public void test_calculateMeterDailyTotalsFromListOfPoints_Returns_Valid_Data_With_Three_Midnight_Items() {
        List<Point> results = new ArrayList<>();
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point("1/1/2022 00:00:00 AM EST", "0f"));
            pointList.add(new Point("1/2/2022 00:00:00 AM EST", "1f"));
            pointList.add(new Point("1/3/2022 00:00:00 AM EST", "3f"));
        }
        catch (Exception ex) {}
        results = Calculator.calculateMeterDailyTotalsFromListOfPoints(pointList);
        assertEquals(2, results.size());
        assertEquals("Sat Jan 01 00:00:00 EST 2022", results.get(0).getDate().toString());
        assertEquals(1f, results.get(0).getValue());
        assertEquals("Sun Jan 02 00:00:00 EST 2022", results.get(1).getDate().toString());
        assertEquals(2f, results.get(1).getValue());
    }

    // Starts at Midnight
    public void test_calculateMeterDailyTotalsFromListOfPoints_Returns_Valid_Data_With_26_Hourly_Items() {
        List<Point> results = new ArrayList<>();
        List<Point> pointList = new ArrayList<>();
        Date date = DateTools.convertStringToDateWithFormat("1/1/2022 00:00:00 AM EST", Configuration.trendCSVFileDatePattern);
        for (int i = 0; i < 26; i++) {
            try {
                Long dateLong = date.getTime() + i * 3600000l;
                Float floatValue = (float)(i * 1.0);
                pointList.add(new Point(new Date(dateLong), floatValue));
            }
            catch (Exception ex) {}
        }
        results = Calculator.calculateMeterDailyTotalsFromListOfPoints(pointList);
        assertEquals(1, results.size());
        assertEquals("Sat Jan 01 00:00:00 EST 2022", results.get(0).getDate().toString());
        assertEquals(24f, results.get(0).getValue());
    }

    // Starts at Midnight
    public void test_calculateMeterDailyTotalsFromListOfPoints_Returns_Valid_Data_With_30000_Evenly_Spaced_Items() {
        List<Point> results = new ArrayList<>();
        List<Point> pointList = new ArrayList<>();
        Date date = DateTools.convertStringToDateWithFormat("1/1/2022 00:00:00 AM EST", Configuration.trendCSVFileDatePattern);
        for (int i = 0; i < 30000; i++) {
            try {
                Long dateLong = date.getTime() + i * 3000l;
                Float floatValue = (float)(i * 1.0);
                pointList.add(new Point(new Date(dateLong), floatValue));
            }
            catch (Exception ex) {}
        }
        results = Calculator.calculateMeterDailyTotalsFromListOfPoints(pointList);
        assertEquals(1, results.size());
        assertEquals("Sat Jan 01 00:00:00 EST 2022", results.get(0).getDate().toString());
        assertEquals(28800f, results.get(0).getValue());
    }

    // Starts at Midnight
    public void test_calculateMeterDailyTotalsFromListOfPoints_Returns_Valid_Data_With_30000_Unevenly_Spaced_Items() {
        Long subidivsions = 4444l;
        List<Point> results = new ArrayList<>();
        List<Point> pointList = new ArrayList<>();
        Date date = DateTools.convertStringToDateWithFormat("1/1/2022 00:00:00 AM EST", Configuration.trendCSVFileDatePattern);
        for (int i = 0; i < 30000; i++) {
            try {
                Long dateLong = date.getTime() + i * subidivsions;
                Float floatValue = (float)(i * 1.0);
                pointList.add(new Point(new Date(dateLong), floatValue));
            }
            catch (Exception ex) {}
        }
        results = Calculator.calculateMeterDailyTotalsFromListOfPoints(pointList);
        Float result = (float)86400000 / (float)subidivsions;
        assertEquals(1, results.size());
        assertEquals("Sat Jan 01 00:00:00 EST 2022", results.get(0).getDate().toString());
        assertEquals(result, results.get(0).getValue(),0.01f);
    }

    // Starts at Midnight
    public void test_calculateMeterDailyTotalsFromListOfPoints_Returns_Valid_Data_With_60000_Unevenly_Spaced_Items() {
        Long subidivsions = 4444l;
        List<Point> results = new ArrayList<>();
        List<Point> pointList = new ArrayList<>();
        Date date = DateTools.convertStringToDateWithFormat("1/1/2022 00:00:00 AM EST", Configuration.trendCSVFileDatePattern);
        for (int i = 0; i < 60000; i++) {
            try {
                Long dateLong = date.getTime() + i * subidivsions;
                Float floatValue = (float)(i * 1.0);
                pointList.add(new Point(new Date(dateLong), floatValue));
            }
            catch (Exception ex) {}
        }
        results = Calculator.calculateMeterDailyTotalsFromListOfPoints(pointList);
        Float result = (float)86400000 / (float)subidivsions;
        assertEquals(3, results.size());
        assertEquals("Sat Jan 01 00:00:00 EST 2022", results.get(0).getDate().toString());
        assertEquals(result, results.get(0).getValue(),0.01f);
        assertEquals("Sun Jan 02 00:00:00 EST 2022", results.get(1).getDate().toString());
        assertEquals(result, results.get(1).getValue(),0.01f);
        assertEquals("Mon Jan 03 00:00:00 EST 2022", results.get(2).getDate().toString());
        assertEquals(result, results.get(2).getValue(),0.01f);
    }

*/
    /* protected static Float calculatePeriodicValue(Point previousPoint, Point currentPoint) */
    /*            Day 1                   Day 2           Day 3          */
    /*      A       B       C       D       E       F       G       H    */

    public void test_calculatePeriodicValue_with_previous_value_before_midnight_and_current_value_after_midnight()
    throws ParseException, Exception
    {   // Points A and C
        Point previousPoint = new Point("1/1/2022 11:00:00 PM EST", "0f");
        Point currentPoint = new Point("1/2/2022 01:00:00 AM EST", "1f");
        Float midnightValue = Calculator.calculatePeriodicValue(previousPoint, currentPoint);
        assertEquals(0.5f, midnightValue);
    }

    public void test_calculatePeriodicValue_with_previous_and_current_values_one_day_apart()
            throws ParseException, Exception
    {   // Points B and E
        Point previousPoint = new Point("1/1/2022 00:00:00 AM EST", "0f");
        Point currentPoint = new Point("1/2/2022 00:00:00 AM EST", "1f");
        Float midnightValue = Calculator.calculatePeriodicValue(previousPoint, currentPoint);
        assertEquals(1f, midnightValue);
    }

    public void test_calculatePeriodicValue_with_previous_and_current_values_one_midnight_apart_with_negative_value()
            throws ParseException, Exception
    {   // Points B and E
        Point previousPoint = new Point("1/1/2022 00:00:00 AM EST", "2f");
        Point currentPoint = new Point("1/2/2022 00:00:00 AM EST", "1f");
        Float midnightValue = Calculator.calculatePeriodicValue(previousPoint, currentPoint);
        assertEquals(-1f, midnightValue);
    }

    public void test_calculatePeriodicValue_with_previous_and_current_values_before_report_time_results_in_total()
            throws ParseException, Exception
    {   // Points C and D
        Point previousPoint = new Point("1/1/2022 00:01:00 AM EST", "0f");
        Point currentPoint = new Point("1/1/2022 00:02:00 AM EST", "1f");
        Date midnightPoint = DateTools.incrementDate(DateTools.getThisMidnight(currentPoint.getDate()),1);
        Float midnightValue = Calculator.calculatePeriodicValue(previousPoint, currentPoint, midnightPoint);
        assertEquals(1f, midnightValue);
    }

    public void test_calculatePeriodicValue_with_current_point_before_previous_point_throws_Exception()
            throws ParseException, Exception
    {   // Points C and D
        Point previousPoint = new Point("1/1/2022 00:01:00 AM EST", "2f");
        Point currentPoint = new Point("1/1/2022 00:02:00 AM EST", "1f");
        Date midnightPoint = DateTools.incrementDate(DateTools.getThisMidnight(currentPoint.getDate()),1);
        try {
            Float midnightValue = Calculator.calculatePeriodicValue(currentPoint, previousPoint, midnightPoint);
            assert(false);
        }
        catch (Exception ex) {
            assertEquals("Previous Date is after Current Date.", ex.getMessage());
        }
    }

    public void test_calculatePeriodicValue_with_previous_value_before_midnight_and_current_value_after_next_midnight_throws_Exception()
            throws ParseException
    {   // Points A and F
        Point previousPoint = new Point("1/1/2022 11:00:00 PM EST", "0f");
        Point currentPoint = new Point("1/3/2022 01:00:00 AM EST", "1f");
        try {
            Float midnightValue = Calculator.calculatePeriodicValue(previousPoint, currentPoint);
            assertEquals(0.5f, midnightValue);
            assert(false);
        }
        catch (Exception ex) {
            assertEquals("Previous and Current Dates are more than 1 day apart.", ex.getMessage());
        }
    }
}