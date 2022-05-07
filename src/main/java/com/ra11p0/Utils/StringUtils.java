package com.ra11p0.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StringUtils {
    public static String findCommon(List<String> inputArray){ return findCommon(new ArrayList<>(inputArray)); }
    public static String findCommon(ArrayList<String> inputArray) {
        ArrayList<String> arr = new ArrayList<>();
        for(String string : inputArray) arr.add(string.replaceAll("-", " "));
        ArrayList<String> outputArray = new ArrayList<>();
        for(String string : arr) {
            StringBuilder result = new StringBuilder();
            String[] words = string.split(" ");
            for (String word : words) {
                if (word.length() > 1) {
                    boolean contains = true;
                    for (String name : arr) {
                        if (!name.toUpperCase(Locale.ROOT).contains(word.toUpperCase(Locale.ROOT))) {
                            contains = false;
                            break;
                        }
                    }
                    if (contains) result.append(word).append(" ");
                }
            }
            outputArray.add(result.toString());
        }
        int length = 0;
        int index = 0;
        for(String string : outputArray){
            if(string.split(" ").length > length){
                index = outputArray.indexOf(string);
                length = string.split(" ").length;
            }
        }
        if(outputArray.isEmpty()) return "";
        return outputArray.get(index);
    }
}
