package com.example.allrim;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MealEx2Activity extends Activity {
    EditText inputDateText;
    TextView text;

    XmlPullParser xpp;
    String key = "6d21d81e36ec443c80712c99bc327083"; //인증키
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_ex1); //이 xml 화면에 뿌려주기

        inputDateText=findViewById(R.id.input_dateText);
        text=findViewById(R.id.showMenu);
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
        String date= URLEncoder.encode(str); //한글의 경우 인식이 안되기 때문에 utf-8로 해주
        //String query;
        String queryURL="https://open.neis.go.kr/hub/mealServiceDietInfo?" +
                "ATPT_OFCDC_SC_CODE=B10&" +
                "SD_SCHUL_CODE=7010569&" +
                //"MMEAL_SC_CODE=2&" +
                "MLSV_YMD="+date+"&" +
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

                        if(tag.equals("row")); //첫번째 검색결과
                        else if(tag.equals("ATPT_OFCDC_SC_CODE")){
                            buffer.append("시도교육청코드: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("ATPT_OFCDC_SC_NM")){
                            buffer.append("교육청 이름: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("SD_SCHUL_CODE")){
                            buffer.append("학교 코드: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("SCHUL_NM")){
                            buffer.append("학교명: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("MMEAL_SC_CODE")){
                            buffer.append("급식 코드: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("MMEAL_SC_NM")){
                            buffer.append("급식 종류: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }else if(tag.equals("MLSV_YMD")){
                            buffer.append("날짜: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }else if(tag.equals("MLSV_FGR")){
                            buffer.append("급식인원수: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("DDISH_NM")){
                            buffer.append("식사: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("ORPLC_INFO")){
                            buffer.append("원산지정보: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }else if(tag.equals("CAL_INFO")){
                            buffer.append("칼로리정보: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }else if(tag.equals("NTR_INFO")){
                            buffer.append("영양정보: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }else if(tag.equals("MLSV_FROM_YMD")){
                            buffer.append("급식 시작 일자: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if(tag.equals("MLSV_TO_YMD")){
                            buffer.append("급식 종료 일자: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
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
        buffer.append("파싱 끝 \n");
        return buffer.toString(); //StringBuffer를 문자열 객체로 반환
    }//end of getXmlData method
}
