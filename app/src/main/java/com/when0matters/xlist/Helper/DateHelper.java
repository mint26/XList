package com.when0matters.xlist.Helper;

import com.when0matters.xlist.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HuiMin on 1/31/2017.
 */

public class DateHelper {

    private static SimpleDateFormat sdf_default = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_default));
    private static SimpleDateFormat sdf_month_year = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_month_year));
    private static SimpleDateFormat sdf_day = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_day));
    private static SimpleDateFormat sdf_day_month_year = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_day_month_year));
    private static SimpleDateFormat sdf_year_month_day = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_year_month_year));

    public static String formatDate(Date date) {
        return sdf_default.format(date);
    }

    public static String formatMonthYear(Date date) {
        return sdf_month_year.format(date);
    }

    public static String formatDay(Date date) {
        return sdf_day.format(date);
    }

    public static String formatDayMonthYear(Date date) {
        return sdf_day_month_year.format(date);
    }

    public static String formatYearMonthDay(Date date) {
        return sdf_year_month_day.format(date);
    }

    public static int GetYear(Date date){
        String formattedDate = formatDate(date);
        if (!formattedDate.isEmpty()){
            String tmp = formattedDate.substring(0,4);
            return Integer.parseInt(tmp);
        }
        return -1;
    }

    public static int GetMonth(Date date){
        String formattedDate = formatDate(date);
        if (!formattedDate.isEmpty()){
            String tmp = formattedDate.substring(4,6);
            return Integer.parseInt(tmp) - 1;
        }
        return -1;
    }

    public static int GetDay(Date date){
        String formattedDate = formatDate(date);
        if (!formattedDate.isEmpty()){
            String tmp = formattedDate.substring(6);
            return Integer.parseInt(tmp);
        }
        return -1;
    }

    public static String GetCurrentDateString(){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date dateWithoutTime = cal.getTime();

        return formatYearMonthDay(dateWithoutTime);
    }
}
