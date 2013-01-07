package com.cadang.android.bbrproject;

import java.util.ArrayList;
import java.util.Arrays;
import com.android.view.ViewHolder;

import com.cadang.android.bbrproject.util.*;
import com.cadang.android.bbrproject.util.MediaPlayerService.MediaPlayerBinder;
import com.cadang.android.bbrproject.util.MediaStream.MediaType;
import com.cadang.android.bbrproject.util.MediaStream.OnBuildMediaStreamListener;
import com.cadang.android.bbrproject.util.StatefulMediaPlayer.MPStates;
import com.koushikdutta.urlimageviewhelper.UrlImageCache;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pheelicks.visualizer.VisualizerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class BbrActivity extends Activity 
						 implements OnMediaPlayerServiceListener{
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 5000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	/**
	 * Stateful Media and Service*
	 */
	private static final int SWIPE_MIN_DISTANCE = 150;
    private static final int SWIPE_MAX_OFF_PATH = 150;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    
	private StatefulMediaPlayer 	mMediaPlayer;
	private MediaPlayerService	mService;
	private boolean				mBound;
	private MediaStream[]		mPlayStreams 	= ExampleStream.MEDIASTREAMS;
	private MediaStream			mSelectedStream = ExampleStream.DEFAULT_STREAM_MEDIA;
	private int							mStreamPosition;
	private static boolean				mAutoPlay 	= true;
	private static boolean				mRepeated 	= false;
	private static int					mPlayListNo = -1;
	private static Bitmap mBitmap; 
	private boolean				mIntroShow	= false;
	private MediaStream[]	mSlideStream;
	private int				mSlideStreamPosition = 0;
	//private static Bitmap[] 					mSlides = new Bitmap[3];
	private Bitmap 					mSlideBitmap;
	//private static int						mSlidePosition = -1;
	private static final int		SLIDE_DELAY	= 10000;
	private static final int		FADE_DURATION = 300;
	//private static boolean					mSlidetoLeft = false;
	private String			mIdeal = "Have a good Time";
	private static final int				IDEAL_DELAY	= 6000;
	private static boolean			mIdealShow = true;
	
	private final int		NEWS_MAX_PAGE	= 9;
	//private MediaStream[]	mNewsStream;
	private MediaStream[][] newsCache = new MediaStream[NEWS_MAX_PAGE + 1][];
	private int				newsPage = -1;
	private ViewHolder viewHolder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bbr);
				
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		// setup ViewHolder
		viewHolder = new ViewHolder();
		viewHolder.appControlBar 	= controlsView;
		viewHolder.appContent		= contentView;
		viewHolder.appMediaButton 	= (ImageButton)findViewById(R.id.app_media);
		viewHolder.mediaControlBar	= (View)findViewById(R.id.media_controls);
		viewHolder.mediaSlideShow 	= (ImageView)findViewById(R.id.media_slideshow);
		viewHolder.mediaSlideShow2 	= (ImageView)findViewById(R.id.media_slideshow2);
		
		viewHolder.knowledgeDetail	= (View)findViewById(R.id.news_detail);
		viewHolder.knowledgeItems	= (View)findViewById(R.id.news_listing);
		viewHolder.knowledgeDetailStatus = (TextView)findViewById(R.id.knowledge_detail_status);
		viewHolder.knowledgeItemsStatus = (TextView)findViewById(R.id.knowledge_items_status);
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()									
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);							
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
							
						}
						mediaControlsVisible(visible);
						viewHolder.knowledgeDetailStatus.setVisibility(visible? View.VISIBLE : View.GONE);
						viewHolder.knowledgeItemsStatus.setVisibility(visible? View.VISIBLE : View.GONE);
						Log.d("Visiblity Changed", Boolean.toString(visible));
						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});
		//final View mediaControls = (View)findViewById(R.id.media_controls);
		//mediaControls.setOnTouchListener(new OnTouchListener() {
		viewHolder.mediaControlBar.setOnTouchListener(new OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				// TODO Auto-generated method stub
				return false;
			}
		});
		// Set up the user interaction to manually show or hide the system UI.
		//contentView.setOnClickListener(new View.OnClickListener() {
		
		viewHolder.mediaSlideShow.setOnClickListener(new View.OnClickListener() {
			@Override			
			public void onClick(View view) {	
				
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				Log.d("Click slide show","any changed?");				
			}
		});
		//---
		//contentView.setOnTouchListener(mDelayHideTouchListener);
		//---
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		//findViewById(R.id.dummy_button).setOnTouchListener(
		//		mDelayHideTouchListener);
		myInitalize();
		bindToService();
		
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);		
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
		
	};

	Handler mHideHandler = new Handler();	
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void mediaControlsVisible(boolean visible){
		//final View mediaControls = (View)findViewById(R.id.media_controls);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
			//mediaControls
			viewHolder.mediaControlBar
				.animate()
				.alpha(visible? 1 : 0)
				//.translationX(visible? 0 : mediaControls.getWidth())
				.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
		}
		else{
			viewHolder.mediaControlBar.setVisibility(visible? View.VISIBLE : View.GONE);
		}
	}
	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	@Override
	public void onInitializePlayerStart(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitializePlayerSuccess() {
		// TODO Auto-generated method stub
		if(mAutoPlay){
			mService.getMediaPlayer().setState(MPStates.STARTED);
			final ImageButton mPlayPause = (ImageButton)findViewById(R.id.button_media_play);
			mPlayPause.setImageResource(R.drawable.media_pause_button);
		}
		if(!mIntroShow){
			
			Log.d("Start Slide Shhow", " Some thing gone??");
			new MediaStream.Builder(ServerConfig.URLPICTURE, MediaType.PICTURE, onBuildSlideShow);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final ImageView imIntro = (ImageView)findViewById(R.id.app_intro);
			imIntro.setVisibility(View.GONE);
			Log.d("Intro show"," GONE");
			
			mSystemUiHider.show();
			mIntroShow = true;
		}
	}

	@Override
	public boolean onPlayerCompletedMedia() {
		// TODO Auto-generated method stub
		if(mStreamPosition < mPlayStreams.length - 1){
			mStreamPosition += 1;			
		}
		else{
			mStreamPosition = 0;
		}
		mSelectedStream = mPlayStreams[mStreamPosition];
		if((!mRepeated)&&(mStreamPosition == 0)){						
				final ImageButton mPlayPause = (ImageButton)findViewById(R.id.button_media_play);
				mPlayPause.setImageResource(R.drawable.media_play_button);
				mAutoPlay = false;
				mService.getMediaPlayer().setState(MPStates.STOPPED);
				updatePlayerStatus();
				return true;
			
		}
		mService.initializePlayer(mSelectedStream);
		return false;
	}

	@Override
	public void onPlayerError() {
		// TODO Auto-generated method stub
		Log.d("Media Player Activity", "Error");
	}

	@Override
	public Notification onCreateNotification() {
		// TODO Auto-generated method stub
		Notification notif = new NotificationCompat.Builder(this)
						.setContentTitle(getResources().getString(R.string.app_name))
						.setContentTitle(updatePlayerStatus())
						.setSmallIcon(R.drawable.ic_launcher)						
						.build();		
		notif.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), 0);
		return notif;
	}
	public void bindToService() {
        Intent intent = new Intent(this, MediaPlayerService.class);
 
        if (MediaPlayerServiceRunning()) {
            // Bind to LocalService
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            Log.d("Player Service"," bin to service: already running");
        }
        else {
            startService(intent);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            Log.d("Player Service", "bin to service: now running");            
        }
    }
	private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            Log.d("Service Connection","Service connected");
 
            //bound with Service. get Service instance
            MediaPlayerBinder binder = (MediaPlayerBinder) serviceBinder;
            mService = binder.getService();            
            //send this instance to the service, so it can make callbacks on this instance as a client
            mService.setMPSListener(BbrActivity.this);
            mBound = true;
            mService.initializePlayer(mSelectedStream);
            mMediaPlayer = mService.getMediaPlayer();
            
        }
 
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
	private boolean MediaPlayerServiceRunning() {
   	 
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
 
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.cadang.android.bbrproject.util.MediaPlayerService".equals(service.service.getClassName())) {
                return true;
            }
        } 
        return false;
    }
	/**
    * Closes unbinds from service, stops the service, and calls finish()
    */
    public void shutdownActivity() {
 
        if (mBound) {
            mService.stopMediaPlayer();
            // Detach existing connection.
            unbindService(mConnection);
            mBound = false;
         } 
        Intent intent = new Intent(this, MediaPlayerService.class);
        stopService(intent);
        finish();
 
    }
    private void myInitalize(){
    	
    	final View vMedia = (View)findViewById(R.id.media_screen_content);
		final View vKnowledge = (View)findViewById(R.id.knowledge_screen_content);
		final View vShop = (View)findViewById(R.id.shop_screen_content);
		final View vDairy = (View)findViewById(R.id.dairy_screen_content);
		final View vWish = (View)findViewById(R.id.wish_screen_content);
		hideAllLayer();		
		vMedia.setVisibility(View.VISIBLE);
    	new MediaStream.Builder(ServerConfig.URLSONG, MediaType.MEDIA, onBuildMedia);
    	//new MediaStream.Builder(ServerConfig.URLPICTURE, MediaType.PICTURE, onBuildSlideShow);
    	new MediaStream.Builder(ServerConfig.URLIDEAL, MediaType.IDEAL, onBuildIdeal);
    	
    	idealShow(IDEAL_DELAY);
    	
    	//final ImageButton mAppMedia = (ImageButton)findViewById(R.id.app_media);
    	viewHolder.appMediaButton.setImageResource(R.drawable.app_media);
    	viewHolder.appMediaButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				hideAllLayer();
				viewHolder.appMediaButton.setImageResource(R.drawable.app_media);
				vMedia.setVisibility(View.VISIBLE);
				
			}
		});
    	final ImageButton mAppKnowledge = (ImageButton)findViewById(R.id.app_knowledge);
    	mAppKnowledge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				hideAllLayer();
				mAppKnowledge.setImageResource(R.drawable.app_knowledge);
				vKnowledge.setVisibility(View.VISIBLE);
				if(newsPage < 0){
					newsPage = 1;
					new MediaStream.Builder(ServerConfig.URLNEWS +(newsPage > 0? Integer.toString(newsPage) : ""), MediaType.NEWS, onBuildNews);
					
				}
				viewHolder.knowledgeItems.setVisibility(View.VISIBLE);
				viewHolder.knowledgeDetail.setVisibility(View.GONE);
			}
		});
    	final ImageButton mAppShop = (ImageButton)findViewById(R.id.app_shop);
    	mAppShop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				hideAllLayer();
				mAppShop.setImageResource(R.drawable.app_shop);
				vShop.setVisibility(View.VISIBLE);
			}
		});
    	final ImageButton mAppDairy = (ImageButton)findViewById(R.id.app_dairy);
    	mAppDairy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				hideAllLayer();
				mAppDairy.setImageResource(R.drawable.app_dairy);
				vDairy.setVisibility(View.VISIBLE);
				//startActivity(new Intent("com.cadang.android..IntroLoadingActivity"));
				
			}
		});
    	final ImageButton mAppWish = (ImageButton)findViewById(R.id.app_wish);
    	mAppWish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				hideAllLayer();
				mAppWish.setImageResource(R.drawable.app_wish);
				vWish.setVisibility(View.VISIBLE);
			}
		});
    	
    	final ImageButton mPlayPause = (ImageButton)findViewById(R.id.button_media_play);
    	mPlayPause.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				if(mBound){
					mMediaPlayer = mService.getMediaPlayer();
					Log.d("Play Click", mMediaPlayer.getState().toString());
					mAutoPlay = !mAutoPlay;
					if(!mAutoPlay){
						if(mMediaPlayer.isPlaying()){
							mService.pauseMediaPlayer();
						}
					}
					else{
						if (mMediaPlayer.isStopped()
	                            || mMediaPlayer.isCreated()
	                            || mMediaPlayer.isEmpty()){                        	
	                    	mService.initializePlayer(mSelectedStream);
	                    } 
						else if (mMediaPlayer.isPrepared()
	                            || mMediaPlayer.isPaused()){
	                        mService.startMediaPlayer();
						}
					}
					Log.d("Play Click", mMediaPlayer.getState().toString());
					if(mAutoPlay){
						mPlayPause.setImageResource(R.drawable.media_pause_button);
					}
					else{
						mPlayPause.setImageResource(R.drawable.media_play_button);
					}
					updatePlayerStatus();
				}
			}
		});
    	final ImageButton mNext = (ImageButton)findViewById(R.id.button_media_next);
    	mNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				if(mStreamPosition < mPlayStreams.length - 1){
					mStreamPosition += 1;
				}
				else{
					mStreamPosition = 0;
				}
				mSelectedStream = mPlayStreams[mStreamPosition];
				mService.initializePlayer(mSelectedStream);
				updatePlayerStatus();
			}
		});
    	final ImageButton mPrev = (ImageButton)findViewById(R.id.button_media_prev);
    	mPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				if(mStreamPosition > 0){
					mStreamPosition -= 1;
				}
				else{
					mStreamPosition = mPlayStreams.length - 1;
				}
				mSelectedStream = mPlayStreams[mStreamPosition];
				mService.initializePlayer(mSelectedStream);
				updatePlayerStatus();
			}
		});
    	final TextView tvIdeal = (TextView)findViewById(R.id.media_ideal_text);
    	tvIdeal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				String stIdeal = (String) tvIdeal.getText();
				Toast.makeText(getApplicationContext(), stIdeal, Toast.LENGTH_LONG).show();
			}
		});
    }    
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	private String updatePlayerStatus(){
		String status;
		mMediaPlayer = mService.getMediaPlayer();
		status = getResources().getStringArray(R.array.playerStatus)[ mMediaPlayer.getState().getValue()] 
				+ "  " + mMediaPlayer.getMediaStream().getTitle();
		final TextView tvStatus =(TextView)findViewById(R.id.media_info_status);
		tvStatus.setText(status);
		tvStatus.setSelected(true);		
		return status;
	}
	
	Handler mMediaHandler = new Handler();
	
	OnBuildMediaStreamListener onBuildMedia = new OnBuildMediaStreamListener() {
		
		@Override
		public void onBuildMediaStreamPreExecute() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onBuildMediaStreamCompleted(MediaStream[] result) {
			// TODO Auto-generated method stub
			if(result != null){
				mPlayStreams = result;
	        	mStreamPosition = 0;
	    		mSelectedStream = mPlayStreams[mStreamPosition];
	    		if(mBound){
	    			if(mService.getMediaPlayer().isPlaying()){
	    				mService.stopMediaPlayer();        		
	    			}
	    			mService.initializePlayer(mSelectedStream);
	    		}
			}
		}
	};
	OnBuildMediaStreamListener onBuildIdeal = new OnBuildMediaStreamListener() {
		
		@Override
		public void onBuildMediaStreamPreExecute() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onBuildMediaStreamCompleted(MediaStream[] result) {
			// TODO Auto-generated method stub
			if(result != null){
				mIdeal = result[0].getTitle();
			}
			//idealShow(IDEAL_DELAY);
		}
	};
	OnBuildMediaStreamListener onBuildSlideShow = new OnBuildMediaStreamListener() {
		
		@Override
		public void onBuildMediaStreamPreExecute() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onBuildMediaStreamCompleted(MediaStream[] result) {
			// TODO Auto-generated method stub
			if(result != null){
				if(mSlideStream != null){
					
				}
				mSlideStream = result;
				mSlideStreamPosition = 0;				
				makeSlideShow(SLIDE_DELAY);				
			}
		}
	};
	 
	OnMakeSlideListener onMakeSlideListener = new OnMakeSlideListener() {
		
		@Override
		public void onMakeSlideCompelted(Bitmap result) {
			// TODO Auto-generated method stub
			if(result != null){
				if(mSlideBitmap != null){
					mSlideBitmap.recycle();
					mSlideBitmap = null;
				}
				mSlideBitmap = result;
				viewHolder.mediaSlideShow2.setImageBitmap(mSlideBitmap);
				
			}
		}
	};
		
	Runnable rSlideShow = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//final ImageView img = (ImageView)findViewById(R.id.media_slideshow);
			//NewSlideShow.fadeEffect(viewHolder.mediaSlideShow, viewHolder.mediaSlideShow2, FADE_DURATION);
			makeSlideShow(SLIDE_DELAY);
		}
	};
	
	public void makeSlideShow(int duration){
		mMediaHandler.removeCallbacks(rSlideShow);		
		if(mSlideStreamPosition >= mSlideStream.length - 1){
			mSlideStreamPosition = 0;
			new MediaStream.Builder(ServerConfig.URLPICTURE, MediaType.PICTURE, onBuildSlideShow);
		}
		else{
			NewSlideShow.fadeEffect(viewHolder.mediaSlideShow, viewHolder.mediaSlideShow2, FADE_DURATION);
			new LoadBitmapFromUrl(mSlideStream[mSlideStreamPosition].getUrl(), onMakeSlideListener);
			//slide.setMakeSlideListener(onMakeSlideListener);
			//slide.execute(mSlideStream[mSlideStreamPosition].getUrl());
			//UrlImageViewHelper.setUrlDrawable(viewHolder.mediaSlideShow2, mSlideStream[mSlideStreamPosition].getUrl());
			
			mMediaHandler.postDelayed(rSlideShow, SLIDE_DELAY);
			mSlideStreamPosition += 1;
		}
		//Log.d("Slide Stream Position: ",Integer.toString(mSlideStreamPosition));
	}
	Runnable rShowIdeal = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			idealShow(IDEAL_DELAY);
		}
	};
	private void idealShow(int delay){
		idealVisible(mIdealShow);
		mMediaHandler.removeCallbacks(rShowIdeal);
		
		if(mIdealShow){
			mMediaHandler.postDelayed(rShowIdeal, delay * 2);
		}
		else{
			mMediaHandler.postDelayed(rShowIdeal, delay);		
			new MediaStream.Builder(ServerConfig.URLIDEAL, MediaType.IDEAL, onBuildIdeal);
		}
		mIdealShow = !mIdealShow;
		
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void idealVisible(boolean visible){
		final TextView tvIdeal = (TextView)findViewById(R.id.media_ideal_text);
		if(mIdeal != null){
			tvIdeal.setText(mIdeal);
		}
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
				tvIdeal
					.animate()
					.alpha(visible? 1 : 0)
					.translationX(visible? 0 : tvIdeal.getWidth())
					.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
			}
			else{
				tvIdeal.setVisibility(visible? View.VISIBLE : View.INVISIBLE);
			}		
	}
	private void hideAllLayer(){
		final View vMedia = (View)findViewById(R.id.media_screen_content);
		final View vKnowledge = (View)findViewById(R.id.knowledge_screen_content);
		final View vShop = (View)findViewById(R.id.shop_screen_content);
		final View vDairy = (View)findViewById(R.id.dairy_screen_content);
		final View vWish = (View)findViewById(R.id.wish_screen_content);
		
		final ImageButton imgMedia = (ImageButton)findViewById(R.id.app_media);
		final ImageButton imgKnowledge = (ImageButton)findViewById(R.id.app_knowledge);
		final ImageButton imgShop = (ImageButton)findViewById(R.id.app_shop);
		final ImageButton imgDairy = (ImageButton)findViewById(R.id.app_dairy);
		final ImageButton imgWish = (ImageButton)findViewById(R.id.app_wish);
		
		vMedia.setVisibility(View.GONE);
		vKnowledge.setVisibility(View.GONE);
		vShop.setVisibility(View.GONE);
		vDairy.setVisibility(View.GONE);
		vWish.setVisibility(View.GONE);
		
		imgMedia.setImageResource(R.drawable.app_media_i);
		imgKnowledge.setImageResource(R.drawable.app_knowledge_i);
		imgShop.setImageResource(R.drawable.app_shop_i);
		imgDairy.setImageResource(R.drawable.app_dairy_i);
		imgWish.setImageResource(R.drawable.app_wish_i);
	}
	OnBuildMediaStreamListener onBuildNews = new OnBuildMediaStreamListener() {
		
		@Override
		public void onBuildMediaStreamPreExecute() {
			// TODO Auto-generated method stub
		
		}
		
		@Override
		public void onBuildMediaStreamCompleted(MediaStream[] result) {
			// TODO Auto-generated method stub
			if(result != null){
				//mNewsStream = result;
				newsCache[newsPage] = result;
				newsListSetup(newsCache[newsPage]);
			}
		}
	};
	private void newsListSetup(MediaStream[] newsStream){
		ListView list = (ListView)findViewById(R.id.listNews);
		ArrayList<MediaStream> arrayList = new ArrayList<MediaStream>();
		for(MediaStream mSt: newsStream){
			arrayList.add(mSt);
		}
		NewsListAdapter adapter = new NewsListAdapter(this, android.R.layout.simple_list_item_single_choice, arrayList);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				// TODO Auto-generated method stub
				MediaStream mStr = (MediaStream)parent.getItemAtPosition(position);
				Log.d("Item Click " + Integer.toString(position), ServerConfig.URLNEWS_DETAIL + mStr.getUrl());
				if(mStr != null){
					WebView webView = (WebView)findViewById(R.id.news_detailweb);
					webView.setOnTouchListener(knowledgeDetailTouch);
					viewHolder.knowledgeDetail.setVisibility(View.VISIBLE);
					webView.clearView();
					webView.setVisibility(View.VISIBLE);
					webView.setWebViewClient(new WebViewClient());
					webView.loadUrl(ServerConfig.URLNEWS_DETAIL + mStr.getUrl());
					
				}
			}
		});
		//final GestureDetector gestureDetecter = new GestureDetector(new MyGestureDetector());
		View.OnTouchListener newsListOnTouchListener = new View.OnTouchListener() {
			float x1, y1;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
				// TODO Auto-generated method stub
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					x1 = event.getX();
					y1 = event.getY();
					return false;
				case MotionEvent.ACTION_UP:
					try {
		                if (Math.abs(y1 - event.getY()) > SWIPE_MAX_OFF_PATH)
		                	return false;
		                // right to left swipe
		                if(x1 - event.getX() > SWIPE_MIN_DISTANCE){
		                    //Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
		                	//gesture.onSwipeLeft();
		                    if(newsPage < NEWS_MAX_PAGE){
		                    	newsPage++;
		                    	Toast.makeText(getApplicationContext(), "Loading page " + Integer.toString(newsPage), Toast.LENGTH_SHORT).show();
		                    	//new MediaStream.Builder(ServerConfig.URLNEWS +(newsPage > 0? Integer.toString(newsPage) : ""), MediaType.NEWS, onBuildNews);
		                    	if(newsCache[newsPage] != null){
		                    		newsListSetup(newsCache[newsPage]);
		                    	}
		                    	else{
		                    		new MediaStream.Builder(ServerConfig.URLNEWS +(newsPage > 0? Integer.toString(newsPage) : ""), MediaType.NEWS, onBuildNews);
		                    	}
		                    }
		                    else{
		                    	Toast.makeText(getApplicationContext(), "Last page loaded!", Toast.LENGTH_SHORT).show();
		                    }
		                    return true;
		                }  else if (event.getX() - x1 > SWIPE_MIN_DISTANCE){
		                    //Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
		                	//gesture.onSwipeRight();
		                    if(newsPage > 1){
		                    	newsPage--;
		                    	Toast.makeText(getApplicationContext(), "Loading page " + Integer.toString(newsPage), Toast.LENGTH_SHORT).show();
		                    	//new MediaStream.Builder(ServerConfig.URLNEWS +(newsPage > 0? Integer.toString(newsPage) : ""), MediaType.NEWS, onBuildNews);
		                    	if(newsCache[newsPage] != null){
		                    		newsListSetup(newsCache[newsPage]);
		                    	}
		                    	else{
		                    		new MediaStream.Builder(ServerConfig.URLNEWS +(newsPage > 0? Integer.toString(newsPage) : ""), MediaType.NEWS, onBuildNews);
		                    	}
		                    }
		                    else{
		                    	Toast.makeText(getApplicationContext(), "First page!", Toast.LENGTH_SHORT).show();
		                    }
		                    return true;
		                }
		                return false;
		            } catch (Exception e) {
		                // nothing
		            }
				}
				
				return false;
			}
		};
		list.setOnTouchListener(newsListOnTouchListener);
		//viewHolder.knowledgeDetail.setOnTouchListener(knowledgeDetailTouch);
	}
	OnTouchListener knowledgeDetailTouch = new OnTouchListener() {
		float x1, y1;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			// TODO Auto-generated method stub
			Log.d("Touch Detail", " do something");
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				x1 = event.getX();
				y1 = event.getY();
				Log.d("Touch Detail", " actiov down");
				return false;
			case MotionEvent.ACTION_UP:
				try {
	                if (Math.abs(y1 - event.getY()) > SWIPE_MAX_OFF_PATH)
	                	return false;
	                // right to left swipe
	                if(x1 - event.getX() > SWIPE_MIN_DISTANCE){
	                    //Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
	                	//gesture.onSwipeLeft();
	                	viewHolder.knowledgeDetail.setVisibility(View.GONE);
						viewHolder.knowledgeItems.setVisibility(View.VISIBLE);
						
	                    return true;
	                }  else if (event.getX() - x1 > SWIPE_MIN_DISTANCE){
	                    //Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
	                	//gesture.onSwipeRight();
	                	viewHolder.knowledgeDetail.setVisibility(View.GONE);
						viewHolder.knowledgeItems.setVisibility(View.VISIBLE);
						
	                    return true;
	                }
	                return false;
	            } catch (Exception e) {
	                // nothing
	            }
				/*
				x2 = event.getX();
				y2 = event.getY();
				//Log.d("Touch Detail", " action up"+ Float.toString(x1)+Float.toString(y1)+Float.toString(x2)+Float.toString(y2));
				if(Math.abs(y2 - y1) < 30){
					if(Math.abs(x2 - x1) > 50){
						Log.d("", Float.toString(x2 - x1) + Float.toString(y2 - y1));
					//Toast.makeText(getApplicationContext(), "Close detail", Toast.LENGTH_LONG).show();
						viewHolder.knowledgeDetail.setVisibility(View.GONE);
						viewHolder.knowledgeItems.setVisibility(View.VISIBLE);
						return true;
					}
				}
				*/
			}
			return false;
		}
	};
	//end of bbr class
	 /* static class ViewHolder{
		ImageButton appMediaButton;
		ImageButton appKnowledgeButton;
		ImageButton appShopButton;
		ImageButton appDairyButton;
		ImageButton appWishButton;
		View 		appBackGround;
		View 		appControlBar;
		View 		appContent;
		View		appMediaContent;
		View		appKnowledgeContent;
		View		appShopContent;
		View		appDairyContent;
		View		appWishContent;
		
		ImageButton 	mediaPrevButton;
		ImageButton 	mediaPlayPauseButton;
		ImageButton 	mediaNextButton;
		View 			mediaControlBar;
		ImageView 		mediaSlideShow;
		ImageView		mediaSlideShow2;
		TextView 		mediaStatus;
		TextView 		mediaIdeal;
		VisualizerView 	mediaVisualizer;
		
		TextView		knowledgeTitle;
		TextView		knowledgeItemsStatus;
		TextView		knowledgeDetailStatus;
		View			knowledgeItems;
		View			knowledgeDetail;
		
		public ViewHolder(){
			
		}
	} */
	public class MyGestureDetector extends SimpleOnGestureListener{

		/* (non-Javadoc)
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
		 */
		private OnMyGestureDetector gesture;
		public void setMyGestureListener(OnMyGestureDetector listener){
			gesture = listener;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                	return super.onFling(e1, e2, velocityX, velocityY);
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(SelectFilterActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                	gesture.onSwipeLeft();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(SelectFilterActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                	gesture.onSwipeRight();
                }
            } catch (Exception e) {
                // nothing
            }
			return super.onFling(e1, e2, velocityX, velocityY);
			
		}
		
	}
	public interface OnMyGestureDetector{
		public void onSwipeLeft();
		public void onSwipeRight();
	}	
}
