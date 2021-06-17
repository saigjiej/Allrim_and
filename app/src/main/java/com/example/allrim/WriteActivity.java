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


import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import android.os.Bundle;

public class WriteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private DrawerLayout mDrawerLayout;

    private TextView tv_nickname; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰

    private static String IP_ADDRESS = "125.141.36.87";
    private static String TAG="phptest";
    private EditText mTitleText;
    private EditText mContentText;

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
        Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl(); // MainActivity로 부터 프로필사진 Url 전달받음

        mTitleText=findViewById(R.id.editTextTextPersonName); //제목
        mContentText=findViewById(R.id.editTextTextMultiLine); //내용

        findViewById(R.id.bt_writing_cancel).setOnClickListener(onClickListener);
        findViewById(R.id.bt_writing_submit).setOnClickListener(onClickListener);

        community = getIntent().getStringExtra("community");
        mTitleText.setText(community);
        //등록버튼 눌렀을 때 글 DB에 추가되기
//        Button submitBtn = findViewById(R.id.submitBtn);
//        submitBtn.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                String title=mTitleText.getText().toString();
//                String content=mContentText.getText().toString();
//
//                InsertData task=new InsertData();
//                task.execute("http://"+IP_ADDRESS+"/Allrim_test1/insert.php",title,content);
//
//                mTitleText.setText("");
//                mContentText.setText("");
//            }
        //});
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
                    postWriting();
                    finish();
                }
                break;
        }
    };

    private void postWriting() {
        // 정보 받아서 디비 업뎃하는 알고리즘 작성
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
//    //InsertData 클래스 시작
//    class InsertData extends AsyncTask<String,Void,String>{
//        ProgressDialog progressDialog = ProgressDialog.show(this,
//                "Please wait",null,true,true);
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        //데이터삽입역할
//        @Override
//        protected String doInBackground(String... params) {
//            String title=(String)params[1];
//            String content=(String)params[2];
//
//            String serverURL=(String)params[0];
//            String postParameters="title="+title+"&content="+content;
//
//            try{
//                URL url = new URL(serverURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.connect();
//
//                OutputStream opstream = httpURLConnection.getOutputStream();
//                opstream.write(postParameters.getBytes("UTF-8"));
//                opstream.flush();
//                opstream.close();
//
//                int responseStatusCode =httpURLConnection.getResponseCode();
//                Log.d("phptest","POST response-code : "+responseStatusCode);
//
//                InputStream ipstream;
//                if(responseStatusCode == HttpURLConnection.HTTP_OK){
//                    ipstream=httpURLConnection.getInputStream();
//                }else{
//                    ipstream=httpURLConnection.getErrorStream();
//                }
//
//                InputStreamReader inputStreamReader = new InputStreamReader(ipstream,"UTF_8");
//                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
//
//                StringBuilder sb=new StringBuilder();
//                String line=null;
//
//                while((line=bufferedReader.readLine())!= null){
//                    sb.append(line);
//                }
//
//                bufferedReader.close();
//                return sb.toString();
//
//            }catch(Exception e){
//                return new String("Error: "+e.getMessage());
//            }
//        }

}