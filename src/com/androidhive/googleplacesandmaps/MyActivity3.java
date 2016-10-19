package com.androidhive.googleplacesandmaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity3 extends Activity {

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	

	// Places List
	PlacesList nearPlaces;

	// GPS Location
	GPSTracker gps;
	
	// Button
	Button btnShowOnMap;

	// Progress dialog
	ProgressDialog pDialog;
	
	String typeselect="2";
	String myjsonstring;
    //public ArrayList<Place> beanPostArrayList;
    private StringBuffer postList;
    String aJsonString;
	// Places Listview
	ListView lv;
	
	PlaceDetails det;
	
	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	 List<Place> beanPostArrayList;
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
//		if (!isInternetPresent) {
//			// Internet Connection is not present
//			alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
//					"Please connect to working Internet connection", false);
//			// stop executing code by return
//			return;
//		}

		// creating GPS Class object
		gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
		}
//		else {
//			// Can't get user's current location
//			alert.showAlertDialog(MainActivity.this, "GPS Status",
//					"Couldn't get location information. Please enable GPS",
//					false);
//			// stop executing code by return
//			return;
//		}

		// Getting listview
		lv = (ListView) findViewById(R.id.list);
		
		// button show on map
		btnShowOnMap = (Button) findViewById(R.id.btn_show_map);
		
		
		//TEST****
		
		
		
		try {


			
			
			StringBuffer sb = new StringBuffer();
	        BufferedReader br = null;
	        try {
	            br = new BufferedReader(new InputStreamReader(getAssets().open("jasondata.txt")));
	            String temp;
	            while ((temp = br.readLine()) != null)
	                sb.append(temp);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
//	        finally {
//	            try {
//	            //    br.close(); // stop reading
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	            }
//	        }

	       myjsonstring = sb.toString();

	        // Try to parse JSON


	        // Creating JSONObject from String
	        JSONObject jsonObjMain = null;

	        try {
	            jsonObjMain = new JSONObject(myjsonstring);
	             aJsonString = jsonObjMain.getString("status");
	            //txtPostList.setText(aJsonString);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        // Creating JSONArray from JSONObject
	        JSONArray jsonArray = null;
	        try {

	            jsonArray = jsonObjMain.getJSONArray("results");
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
//	            JSONObject uniObject = jsonObjMain.getJSONObject("employee");


	        // JSONArray has four JSONObject
	        // for (int i = 0; i < jsonArray.length(); i++)

	        // Creating JSONObject from JSONArray
	        //JSONObject jsonObj = jsonArray.getJSONObject(1);
//	        Gson gson = new GsonBuilder().create();
//	        // List<Place> list = gson.fromJson(String.valueOf(jsonArray), Place.class);
	        
	        
	       
	        Type listType = new TypeToken<ArrayList<Place>>(){}.getType();
	        beanPostArrayList = new GsonBuilder().create().fromJson(String.valueOf(jsonArray), listType);
	      
	        ListIterator<Place> li = beanPostArrayList.listIterator(beanPostArrayList.size());


            while(li.hasPrevious()) {

                Place post=li.previous();
                if(!post.getType().equals(typeselect))
                {
                    li.remove();
                }


            }
	        
	         nearPlaces=new PlacesList(aJsonString,beanPostArrayList);
//	        postList=new StringBuffer();
//	        for(Place post: beanPostArrayList) {
//	            postList.append("\n name: " + post.getName() + "\n vicinity: " + post.getVicinity() +"\nlat: "+post.getLat()+"\n lng: "+post.getLng()+ "\n\n");
//	        }
//	        postList.append("\n status: "+aJsonString);
	       // Log.d("You", "String:" + postList);
	   		  //return list;

		} catch (JsonSyntaxException e) {
			//Log.e("Error:", e.getMessage());
			
		}
		 //**TEST
		
		
		
		

		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		new LoadPlaces().execute();

		/** Button click event for shown on map */
		btnShowOnMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Loading Map. Please Wait.. ", Toast.LENGTH_LONG).show();
				Intent i = new Intent(getApplicationContext(),
						PlacesMapActivity.class);
				// Sending user current geo location
				i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
				i.putExtra("user_longitude", Double.toString(gps.getLongitude()));
				
				// passing near places to map activity
				i.putExtra("near_places", nearPlaces);
				// staring activity
				startActivity(i);
			}
		});
		
		
		/**
		 * ListItem click event
		 * On selecting a listitem SinglePlaceActivity is launched
		 * */
		lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	// getting values from selected ListItem
                String ref = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                
                
                //**
                //Place now;
                
                for(Place post: beanPostArrayList) {
//    	            postList.append("\n name: " + post.getName() + "\n vicinity: " + post.getVicinity() +"\nlat: "+post.getLat()+"\n lng: "+post.getLng()+ "\n\n");
                	if(post.getId().equals(ref)){
                		det = new PlaceDetails(aJsonString, post);
                	}
                }
                
                
