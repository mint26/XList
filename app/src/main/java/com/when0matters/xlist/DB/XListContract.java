package com.when0matters.xlist.DB;

import android.provider.BaseColumns;

/**
 * Created by HuiMin on 1/29/2017.
 */

public class XListContract {

    //To prevent someone from instantiating contract accidentally
    private XListContract(){};

    public static class ItemSchema implements BaseColumns {
        public static final String TABLE_NAME = "todoitems";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DUEDATE = "duedate";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_ISCOMPLETED = "is_completed";
        public static final String CATEGORY_COLUMN_FK = "category_id";
    }

    public static class CategorySchema implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME = "name";
    }

}
