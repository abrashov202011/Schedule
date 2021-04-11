package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity главного экрана
 * @author Abrashov
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnTeacherClick(View view) {
        startActivityForSimpleItem("TEACHER");
    }

    public void btnDisciplineClick(View view) {
        startActivityForSimpleItem("DISCIPLINE");
    }

    public void btnAuditoryClick(View view) {
        startActivityForSimpleItem("AUDITORY");
    }

    public void btnGroupClick(View view) {
        startActivityForSimpleItem("GROUPS");
    }

    void startActivityForSimpleItem(String itemType){
        Intent intent = new Intent(this, SimpleItemsListActivity.class);
        intent.putExtra("itemType", itemType);
        startActivity(intent);
    }

    public void btnScheduleClick(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }
}