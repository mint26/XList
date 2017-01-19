package com.when0matters.xlist.General;

import com.when0matters.xlist.Entity.ToDoItem;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by HuiMin on 1/18/2017.
 */

public class ItemComparator implements Comparator<ToDoItem> {

    public int compare(ToDoItem item1, ToDoItem item2) {
        int date1 = item1.getDueDateYear() * 10000 + item1.getDueDateMth() * 100 + item1.getDueDateDay();
        int date2 = item2.getDueDateYear() * 10000 + item2.getDueDateMth() * 100 + item2.getDueDateDay();

        if (date1 > date2)
            return 1;
        else if (date1 < date2)
            return -1;
        else
            return 0;
    }

    public boolean isPastDates(ToDoItem item1,int curDate){
        int date1 = item1.getDueDateYear() * 10000 + item1.getDueDateMth() * 100 + item1.getDueDateDay();

        if (curDate > date1)
            return true;

        return false;
    }

    public int getCurrentDate(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return year * 10000 +  (month + 1) * 100 + day;

    }


}