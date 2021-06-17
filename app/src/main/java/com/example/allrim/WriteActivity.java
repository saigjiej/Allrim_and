package com.example.allrim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.Buffer;

public class WriteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private DrawerLayout mDrawerLayout;

    private TextView tv_nickname; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰
    private EditText mTitleText;
    private EditText mContentText;

    private String community;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        //insert
        mTitleText = findViewById(R.id.editTitle);
        mContentText = findViewById(R.id.editContent);

        Intent intents = getIntent();
        String title = intents.getStringExtra("title");
        String contents = intents.getStringExtra("contents");


        mTitleText.setText(title);
        mContentText.setText(contents);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        String nickName = mAuth.getCurrentUser().getDisplayName(); // MainActivity로 부터 닉네임 전달받음
        Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl(); // MainActivity로 부터 프로필사진 Url 전달받음

        mTitleText=findViewById(R.id.editTitle); //제목
        mContentText=findViewById(R.id.editContent); //내용

        findViewById(R.id.bt_writing_cancel).setOnClickListener(onClickListener);
        findViewById(R.id.bt_writing_submit).setOnClickListener(onClickListener);

        community = getIntent().getStringExtra("community");
        mTitleText.setText(community);

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
                    Toast.makeText(WriteActivity.this, "글 등록 성공", Toast.LENGTH_SHORT).show();
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