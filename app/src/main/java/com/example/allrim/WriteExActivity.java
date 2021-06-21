package com.example.allrim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import android.Manifest;
import android.content.ClipData;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.content.Intent;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WriteExActivity extends AppCompatActivity {
    private EditText mEditTitle; //타이틀
    private EditText mEditContent; //content
    private Button btn_insert; //등록 버튼
    private Button btn_cancel; //취소 버튼
    private Button btn_upload; //파일 업로드 버튼
    private ImageView iv; //이미지 확인
    private String community;
    String imageEncoded;

    //서버로 업로드할 파일관련 변수
    public String uploadFilePath;
    public String uploadFileName;
    private final int GET_GALLERY_IMAGE = 200;

    //파일을 업로드 하기 위한 변수 선언
    private int serverResponseCode=0;

    private Bitmap bitmap;
    private ArrayList<String> imagesEncodedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        //insert
        mEditTitle = findViewById(R.id.editTitle);
        mEditContent = findViewById(R.id.editContent);

        btn_insert=(Button)findViewById(R.id.bt_writing_submit);
        btn_cancel = (Button)findViewById(R.id.bt_writing_cancel);
        btn_upload = (Button)findViewById(R.id.submit_file_btn);
        //이미지 View
        iv=findViewById(R.id.iv);
        checkSelfPermmision();
        btn_insert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //타이틀 (글제목)
                String title = mEditTitle.getText().toString().trim();
                //글 내용
                String contents = mEditContent.getText().toString().trim();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            //게시글 등록 성공시
                            if(success){
                                Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WriteExActivity.this,ShowWritingsActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }; //end of responseListener

                //서버로 Volley를 이용해서 요청 -> 여기에서 add되는 거 같음
                if(title.equals("") || contents.equals("")){
                    Toast.makeText(WriteExActivity.this, "제목과 내용을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    InsertRequest registerRequest = new InsertRequest(title,contents, responseListener);
                    RequestQueue queue = Volley.newRequestQueue( WriteExActivity.this );
                    queue.add( registerRequest );
                }
            }
        }); //end of btn_insert setOnclick

        //start button_cancel
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WriteExActivity.this, "글 등록 취소", Toast.LENGTH_SHORT).show();
                finish(); //이번 액티비티 종류
            }
        });//end of button_cancel

        //이미지 업로드
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(Intent.ACTION_PICK);
                imgIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(imgIntent,GET_GALLERY_IMAGE);
            }
        }); //end of ClickEvent
    } //end of onCreate

    //권한에 대한 응답이 있을 때 작동
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //권한 허용
        if(requestCode==1){
            int length = permissions.length;
            for(int i=0;i<length;i++){
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    Log.d("WriteExActivity","권한 허용: "+permissions[i]);
                }
            }
        }//end of if
    }//end of onRequestPermmsion

    public void checkSelfPermmision(){
        String temp="";

        //파일 읽기 권한 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            temp+=Manifest.permission.READ_EXTERNAL_STORAGE+" ";
        }

        //파일 쓰기 권환 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            temp+=Manifest.permission.WRITE_EXTERNAL_STORAGE+" ";
        }

        if(TextUtils.isEmpty(temp)==false){
            //권한 요청
            ActivityCompat.requestPermissions(this,temp.trim().split(" "),1);
        }else{
            Toast.makeText(this,"권한 모두 허용",Toast.LENGTH_SHORT).show();
        }
    }//end of checkSelfPermmssion


    //갤러리에서 이미지 불러온 후에
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if(requestCode==GET_GALLERY_IMAGE && resultCode==RESULT_OK && data !=null
           && data.getData()!=null) {
          Uri uri = data.getData();
          String path = getPath(uri);
          //String name=getName(uri);  //각각 메소드가 있음
          uploadFilePath=path;
          //uploadFileName=name;

          Bitmap bit= BitmapFactory.decodeFile(path);
          iv.setImageBitmap(bit);
      }
    } //end of onActivityResult

    //실제 경로 찾기
    private String getPath(Uri uri){
        String [] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor=managedQuery(uri,projection,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //파일명 찾기, 먼저 데이터베이스에 넣고 이미지 있으면 update하기?
//    private  String getName(Uri uri){
//        String[]projection = {MediaStore.Images.ImageColumns.DISPLAY_NAME};
//        Cursor cursor=managedQuery(uri,pr)
//    }
}