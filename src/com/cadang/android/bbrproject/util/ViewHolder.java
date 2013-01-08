package com.cadang.android.bbrproject.util;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pheelicks.visualizer.VisualizerView;

public class ViewHolder {

	// App view
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
	// Media view
	public View mediaScreen;
	public ImageButton mediaPrevButton;
	public ImageButton mediaPlayPauseButton;
	public ImageButton mediaNextButton;
	public View mediaControlBar;
	public ImageView mediaSlideShow;
	public ImageView mediaSlideShow2;
	public TextView mediaStatus;
	public TextView mediaIdeal;
	public VisualizerView mediaVisualizer;
	// Knowledge view
	public View knowledgeScreen;
	public TextView knowledgeTitle;
	public TextView knowledgeItemsStatus;
	public TextView knowledgeDetailStatus;
	public View knowledgeItems;
	public View knowledgeDetail;
	// Shop view
	public View shopScreen;
	// Dairy view
	public View dairyScreen;
	public ListView dairyListMessages;
	public ImageButton dairySaveMessageButton;
	public EditText dairyMessage;
	// Wish view
	public View wishScreen;
	public ImageButton wishButton;
	public EditText wishMessage;
	public ImageView wishImage;
	
	public ViewHolder() {

	}
}
