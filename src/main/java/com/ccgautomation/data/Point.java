package com.ccgautomation.data;

import com.ccgautomation.utilities.Configuration;
import com.ccgautomation.utilities.DateTools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    Construction of this object guarantees that all fields are valid objects of the required type.
    If any fields are not valid during construction, the constructor throws the appropriate exception.
 */

public class Point {

    private final Date date;
    private final Float value;

    public Point(String dateString, String valueString) throws ParseException, NumberFormatException, NullPointerException {
        if (dateString == null) throw new NullPointerException("Date cannot be null.");
        if (valueString == null) throw new NullPointerException("Value cannot be null.");
        DateTools dateTools = new DateTools(Configuration.trendCSVFileDatePattern);
        this.date = dateTools.convertStringToDate(dateString);
        this.value = Float.parseFloat(valueString);
    }

    public Point(Date date, Float value) throws NullPointerException {
        if (date == null) throw new NullPointerException("Date cannot be null.");
        if (value == null) throw new NullPointerException("Value cannot be null.");
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public Float getValue() {
        return value;
    }

    @Override
    public String toString(){
        String dateString = new DateTools().convertDateToStringWithFormat(date, Configuration.datePattern);
        return dateString + "," + value.toString();
    }

    public static List<String> convertPointListToStringList(List<Point> points) {
        if (pointListIsNull(points)) return new ArrayList<>();
        List<String> results = new ArrayList<>();
        for (Point p : points) {
            results.add(p.toString());
        }
        return results;
    }

    private static boolean pointListIsNull(List<Point> points) {
        return points == null;
    }
}
