package com.example.allrim;

import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Item{
    String MMEAL_SC_CODE; //조식:1 중식:2 석식:3
    String MMEAL_SC_NM; //텍스트로
    String MLSV_YMD; //날짜
    String DDISH_NM; //메뉴

    //setter
    public void setMMEAL_SC_CODE(String MMEAL_SC_CODE){
        this.MMEAL_SC_CODE=MMEAL_SC_CODE;
    }

    public void setMMEAL_SC_NM(String MMEAL_SC_NM){
        this.MMEAL_SC_NM=MMEAL_SC_NM;
    }

    public void setMLSV_YMD(String MLSV_YMD){
        this.MLSV_YMD=MLSV_YMD;
    }

    public void setDDISH_NM(String DDISH_NM){
        this.DDISH_NM=DDISH_NM;
    }

    //getter
    public String getMMEAL_SC_CODE(){
        return MMEAL_SC_CODE;
    }

    public String getMMEAL_SC_NM(){
        return MMEAL_SC_NM;
    }

    public String getMLSV_YMD(){
        return MLSV_YMD;
    }

    public String getDDISH_NM(){
        return DDISH_NM;
    }

}
