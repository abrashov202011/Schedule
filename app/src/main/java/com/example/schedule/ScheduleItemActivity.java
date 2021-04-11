package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Activity экрана редактирования элемента расписания занятий
 * @author Abrashov
 */
public class ScheduleItemActivity extends AppCompatActivity {

    ScheduleItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_item);
        setTitle("Элемент расписания");

        Bundle arguments = getIntent().getExtras();
        item = (ScheduleItem) arguments.getSerializable("item");

        findViewById(R.id.btnDelete).setVisibility(item.id != 0 ? View.VISIBLE : View.INVISIBLE);

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        ((TextView) findViewById(R.id.txbDate)).setText(df.format(item.date));
        setSpinnerData(R.id.selTeacher, "TEACHER", item.teacherId);
        setSpinnerData(R.id.selAuditory, "AUDITORY", item.auditoryId);
        setSpinnerData(R.id.selGroup, "GROUPS", item.groupId);
        setSpinnerData(R.id.selDiscipline, "DISCIPLINE", item.disciplineId);

        ArrayList<Integer> pairNumbers = new ArrayList<Integer>();
        for (int i = 1; i <= 8; i++) {
            pairNumbers.add(i);
        }
        Spinner spinner = findViewById(R.id.selPair);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, pairNumbers);
        spinner.setAdapter(adapter);
        if (item.pairNumber > 0) {
            spinner.setSelection(item.pairNumber - 1);
        }
    }

    void setSpinnerData(int spinnerId, String itemType, int selectedItemId) {
        Database database = new Database(getApplicationContext());
        ArrayList<SimpleItem> items = database.getSimpleItems(itemType);
        items.add(0, new SimpleItem(0, "(не выбрано)"));
        Spinner spinner = (Spinner) findViewById(spinnerId);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(adapter);
        for(int i=0; i<items.size(); i++) {
            if (items.get(i).id == selectedItemId) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public void btnSaveClick(View view) {
        int auditoryId = ((SimpleItem) ((Spinner) findViewById(R.id.selAuditory)).getSelectedItem()).id;
        int groupId = ((SimpleItem) ((Spinner) findViewById(R.id.selGroup)).getSelectedItem()).id;
        int disciplineId = ((SimpleItem) ((Spinner) findViewById(R.id.selDiscipline)).getSelectedItem()).id;
        int teacherId = ((SimpleItem) ((Spinner) findViewById(R.id.selTeacher)).getSelectedItem()).id;
        int pairNumber = ((int) ((Spinner) findViewById(R.id.selPair)).getSelectedItem());
        if (auditoryId == 0 || groupId == 0 || disciplineId == 0 || teacherId == 0 || pairNumber == 0) {
            return;
        }

        Database db = new Database(this);
        ArrayList<ScheduleItem> existingSchedule = db.getScheduleItems(item.date, 0, 0, 0, 0);
        String msg = ScheduleValidator.ValidateItem(existingSchedule, item.id, item.date, pairNumber, auditoryId, teacherId, groupId);
        if(msg!=null) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage(msg);
            dlgAlert.setTitle("Ошибка");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            dlgAlert.create().show();
            return;
        }

        if (item.id != 0) {
            db.updateScheduleItem(item.id, item.date, pairNumber, auditoryId, groupId, disciplineId, teacherId);
        } else {
            db.createScheduleItem(item.date, pairNumber, auditoryId, groupId, disciplineId, teacherId);
        }
        finish();
    }

    boolean validateItem(){
        return  false;
    }

    public void btnDeleteClick(View view) {
        Database db = new Database(this);
        db.deleteScheduleItem(item.id);
        finish();
    }
}