package com.example.jsonparser;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {
    public HttpHandler() {}

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamtoString(in);
        }catch (MalformedURLException e){
            Log.e("httpHandler", "MalformedURLException: "+e.getMessage());
        }catch (ProtocolException e){
            Log.e("httpHandler", "ProtocolException: "+e.getMessage());
        }catch (IOException e){
            Log.e("httpHandler", "IOException: "+e.getMessage());
        }catch (Exception e){
            Log.e("httpHandler", "Exception: "+e.getMessage());
        }
        return response;
    }

    private String convertStreamtoString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}