package com.example.a20151203.testvoice;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by 20151203 on 2017/7/6.
 */

public class FileManagerActivity extends Activity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        listView = (ListView) findViewById(R.id.listView);

    }
}
