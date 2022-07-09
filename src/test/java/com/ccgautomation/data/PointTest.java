package com.ccgautomation.data;

import com.ccgautomation.utilities.Configuration;
import com.ccgautomation.utilities.DateTools;
import junit.framework.TestCase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PointTest extends TestCase {

    /* Point(Date date, Float value) */

    public void test_Get_Date_With_Date_Object_Given() {
        String dateString = "1/1/1970 12:00:00 AM EST";
        Date date = new DateTools().convertStringToDateWithFormat(dateString, Configuration.trendCSVFileDatePattern);
        Point newPoint = new Point(date, 0f);
        String retrievedDateString = new DateTools().convertDateToStringWithFormat(newPoint.getDate(), Configuration.trendCSVFileDatePattern);
        assertEquals(dateString, retrievedDateString);
    }

    public void test_Get_Value_With_Float_Object_Given() {
        Point newPoint = new Point(new Date(0), 8675309f);
        assertEquals(8675309f, newPoint.getValue());
    }

    public void test_Point_With_Null_Date_Throws_NullPointerException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point(null, 0f));
            assert(false);
        }
        catch(NullPointerException ex) {
            assertEquals("Date cannot be null.", ex.getMessage());
        }
    }

    public void test_Point_Wit_Null_Value_Throws_NullPointerException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point(new Date(0), null));
            assert(false);
        }
        catch(NullPointerException ex) {
            assertEquals("Value cannot be null.", ex.getMessage());
        }
    }

    /* Point(String dateString, String valueString) */

    public void test_Point_With_Zero_Length_String_For_Date_Value_Throws_ParseException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point("", "0f"));
            assert(false);
        }
        catch(ParseException ex) {

        }
        catch(NumberFormatException ex) {
            assert(false);
        }
        catch(NullPointerException ex) {
            assert(false);
        }
    }

    public void test_Point_With_Zero_Length_String_For_Value_Throws_Number_Format_Exception() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point("1/1/1970 12:00:00 AM EST", ""));
            assert(false);
        }
        catch(ParseException ex) {
            System.out.println(ex.getMessage());
            assert(false);
        }
        catch(NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void test_Point_With_Null_String_For_Date_Value_Throws_NullPointerException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point(null, ""));
            assert(false);
        }
        catch(ParseException ex) {
            assert(false);
        }
        catch(NumberFormatException ex) {
            assert(false);
        }
        catch(NullPointerException ex) {
            assertEquals("Date cannot be null.", ex.getMessage());
        }
    }

    public void test_Point_With_Null_String_For_Value_Throws_NullPointerException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point("1/1/1970 12:00:00 AM EST", null));
            assert(false);
        }
        catch(ParseException ex) {
            assert(false);
        }
        catch(NumberFormatException ex) {
            assert(false);
        }
        catch(NullPointerException ex) {

        }
    }

    public void test_Point_With_Invalid_String_For_Date_Value_Throws_ParseException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point("invalid", "0f"));
            assert(false);
        }
        catch(ParseException ex) {
            System.out.println(ex.getMessage());
        }
        catch(NumberFormatException ex) {
            System.out.println(ex.getMessage());
            assert(false);
        }
    }

    public void test_Point_With_Invalid_String_For_Value_Throws_NumberFormatException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point("1/1/1970 12:00:00 AM EST", "invalid"));
            assert(false);
        }
        catch(ParseException ex) {
            System.out.println(ex.getMessage());
            assert(false);
        }
        catch(NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /* Date getDate() */

    public void test_Get_Date_With_Date_String_Given() {
        String dateString = "1/1/1970 12:00:00 AM EST";
        try {
            Point newPoint = new Point(dateString, "0");
            String retrievedDateString = new DateTools().convertDateToStringWithFormat(newPoint.getDate(), Configuration.trendCSVFileDatePattern);
            assertEquals(dateString, retrievedDateString);
        }
        catch(Exception ex) {
            assert(false);
        }
    }

    /* Float getValue() */

    public void test_Get_Value_With_Float_String_Given() {
        String dateString = "1/1/1970 12:00:00 AM EST";
        try {
            Point newPoint = new Point(dateString, "101.7");
            assertEquals(101.7f, newPoint.getValue());
        }
        catch(Exception ex) {
            assert(false);
        }
    }

    /* String toString() */

    public void test_ToString() {
        Point newPoint = new Point(new Date(0), 0f);
        assertEquals("12/31/1969 07:00:00 PM EST,0.0", newPoint.toString());
    }

    /*  List<String> convertPointListToStringList(List<Point> points) */

    public void test_Convert_PointList_To_StringList() {
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            pointList.add(new Point(new Date(i*3600000), (float)i));
        }
        List<String> stringList = Point.convertPointListToStringList(pointList);

        assertEquals("12/31/1969 07:00:00 PM EST,0.0",stringList.get(0).toString());
        assertEquals("12/31/1969 08:00:00 PM EST,1.0",stringList.get(1).toString());
        assertEquals("12/31/1969 09:00:00 PM EST,2.0",stringList.get(2).toString());

    }

    public void test_Convert_PointList_To_StringList_With_Null_PointList_Returns_Empty_StringList() {
        List<String> stringList = Point.convertPointListToStringList(null);
        assertEquals(0, stringList.size());
    }

    public void test_Convert_PointList_To_StringList_With_Empty_PointList_Returns_Empty_StringList() {
        List<String> stringList = Point.convertPointListToStringList(new ArrayList<Point>());
        assertEquals(0, stringList.size());
    }

}