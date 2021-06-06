package com.example.allrim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class writing extends AppCompatActivity implements View.OnClickListener {
    private EditText title,contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        title = findViewById(R.id.editTextTextPersonName);
        contents = findViewById(R.id.editTextTextMultiLine);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.
//        }
    }
}