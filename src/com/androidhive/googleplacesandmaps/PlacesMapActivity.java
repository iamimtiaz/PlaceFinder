package com.androidhive.googleplacesandmaps;

import java.lang.reflect.Field;
import java.util.List;








import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.gms.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class PlacesMapActivity extends Fragment
{
	 
    // Google Map
	private MapView mMapView;
	private GoogleMap googleMap;
	private Bundle mBundle;
	
	//private GoogleMap googleMap;
    PlacesList nearPlaces;
    List<Overlay> mapOverlays;
    AddItemizedOverlay itemizedOverlay;

	GeoPoint geoPoint;
	// Map controllers
	MapController mc;
	
	double latitude;
	double longitude;
	OverlayItem overlayitem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflatedView = inflater.inflate(R.layout.map_places, container, false);

		MapsInitializer.initialize(getActivity());

		mMapView = (MapView) inflatedView.findViewById(R.id.map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded(inflatedView);

		return inflatedView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (googleMap == null) {
			googleMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
			if (googleMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		//googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
		 try {
	            // Loading map
	           // initilizeMap();
	            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	            googleMap.getUiSettings().setZoomControlsEnabled(true);
	            googleMap.getUiSettings().setZoomGesturesEnabled(true);
	            googleMap.getUiSettings().setRotateGesturesEnabled(true);
	         // Getting intent data
	    		//Intent i = getIntent();
	    		
	    		// Users current geo location
	            String user_latitude =Double.toString( getArguments().getDouble("user_latitude"));
	    		String user_longitude =Double.toString( getArguments().getDouble("user_longitude"));
	    		
	    		// Nearplaces list
	    		nearPlaces = (PlacesList) getArguments().getSerializable("near_places");
	    	 final LatLng pos = new LatLng(Double.parseDouble(user_latitude),Double.parseDouble(user_longitude));
	    	 Marker mypos = googleMap.addMarker(new MarkerOptions()
	             .position(pos)
	             .title("YOur location")
	             .snippet("That is You")
	             .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_red)));
//	    		MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(user_latitude), Double.parseDouble(user_longitude))).title("");
	    	
	    	 googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
	    	        public void onCameraChange(CameraPosition arg0) {
	    	            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 13));
	    	        }
	    	    });
	    	 
	    	 if (nearPlaces.results != null){
	    		 
	    		 final LatLngBounds.Builder builder = new LatLngBounds.Builder();
	    		 for (Place place : nearPlaces.results){
	    			 latitude = place.lat; // latitude
		 				longitude = place.lng; // longitude // longitude
	 				//String NAMEplace.name
	 				final LatLng nearpos = new LatLng(latitude,longitude);
	 				 builder.include(nearpos);
	 				googleMap.addMarker(new MarkerOptions()
	 				                        .position(nearpos)
	 				                        .title(place.name)
	 				                        .snippet(place.vicinity)
	 				                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_blue)));
	    		 }
	    		 
	    		 googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
	                 
	                 public void onCameraChange(CameraPosition arg0) {
	                     googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),50));
	                     googleMap.setOnCameraChangeListener(null);
	                 }
	             });
	    		 
	    	 }
	    	 
	        }
	        
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

 
}