package com.example.allrim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;

    private TextView tv_email; // 닉네임 text
    private TextView tv_nickname; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer);
            NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                    .setDrawerLayout(drawer)
                    .build();


            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            findViewById(R.id.logoutButton).setOnClickListener(onClickListener);

            String email =  mAuth.getCurrentUser().getEmail();
            String nickName = mAuth.getCurrentUser().getDisplayName(); // MainActivity로 부터 닉네임 전달받음
            Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl(); // MainActivity로 부터 프로필사진 Url 전달받음

            tv_email = (TextView) headerView.findViewById(R.id.tv_googleEmail);
            tv_email.setText(email); // 닉네임 text를 텍스트 뷰에 세팅

            tv_nickname = (TextView) headerView.findViewById(R.id.tv_googleName);
            tv_nickname.setText(nickName); // 닉네임 text를 텍스트 뷰에 세팅
        }

    }

    // 버튼 클릭 부분
    View.OnClickListener onClickListener = v -> {
        switch(v.getId()){
            case R.id.logoutButton:
                signOut();
                finishAffinity();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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