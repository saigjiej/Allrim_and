package com.example.allrim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MealActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private DrawerLayout mDrawerLayout;

    private TextView tv_nickname; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰

    private EditText inputDateText;
    private TextView text;

    private XmlPullParser xpp;
    private String key = "6d21d81e36ec443c80712c99bc327083"; //인증키
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal); //이 xml 화면에 뿌려주기

        inputDateText=findViewById(R.id.input_dateText);
        text=findViewById(R.id.showMenu);

        new Thread(new Runnable() {
            @Override
            public void run() {
                data=getXmlData(); //메소드 만들어줘야함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(data);
                    }
                });
            }
        }).start();

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        navigationView.getMenu().getItem(3).setChecked(true); // 페이지별로 바꾸기

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            if(!menuItem.isChecked()){
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

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInClient googleApiClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());
        googleApiClient.signOut();
    }

    //Button을 클릭했을 때 자동으로 호출되는 callback method
    public void buttonClicked(View v){
        switch (v.getId()){
            case R.id.search_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data=getXmlData(); //메소드 만들어줘야함
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    public String getXmlData(){
        StringBuffer buffer = new StringBuffer();
        String str=inputDateText.getText().toString(); //EditText에 입력된 값을 String으로 받아오기
        String date= URLEncoder.encode(str); //한글의 경우 인식이 안되기 때문에 utf-8로 해줌

        SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMdd"); //20210609 이런 형태로 받기
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        String today_str = dtf.format(today);
        Log.d("오늘날짜 ",today_str);
        Log.d("입력날짜 ",date);

        if(date.length()!= 0){
            today_str=date;
        }
        //옆에 오늘 날짜로 돌아가는 버튼이 있어도 좋을 듯
        String queryURL="https://open.neis.go.kr/hub/mealServiceDietInfo?" +
                "ATPT_OFCDC_SC_CODE=B10&" +
                "SD_SCHUL_CODE=7010569&" +
                //"MMEAL_SC_CODE=2&" +
                "MLSV_YMD="+today_str+"&" +
                "Key="+key+"&" +
                "Type=xml&pIndex=1&pSize=5";

        try{
            URL url = new URL(queryURL); //문자열로 된 요청url을 URL 객체로 생성
            InputStream is=url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xpp=factory.newPullParser();
            xpp.setInput(new InputStreamReader(is,"UTF-8")); //inputstream으로부터 xml입력받기

            String tag;

            xpp.next();
            int eventType=xpp.getEventType();

            //END_DOCUMENT : 1
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;
                    case XmlPullParser.START_TAG:
                        tag=xpp.getName(); //태그이름 가져오기
                        Log.d("test",tag);

                        if(tag.equals("row")); //첫번째 검색결과
                        else if(tag.equals("MMEAL_SC_NM")){
                            buffer.append("급식 종류: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("MLSV_YMD")){
                            buffer.append("날짜: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("DDISH_NM")){
                            buffer.append("메뉴 \n");
                            xpp.next();
                            String[]menu=xpp.getText().split("<br/>");
                            for(int i=0;i<menu.length;i++){
                                buffer.append(menu[i]+"\n"); //뒤에 숫자 나오는 것도 삭제하기
                                //문자열 효과 넣기
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:break;
                    case XmlPullParser.END_TAG:
                        tag=xpp.getName(); //태그이름 가져오기
                        if(tag.equals("row")) buffer.append("\n"); //첫번째 검색결과종료 후 줄바꿈
                        break;
                }
                eventType=xpp.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //buffer.append("파싱 끝 \n");
        String result="";
        Log.d("버퍼내용",buffer.toString());
        Log.d("버퍼길이",Integer.toString(buffer.length()));
        if(buffer.length()==0){
            result="오늘은 급식이 나오지 않습니다.";
        }else{
            result=buffer.toString();
        }
        return result; //StringBuffer를 문자열 객체로 반환
    }//end of getXmlData method
}
