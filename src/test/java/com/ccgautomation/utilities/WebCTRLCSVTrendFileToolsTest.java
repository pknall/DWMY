package com.ccgautomation.utilities;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class WebCTRLCSVTrendFileToolsTest extends TestCase {

    public void testPreprocessMeterData() {
    }

    public void testRemovePathAndHeadingFromList() {
        WebCTRLCSVTrendFileTools tools = new WebCTRLCSVTrendFileTools();
        List<String> stringList = new ArrayList<>();
        stringList.add("one");
        stringList.add("two");
        stringList.add("three");

        tools.removePathAndHeadingFromData(stringList);
        assertEquals(1, stringList.size());
        assertEquals("three", stringList.get(0));
    }
}