package com.example.allrim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;

import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class WriteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;

    private EditText mTitleText; //타이틀
    private EditText mContentText; //content

    private String community;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String nickName = mAuth.getCurrentUser().getDisplayName(); // MainActivity로 부터 닉네임 전달받음

        mTitleText = findViewById(R.id.editTitle); //제목
        mContentText = findViewById(R.id.editContent); //내용

        // 확인
        Intent intents = getIntent();
        String title = intents.getStringExtra("title");
        String contents = intents.getStringExtra("contents");

        mTitleText.setText(title);
        mContentText.setText(contents);

        findViewById(R.id.bt_writing_cancel).setOnClickListener(onClickListener);
        findViewById(R.id.bt_writing_submit).setOnClickListener(onClickListener);

        community = getIntent().getStringExtra("community");
    }

    // 버튼 클릭 부분
    View.OnClickListener onClickListener = v -> {
        switch(v.getId()){
            case R.id.bt_writing_cancel:
                Toast.makeText(WriteActivity.this, "글 등록 취소", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.bt_writing_submit:
                if(mTitleText.getText().toString().trim().equals("")||mContentText.getText().toString().trim().equals("")){
                    Toast.makeText(WriteActivity.this, "제목과 내용을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    finish();
                }
                break;
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}