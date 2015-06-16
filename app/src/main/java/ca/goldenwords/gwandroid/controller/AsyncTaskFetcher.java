package ca.goldenwords.gwandroid.controller;

import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.security.InvalidParameterException;

import javax.net.ssl.HttpsURLConnection;


public abstract class AsyncTaskFetcher extends AsyncTask<String, Void, String> {

    protected String stringUrl;
    protected ContentValues params;

    protected String method="GET";
    protected URL url;
    protected HttpURLConnection conn;
    protected OutputStream os;
    protected BufferedWriter writer;

    public AsyncTaskFetcher(){

    }

    public AsyncTaskFetcher(String url,ContentValues params){
        this();
        this.stringUrl = url;
        this.params = params;
        if(!validateData()) throw new InvalidParameterException("Url and/or params invalid");
    }

    public AsyncTaskFetcher(String url){
        this();
        this.stringUrl = url;
        if(!validateData()) throw new InvalidParameterException("Url and/or params invalid");
    }

    public void addParam(String key,String value){
        params.put(key, value);
    }
    public void addParam(String key,int value){
        params.put(key,value);
    }
    public void addParam(String key,boolean value){
        params.put(key,value);
    }

    @Override protected void onPreExecute() {
    }

    @Override protected String doInBackground(String... params) {
        String response="";
        try{
            url = new URL(stringUrl+"?"+paramsToString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(this.method);
            conn.setDoInput(true);

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }else{
                response="";
                throw new Exception("HTTP Request Failed: "+responseCode);
            }

        }catch(Exception e){  // pokemon style ftw
            e.printStackTrace();
        }

        return response;
    }

    private String paramsToString() throws UnsupportedEncodingException {
        if(this.params!=null){
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(String key:params.keySet()){
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.get(key).toString(), "UTF-8"));
            }
            return result.toString();
        }
        return "";
    }

    public boolean validateData(){ // for optional overide
        return true;
    }

}

