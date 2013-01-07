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


import android.os.AsyncTask;
import android.util.Log;

public class BuildMediaStream{
	enum MediaType{
		MEDIA(0), PICTURE(1), IDEAL(2), NEWS(3), NONE(-1);
		private int _value;
		private MediaType(int val){
			this._value = val; 
		}
		public int getValue(){
			return _value;
		}
	}
	private MediaStream[] mStreams;
	private boolean mReady;
	private String mStreamUrl;
	private int mMediaType;
	private AsyncBuild mBuild;
	public BuildMediaStream(){
		mReady = false;
		mStreams = null;
		mStreamUrl ="";
	}
	public BuildMediaStream(String url){
		mStreamUrl = url;
		mBuild = new AsyncBuild();
		mBuild.setBuildStreamListener(new OnBuildStreamListener() {
			
			@Override
			public void onBuildStreamPreExecute() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBuildStreamCompleted(MediaStream[] result) {
				// TODO Auto-generated method stub
				if(result != null){
					mReady = true;
					mStreams = result;
				}
				else{
					mReady = false;
					mStreams = null;
				}
			}
		});		
	}
	public boolean isReady(){
		if(mReady){
			return true;
		}
		return false;
	}
	public MediaStream[] getMediaStreams(){
		if(mReady){
			mReady = false;
			return mStreams;
		}
		return null;
	}
	public void build(MediaType mType){
		if(mStreamUrl != ""){
			mMediaType = mType.getValue();
			mBuild.execute(mStreamUrl);
		}
	}
	public interface OnBuildStreamListener{
		public void onBuildStreamCompleted(MediaStream[] result);
		public void onBuildStreamPreExecute();
	}
	public class AsyncBuild extends AsyncTask<String, Integer/**/, MediaStream[]>{ 
		
		private OnBuildStreamListener mListener;
		@Override
		protected MediaStream[] doInBackground(String... urls) {
			// TODO Auto-generated method stub
			return getPlayListJSONfromURL(urls[0]);
		}
		@Override
		protected void onPostExecute(MediaStream[] result){
			mListener.onBuildStreamCompleted(result);
		}
		@Override
		protected void onPreExecute(){
			mListener.onBuildStreamPreExecute();
		}
		public void setBuildStreamListener(OnBuildStreamListener _listener){
			mListener = _listener;
		}
		public MediaStream[] getPlayListJSONfromURL(String url){
			InputStream is = null;
			String result = "";
			JSONArray jArray = null;			
			
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
		           switch(mMediaType){
		           case 0:
		        	   return getMedia(jArray);
		        	   
		           case 1:
		        	   return getPicture(jArray);
		        	   
		           case 2:
		        	   return getIdeal(jArray);
		        	   
		           case 3:
		        	   return getNews(jArray);		        	   
		           
		           }
		    }catch(JSONException e){
		            Log.e("log_tag", "Error parsing data "+e.toString());
		            return null;
		    }
		    
		    return null;	   
		    
		}
		public MediaStream[] getMedia(JSONArray jArray){
			List<MediaStream> list = new ArrayList<MediaStream>();
			try{	    		
		           
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
		public MediaStream[] getIdeal(JSONArray jArray){
			return null;
		}
		public MediaStream[] getPicture(JSONArray jArray){
			List<MediaStream> list = new ArrayList<MediaStream>();
			try{
				for(int i = 0; i < jArray.length(); i++){
					MediaStream mStr = new MediaStream("Picture " + Integer.toString(i) , jArray.getString(i));
					list.add(mStr);
				}
			}catch(Exception e){
				return null;
			}
			MediaStream[] smArr = list.toArray(new MediaStream[list.size()]);
		    return smArr;
		}
		public MediaStream[] getNews(JSONArray jArray){
			return null;
		}
	}
}
