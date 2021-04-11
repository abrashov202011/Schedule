package com.example.schedule;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity экрана редактирования элемента справочника
 * @author Abrashov
 */
public class SimpleItemActivity extends AppCompatActivity {

    String itemType;
    SimpleItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_item);

        Bundle arguments = getIntent().getExtras();
        itemType = arguments.getString("itemType");
        item = (SimpleItem) arguments.get("item");

        findViewById(R.id.btnDelete).setVisibility(item.id != 0 ? View.VISIBLE : View.INVISIBLE);
        ((EditText)findViewById(R.id.txbName)).setText(item.name);
        ((EditText)findViewById(R.id.txbName)).requestFocus();

        switch (itemType) {
            case "TEACHER":
                setTitle("Преподаватель");
                break;
            case "AUDITORY":
                setTitle("Аудитория");
                break;
            case "DISCIPLINE":
                setTitle("Предмет");
                break;
            case "GROUPS":
                setTitle("Группа");
                break;
        }
    }

    public void btnSaveClick(View view) {
        String name = ((TextView) findViewById(R.id.txbName)).getText().toString();
        if (name.isEmpty()) {
            return;
        }
        Database db = new Database(this);
        if (item.id != 0) {
            db.updateSimpleItem(itemType, item.id, name);
        } else {
            db.createSimpleItem(itemType, name);
        }
        finish();
    }

    public void btnDeleteClick(View view) {
        Database db = new Database(this);
        db.deleteSimpleItem(itemType, item.id);
        finish();
    }
}