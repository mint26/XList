package com.when0matters.xlist.Entity;

/**
 * Created by HuiMin on 1/17/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by HuiMin on 1/15/2017.
 */

public class ToDoItem extends SugarRecord implements Parcelable {

    public String task;
    public String description;

    public int dueDateDay;
    public int dueDateMth;
    public int dueDateYear;
    public int priority;
    public boolean isCompleted;

    public ToDoItem(){
    }


    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public int getDueDateDay() {
        return dueDateDay;
    }

    public void setDueDateDay(int dueDateDay) {
        this.dueDateDay = dueDateDay;
    }

    public int getDueDateMth() {
        return dueDateMth;
    }

    public void setDueDateMth(int dueDateMth) {
        this.dueDateMth = dueDateMth;
    }

    public int getDueDateYear() {
        return dueDateYear;
    }

    public void setDueDateYear(int dueDateYear) {
        this.dueDateYear = dueDateYear;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDateYear + "-" + dueDateMth + "-" + dueDateDay;
    }

    public String getDueDateMonthYear(){

        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String month = months[dueDateMth - 1];
        return month + " " + dueDateYear;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(task);
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeInt(dueDateDay);
        dest.writeInt(dueDateMth);
        dest.writeInt(dueDateYear);
        if (getId() != null)
            dest.writeLong(getId());

        //dest.writeInt(isCompleted);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ToDoItem createFromParcel(Parcel in) {
            return new ToDoItem(in);
        }
        public ToDoItem[] newArray(int size) {
            return new ToDoItem[size];
        }
    };
    private ToDoItem(Parcel in) {
        task = in.readString();
        description = in.readString();
        priority = in.readInt();
        dueDateDay = in.readInt();
        dueDateMth = in.readInt();
        dueDateYear = in.readInt();
        setId(in.readLong());

    }

    @Override
    public int describeContents() {
        return 0;
    }
    public String getPriorityString(){

        if (this.priority == PriorityEnum.High.ordinal())
            return PriorityEnum.High.toString();
        else if (this.priority == PriorityEnum.Medium.ordinal())
            return PriorityEnum.Medium.toString();
        else
            return PriorityEnum.Low.toString();
    }

    public int GetPriorityOrdinal(String priorityString){
        if (priorityString == PriorityEnum.High.toString())
            return PriorityEnum.High.ordinal();
        else if (priorityString== PriorityEnum.Medium.toString())
            return PriorityEnum.Medium.ordinal();
        else
            return PriorityEnum.Low.ordinal();

    }

    public enum PriorityEnum{
        High,
        Medium,
        Low
    }
}
