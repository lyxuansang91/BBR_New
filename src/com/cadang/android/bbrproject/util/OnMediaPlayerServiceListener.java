package com.cadang.android.bbrproject.util;

import android.app.Notification;

public interface OnMediaPlayerServiceListener {
	public void onInitializePlayerStart(String message);
	public void onInitializePlayerSuccess();
	public boolean onPlayerCompletedMedia();
	public void onPlayerError();
	public Notification onCreateNotification();
}
