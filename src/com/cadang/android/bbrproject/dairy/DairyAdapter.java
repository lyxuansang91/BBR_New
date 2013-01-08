package com.cadang.android.bbrproject.dairy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cadang.android.bbrproject.R;
import com.cadang.android.bbrproject.R.id;

public class DairyAdapter extends ArrayAdapter<MyDairy> {
	private ArrayList<MyDairy> glstNhatKy;
	private Context mContext;
	private ViewHolder holder;
	public DairyAdapter(Context context, int textViewResourceId,
			ArrayList<MyDairy> objects) {
		super(context, textViewResourceId, objects);
		this.mContext = context;
		this.glstNhatKy = objects;
		holder = new ViewHolder();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View dairy = convertView;
		if(dairy == null){
			LayoutInflater inf = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dairy = inf.inflate(R.layout.layout_dairy, null);
			holder = new ViewHolder();
			holder.tvTime 		= (TextView)dairy.findViewById(id.dairy_text_hour);
			holder.tvDate 		= (TextView)dairy.findViewById(id.dairy_text_day);
			holder.tvMonthYear	= (TextView)dairy.findViewById(id.dairy_text_monthyear);
			holder.tvMessageBody= (TextView)dairy.findViewById(id.dairy_text_content);
			dairy.setTag(holder);
		}
		else{
			holder = (ViewHolder)dairy.getTag();		
		}
		MyDairy nhatky = glstNhatKy.get(position);
		if(nhatky != null){
			Date d = nhatky.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String st = sdf.format(d);
			holder.tvTime.setText(st);
			sdf = new SimpleDateFormat("EEEE");
			st = sdf.format(d);
			holder.tvDate.setText(st);
			sdf = new SimpleDateFormat("MM - yyyy");
			st = sdf.format(d);
			holder.tvMonthYear.setText(st);
			holder.tvMessageBody.setText(nhatky.getMessage());
		}
		return dairy;
	}

	@Override
	public MyDairy getItem(int position) {
		return glstNhatKy.get(position);
	}
	private static class ViewHolder{
		TextView tvTime;
		TextView tvDate;
		TextView tvMonthYear;
		TextView tvMessageBody;
	}
}
