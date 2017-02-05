package com.when0matters.xlist.Helper;

import com.when0matters.xlist.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HuiMin on 1/31/2017.
 */

public class DateHelper {

    private static SimpleDateFormat sdf_default = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_default));
    private static SimpleDateFormat sdf_month_year = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_month_year));
    private static SimpleDateFormat sdf_day = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_day));
    private static SimpleDateFormat sdf_day_month_year = new SimpleDateFormat(ResourceHelper.getString(R.string.sdf_day_month_year));

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
}
