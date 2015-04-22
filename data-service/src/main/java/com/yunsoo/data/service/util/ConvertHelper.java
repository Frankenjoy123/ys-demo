package com.yunsoo.data.service.util;

/**
 * Created by admin on 2015/3/8.
 */
public class ConvertHelper {
    public static String joinLongArray(String split, long[] input) {
        String output = "";
        for (int i=0;i<input.length;i++){
            if (i<input.length - 1) {
                output += Long.toString(input[i]) + split;
            } else {
                output += Long.toString(input[i]);
            }
        }
        return output;
    }
}
