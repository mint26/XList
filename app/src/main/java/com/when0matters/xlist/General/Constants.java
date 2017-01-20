package com.when0matters.xlist.General;

/**
 * Created by HuiMin on 1/17/2017.
 */

public class Constants {

    public static String UPDATE_ITEM = "UPDATE_ITEM";
    public static String ITEM_UPDATED = "ITEM_UPDATED";
    public static String MODE = "MODE";
    public static String TODOITEM = "TODO_ITEM";
    public static String ERROR_MESSAGE = "ERROR_MESSAGE";

    public static String MISSING_TASKNAME = "Task name required.";
    public static String INVALID_DATE = "Due date must be later than today date.";

    public static int EDIT_ITEM_REQUEST_CODE = 0;
    public static int ADD_ITEM_REQUEST_CODE = 1;

    public static class Mode
    {
        public static int ADD = 0;
        public static int EDIT = 1;
        public static int VIEW = 2;
    }
}
