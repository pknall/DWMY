package com.ccgautomation.data;

import com.ccgautomation.utilities.Configuration;
import com.ccgautomation.utilities.DateTools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Point {

    private Date date;
    private Float value;

    public Point(String dateString, String valueString) throws ParseException, NumberFormatException, NullPointerException {
        if (dateString == null) throw new NullPointerException("Date cannot be null.");
        if (valueString == null) throw new NullPointerException("Value cannot be null.");
        DateTools dateTools = new DateTools(Configuration.trendCSVFileDatePattern);
        this.date = dateTools.convertStringToDate(dateString);
        value = Float.parseFloat(valueString);
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
        List<String> results = new ArrayList<>();
        for (Point p : points) {
            results.add(p.toString());
        }
        return results;
    }
}
