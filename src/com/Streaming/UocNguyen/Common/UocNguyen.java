/**
 * 
 */
package com.Streaming.UocNguyen.Common;
import java.util.ArrayList;

import android.graphics.Bitmap;

/**
 * @author sang
 *
 */
public class UocNguyen {
	private String message;
	private String id;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @param message
	 * @param id
	 */
	public UocNguyen(String message, String id) {
		super();
		this.message = message;
		this.id = id;
	}
	

	/**
	 * @param message
	 */
	public UocNguyen(String message) {
		super();
		this.message = message;
	}

	/**
	 * 
	 */
	public UocNguyen() {
		//super();
	}
	
	
	
	
	
	
}
