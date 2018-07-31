package com.cashEquityProject.cashEquity.extras;

public class TimeComparator {

    /*
     * Time is of type String. Format is hh:mm
     */

    static int compare(String o1, String o2) {

        String[] timeFields1 = o1.split(":");
        String[] timeFields2 = o2.split(":");

        Integer hour1 = Integer.parseInt(timeFields1[0]);
        Integer min1 = Integer.parseInt(timeFields1[1]);

        Integer hour2 = Integer.parseInt(timeFields2[0]);
        Integer min2 = Integer.parseInt(timeFields2[1]);

        int hourCmp = hour1.compareTo(hour2);

        if (hourCmp == 0) {
            return min1.compareTo(min2);
        } else {
            return hourCmp;
        }

    }
}
