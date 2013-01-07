/**
 * 
 */
package com.cadang.android.bbrproject.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Cadang
 *
 */
public class LoadBitmapFromUrl extends AsyncTask<String, Integer, Bitmap> {
	
	private OnMakeSlideListener mListener;
	public LoadBitmapFromUrl(){
		
	}
	public LoadBitmapFromUrl(String _url, OnMakeSlideListener _listener){
		mListener = _listener;
		this.execute(_url);
	}
	@Override
	protected Bitmap doInBackground(String... url) {
		// TODO Auto-generated method stub
		InputStream is = null;
		Bitmap mBitmap = null;
		try{
    		HttpURLConnection con = (HttpURLConnection) new URL(url[0]).openConnection();
    		con.connect();
    		is = con.getInputStream();	    	    
    		mBitmap = BitmapFactory.decodeStream(is);
    	}catch(Exception e){
            Log.e("log_tag", "Error in loading image " + e.toString());
            mBitmap = null;		
		}
		try {
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mBitmap;	
	}
	@Override
	protected void onPostExecute(Bitmap result){
		
		mListener.onMakeSlideCompelted(result);
	}
	public void setMakeSlideListener(OnMakeSlideListener _slide){
		mListener = _slide;
	}
	
}
