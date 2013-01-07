/**
 * 
 */
package com.Streaming.UocNguyen.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.view.ViewHolder;
import com.Streaming.UocNguyen.Common.UocNguyen;


/**
 * @author sang
 *
 */
public class UocNguyenView extends ViewHolder {
	private ListView lstUocNguyenView;
	private TextView txtTitleUocNguyen;
	private ImageButton sendUocNguyenButton;
	
	
	/**
	 * @return the lstUocNguyenView
	 */
	public ListView getLstUocNguyenView() {
		return lstUocNguyenView;
	}
	/**
	 * @param lstUocNguyenView the lstUocNguyenView to set
	 */
	public void setLstUocNguyenView(ListView lstUocNguyenView) {
		this.lstUocNguyenView = lstUocNguyenView;
	}
	/**
	 * @return the txtTitleUocNguyen
	 */
	public TextView getTxtTitleUocNguyen() {
		return txtTitleUocNguyen;
	}
	/**
	 * @param txtTitleUocNguyen the txtTitleUocNguyen to set
	 */
	public void setTxtTitleUocNguyen(TextView txtTitleUocNguyen) {
		this.txtTitleUocNguyen = txtTitleUocNguyen;
	}
	
	public ImageButton getSendUocNguyenButton() {
		return sendUocNguyenButton;
	}
	/**
	 * @param sendUocNguyenButton the sendUocNguyenButton to set
	 */
	public void setSendUocNguyenButton(ImageButton sendUocNguyenButton) {
		this.sendUocNguyenButton = sendUocNguyenButton;
	}
		
	
}
