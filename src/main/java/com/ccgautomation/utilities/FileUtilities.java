package com.ccgautomation.utilities;

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

    public static List<String> readCSVFileAsListOfString(String filename){
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

}
