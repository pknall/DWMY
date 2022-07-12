package com.ccgautomation.utilities;

import com.ccgautomation.data.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pknall on 3/6/2018.
 */
public class FileUtilities {

    public static String readTextFileAsString(String path)
    {
        StringBuilder sb = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return sb.toString();
    }


    public static void writeTextFileAsString(String content, String path)
    {
        File file = new File(path);
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] contentInBytes =  content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static List<String> readCSVFileIntoAListOfString(String filename){
        List<String> results = new ArrayList<>();
        String line;

        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                results.add(line);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return results;
    }

    /*
     Returns a two-column CSV list of Date and Value.
     The original data file is organized as follows:
     (line 1)  Path of Trend Point  (skipFirstTwoRows == 0)
     (line 2)  Column headers (4)   (skipFirstTwoRows == 1)
     (line 3+) Date, Excel Date, Value, Notes
     If there are notes in column 4, there is no value in column 3.
     */
    public static List<String> readWebCtrlTrendCSVFileIntoAListOfStrings(String filename){
        List<String> results = new ArrayList<>();
        String line;
        int skipFirstTwoRows = 0;

        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                if (skipFirstTwoRows++ > 1) {
                    line = StringTools.removeDoubleQuotesFromStrings(line);
                    String[] fields = line.split(",");
                    if ((fields != null)
                            && (fields.length > 2)
                            && (fields[0].length() > 0)
                            && (fields[2].length() > 0)) {
                        String dateString = DateTools.removeDecimalPortionOfSeconds(fields[0]);
                        String valueString = fields[2];
                        results.add(StringTools.removeDoubleQuotesFromStrings( dateString+ "," + valueString));
                    }
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return results;
    }
}
