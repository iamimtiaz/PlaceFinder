package com.androidhive.googleplacesandmaps;

//import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
//import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SinglePlaceActivity extends Fragment {
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	MyActivity8 googlePlaces;
	
	// Place Details
	PlaceDetails placeDetails;
	
	// Progress dialog
	ProgressDialog pDialog;
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place

	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 View root = inflater.inflate(R.layout.single_place, container, false);
//			TextView lbl_name = (TextView) root. findViewById(R.id.name);
//			TextView lbl_address = (TextView) root.findViewById(R.id.address);
//			TextView lbl_phone = (TextView) root.findViewById(R.id.phone);
//			TextView lbl_location = (TextView) root.findViewById(R.id.location);
//		
		 placeDetails =  (PlaceDetails) getArguments().getSerializable("Details");
			
			// Calling a Async Background thread
			new LoadSinglePlaceDetails().execute();
	        return root;
	    }
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		
		//Intent i = getIntent();
		
		// Place referece id
		
	}
	
	
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			//String reference = args[0];
			
			// creating Places class object
			//googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
//			try {
//				//placeDetails = googlePlaces.getPlaceDetails(reference);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					if(placeDetails != null){
						String status = placeDetails.status;
						
						// check place deatils status
						// Check for all possible status
						if(status.equals("OK")){
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								String address = placeDetails.result.vicinity;
								String phone = placeDetails.result.phone_number;
								String latitude = Double.toString(placeDetails.result.lat);
								String longitude = Double.toString(placeDetails.result.lng);
								
								Log.d("Place ", name + address + phone + latitude + longitude);
								
								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) getView(). findViewById(R.id.name);
								TextView lbl_address = (TextView) getView().findViewById(R.id.address);
								TextView lbl_phone = (TextView) getView().findViewById(R.id.phone);
							TextView lbl_location = (TextView) getView().findViewById(R.id.location);
//								
								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present" : latitude;
								longitude = longitude == null ? "Not present" : longitude;
								
								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
								lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
							}
						}
						else if(status.equals("ZERO_RESULTS")){
							alert.showAlertDialog(getActivity(), "Near Places",
									"Sorry no place found.",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(getActivity(), "Places Error",
									"Sorry unknown error occured.",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(getActivity(), "Places Error",
									"Sorry query limit to google places is reached",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(getActivity(), "Places Error",
									"Sorry error occured. Request is denied",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(getActivity(), "Places Error",
									"Sorry error occured. Invalid Request",
									false);
						}
						else
						{
							alert.showAlertDialog(getActivity(), "Places Error",
									"Sorry error occured.",
									false);
						}
					}else{
						alert.showAlertDialog(getActivity(), "Places Error",
								"Sorry error occured.",
								false);
					}
					
					
				}
			});

		}

	}

}
