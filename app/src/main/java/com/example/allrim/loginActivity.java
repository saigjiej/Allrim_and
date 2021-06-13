package com.example.allrim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class loginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth auth; // 파이어 베이스 인증 객체
    private GoogleSignInClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int REO_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드
    @Override
    protected void onCreate(Bundle savedInstanceState) { // 앱이 실행될 때 처음 수행되는 곳
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), loginActivity.class);
            startActivity(intent);
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.bt_login).setOnClickListener(onClickListener);
    }

    //자동로그인
    @Override
    protected void onStart() {
        //프래그먼트에서는 public이지만 여기에서는 protected
        super.onStart();
        FirebaseUser user = auth.getCurrentUser(); //현재 로그인되어있는 사용자를 가져옴
        if(user!=null){
            Toast.makeText(this,"자동 로그인: "+user.getUid(),Toast.LENGTH_SHORT).show();
        }
    }

    // 버튼 클릭 부분
    View.OnClickListener onClickListener = v -> {
        switch(v.getId()){
            case R.id.bt_login:
                login();
                break;
        }
    };

    private void login(){
        Intent signInIntent = googleApiClient.getSignInIntent();
        startActivityForResult(signInIntent, REO_SIGN_GOOGLE);
    }

    // startActivityForResult한 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // 구글 로그인 인증을 요청 했을 때 결과 값을 되돌려 받는 곳
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REO_SIGN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class); // account라는 데이터는 구글 로그인 정보를 담고 있다. (닉네임, 프로필 사진Url, 이메일 주소 등)
                if (account.getEmail().split("@")[1].equals("e-mirim.hs.kr")) {
                    resultLogin(account); // 로그인 결과 값 출력 수행하라는 메소드
                } else {
                    Toast.makeText(loginActivity.this, "e-mirim계정만 사용 가능합니다.", Toast.LENGTH_SHORT).show();
                    googleApiClient.signOut();
                }
            }catch (ApiException e) {
                e.printStackTrace();
            }
        }
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

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){ // 로그인이 성공했으면
                            Toast.makeText(loginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        }else{ // 로그인이 실패했으면
                            Toast.makeText(loginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            if(true){
                String[] info = getInfo(user); // 학년 반 과 담긴 배열 반환
                Toast.makeText(loginActivity.this, info[0]+ info[1] +  info[2], Toast.LENGTH_SHORT).show();
                // 없으면 회원정보 db에 인서트
            }
            //user.getEmail()이 디비에 있으면 걍 로그인
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private String[] getInfo(FirebaseUser user){
        String[] info = new String[3]; // 학년 반 과
        // 학년, 반 입력
        if(user.getDisplayName().substring(0,3).equals("졸업생")){
            info[0] = null; info[1] = null;
        }else{
            info[0] = user.getDisplayName().substring(0,1); info[1] = user.getDisplayName().substring(1,2);
        }
        // 과 입력
        // 2020년 이후 입학이면(형식이 다름)
        if(user.getEmail().substring(0,user.getEmail().lastIndexOf("@")).length()==5){
            info[2] = user.getEmail().substring(0,1);
        }else if(user.getEmail().substring(0,user.getEmail().lastIndexOf("@")).length()==8){
            info[2] = user.getEmail().substring(5,6);
        }else{
            Toast.makeText(loginActivity.this, "지원하지 않는 이메일 양식입니다, 문의바람", Toast.LENGTH_SHORT).show();
        }
        switch (info[2]){
            case "s":
                info[2] = "뉴미디어소프트웨어과";
                break;
            case "w":
                info[2] = "뉴미디어웹솔루션과";
                break;
            case "d":
                info[2] = "뉴미디어디자인과";
                break;
        }
        return info;

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}