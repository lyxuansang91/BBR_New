/**
 * 
 */
package com.cadang.android.bbrproject.util;

/**
 * @author sang
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class SendWishedMessage extends AsyncTask<Void, Void, Void> {
	protected String mMessage;
	protected String mUrl;

	public SendWishedMessage(String mMessage, String mUrl) {
		super();
		this.mMessage = mMessage;
		this.mUrl = mUrl;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(mUrl);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("wish", mMessage));
			Log.v("Data to send ", nameValuePairs.toString());
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			Log.v("Respone ", response.getEntity().toString());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e("ClientProtocol Exception ", e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("IO Exception ", e.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
