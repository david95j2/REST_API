package com.example.restapi.utils;

public class Util {
    public static String convertToMySQLFormat(String input) {
        String datePart = input.substring(0, 8); // 20230829
        String timePart = input.substring(9);     // 161503

        String year = datePart.substring(0, 4);
        String month = datePart.substring(4, 6);
        String day = datePart.substring(6, 8);

        String hour = timePart.substring(0, 2);
        String minute = timePart.substring(2, 4);
        String second = timePart.substring(4, 6);

        return String.format("%s-%s-%s %s:%s:%s", year, month, day, hour, minute, second);
    }
    public static String convertFromMySQLFormat(String input) {
        String datePart = input.split(" ")[0];
        String timePart = input.split(" ")[1];

        String year = datePart.split("-")[0];
        String month = datePart.split("-")[1];
        String day = datePart.split("-")[2];

        String hour = timePart.split(":")[0];
        String minute = timePart.split(":")[1];
        String second = timePart.split(":")[2];

        return year + month + day + "_" + hour + minute + second;
    }
}
