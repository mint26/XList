package com.when0matters.xlist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.when0matters.xlist.DB.XListSQLiteHelper;
import com.when0matters.xlist.Helper.Constants;
import com.when0matters.xlist.Helper.DateHelper;
import com.when0matters.xlist.Models.Item;
import com.when0matters.xlist.R;

import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {

    Spinner spinnerPriority;
    String[] arrayPriority = new String[]{
            Item.PriorityEnum.High.toString(),
            Item.PriorityEnum.Medium.toString(),
            Item.PriorityEnum.Low.toString()
    };
    String errMsg="";
    int currentMode = Constants.Mode.ADD;
    int position;
    Item currentItem = null;
    XListSQLiteHelper xListSQLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayPriority);
        spinnerPriority.setAdapter(adapter);
        xListSQLiteHelper = XListSQLiteHelper.getInstance(this);

        Intent intent = getIntent();
        int mode = intent.getIntExtra(Constants.MODE,Constants.Mode.ADD);
        if (mode == Constants.Mode.EDIT){
            currentMode = Constants.Mode.EDIT;
            position = intent.getIntExtra(Constants.POSITION,-1);
            currentItem = intent.getParcelableExtra(Constants.TODOITEM);
            bindItemToViews();
        }
        else{
            currentItem = new Item();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            returnIntent();
        }
        else if (id == R.id.action_cancel){
            cancelAddItem();
        }


        return super.onOptionsItemSelected(item);
    }

    public void bindViewsToItem(){
        String task = ((EditText)findViewById(R.id.etTask)).getText().toString();
        String description = ((EditText)findViewById(R.id.etDescription)).getText().toString();
        String priority = ((Spinner)findViewById(R.id.spinnerPriority)).getSelectedItem().toString();
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePickerDueDate);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0);
        currentItem.setDueDate(DateHelper.formatDate(c.getTime()));
        currentItem.setName(task);
        currentItem.setDescription(description);
        currentItem.setPriority(Item.GetPriorityOrdinal(priority));
        errMsg = ValidateMandatoryFields(task, currentItem.getDate());

    }

    public void bindItemToViews(){
        ((EditText)findViewById(R.id.etTask)).setText(currentItem.getName());
        ((EditText)findViewById(R.id.etDescription)).setText(currentItem.getDescription());
        ((Spinner)findViewById(R.id.spinnerPriority)).setSelection(currentItem.getPriority());
        ((DatePicker)findViewById(R.id.datePickerDueDate)).updateDate(currentItem.getYear(),
                                                                    currentItem.getMonth(),
                                                                    currentItem.getDay());
    }

    public void returnIntent(){
        bindViewsToItem();
        if (errMsg == ""){
            Intent intent = new Intent();
            if (currentMode == Constants.Mode.ADD) {
                Item.addItem(xListSQLiteHelper, currentItem);
            }
            else {
                Item.updateItem(xListSQLiteHelper, currentItem);
                intent.putExtra(Constants.POSITION,position);
            }

            intent.putExtra(Constants.TODOITEM, currentItem);
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),errMsg,Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAddItem(){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public String ValidateMandatoryFields(String task, Date itemDate){
        String errMsg = "";
        if (task.trim().isEmpty()){
            errMsg = Constants.MISSING_TASKNAME;
            return errMsg;
        }
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        String currentDateString = DateHelper.formatDate(currentDate);
        String itemDateString = DateHelper.formatDate(itemDate);

        if (itemDateString.compareTo(currentDateString) < 0){
            errMsg = Constants.INVALID_DATE;
            return errMsg;
        }

        return errMsg;
    }
}
