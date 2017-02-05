package com.when0matters.xlist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.when0matters.xlist.Adapters.ItemAdapter;
import com.when0matters.xlist.DB.XListSQLiteHelper;
import com.when0matters.xlist.Helper.Constants;
import com.when0matters.xlist.Models.Item;
import com.when0matters.xlist.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Item> items = null;
    Toolbar toolbar;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    LinearLayoutManager mLinearLayoutManager;
    ItemAdapter itemAdapter;
    XListSQLiteHelper xListSQLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        xListSQLiteHelper = XListSQLiteHelper.getInstance(this);
        items = Item.getAllItems(xListSQLiteHelper);
        itemAdapter = new ItemAdapter(items, this);
        recyclerView.setAdapter(itemAdapter);


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

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        itemAdapter.onActivityResult(requestCode,resultCode,data);
    }

}
