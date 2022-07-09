package com.ccgautomation.utilities;

import junit.framework.TestCase;

public class DateToolsTest extends TestCase {

    public void testConvertDateWithoutSeconds() {
        String testString = "2/1/2017 08:20:15 AM EST";
        DateTools dateTools = new DateTools(Configuration.trendCSVFileDatePattern);
        try {
            assertEquals(1485955215000L, dateTools.convertStringToDate(testString).getTime());
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void testRemoveDecimalSecondsWithDecimalSeconds() {
        String testString = "2/1/2017 08:20:15 AM EST";
        DateTools dateTools = new DateTools(Configuration.trendCSVFileDatePattern);
        assertEquals("2/1/2017 08:20:15 AM EST", dateTools.removeDecimalPortionOfSeconds(testString));
        try {
            assertEquals(1485955215000L,dateTools.convertStringToDate(dateTools.removeDecimalPortionOfSeconds(testString)).getTime());
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void testWhenTextDataIsSent() {
        String testString = "Akron Children's Hospital / Campus Metering & Monitoring / Campus Electric Metering Overview / Main Hospital N. - Electric Meter (Sub #3) / Modbus kWh";
        DateTools dateTools = new DateTools(Configuration.trendCSVFileDatePattern);
        System.out.println(dateTools.removeDecimalPortionOfSeconds(testString));
        try {
            System.out.println(dateTools.convertStringToDate(testString));
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}