//                String name = det.result.name;
//				String address = det.result.vicinity;
//				String phone = det.result.phone_number;
//				String latitude = Double.toString(det.result.lat);
//				String longitude = Double.toString(det.result.lng);
//                
//                 String po=name+address+phone+latitude+longitude;
//                 
//                
//                
//                Toast.makeText(getApplicationContext(), po, Toast.LENGTH_LONG).show();
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);
                
                // Sending place refrence id to single place activity
                // place refrence id used to get "Place full details"
                in.putExtra("Details", det);
                startActivity(in);
            }
        });
	}

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MyActivity3.this);
			pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Nearby Fitness Centre..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			//googlePlaces = new GooglePlaces();
			
			try {
				// PlacesList list;
				

				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				// 
				//String types = "gym"; // Listing places only cafes, restaurants
				
				// Radius in meters - increase this value if you don't find any places
			//	double radius = 5000; // 1000 meters 
				
				// get nearest places
				//nearPlaces = list;
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * and show the data in UI
		 * Always use runOnUiThread(new Runnable()) to update UI from background
		 * thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = nearPlaces.status;
					
					// Check for all possible status
					if(status.equals("OK")){
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();
								
								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map.put(KEY_REFERENCE, p.id);
								
								// Place name
								map.put(KEY_NAME, p.name);
								
								
								// adding HashMap to ArrayList
								placesListItems.add(map);
							}
							// list adapter
							ListAdapter adapter = new SimpleAdapter(MyActivity3.this, placesListItems,
					                R.layout.list_item,
					                new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
					                        R.id.reference, R.id.name });
							
							// Adding data into listview
							lv.setAdapter(adapter);
						}
					}
					else if(status.equals("ZERO_RESULTS")){
						// Zero results found
						alert.showAlertDialog(MyActivity3.this, "Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					}
					else if(status.equals("UNKNOWN_ERROR"))
					{
						alert.showAlertDialog(MyActivity3.this, "Places Error",
								"Sorry unknown error occured.",
								false);
					}
					else if(status.equals("OVER_QUERY_LIMIT"))
					{
						alert.showAlertDialog(MyActivity3.this, "Places Error",
								"Sorry query limit to google places is reached",
								false);
					}
					else if(status.equals("REQUEST_DENIED"))
					{
						alert.showAlertDialog(MyActivity3.this, "Places Error",
								"Sorry error occured. Request is denied",
								false);
					}
					else if(status.equals("INVALID_REQUEST"))
					{
						alert.showAlertDialog(MyActivity3.this, "Places Error",
								"Sorry error occured. Invalid Request",
								false);
					}
					else
					{
						alert.showAlertDialog(MyActivity3.this, "Places Error",
								"Sorry error occured.",
								false);
					}
				}
			});

		}
		
		
//		public PlacesList search()throws Exception {
//
////			this._latitude = latitude;
////			this._longitude = longitude;
////			this._radius = radius;
//
//		
//			
//			
//			try {
//
////				HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
////				HttpRequest request = httpRequestFactory
////						.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
////				request.getUrl().put("key", API_KEY);
////				request.getUrl().put("location", _latitude + "," + _longitude);
////				request.getUrl().put("radius", _radius); // in meters
////				request.getUrl().put("sensor", "false");
////				if(types != null)
////					request.getUrl().put("types", types);
//	//
////				PlacesList list = request.execute().parseAs(PlacesList.class);
////				// Check log cat for places response status
////				Log.d("Places Status", "" + list.status);
////				return list;
//				
//				
//				StringBuffer sb = new StringBuffer();
//		        BufferedReader br = null;
//		        try {
//		            br = new BufferedReader(new InputStreamReader(getAssets().open("jasondata.txt")));
//		            String temp;
//		            while ((temp = br.readLine()) != null)
//		                sb.append(temp);
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
////		        finally {
////		            try {
////		            //    br.close(); // stop reading
////		            } catch (IOException e) {
////		                e.printStackTrace();
////		            }
////		        }
//
//		       myjsonstring = sb.toString();
//
//		        // Try to parse JSON
//
//
//		        // Creating JSONObject from String
//		        JSONObject jsonObjMain = null;
//
//		        try {
//		            jsonObjMain = new JSONObject(myjsonstring);
//		             aJsonString = jsonObjMain.getString("status");
//		            //txtPostList.setText(aJsonString);
//		        } catch (JSONException e) {
//		            e.printStackTrace();
//		        }
//
//		        // Creating JSONArray from JSONObject
//		        JSONArray jsonArray = null;
//		        try {
//
//		            jsonArray = jsonObjMain.getJSONArray("results");
//		        } catch (JSONException e) {
//		            e.printStackTrace();
//		        }
////		            JSONObject uniObject = jsonObjMain.getJSONObject("employee");
//
//
//		        // JSONArray has four JSONObject
//		        // for (int i = 0; i < jsonArray.length(); i++)
//
//		        // Creating JSONObject from JSONArray
//		        //JSONObject jsonObj = jsonArray.getJSONObject(1);
////		        Gson gson = new GsonBuilder().create();
////		        // List<Place> list = gson.fromJson(String.valueOf(jsonArray), Place.class);
//		        
//		        
//		        List<Place> beanPostArrayList;
//		        Type listType = new TypeToken<ArrayList<Place>>(){}.getType();
//		        beanPostArrayList = new GsonBuilder().create().fromJson(String.valueOf(jsonArray), listType);
//		      
//		        PlacesList list=new PlacesList(aJsonString,beanPostArrayList);
////		        postList=new StringBuffer();
////		        for(Place post: beanPostArrayList) {
////		            postList.append("\n name: " + post.getName() + "\n vicinity: " + post.getVicinity() +"\nlat: "+post.getLat()+"\n lng: "+post.getLng()+ "\n\n");
////		        }
////		        postList.append("\n status: "+aJsonString);
//		        Log.d("You", "String:" + postList);
//		   		  return list;
//
//			} catch (JsonSyntaxException e) {
//				Log.e("Error:", e.getMessage());
//				return null;
//			}
//
//		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

}
