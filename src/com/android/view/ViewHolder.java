/**
 * 
 */
package com.android.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pheelicks.visualizer.VisualizerView;

/**
 * @author sang
 * 
 */
public class ViewHolder {

	public ImageButton appMediaButton;
	public ImageButton appKnowledgeButton;
	public ImageButton appShopButton;
	public ImageButton appDairyButton;
	public ImageButton appWishButton;
	public View appBackGround;
	public View appControlBar;
	public View appContent;
	public View appMediaContent;
	public View appKnowledgeContent;
	public View appShopContent;
	public View appDairyContent;
	public View appWishContent;

	public ImageButton mediaPrevButton;
	public ImageButton mediaPlayPauseButton;
	public ImageButton mediaNextButton;
	public View mediaControlBar;
	public ImageView mediaSlideShow;
	public ImageView mediaSlideShow2;
	public TextView mediaStatus;
	public TextView mediaIdeal;
	public VisualizerView mediaVisualizer;

	public TextView knowledgeTitle;
	public TextView knowledgeItemsStatus;
	public TextView knowledgeDetailStatus;
	public View knowledgeItems;
	public View knowledgeDetail;
	/*  begin Uoc Nguyen */
	public ImageButton btnUocNguyen;
	public ListView lstUocNguyen;
	public TextView lblUocNguyen;
	
	
	/* end Uoc Nguyen */

	public ViewHolder() {

	}

}
