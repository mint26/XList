package com.when0matters.xlist.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.when0matters.xlist.Activity.AddItemActivity;
import com.when0matters.xlist.Application.XListApplication;
import com.when0matters.xlist.DB.XListSQLiteHelper;

import com.when0matters.xlist.Helper.Constants;
import com.when0matters.xlist.Helper.DateHelper;
import com.when0matters.xlist.Helper.ResourceHelper;
import com.when0matters.xlist.Models.Item;
import com.when0matters.xlist.R;

import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.when0matters.xlist.Helper.ResourceHelper.getString;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener,
    View.OnClickListener, View.OnLongClickListener{
    private List<Item> itemsDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvMthYear;
        public TextView tvDay;
        public TextView tvTaskName;
        public TextView tvPriorityText;
        public TextView tvDividerDate;
        public CheckBox cbStatus;
        public LinearLayout llItem;

        public ViewHolder(View view) {
            super(view);
            this.tvMthYear = (TextView) view.findViewById(R.id.tvMthYear);
            this.tvDay = (TextView) view.findViewById(R.id.tvDay);
            this.tvTaskName = (TextView) view.findViewById(R.id.tvTaskName);
            this.tvPriorityText = (TextView) view.findViewById(R.id.tvPriorityText);
            this.tvDividerDate = (TextView) view.findViewById(R.id.tvDividerDate);
            this.cbStatus = (CheckBox) view.findViewById(R.id.cbStatus);
            this.llItem = (LinearLayout) view.findViewById(R.id.llItem);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(List<Item> myDataset, Context context) {
        itemsDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        vh.cbStatus.setOnCheckedChangeListener(this);
        vh.llItem.setOnClickListener(this);
        vh.llItem.setOnLongClickListener(this);
        return vh;
    }

    private String prevDate = "";
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemsDataset.get(position);
        String itemDate = DateHelper.formatDayMonthYear(item.getDueDate());
        if (!prevDate.equalsIgnoreCase(itemDate)) {
            holder.tvDividerDate.setVisibility(View.VISIBLE);
            holder.tvDividerDate.setText(itemDate);
        }
        else {
            holder.tvDividerDate.setVisibility(View.GONE);
        }
        prevDate = itemDate;
        holder.tvTaskName.setText(item.getName());
        holder.tvDay.setText(DateHelper.formatDay(item.getDueDate()));
        holder.tvMthYear.setText(DateHelper.formatMonthYear(item.getDueDate()));
        holder.tvPriorityText.setText(item.getPriorityString());
        holder.cbStatus.setChecked(item.getIsCompleted() == 1);
        holder.cbStatus.setTag(position);
        holder.llItem.setTag(position);
        int priorityColor = R.color.colorGreen;
        if (item.getPriority() == Item.PriorityEnum.High.ordinal()){
            priorityColor = R.color.colorRed;
        }
        else if (item.getPriority() == Item.PriorityEnum.Medium.ordinal()){
            priorityColor = R.color.colorYellow;
        }
        holder.tvMthYear.setTextColor(ResourceHelper.getColor(priorityColor));
        holder.tvDay.setTextColor(ResourceHelper.getColor(priorityColor));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsDataset.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        int position = buttonView.getTag() != null ? (int)buttonView.getTag(): -1 ;
        if (position != -1) {
            Item item = itemsDataset.get(position);
            item.setIsCompleted(isChecked ? 1 : 0);
            XListSQLiteHelper xListSQLiteHelper = XListSQLiteHelper.getInstance(buttonView.getContext());
            Item.updateItem(xListSQLiteHelper, item);
        }
    }

    @Override
    public void onClick(View view) {
        //curPosition = position;
        int position = view.getTag() != null ? (int)view.getTag(): -1 ;
        if (position != -1) {
            Intent intent = new Intent(view.getContext(), AddItemActivity.class);
            intent.putExtra(Constants.MODE, Constants.Mode.EDIT);
            Item item = itemsDataset.get(position);
            intent.putExtra(Constants.TODOITEM, item);
            intent.putExtra(Constants.POSITION, position);
            ((Activity)mContext).startActivityForResult(intent, Constants.EDIT_ITEM_REQUEST_CODE);
        }

    }

    @Override
    public boolean onLongClick(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        final int position = view.getTag() != null ? (int)view.getTag(): -1 ;
        final ItemAdapter itemAdapter = this;
        if (position != -1) {
          // set title
          alertDialogBuilder.setTitle("Remove " + itemsDataset.get(position).getName());
          // set dialog message
          alertDialogBuilder
                  .setMessage(getString(R.string.remove_message))
                  .setCancelable(false)
                  .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          Item removedItem = itemsDataset.get(position);
                          itemsDataset.remove(position);
                          XListSQLiteHelper xListSQLiteHelper = XListSQLiteHelper.getInstance(XListApplication.getContext());
                          Item.deleteItem(xListSQLiteHelper,removedItem.get_Id());
                          itemAdapter.notifyDataSetChanged();
                          Toast.makeText(XListApplication.getContext(), getString(R.string.item_removed), Toast.LENGTH_SHORT).show();

                      }
                  })
                  .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          dialog.cancel();
                      }
                  });

          // create alert dialog
          AlertDialog alertDialog = alertDialogBuilder.create();

          // show it
          alertDialog.show();
        }
            return true;
    }

    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Constants.ADD_ITEM_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                XListSQLiteHelper xListSQLiteHelper = XListSQLiteHelper.getInstance(XListApplication.getContext());
                updateData(Item.getAllItems(xListSQLiteHelper, true));
                notifyDataSetChanged();
                Toast.makeText(mContext, getString(R.string.item_added_successfully), Toast.LENGTH_SHORT).show();
            } else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(mContext, getString(R.string.item_added_error), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Constants.EDIT_ITEM_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(Constants.POSITION, -1);
                if (position != -1) {
                    XListSQLiteHelper xListSQLiteHelper = XListSQLiteHelper.getInstance(XListApplication.getContext());
                    updateData(Item.getAllItems(xListSQLiteHelper, true));
                    Toast.makeText(mContext, getString(R.string.item_updated_successfully), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(mContext, getString(R.string.item_added_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateData(List<Item> items) {
        prevDate = "";
        itemsDataset.clear();
        itemsDataset.addAll(items);
        notifyDataSetChanged();
    }
}