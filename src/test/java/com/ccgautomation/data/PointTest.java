package com.ccgautomation.data;

import com.ccgautomation.utilities.Configuration;
import com.ccgautomation.utilities.DateTools;
import junit.framework.TestCase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PointTest extends TestCase {

    public void testGetDateWithDateObjectGiven() {
        String dateString = "1/1/1970 12:00:00 AM EST";
        Date date = new DateTools().convertStringToDateWithFormat(dateString, Configuration.trendCSVFileDatePattern);
        Point newPoint = new Point(date, 0f);
        String retrievedDateString = new DateTools().convertDateToStringWithFormat(newPoint.getDate(), Configuration.trendCSVFileDatePattern);
        assertEquals(dateString, retrievedDateString);
    }

    public void testGetValueWithFloatObjectGiven() {
        String dateString = "1/1/1970 12:00:00 AM EST";
        Date date = new DateTools().convertStringToDateWithFormat(dateString, Configuration.trendCSVFileDatePattern);
        Point newPoint = new Point(date, 0f);
        assertEquals(0f, newPoint.getValue());
    }

    public void testGetDateWithDateStringGiven() {
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

    public void testGetValueWithFloatStringGiven() {
        String dateString = "1/1/1970 12:00:00 AM EST";
        try {
            Point newPoint = new Point(dateString, "101.7");
            assertEquals(101.7f, newPoint.getValue());
        }
        catch(Exception ex) {
            assert(false);
        }
    }

    public void testTestToString() {
        Point newPoint = new Point(new Date(0), 0f);
        assertEquals("12/31/1969 07:00:00 PM EST,0.0", newPoint.toString());
    }

    public void testConvertPointListToStringList() {
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            pointList.add(new Point(new Date(i*3600000), (float)i));
        }
        List<String> stringList = Point.convertPointListToStringList(pointList);

        assertEquals("12/31/1969 07:00:00 PM EST,0.0",stringList.get(0).toString());
        assertEquals("12/31/1969 08:00:00 PM EST,1.0",stringList.get(1).toString());
        assertEquals("12/31/1969 09:00:00 PM EST,2.0",stringList.get(2).toString());

    }

    public void testPointWithNullValueThrowsException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point(new Date(0), null));
            assert(false);
        }
        catch(Exception ex) {
            assertEquals("Value cannot be null.", ex.getMessage());
        }
    }

    public void testPointWithNullDateThrowsException() {
        List<Point> pointList = new ArrayList<>();
        try {
            pointList.add(new Point(null, 0f));
            assert(false);
        }
        catch(Exception ex) {
            assertEquals("Date cannot be null.", ex.getMessage());
        }
    }

    public void testPointWithZeroLengthStringForDateValueThrowsParseException() {
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

    public void testPointWithInvalidStringForDateValueThrowsParseException() {
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

    public void testPointWithNullStringForDateValueThrowsNullPointerException() {
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

    public void testPointWithZeroLengthStringForValueThrowsNumberFormatException() {
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

    public void testPointWithInvalidStringForValueThrowsNumberFormatException() {
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

    public void testPointWithNullStringForValueThrowsNullPointerException() {
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
}