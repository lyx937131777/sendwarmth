package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.sendwarmth.util.LogUtil;

public class SearchActivity extends AppCompatActivity {

    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        searchText = findViewById(R.id.search_edit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_search:
                search();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    public void search(){
        String keyword = searchText.getText().toString();
        if(keyword.equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请输入关键词")
                    .setPositiveButton("确定", null)
                    .show();
        }else {
            Intent intent = new Intent(this, ProductSearchActivity.class);
            intent.putExtra("keyword",keyword);
            startActivity(intent);
        }
    }
}