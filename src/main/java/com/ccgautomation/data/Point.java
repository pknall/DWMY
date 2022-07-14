package com.ccgautomation.data;

import com.ccgautomation.utilities.Configuration;
import com.ccgautomation.utilities.DateTools;
import com.ccgautomation.utilities.Logger;
import com.ccgautomation.utilities.StringTools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    Construction of this object guarantees that all fields are valid objects of the required type.
    If any fields are not valid during construction, the constructor throws the appropriate exception.

    Valid is used by the calculation class to determine show that this point, when included in a report, was
    calculated smoothly or if there was a difference between the midnight totals and accumulation (Decorator Pattern?)
 */

public class Point {

    private final Date date;
    private final Float value;
    private Float percentValid;

    public Point(String dateString, String valueString) throws ParseException, NumberFormatException, NullPointerException {
        if (dateString == null) throw new NullPointerException("Date cannot be null.");
        if (valueString == null) throw new NullPointerException("Value cannot be null.");
        DateTools dateTools = new DateTools(Configuration.trendCSVFileDatePattern);
        this.date = dateTools.convertStringToDate(dateString);
        this.value = Float.parseFloat(valueString);
        this.percentValid = 100f;
    }

    public Point(Date date, Float value) throws NullPointerException {
        if (date == null) throw new NullPointerException("Date cannot be null.");
        if (value == null) throw new NullPointerException("Value cannot be null.");
        this.date = date;
        this.value = value;
        this.percentValid = 100f;
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

    public static List<String> convertListOfPointsToListOfStrings(List<Point> points) {
        List<String> results = new ArrayList<>();
        if (points == null) return results;
        if (points.size() == 0) return results;
        StringBuilder sb = new StringBuilder();
        for (Point p : points) {
            results.add(p.toString());
        }
        return results;
    }

    public static List<Point> convertListOfStringsToListOfPoints(List<String> data) {
        List<Point> points = new ArrayList<>();
        if (data == null) return points;
        for (String s : data) {
            if (s == null) continue;
            String[] fields = s.split(",");
            if (fields.length < 2) continue;
            String dateString = fields[0];
            String valueString = fields[1];
            try {
                points.add(new Point(dateString, valueString));
            }
            // TODO: Fix Logger
            catch (ParseException ex) {  Logger.log(ex.getMessage()); }
            catch (NumberFormatException ex) {  Logger.log(ex.getMessage()); }
            catch (NullPointerException ex) {  Logger.log(ex.getMessage()); }
        }
        return points;
    }

    public Float getPercentValid() { return percentValid; }
    public void setPercentValid(Float percentValid) { this.percentValid = percentValid; }
}
