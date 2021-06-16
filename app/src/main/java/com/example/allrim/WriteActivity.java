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
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        navigationView.getMenu().getItem(0).setChecked(true); // 페이지별로 바꾸기

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            if (!menuItem.isChecked()) {
                Intent intent;
                switch (id) {
                    case R.id.navigation_item_info:
                        intent = new Intent(this, MyPageActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_writing:
                        intent = new Intent(this, MyWritingActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_schedule:
                        intent = new Intent(this, ScheduleActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_meal:
                        intent = new Intent(this, MealActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_lost:
                        intent = new Intent(this, LostActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_set:
                        intent = new Intent(this, SettingActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                }
            }
            return false;
        });

        String nickName = mAuth.getCurrentUser().getDisplayName(); // MainActivity로 부터 닉네임 전달받음
        Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl(); // MainActivity로 부터 프로필사진 Url 전달받음

        iv_profile = headerView.findViewById(R.id.img_userImage);
        Glide.with(this).load(photoUrl).into(iv_profile); // 프로필 url을 이미지 뷰에 세팅

        tv_nickname = (TextView) headerView.findViewById(R.id.tv_userName);
        tv_nickname.setText(nickName); // 닉네임 text를 텍스트 뷰에 세팅

        headerView.findViewById(R.id.bt_logout).setOnClickListener(onClickListener);

        mTitleText=findViewById(R.id.editTextTextPersonName); //제목
        mContentText=findViewById(R.id.editTextTextMultiLine); //내용

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
            case R.id.bt_logout:
                signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    };

private void signOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInClient googleApiClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());
        googleApiClient.signOut();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
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