package com.example.allrim;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertRequest extends StringRequest {
    static private String host = "34.225.140.23";
    static private String URL = "http://"+host+"/insert.php";
    private Map<String,String> map;

    public InsertRequest(String title, String contents, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);
        map = new HashMap<>();
        map.put("title",title);
        map.put("contents",contents);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
