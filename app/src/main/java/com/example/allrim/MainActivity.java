package com.example.allrim;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private DrawerLayout mDrawerLayout;

    private TextView tv_nickname; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰

    private ListView listview;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            setContentView(R.layout.activity_community_main);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setDisplayHomeAsUpEnabled(true);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

            View headerView = navigationView.getHeaderView(0);
            navigationView.getMenu().getItem(0).setChecked(false);

            navigationView.setNavigationItemSelectedListener(menuItem -> {
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.navigation_item_info:
                        intent = new Intent(this, MyPageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_writing:
                        intent = new Intent(this, MyWritingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_schedule:
                        intent = new Intent(this, ScheduleActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_meal:
                        intent = new Intent(this, MealActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_lost:
                        intent = new Intent(this, LostActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_set:
                        intent = new Intent(this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            });

            String nickName = mAuth.getCurrentUser().getDisplayName(); // MainActivity로 부터 닉네임 전달받음
            Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl(); // MainActivity로 부터 프로필사진 Url 전달받음

            iv_profile = headerView.findViewById(R.id.img_userImage);
            Glide.with(this).load(photoUrl).circleCrop().into(iv_profile); // 프로필 url을 이미지 뷰에 세팅

            tv_nickname = (TextView) headerView.findViewById(R.id.tv_userName);
            tv_nickname.setText(nickName); // 닉네임 text를 텍스트 뷰에 세팅

            headerView.findViewById(R.id.bt_logout).setOnClickListener(onClickListener);
            findViewById(R.id.community_software).setOnClickListener(onClickListener);
            findViewById(R.id.community_solution).setOnClickListener(onClickListener);
            findViewById(R.id.community_design).setOnClickListener(onClickListener);
            findViewById(R.id.community_dormitory).setOnClickListener(onClickListener);
            findViewById(R.id.bt_write).setOnClickListener(onClickListener);

            // 리스트뷰 객체 생성 및 Adapter 설정
            listview = (ListView) findViewById(R.id.writing_listview_main);

            // Adapter 생성
            adapter = new ListViewAdapter();

            // 리스트 뷰 아이템 추가.
            adapter.addItem("익깅", "메인임이거", "진짜배고픔");
            adapter.addItem("익깅2", "정처기어카지", "개망한듯");
            adapter.addItem("익깅3", "집가고싶다", "오늘집감");
            // 리스트 뷰 아이템 추가.
            adapter.addItem("익깅", "배고프다", "진짜배고픔");
            adapter.addItem("익깅2", "정처기어카지", "개망한듯");
            adapter.addItem("익깅3", "집가고싶다", "오늘집감");
            // 리스트 뷰 아이템 추가.
            adapter.addItem("익깅", "배고프다", "진짜배고픔");
            adapter.addItem("익깅2", "정처기어카지", "개망한듯");
            adapter.addItem("익깅3", "집가고싶다", "오늘집감");
            // 리스트 뷰 아이템 추가.
            adapter.addItem("익깅", "배고프다", "진짜배고픔");
            adapter.addItem("익깅2", "정처기어카지", "개망한듯");
            adapter.addItem("익깅3", "집가고싶다", "오늘집감");

            // 리스트뷰의 높이를 계산에서 layout 크기를 설정
            int totalHeight = 0;
            for (int i = 0; i < 2; i++){
                View listItem = adapter.getView(i, null, listview);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight + listview.getDividerHeight();

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                    final ListViewItem item = (ListViewItem) adapter.getItem(a_position);
                    Intent intent = new Intent(getApplicationContext(), ShowWritingActivity.class);
                    intent.putExtra("title", item.getTitle());
                    startActivity(intent);
                }
            });

            listview.setLayoutParams(params);
            listview.setAdapter(adapter);
            
        }

    }

    // 버튼 클릭 부분
    View.OnClickListener onClickListener = v -> {
        Intent intent;
        switch(v.getId()){
            case R.id.bt_logout:
                signOut();
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.community_software:
                intent = new Intent(getApplicationContext(), ShowWritingsActivity.class);
                intent.putExtra("community", "뉴미디어소프트웨어과");
                startActivity(intent);
                break;
            case R.id.community_solution:
                intent = new Intent(getApplicationContext(), ShowWritingsActivity.class);
                intent.putExtra("community", "뉴미디어웹솔루션과");
                startActivity(intent);
                break;
            case R.id.community_design:
                intent = new Intent(getApplicationContext(), ShowWritingsActivity.class);
                intent.putExtra("community", "뉴미디어디자인과");
                startActivity(intent);
                break;
            case R.id.community_dormitory:
                intent = new Intent(getApplicationContext(), ShowWritingsActivity.class);
                intent.putExtra("community", "기숙사 커뮤니티");
                startActivity(intent);
                break;
            case R.id.bt_write:
                intent = new Intent(getApplicationContext(), WriteActivity.class);
                intent.putExtra("community", "메인커뮤니티");
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setNegativeButton("취소",(dialog, which) -> dialog.cancel());
        builder.setPositiveButton("종료", (dialog, which) -> finishAffinity());
        builder.show();
    }
}