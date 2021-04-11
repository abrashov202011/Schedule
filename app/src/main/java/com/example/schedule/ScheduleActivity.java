package com.example.schedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Activity экранной формы просмотра расписания занятий
 * @author Abrashov
 */
public class ScheduleActivity extends AppCompatActivity {

    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setTitle("Расписание занятий");

        date = getCurrentDate();
        displayDate();

        setSpinnerData(R.id.selTeacher, "TEACHER");
        setSpinnerData(R.id.selAuditory, "AUDITORY");
        setSpinnerData(R.id.selGroup, "GROUPS");

        ListView listView = (ListView) findViewById(R.id.scheduleListView);
        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            ScheduleItem item = ((ScheduleItem)listView.getItemAtPosition(position));
            showItemActivity(item);
        });

        refreshData();
    }

    static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    void setSpinnerData(int spinnerId, String itemType) {
        Database database = new Database(getApplicationContext());
        ArrayList items = database.getSimpleItems(itemType);
        items.add(0, new SimpleItem(0, "(без фильтра)"));
        Spinner spinner = (Spinner) findViewById(spinnerId);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                refreshData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    void displayDate(){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        ((TextView) findViewById(R.id.txbDate)).setText(df.format(date));
    }

    public void btnPrevDateClick(View view) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        date = cal.getTime();
        displayDate();
        refreshData();
    }

    public void btnNextDateClick(View view) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        date = cal.getTime();
        displayDate();
        refreshData();
    }

    void refreshData() {
        int auditoryId = ((SimpleItem) ((Spinner) findViewById(R.id.selAuditory)).getSelectedItem()).id;
        int groupId = ((SimpleItem) ((Spinner) findViewById(R.id.selGroup)).getSelectedItem()).id;
        int teacherId = ((SimpleItem) ((Spinner) findViewById(R.id.selTeacher)).getSelectedItem()).id;
        Database database = new Database(getApplicationContext());
        List<ScheduleItem> schedule = database.getScheduleItems(date, auditoryId, groupId, 0, teacherId);
        ListView listView = (ListView) findViewById(R.id.scheduleListView);
        ArrayAdapter<ScheduleItem> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, schedule);
        listView.setAdapter(adapter);
    }

    public void btnAddClick(View view) {
        ScheduleItem item = new ScheduleItem();
        item.date = date;
        item.auditoryId = ((SimpleItem) ((Spinner) findViewById(R.id.selAuditory)).getSelectedItem()).id;
        item.groupId = ((SimpleItem) ((Spinner) findViewById(R.id.selGroup)).getSelectedItem()).id;
        item.teacherId = ((SimpleItem) ((Spinner) findViewById(R.id.selTeacher)).getSelectedItem()).id;
        Intent intent = new Intent(this, ScheduleItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    public void showItemActivity(ScheduleItem item) {
        Intent intent = new Intent(this, ScheduleItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshData();
    }
}