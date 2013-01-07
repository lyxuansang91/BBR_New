package com.cadang.android.bbrproject.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cadang.android.bbrproject.util.*;

import android.os.AsyncTask;
import android.util.Log;

public class MakePlayList extends AsyncTask<String, Integer/**/, MediaStream[]>{ 
							
	private OnMakePlayListListener mListener;
	@Override
	protected MediaStream[] doInBackground(String... urls) {
		// TODO Auto-generated method stub
		return getPlayListJSONfromURL(urls[0]);
	}
	@Override
	protected void onPostExecute(MediaStream[] result){
		mListener.onMakeListCompleted(result);
	}
	@Override
	protected void onPreExecute(){
		mListener.onMakeListPreExecute();
	}
	public void setMakePlayListListener(OnMakePlayListListener _listener){
		mListener = _listener;
	}
	public MediaStream[] getPlayListJSONfromURL(String url){
		InputStream is = null;
		String result = "";
		JSONArray jArray = null;
		List<MediaStream> list = new ArrayList<MediaStream>();
		
		//http post
	    try{
	    		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
	    		con.connect();
	    		is = con.getInputStream();	    	    

	    }catch(Exception e){
	            Log.e("log_tag", "Error in http connection "+e.toString());
	            return null;
	    }
		    
	  //convert response to string
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	            Log.d("Read String", result);
	    }catch(Exception e){
	            Log.e("log_tag", "Error converting result "+e.toString());
	            return null;
	    }
	    
	    try{	    		
	           jArray = new JSONArray(result);
	           for(int i = 0; i < jArray.length(); i++){
	        	   JSONObject jObj = jArray.getJSONObject(i);
	        	   MediaStream sm = new MediaStream(jObj.getString("title"), jObj.getString("link"));
	        	   list.add(sm);
	           }
	    }catch(JSONException e){
	            Log.e("log_tag", "Error parsing data "+e.toString());
	            return null;
	    }
	    MediaStream[] smArr = list.toArray(new MediaStream[list.size()]);
	    return smArr;	   
	    
	}
	
}
