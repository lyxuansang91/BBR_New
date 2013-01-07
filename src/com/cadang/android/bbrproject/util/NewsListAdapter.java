package com.cadang.android.bbrproject.util;

import java.util.ArrayList;
import java.util.List;

import com.cadang.android.bbrproject.R;
import com.cadang.android.bbrproject.R.*;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class NewsListAdapter extends ArrayAdapter<MediaStream> {
	private Context mContext;
	private ArrayList<MediaStream> glstNews;
	public NewsListAdapter(Context context, 
			int textViewResourceId, ArrayList<MediaStream> objects) {
		super(context, textViewResourceId, objects);
		this.mContext = context;
		this.glstNews = objects;
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View newsView = convertView;
		final ViewHolder holder;
		if(newsView == null){
			LayoutInflater inf = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			newsView = inf.inflate(R.layout.news_layout, null);
			holder = new ViewHolder();
			holder.title 		= (TextView)newsView.findViewById(id.news_title);
			holder.shortDesc 	= (TextView)newsView.findViewById(id.news_shortDesc);
			holder.thumb 		= (ImageView)newsView.findViewById(id.news_thumb);
			newsView.setTag(holder);
		}
		else{
			holder = (ViewHolder)newsView.getTag();
		
		}
		if(position%2==0){
			newsView.setBackgroundResource(R.color.black_overlay05);
		}
		MediaStream news_Item = glstNews.get(position);
		if(news_Item != null){
			/*
			ImageView imgThumb = (ImageView)newsView.findViewById(id.news_thumb);
			TextView	tvTitle		= (TextView)newsView.findViewById(id.news_title);
			TextView	tvShortDesc = (TextView)newsView.findViewById(id.news_shortDesc);
			UrlImageViewHelper.setUrlDrawable(imgThumb, news_Item.getThumb());
			tvTitle.setText(news_Item.getTitle());
			tvShortDesc.setText(news_Item.getShortDesc());
			*/
			holder.title.setText(news_Item.getTitle());
			holder.shortDesc.setText(news_Item.getShortDesc());
			/*
			new LoadBitmapFromUrl(news_Item.getThumb(), new OnMakeSlideListener() {
				
				@Override
				public void onMakeSlideCompelted(Bitmap result) {
					// TODO Auto-generated method stub
					if(result != null){
						holder.thumb.setImageBitmap(result);
						//ImageView imgThumb = (ImageView)newsView.findViewById(id.news_thumb);
					}
				}
			});
			*/
			UrlImageViewHelper.setUrlDrawable(holder.thumb,news_Item.getThumb());
		}
		return newsView;
	}
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItem(int)
	 */
	@Override
	public MediaStream getItem(int position) {
		// TODO Auto-generated method stub
		return glstNews.get(position);
	}
	static class ViewHolder{
		TextView title;
		TextView shortDesc;
		ImageView thumb;
		public ViewHolder(){
			/*
			View v;
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.news_layout, null);
			thumb = (ImageView)v.findViewById(id.news_thumb);
			title	= (TextView)v.findViewById(id.news_title);
			shortDesc = (TextView)v.findViewById(id.news_shortDesc);
			*/			
		}
	}
}
