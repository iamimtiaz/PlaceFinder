package com.androidhive.googleplacesandmaps;

import java.io.Serializable;

import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class Place implements Serializable {

	@Key
	public String id;
	
	@Key
	public String name;
	
	@Key
	public String reference;
	
	@Key
	public String icon;
	
	@Key
	public String vicinity;
	
	public String type;
	@Key
	public double lat;
	
	@Key
	public double lng;
	//@Key
	//public Geometry geometry;
	
	//@Key
	//public String formatted_address;
	
	@Key
	public String phone_number;

	
	public  String getName(){

		return name;

		}
		    public  String getId(){

		        return id;

		    }
		    public  String getVicinity(){

		        return vicinity;

		    }
		    public  String getIcon(){

		        return icon;

		    }
		    public  String getType(){

		        return type;

		    }
		    public  Double getLng(){

		        return lng;

		    }
		    public  Double getLat(){

		        return lat;

		    }
	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}
	

}
