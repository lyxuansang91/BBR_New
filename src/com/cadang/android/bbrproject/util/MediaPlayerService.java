package com.cadang.android.bbrproject.util;

import com.cadang.android.bbrproject.util.StatefulMediaPlayer.MPStates;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MediaPlayerService extends Service 
								implements 	OnBufferingUpdateListener, 
											OnInfoListener, 
											OnPreparedListener, 
											OnErrorListener,
											OnCompletionListener{
	private StatefulMediaPlayer mMediaPlayer = new StatefulMediaPlayer();
	private OnMediaPlayerServiceListener mMPSListener;

	private final Binder mBinder = new MediaPlayerBinder();
	
	public class MediaPlayerBinder extends Binder{
		public MediaPlayerService getService(){
			return MediaPlayerService.this;
		}		
	}
	
	public StatefulMediaPlayer getMediaPlayer(){
		return mMediaPlayer;
	}
	
	public void initializePlayer(MediaStream mediaStream) {
		if(mMediaPlayer!=null){
        	mMediaPlayer.release();
        }
        mMediaPlayer = new StatefulMediaPlayer(mediaStream); 
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);               
        mMediaPlayer.prepareAsync();
        mMPSListener.onInitializePlayerStart("Connecting...");        
    }
	public void initializePlayer(String streamUrl) {
		MediaStream stream = new MediaStream("", streamUrl);
		initializePlayer(stream);
    }		
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mMPSListener.onInitializePlayerSuccess();
		
		if(mMediaPlayer.getState()==MPStates.STARTED){
			startMediaPlayer();		
		}else{
			mMediaPlayer.setState(MPStates.PREPARED);
		}		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Player Service", "on Start command");
		return START_STICKY;
    }
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		mMPSListener.onPlayerError();
		mMediaPlayer.reset();		
        return true;
	}
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("MediaPlayerService", "onBind");
		return mBinder;
	}
	public void pauseMediaPlayer() {
		Log.d("MediaPlayerService","pauseMediaPlayer()");
	    mMediaPlayer.pause();
	    stopForeground(true);
	}
	
	public void setMPSListener(OnMediaPlayerServiceListener listener) {
        this.mMPSListener = listener;
    }
	
	public void startMediaPlayer(){		
        
        Log.d("MediaPlayerService","startMediaPlayer()");        
        mMediaPlayer.start();
        mMediaPlayer.setState(MPStates.PLAYING);
        Notification notif = mMPSListener.onCreateNotification();
        if(notif!=null){
        	startForeground(1, notif);
        }
    }
	public void stopMediaPlayer() {
        stopForeground(true);
        mMediaPlayer.stop();
        mMediaPlayer.release();
        Log.d("MediaPlayerService", "stopMediaPlayer()");
    }
 
    public void resetMediaPlayer() {
        stopForeground(true);
        mMediaPlayer.reset();
    }

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		boolean mCompleted = mMPSListener.onPlayerCompletedMedia();
		if(mCompleted){
			stopForeground(true);
			Log.d("MediaPlayerService", "On Completed STOP");
		}
	}    
}

