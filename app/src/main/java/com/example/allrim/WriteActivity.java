package com.example.allrim;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WriteActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private static String IP_ADDRESS = "125.141.36.87";
    private static String TAG="phptest";
    private EditText mTitleText;
    private EditText mContentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        
        mTitleText=findViewById(R.id.editTextTextPersonName); //제목
        mContentText=findViewById(R.id.editTextTextMultiLine); //내용
    
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_info:
                        Toast.makeText(WriteActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_writing:
                        Toast.makeText(WriteActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_schedule:
                        Toast.makeText(WriteActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_meal:
                        Toast.makeText(WriteActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_lost:
                        Toast.makeText(WriteActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_set:
                        Toast.makeText(WriteActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });
        //등록버튼 눌렀을 때 글 DB에 추가되기
        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String title=mTitleText.getText().toString();
                String content=mContentText.getText().toString();

                InsertData task=new InsertData();
                task.execute("http://"+IP_ADDRESS+"/Allrim_test1/insert.php",title,content);

                mTitleText.setText("");
                mContentText.setText("");
            }
        });
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
    //InsertData 클래스 시작
    class InsertData extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(WriteActivity.this,
                    "Please wait",null,true,true);
        }

//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            progressDialog.dismiss();
//        }

        //데이터삽입역할
        @Override
        protected String doInBackground(String... params) {
            String title=(String)params[1];
            String content=(String)params[2];

            String serverURL=(String)params[0];
            String postParameters="title="+title+"&content="+content;

            try{
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream opstream = httpURLConnection.getOutputStream();
                opstream.write(postParameters.getBytes("UTF-8"));
                opstream.flush();
                opstream.close();

                int responseStatusCode =httpURLConnection.getResponseCode();
                Log.d("phptest","POST response-code : "+responseStatusCode);

                InputStream ipstream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK){
                    ipstream=httpURLConnection.getInputStream();
                }else{
                    ipstream=httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(ipstream,"UTF_8");
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

                StringBuilder sb=new StringBuilder();
                String line=null;

                while((line=bufferedReader.readLine())!= null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString();

            }catch(Exception e){
                return new String("Error: "+e.getMessage());
            }
        }
    }
}