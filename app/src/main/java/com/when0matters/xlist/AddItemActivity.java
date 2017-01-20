package com.when0matters.xlist;

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

import com.when0matters.xlist.Entity.ToDoItem;
import com.when0matters.xlist.General.Constants;
import com.when0matters.xlist.General.ItemComparator;

import static com.when0matters.xlist.General.ItemComparator.formatDate;

public class AddItemActivity extends AppCompatActivity {

    Spinner spinnerPriority;
    String[] arrayPriority = new String[]{
            ToDoItem.PriorityEnum.High.toString(),
            ToDoItem.PriorityEnum.Medium.toString(),
            ToDoItem.PriorityEnum.Low.toString()
    };
    String errMsg="";
    int currentMode = Constants.Mode.ADD;
    ToDoItem currentItem = null;
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


        Intent intent = getIntent();
        int mode = intent.getIntExtra(Constants.MODE,Constants.Mode.ADD);
        if (mode == Constants.Mode.EDIT){
            currentMode = Constants.Mode.EDIT;
            currentItem = intent.getParcelableExtra(Constants.TODOITEM);
            bindItemToViews();
        }
        else{
            currentItem = new ToDoItem();
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
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        currentItem.setTask(task);
        currentItem.setDescription(description);
        currentItem.setPriority(currentItem.GetPriorityOrdinal(priority));
        currentItem.setDueDateDay(day);
        currentItem.setDueDateMth(month);
        currentItem.setDueDateYear(year);
        errMsg = ValidateMandatoryFields(task, formatDate(currentItem));

    }

    public void bindItemToViews(){
        ((EditText)findViewById(R.id.etTask)).setText(currentItem.getTask());
        ((EditText)findViewById(R.id.etDescription)).setText(currentItem.getDescription());
        ((Spinner)findViewById(R.id.spinnerPriority)).setSelection(currentItem.getPriority());
        ((DatePicker)findViewById(R.id.datePickerDueDate)).updateDate(currentItem.dueDateYear,
                                                                    currentItem.dueDateMth-1,
                                                                    currentItem.dueDateDay);
    }

    public void returnIntent(){
        bindViewsToItem();
        if (errMsg == ""){
            currentItem.save();
            Intent intent = new Intent();
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

    public String ValidateMandatoryFields(String task, int selectedDate){
        String errMsg = "";
        if (task.trim().isEmpty()){
            errMsg = Constants.MISSING_TASKNAME;
            return errMsg;
        }

        if (selectedDate < ItemComparator.getCurrentDate()){
            errMsg = Constants.INVALID_DATE;
            return errMsg;
        }

        return errMsg;
    }
}
