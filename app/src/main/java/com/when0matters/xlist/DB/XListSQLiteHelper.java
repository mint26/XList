package com.when0matters.xlist.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.when0matters.xlist.DB.XListContract.CategorySchema;
import com.when0matters.xlist.DB.XListContract.ItemSchema;

/**
 * Created by HuiMin on 1/29/2017.
 */

public class XListSQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "XList.db";
    private static final int DATABASE_VERSION = 1;
    private static XListSQLiteHelper mSQLiteHelper = null;


    private XListSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized XListSQLiteHelper getInstance(Context context){
        if (mSQLiteHelper == null)
            mSQLiteHelper =  new XListSQLiteHelper(context);

        return mSQLiteHelper;
    }
    //Create item table query
    private static final String TODO_TABLE_CREATE = "create table " + ItemSchema.TABLE_NAME
            + "( "
            + ItemSchema._ID + " integer primary key autoincrement, "
            + ItemSchema.COLUMN_NAME + " text not null, "
            + ItemSchema.COLUMN_DESCRIPTION + " text, "
            + ItemSchema.COLUMN_DUEDATE + " date, "
            + ItemSchema.COLUMN_PRIORITY + " integer default 0, "
            + ItemSchema.COLUMN_ISCOMPLETED + " integer default 0, "
            + ItemSchema.CATEGORY_COLUMN_FK + " integer, "
            + "foreign key(" + ItemSchema.CATEGORY_COLUMN_FK + ") "
            + "references " + CategorySchema.TABLE_NAME + "(" + CategorySchema._ID + ")"
            + ");";


    //Create category table query
    private static final String CATEGORY_TABLE_CREATE = "create table " + CategorySchema.TABLE_NAME
            + "( "
            + CategorySchema._ID + " integer primary key autoincrement, "
            + CategorySchema.COLUMN_NAME + " text not null "
            + ");";


    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(CATEGORY_TABLE_CREATE);
        database.execSQL(TODO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(XListSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ItemSchema.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategorySchema.TABLE_NAME);
        onCreate(db);
    }

}


