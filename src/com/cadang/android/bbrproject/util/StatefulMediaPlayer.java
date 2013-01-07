package com.cadang.android.bbrproject.util;

import java.io.IOException;


import android.media.AudioManager;
import android.media.MediaPlayer;

public class StatefulMediaPlayer extends MediaPlayer {
	public enum MPStates {
        EMPTY(0), CREATED(1), PREPARE(2), PREPARED(3), PLAYING(4), STARTED(5), PAUSED(6), STOPPED(7), ERROR(8);
        private int _value;
        private MPStates(int value){ this._value = value;}
        public int getValue(){ return _value;}
	}	
    private MPStates mState;
    private MediaStream mMediaStream;
    
    /**
     * @param state the state to set
     */
    public MPStates getState() {
        return mState;
    }
   
    public void setState(MPStates state) {
        this.mState = state;
    }
 
    public boolean isCreated() {
        return (mState == MPStates.CREATED);
    } 
    public boolean isEmpty() {
        return (mState == MPStates.EMPTY);
    } 
    public boolean isStopped() {
        return (mState == MPStates.STOPPED);
    } 
    public boolean isStarted() {
        return (mState == MPStates.STARTED || this.isPlaying());
    } 
    public boolean isPaused() {
        return (mState == MPStates.PAUSED);
    } 
    public boolean isPrepared() {
        return (mState == MPStates.PREPARED);
    }

    public MediaStream getMediaStream() {
        return mMediaStream;
    }
    public void setMediaStream(MediaStream mediaStream) {
        this.mMediaStream = mediaStream;
        try {
            setDataSource(mediaStream.getUrl());
            setState(MPStates.CREATED);
        }
        catch (Exception e) {
            setState(MPStates.ERROR);
        }
    }
    
    public StatefulMediaPlayer() {
        super();
        setState(MPStates.CREATED);
    }
    public StatefulMediaPlayer(MediaStream mediaStream) {
        super();
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mMediaStream = mediaStream;
        try {
            setDataSource(mMediaStream.getUrl());
            setState(MPStates.CREATED);
        }
        catch (Exception e) {
            setState(MPStates.ERROR);
        }
    }
    
    @Override
    public void reset() {
        super.reset();
        this.mState = MPStates.EMPTY;
    }    
    @Override
    public void start() {
        super.start();
        setState(MPStates.STARTED);
        //setState(MPStates.PLAYING);
    }
 
    @Override
    public void pause() { 
        super.pause();
        setState(MPStates.PAUSED); 
    }
 
    @Override
    public void stop() {
        super.stop();
        setState(MPStates.STOPPED);
    }
 
    @Override
    public void release() {
        super.release();
        setState(MPStates.EMPTY);
    }
 
    @Override
    public void prepare() throws IOException, IllegalStateException {
        super.prepare();
        setState(MPStates.PREPARED);
    }
 
    @Override
    public void prepareAsync() throws IllegalStateException {
        super.prepareAsync();
        setState(MPStates.PREPARE);
    }
}
