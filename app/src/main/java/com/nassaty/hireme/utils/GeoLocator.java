package com.nassaty.hireme.utils;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nassaty.hireme.model.Loc;

import java.util.ArrayList;
import java.util.List;

public class GeoLocator {

	private static final String GEO_FIRE_DB = "https://hireme-4b36b.firebaseio.com/";
	private static final String GEO_FIRE_REF = "job_locations";
	private Context context;
	private GeoFire geoFire;

	public GeoLocator(Context context, FirebaseDatabase database) {
		this.context = context;
		DatabaseReference myRef = database.getReference("locations");
		this.geoFire = new GeoFire(myRef);
	}

	public void addGeoLocation(String username, Location location){
		geoFire.setLocation(username, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
			@Override
			public void onComplete(String key, DatabaseError error) {
				if (error != null){
					Toast.makeText(context, "Location couldn't be saved"+error.getMessage(), Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(context, "Location saved successfully", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	public interface gottenLocations{
		void locations(List<Loc> locs);
	}

	public void queryLocAt(double radius, LatLng location, final gottenLocations callback){
		final List<Loc> locations = new ArrayList<>();

		GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.latitude, location.longitude), radius);

		geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
			@Override
			public void onKeyEntered(String key, GeoLocation location) {
				 Loc loc = new Loc();
				 loc.setKey(key);
				 loc.setLatLng(new LatLng(location.latitude, location.longitude));

				 locations.add(loc);
				 callback.locations(locations);
			}

			@Override
			public void onKeyExited(String key) {

			}

			@Override
			public void onKeyMoved(String key, GeoLocation location) {
				for (Loc loc : locations){
					if (key.equals(loc.getKey())){
						locations.remove(loc);
						loc.setLatLng(new LatLng(location.latitude, location.longitude));
					}else
						return;
				}
			}

			@Override
			public void onGeoQueryReady() {
				Toast.makeText(context, "ready to be used", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onGeoQueryError(DatabaseError error) {
				Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public interface userLocation{
		void foundLocation(String key, GeoLocation location);
	}

	public void getLocation(final String username, final userLocation callback){
		geoFire.getLocation(username, new LocationCallback() {
			@Override
			public void onLocationResult(String key, GeoLocation location) {
				if (location == null){
					Toast.makeText(context, "location not found", Toast.LENGTH_SHORT).show();
				}else {
					callback.foundLocation(key, location);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public void removeLocation(String key){
		geoFire.removeLocation(key);
	}
}
