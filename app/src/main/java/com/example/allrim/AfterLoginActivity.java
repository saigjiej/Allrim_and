package com.example.allrim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class AfterLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;

    private TextView tv_nickname; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰
    private Button bt_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String email =  mAuth.getCurrentUser().getEmail();
        String nickName = mAuth.getCurrentUser().getDisplayName(); // MainActivity로 부터 닉네임 전달받음
        Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl(); // MainActivity로 부터 프로필사진 Url 전달받음

        tv_nickname = findViewById(R.id.tv_nickname);
        tv_nickname.setText(nickName); // 닉네임 text를 텍스트 뷰에 세팅

        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photoUrl).into(iv_profile); // 프로필 url을 이미지 뷰에 세팅

        findViewById(R.id.bt_move).setOnClickListener(onClickListener);
    }

    // 버튼 클릭 부분
    View.OnClickListener onClickListener = v -> {
        switch(v.getId()){
            case R.id.bt_move:
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("login", true);
                startActivity(intent);*/
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
    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }
}