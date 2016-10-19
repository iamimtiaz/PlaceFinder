package com.androidhive.googleplacesandmaps;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class PlacesList implements Serializable {

	@Key
	public String status;

	@Key
	//public String statue;
	public List<Place> results;
	
	
//	public PlacesList(String stat, String res ) {
//		this.status=stat;
//		this.statue=res;
//		
//		
//		
//	}
	public PlacesList(String stat, List<Place> res ) {
		this.status=stat;
		this.results=res;
		
		
		
	}

}