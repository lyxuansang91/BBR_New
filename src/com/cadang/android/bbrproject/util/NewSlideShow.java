package com.cadang.android.bbrproject.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class NewSlideShow {
	// swap img from bitmap array using fade effect
	public static void fadeEffect(final ImageView img1, final ImageView img2, int duration){
		
		final Animation fadeOut 	= new AlphaAnimation(0, 1);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		//fadeOut.setStartOffset(fadeDuration + time);
		fadeOut.setDuration(duration * 2);
		
		Animation fadeIn 	= new AlphaAnimation(1, 0);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(duration);
		fadeIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				//img1.setImageBitmap((Bitmap)((BitmapDrawable)img2.getDrawingCache()).getBitmap());
				img1.setImageDrawable(img2.getDrawable());
				img1.startAnimation(fadeOut);
			}
		});
		
		//Bitmap bm = img2.getDrawingCache();
		//Log.d("Fade Effect", bm.toString());
		//Drawable dr = img2.getDrawable();
		//Log.d("Fade Effect", dr.toString());
		//if(dr != null){
		//	Log.d("Fade Effect", dr.toString());
			img1.startAnimation(fadeIn);
		//}else{
		//	Log.d("Fade Effect", "Cant change");
		//}
	}
	public static void fadeEffect(final ImageView img, final Bitmap[] slides, final int pos, int time){
		int fadeDuration = time;
		
		final Animation fadeOut 	= new AlphaAnimation(0, 1);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		//fadeOut.setStartOffset(fadeDuration + time);
		fadeOut.setDuration(fadeDuration * 2);
		
		Animation fadeIn 	= new AlphaAnimation(1, 0);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(fadeDuration);
		fadeIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if((pos >= 0)&&(pos < slides.length - 1)){
					Log.d("Fade effect", "Some thing show");
					img.setImageBitmap(slides[pos]);
					img.startAnimation(fadeOut);
				}
			}
		});
				
		img.startAnimation(fadeIn);
		//img.setImageBitmap(slides[pos]);
	}
}
