package com.example.schedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Activity просмотра списка элементов справочника
 * @author Abrashov
 */
public class SimpleItemsListActivity extends AppCompatActivity {

    String itemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        Bundle arguments = getIntent().getExtras();
        itemType = arguments.getString("itemType");

        ListView listView = (ListView) findViewById(R.id.itemsList);
        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            SimpleItem item = ((SimpleItem)listView.getItemAtPosition(position));
            showItemActivity(item);
        });

        switch (itemType) {
            case "TEACHER":
                setTitle("Преподаватели");
                break;
            case "AUDITORY":
                setTitle("Аудитории");
                break;
            case "DISCIPLINE":
                setTitle("Предметы");
                break;
            case "GROUPS":
                setTitle("Группы");
                break;
        }

        refreshData();
    }

    void refreshData() {
        Database database = new Database(getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.itemsList);
        ArrayList results = database.getSimpleItems(itemType);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);
    }

    void showItemActivity(SimpleItem item) {
        Intent intent = new Intent(this, SimpleItemActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("itemType", itemType);
        startActivityForResult(intent, 0);
    }

    public void btnAddClick(View view) {
        SimpleItem item = new SimpleItem(0, "");
        showItemActivity(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshData();
    }
}