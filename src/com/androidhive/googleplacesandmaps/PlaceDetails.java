package com.androidhive.googleplacesandmaps;

import java.io.Serializable;

import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class PlaceDetails implements Serializable {

	@Key
	public String status;
	
	@Key
	public Place result;

	
	
	public PlaceDetails(String st,Place ret){
		this.status=st;
		this.result=ret;
		
		
	}
	
	@Override
	public String toString() {
		if (result!=null) {
			return result.toString();
		}
		return super.toString();
	}
}
