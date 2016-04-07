/**
 * Copyright (c) 2016 Adrian Tilita <adrian@tilita.ro>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package layout.helper.component;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Result parser - Parse a service result for the JTable result
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0.0
 * @since       2016-04
 */
public class ResultParser {
    public static final String FILE_NAME = "filename";
    public static final String FILE_SIZE = "filesize";
    public static final String FILE_MODIFIED = "filemodified";

    /**
     * Converts resulted data with necessary listing data
     * @param data
     * @return 
     */
    public static ArrayList<ArrayList<Map<String, String>>> parse(Map<String, ArrayList<String>> data) {
        // instantiate the result
        ArrayList<ArrayList<Map<String,String>>> result = new ArrayList<>();
        // convert hashmap so we can use it to iterate (lambda limits scope)
        ArrayList<ArrayList<String>> convertedList = new ArrayList<>();
        data.forEach(
            (hash, fileListArray) -> {
                convertedList.add(fileListArray);
            }
        );
        for (ArrayList<String> list: convertedList) {
            ArrayList<Map<String,String>> temp = new ArrayList<>();
            for (String file: list) {
                Map<String, String> fileDetails = ResultParser.getFileInfo(file);
                temp.add(fileDetails);
            }
            result.add(temp);
        }
        return result;
    }

    /**
     * Get the file information based on the file path
     * @param file
     * @return 
     */
    private static Map<String, String> getFileInfo(String file) {
        HashMap<String, String> result = new HashMap<>();
        File fileObject = new File(file);
        result.put(ResultParser.FILE_NAME, file);
        result.put(ResultParser.FILE_SIZE, ResultParser.convertSize(fileObject.length()));
        result.put(ResultParser.FILE_MODIFIED, ResultParser.convertTime(fileObject.lastModified()));
        return result;
    }

    /**
     * Convert bytes to GB/MB/KB
     * @todo    Move to "bricks.util" class
     * @param bytes
     * @return 
     */
    public static String convertSize(long bytes) {
        String size = "";
        if (bytes > 1073741824) {
            size += round(bytes / 1073741824, 2) + " GB";
        } else if (bytes > 1048576) {
            size += round(bytes / 1048576, 2) + " MB";
        } else {
            size += round(bytes / 1024, 2) + " KB";
        }
        return size;
    }

    /**
     * Round to decimals
     * @param value
     * @param places
     * @return 
     */
    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Convert a long to a string time format
     * @todo    Move to "bricks.util" class
     * @param time
     * @return 
     */
    public static String convertTime(long time){
        java.util.Date date = new java.util.Date(time);
        java.text.Format format = new java.text.SimpleDateFormat("YYYY-MM-dd HH:mm");
        return format.format(date);
    }
}
