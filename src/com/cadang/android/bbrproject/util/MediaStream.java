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

public class MediaStream {
	public enum MediaType{
		MEDIA(0), PICTURE(1), IDEAL(2), NEWS(3), NONE(-1);
		private int mValue;
		private MediaType(int _value){	this.mValue = _value;	}
		public int getValue(){			return mValue;			}
	}
	public interface OnBuildMediaStreamListener{
		public void onBuildMediaStreamCompleted(MediaStream[] result);
		public void onBuildMediaStreamPreExecute();
	}
	private String mID;
	private String mMediaLabel;	// Title
	private String mMediaUrl;
	private String mShortDesc;
	private String mThumb;
	
	public MediaStream(){
		this("","");
	}
	public MediaStream(String streamLabel, String streamUrl){
		mMediaLabel 	= streamLabel;
		mMediaUrl		= streamUrl;
	}
	/**
	 * 
	 * @param strings Title, Url, ShortDesciption, Thumb, ID
	 */
	public MediaStream(String...strings){
		int i = 0;
		if(i < strings.length - 1){
			mMediaLabel = strings[i];
			i++;
		}
		if(i < strings.length - 1){
			mMediaUrl	= strings[i];
			i++;
		}
		if(i < strings.length - 1){
			mShortDesc	= strings[i];
			i++;
		}
		if(i < strings.length - 1){
			mThumb		= strings[i];
			i++;
		}
		if(i < strings.length - 1){
			mID			= strings[i];
			i++;
		}
	}
	public String getLabel(){
		return mMediaLabel;
	}
	public String getTitle(){
		return mMediaLabel;
	}
	public String getID(){
		return mID;
	}
	public String getThumb(){
		return mThumb;
	}
	public String getShortDesc(){
		return mShortDesc;
	}
	public String getUrl(){
		return mMediaUrl;
	}
	public void setLabel(String mediaLabel){
		mMediaLabel = mediaLabel;
	}
	public void setUrl(String mediaUrl){
		mMediaUrl = mediaUrl;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaStream other = (MediaStream) obj;
        if (mMediaLabel == null) {
            if (other.mMediaLabel != null)
                return false;
        }
        else if (!mMediaLabel.equals(other.mMediaLabel))
            return false;
        if (mMediaUrl == null) {
            if (other.mMediaUrl != null)
                return false;
        }
        else if (!mMediaUrl.equals(other.mMediaUrl))
            return false;
        return true;
	}
	@Override
	public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mMediaLabel == null) ? 0 : mMediaLabel.hashCode());
        result = prime * result
                + ((mMediaUrl == null) ? 0 : mMediaUrl.hashCode());
        return result;
    }
	public static class Builder {
		
		private MediaStream[] mStreams;
		private boolean mReady;
		private String 	mStreamUrl;
		private int 	mMediaType;
		private OnBuildMediaStreamListener mListener;
		public Builder(){
			mReady 		= false;
			mStreams 	= null;
			mStreamUrl 	= "";
			mMediaType = MediaType.NONE.getValue();
		}
		public Builder(String url, MediaType type, OnBuildMediaStreamListener _listener){
			mStreamUrl = url;
			mMediaType = type.getValue();
			this.mListener = _listener;
			AsyncBuild mBuild = new AsyncBuild();
			mBuild.setBuildMediaStreamListener(new OnBuildMediaStreamListener() {
				
				@Override
				public void onBuildMediaStreamPreExecute() {
					// TODO Auto-generated method stub
					Log.d("PreExecute", "Prepare info");
				}
				
				@Override
				public void onBuildMediaStreamCompleted(MediaStream[] result) {
					// TODO Auto-generated method stub
					Log.d("Media Completed", "Ready for data");
					if(result != null){
						mReady = true;
						mStreams = result;
					}
					else{
						mReady = false;
						mStreams = null;
					}
					mListener.onBuildMediaStreamCompleted(result);
				}
			});				
			mBuild.execute(mStreamUrl);		
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
		public void build(){
			AsyncBuild mBuild = new AsyncBuild();
			mBuild.setBuildMediaStreamListener(new OnBuildMediaStreamListener() {
				
				@Override
				public void onBuildMediaStreamPreExecute() {
					// TODO Auto-generated method stub
					Log.d("PreExecute", "Prepare info");
				}
				
				@Override
				public void onBuildMediaStreamCompleted(MediaStream[] result) {
					// TODO Auto-generated method stub
					Log.d("Media Completed", "Ready for data");
					if(result != null){
						mReady = true;
						mStreams = result;
					}
					else{
						mReady = false;
						mStreams = null;
					}
					mListener.onBuildMediaStreamCompleted(result);
				}
			});				
			mBuild.execute(mStreamUrl);
		}		
		public class AsyncBuild extends AsyncTask<String, Integer/**/, MediaStream[]>{ 
			
			private OnBuildMediaStreamListener mListener;
			@Override
			protected MediaStream[] doInBackground(String... urls) {
				// TODO Auto-generated method stub
				Log.d("backgound running", "waiting");
				MediaStream[] test = getMediaStreamFromURL(urls[0]);
				Log.d("backgound running", "return");
				return test;
			}
			@Override
			protected void onPostExecute(MediaStream[] result){
				Log.d("Executed", "Ready for data");
				mListener.onBuildMediaStreamCompleted(result);
			}
			@Override
			protected void onPreExecute(){
				mListener.onBuildMediaStreamPreExecute();
			}
			public void setBuildMediaStreamListener(OnBuildMediaStreamListener _listener){
				mListener = _listener;
			}
			public MediaStream[] getMediaStreamFromURL(String url){
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
			           Log.d("JSON OK","Parse JSON Obj");
			           switch(mMediaType){
			           case 0:
			        	   Log.d("Get Media", "Return Media");
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
			    Log.d("Media Ok", "Return data");
			    return smArr;
			}
			public MediaStream[] getIdeal(JSONArray jArray){
				MediaStream[] msArr = new MediaStream[1];
				if(jArray != null){
					try{
						JSONObject jO = jArray.getJSONObject(0);
						msArr[0] = new MediaStream(jO.getString("content"), "");
					}catch(Exception e){
						
					}
				}
				return msArr;
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
				List <MediaStream> list = new ArrayList<MediaStream>();
				try{
					for(int i = 0; i < jArray.length(); i++){
						JSONObject jObj = jArray.getJSONObject(i);
						MediaStream mStream = new MediaStream(
													jObj.getString("title"),//title
													jObj.getString("id"), 	//url
													jObj.getString("shortDescription"),
													jObj.getString("thumb"),
													jObj.getString("id")
														);
						list.add(mStream);
					}
				}catch(Exception e){
					
				}
				MediaStream[] smArr = list.toArray(new MediaStream[list.size()]);
			    Log.d("News Ok", "Return data");
			    return smArr;
			}
		}
	}

}
