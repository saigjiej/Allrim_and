package com.example.allrim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.login_page);
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btn_google).setOnClickListener(onClickListener);
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
            case R.id.btn_google:
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

    /*private void signOut(){
        googleApiClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }*/

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
            Intent intent = new Intent(this, AfterLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}