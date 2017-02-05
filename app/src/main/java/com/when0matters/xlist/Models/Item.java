package com.when0matters.xlist.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.when0matters.xlist.DB.XListContract;
import com.when0matters.xlist.DB.XListSQLiteHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HuiMin on 1/28/2017.
 */

public class Item implements Parcelable {

    private int _Id;
    private String Name;
    private String Description;
    private String DueDate;
    private int Priority;
    private int IsCompleted;
    private Category Category;

    //region constructor, getter and setter
    public Item(){}

    public Item(int _Id, String name, String description, String dueDate, int priority, int isCompleted, Category category) {
        this._Id = _Id;
        Name = name;
        Description = description;
        DueDate = dueDate;
        Priority = priority;
        IsCompleted = isCompleted;
        Category = category;
    }



    public int get_Id() {
        return _Id;
    }

    public void set_Id(int _Id) {
        this._Id = _Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDueDate() {
        return DueDate;
    }


    public void setDueDate(String date) {
        DueDate = date;
    }

    public int getDay(){
        String day = getDueDate().substring(6);
        return Integer.parseInt(day);
    }

    public int getMonth(){
        String mth = getDueDate().substring(4,6);
        return Integer.parseInt(mth) - 1;
    }

    public int getYear(){
        String year = getDueDate().substring(0,4);
        return Integer.parseInt(year);
    }

    public Date getDate(){
        Calendar c = Calendar.getInstance();
        c.set(getYear(), getMonth(), getDay(), 0, 0);
        return c.getTime();
    }

    public int getPriority() {
        return Priority;
    }

    public String getPriorityString(){

        if (this.getPriority() == Item.PriorityEnum.High.ordinal())
            return Item.PriorityEnum.High.toString();
        else if (this.getPriority() == Item.PriorityEnum.Medium.ordinal())
            return Item.PriorityEnum.Medium.toString();
        else
            return Item.PriorityEnum.Low.toString();
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

    public int getIsCompleted() {
        return IsCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        IsCompleted = isCompleted;
    }

    //endregion

    //region parceable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(getName());
        dest.writeString(getDescription());
        dest.writeInt(getPriority());
        dest.writeString(getDueDate());
        dest.writeInt(get_Id());
        dest.writeInt(getIsCompleted());
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    private Item(Parcel in) {
        setName(in.readString());
        setDescription(in.readString());
        setPriority(in.readInt());
        setDueDate(in.readString());
        set_Id(in.readInt());
        setIsCompleted(in.readInt());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static int GetPriorityOrdinal(String priorityString){
        if (priorityString == Item.PriorityEnum.High.toString())
            return Item.PriorityEnum.High.ordinal();
        else if (priorityString== Item.PriorityEnum.Medium.toString())
            return Item.PriorityEnum.Medium.ordinal();
        else
            return Item.PriorityEnum.Low.ordinal();

    }
    //endregion

    //region sql query
    public static List<Item> getAllItems(XListSQLiteHelper xListSQLiteHelper, boolean includePastItems){

        SQLiteDatabase db = xListSQLiteHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                XListContract.ItemSchema._ID,
                XListContract.ItemSchema.COLUMN_NAME,
                XListContract.ItemSchema.COLUMN_DESCRIPTION,
                XListContract.ItemSchema.COLUMN_DUEDATE,
                XListContract.ItemSchema.COLUMN_PRIORITY,
                XListContract.ItemSchema.COLUMN_ISCOMPLETED
        };


        String sortOrder =
                XListContract.ItemSchema.COLUMN_DUEDATE + " ASC";

        //String whereClause = XListContract.ItemSchema.COLUMN_DUEDATE+"=?";

        Cursor cursor = db.query(
                XListContract.ItemSchema.TABLE_NAME,                     // The table to query
                projection,                          // The columns to return
                null,                                // The columns for the WHERE clause
                null,                                // The values for the WHERE clause
                null,                                // don't group the rows
                null,                                // don't filter by row groups
                sortOrder                            // The sort order
        );

        List<Item> items = new ArrayList<Item>();
        while(cursor.moveToNext()) {
            Item item = new Item(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2),cursor.getString(3), cursor.getInt(4),cursor.getInt(5), null);
            items.add(item);
        }
        cursor.close();

        return items;
    }

    public static Item getItem(XListSQLiteHelper xListSQLiteHelper, int id){

        SQLiteDatabase db = xListSQLiteHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                XListContract.ItemSchema._ID,
                XListContract.ItemSchema.COLUMN_NAME,
                XListContract.ItemSchema.COLUMN_DESCRIPTION,
                XListContract.ItemSchema.COLUMN_DUEDATE,
                XListContract.ItemSchema.COLUMN_PRIORITY,
                XListContract.ItemSchema.COLUMN_ISCOMPLETED
        };

        String selection = XListContract.ItemSchema._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        String sortOrder =
                XListContract.ItemSchema.COLUMN_DUEDATE + " DESC";

        Cursor cursor = db.query(
                XListContract.ItemSchema.TABLE_NAME,       // The table to query
                projection,                          // The columns to return
                selection,                           // The columns for the WHERE clause
                selectionArgs,                       // The values for the WHERE clause
                null,                                // don't group the rows
                null,                                // don't filter by row groups
                sortOrder                            // The sort order
        );
        Item item = null;
        if (cursor != null) {
            cursor.moveToFirst();
            item = new Item(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2),cursor.getString(3), cursor.getInt(4),cursor.getInt(5), null);
        }
        cursor.close();

        return item;
    }

    public static long addItem(XListSQLiteHelper xListSQLiteHelper, Item item){

        SQLiteDatabase db = xListSQLiteHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(XListContract.ItemSchema.COLUMN_NAME, item.getName());
        values.put(XListContract.ItemSchema.COLUMN_DESCRIPTION, item.getDescription());
        values.put(XListContract.ItemSchema.COLUMN_DUEDATE, item.getDueDate());
        values.put(XListContract.ItemSchema.COLUMN_PRIORITY, item.getPriority());
        values.put(XListContract.ItemSchema.COLUMN_ISCOMPLETED, item.getIsCompleted());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(XListContract.ItemSchema.TABLE_NAME, null, values);

        return newRowId;
    }


    public static int updateItem(XListSQLiteHelper xListSQLiteHelper, Item item){
        SQLiteDatabase db = xListSQLiteHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(XListContract.ItemSchema.COLUMN_NAME, item.getName());
        values.put(XListContract.ItemSchema.COLUMN_DESCRIPTION, item.getDescription());
        values.put(XListContract.ItemSchema.COLUMN_DUEDATE, item.getDueDate());
        values.put(XListContract.ItemSchema.COLUMN_PRIORITY, item.getPriority());
        values.put(XListContract.ItemSchema.COLUMN_ISCOMPLETED, item.getIsCompleted());

        // Which row to update, based on the title
        String selection = XListContract.ItemSchema._ID + " = ?";
        String[] selectionArgs = { String.valueOf(item.get_Id()) };

        int count = db.update(
                XListContract.ItemSchema.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

    public static void deleteItem(XListSQLiteHelper xListSQLiteHelper ,int id){

        SQLiteDatabase db = xListSQLiteHelper.getReadableDatabase();
        // Define 'where' part of query.
        String selection = XListContract.ItemSchema._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(XListContract.ItemSchema.TABLE_NAME, selection, selectionArgs);
    }

    //endregion


    public enum PriorityEnum{
        High,
        Medium,
        Low
    }
}
