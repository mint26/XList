package com.when0matters.xlist;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarContext;
import com.when0matters.xlist.Adapters.ToDoItemsAdapter;
import com.when0matters.xlist.Entity.ToDoItem;
import com.when0matters.xlist.General.Constants;
import com.when0matters.xlist.General.ItemComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvItems;
    ToDoItemsAdapter todoItemAdapter;
    ArrayList<ToDoItem> todoItems;
    int curPosition;
    ItemComparator itemComparator;
    public boolean includePastItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SugarContext.init(this);
        lvItems = (ListView) findViewById(R.id.lvItems);
        todoItems = new ArrayList<ToDoItem>();
        itemComparator = new ItemComparator();
        todoItemAdapter = new ToDoItemsAdapter<ToDoItem>(this, R.layout.todo_item, todoItems);
        lvItems.setAdapter(todoItemAdapter);
        setupListViewListener();
        includePastItem = false;
        loadItemsList(includePastItem);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                intent.putExtra(Constants.MODE, Constants.Mode.ADD);
                startActivityForResult(intent, Constants.ADD_ITEM_REQUEST_CODE);
            }
        });
    }

    private void loadItemsList(boolean includePastItems) {

        List<ToDoItem> items = ToDoItem.listAll(ToDoItem.class);
        items = sortItemsList(items);
        int currentDate = ItemComparator.getCurrentDate();
        for (ToDoItem i : items) {
            if (!includePastItems && ItemComparator.isPastDates(i, currentDate))
                continue;
            todoItems.add(i);

        }
    }

    private List<ToDoItem> sortItemsList(List<ToDoItem> items) {
        if (items.size() > 0) {
            Collections.sort(items, new ItemComparator());
        }
        return items;
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                        final int pos = position;
                        // set title
                        alertDialogBuilder.setTitle("Remove " + todoItems.get(pos).getTask());
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(getString(R.string.remove_message))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ToDoItem removedItem = todoItems.get(pos);
                                        todoItems.remove(pos);
                                        removedItem.delete();
                                        todoItemAdapter.notifyDataSetChanged();
                                        Toast.makeText(getApplicationContext(), getString(R.string.item_removed), Toast.LENGTH_SHORT).show();

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
                        return true;
                    }
                });

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        curPosition = position;
                        Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                        intent.putExtra(Constants.MODE, Constants.Mode.EDIT);
                        ToDoItem toDoItem = (ToDoItem) parent.getItemAtPosition(position);
                        intent.putExtra(Constants.TODOITEM, toDoItem);
                        startActivityForResult(intent, Constants.EDIT_ITEM_REQUEST_CODE);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            includePastItem = !includePastItem;
            todoItems.clear();
            loadItemsList(includePastItem);
            todoItemAdapter.notifyDataSetChanged();
            if (includePastItem)
                item.setTitle(getResources().getString(R.string.action_show_ongoing));
            else
                item.setTitle(getResources().getString(R.string.action_show_all));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Constants.ADD_ITEM_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                ToDoItem item = data.getParcelableExtra(Constants.TODOITEM);
                todoItems.add(item);
                sortItemsList(todoItems);
                todoItemAdapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.item_added_successfully), Toast.LENGTH_SHORT).show();
            } else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(this, getString(R.string.item_added_error), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Constants.EDIT_ITEM_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                todoItems.clear();
                loadItemsList(includePastItem);
                todoItemAdapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.item_updated_successfully), Toast.LENGTH_SHORT).show();
            } else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(this, getString(R.string.item_added_error), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
