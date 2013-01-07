/**
 * 
 */
package com.Streaming.UocNguyen.Controller;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.Streaming.UocNguyen.View.UocNguyenView;
import com.Streaming.UocNguyen.Model.*;
/**
 * @author sang
 *
 */
public class UocNguyenController {
	
	private static final String TAG = "UocNguyen";
	
	private UocNguyenView viewUocNguyen;
	private UocNguyenModel modelUocNguyen;
	
	public void InitViews(){
		UocNguyenView currView =this.getViewUocNguyen();
		currView.getSendUocNguyenButton().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "On Click Send:");
				
				/* On Click Send Button */
				Toast.makeText(null, "Send Button Uoc Nguyen", Toast.LENGTH_LONG).show();
				
				/* End Click */
			}
		});
		
		
		
		
	}
	
	

	/**
	 * @param viewUocNguyen
	 * @param modelUocNguyen
	 */
	public UocNguyenController(UocNguyenView viewUocNguyen,
			UocNguyenModel modelUocNguyen) {
		super();
		this.viewUocNguyen = viewUocNguyen;
		this.modelUocNguyen = modelUocNguyen;
	}
	/**
	 * 
	 */
	public UocNguyenController() {
		super();
	}
	
	/**
	 * @return the viewUocNguyen
	 */
	public UocNguyenView getViewUocNguyen() {
		return viewUocNguyen;
	}

	/**
	 * @param viewUocNguyen the viewUocNguyen to set
	 */
	public void setViewUocNguyen(UocNguyenView viewUocNguyen) {
		this.viewUocNguyen = viewUocNguyen;
	}



	/**
	 * @return the modelUocNguyen
	 */
	public UocNguyenModel getModelUocNguyen() {
		return modelUocNguyen;
	}



	/**
	 * @param modelUocNguyen the modelUocNguyen to set
	 */
	public void setModelUocNguyen(UocNguyenModel modelUocNguyen) {
		this.modelUocNguyen = modelUocNguyen;
	}
	
	

}
