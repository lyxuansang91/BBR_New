package com.cadang.android.bbrproject.dairy;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.cadang.android.bbrproject.util.ViewHolder;

public class DairyListener {
	private ViewHolder holder;
	private Context context;
	private DairyAction mDairyAction;
	public DairyListener(Context _context, ViewHolder _holder){
		holder = _holder;
		context = _context;
		mDairyAction = new DairyAction(context);
	}
	public void setupDairy(){
		
		updateDairyList();
		holder.dairySaveMessageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(holder.dairyMessage.getText().toString() != ""){
					mDairyAction.insertNotHasImage("", holder.dairyMessage.getText().toString(), new Date());
					holder.dairyMessage.setText("");
					updateDairyList();
				}
			}
		});		
	}
	public void updateDairyList(){
		try{
			ArrayList<MyDairy> arrayList = mDairyAction.getAllNhatKy();
			if(arrayList != null){
				DairyAdapter adapter = new DairyAdapter(context, android.R.layout.simple_expandable_list_item_1, arrayList);
				holder.dairyListMessages.setAdapter(adapter);
			}
			else{
				
			}
		}catch(Exception e){
			
		}
	}
	
}
