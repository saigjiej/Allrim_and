package com.example.allrim;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MealExample extends AsyncTask<Void,Void,String> {
    //학교명,조식,중식,석식 메뉴들만 가져오기
    //학교코드 B10 : 미림여정 , 일단은 오늘날짜 임의로 집어넣음 추후에 오늘날짜 받아서 변수에 저장한 후
    //가져올 계획.
    private String url;

    //파싱할 url 지정
    //String url="https://open.neis.go.kr/hub/mealServiceDietInfo?" +
    //        "ATPT_OFCDC_SC_CODE=B10&Key="+key+"&Type=xml&MLSV_YMD=20210603";

    //페이지에 접근해줄 Document객체 생성
    //파싱할 url의 요소 읽어들이기 (XML 최상위 tag값 : mealServiceDietInfo 가져옴)

    @Override
    protected String doInBackground(Void... params) {
        //parsing할 url 지정 (API 키 포함해서)
        String key = "6d21d81e36ec443c80712c99bc327083"; //인증키
        String date = "20210603";
        String requestURL = "https://open.neis.go.kr/hub/mealServiceDietInfo?Key=" + key +
                "ATPT_OFDCD_SC_CODE=B10&SD_SCHUL_CODE=7010569&Type=xml&MLSV_YMD=" + date;
//        MealExample meal = new MealExample(requestURL);
//        meal.execute();
        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactoty.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = dBuilder.parse(url);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        //root tag : mealServiceDietInfo
        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        //파싱할 tag
        NodeList nList = doc.getElementsByTagName("row");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                Log.d("MealAPI","오늘 날짜: " + getTagValue("MLSV_YMD", eElement));
                Log.d("MealAPI","급식 종류: " + getTagValue("MMEAL_SC_NM", eElement));
                Log.d("MealAPI","메뉴: " + getTagValue("DDISH_NM", eElement));
            }//if end
        }//for end
        return null;
    }




    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }
}