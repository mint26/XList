package com.when0matters.xlist.Adapters;

/**
 * Created by HuiMin on 1/17/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.when0matters.xlist.Entity.ToDoItem;
import com.when0matters.xlist.R;

import java.util.ArrayList;

public class ToDoItemsAdapter<T> extends ArrayAdapter<ToDoItem> implements CompoundButton.OnCheckedChangeListener {

    public ToDoItemsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ToDoItemsAdapter(Context context, int resource, ArrayList<ToDoItem> toDoItems) {
        super(context, resource, toDoItems);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cbStatus = (CheckBox) view.findViewById(R.id.cbStatus);
            viewHolder.tvTask= (TextView) view.findViewById(R.id.tvTaskName);
            viewHolder.tvMthYear= (TextView) view.findViewById(R.id.tvMthYear);
            viewHolder.tvPriorityText = (TextView) view.findViewById(R.id.tvPriorityText);
            viewHolder.tvDay= (TextView) view.findViewById(R.id.tvDay);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        ToDoItem toDoItem = getItem(position);
        // Populate the data into the template view using the data object
        if (toDoItem != null) {
            viewHolder.tvTask.setText(toDoItem.task);
            viewHolder.tvMthYear.setText(toDoItem.getDueDateMonthYear());
            viewHolder.tvDay.setText(Integer.toString(toDoItem.dueDateDay));
            viewHolder.tvPriorityText.setText(toDoItem.getPriorityString());
            viewHolder.cbStatus.setChecked(toDoItem.isCompleted);
            viewHolder.cbStatus.setTag(position);
            viewHolder.cbStatus.setOnCheckedChangeListener(this);

            int priorityColor = R.color.colorGreen;
            if (toDoItem.priority == ToDoItem.PriorityEnum.High.ordinal()){
                priorityColor = R.color.colorRed;
            }
            else if (toDoItem.priority == ToDoItem.PriorityEnum.Medium.ordinal()){
                priorityColor = R.color.colorYellow;
            }
            viewHolder.tvMthYear.setTextColor(view.getResources().getColor(priorityColor));
            viewHolder.tvDay.setTextColor(view.getResources().getColor(priorityColor));
        }
        // Return the completed view to render on screen
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        int position = (int)buttonView.getTag();
        ToDoItem toDoItem = getItem(position);
        toDoItem.isCompleted = isChecked;
        toDoItem.save();
    }




    static class ViewHolder{
        TextView tvTask;
        TextView tvMthYear;
        TextView tvDay;
        TextView tvPriorityText;
        CheckBox cbStatus;
    }
}

