package com.nassaty.hireme.activities;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Loc;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.GeoLocator;
import com.nassaty.hireme.utils.PermissionUtils;
import com.nassaty.hireme.utils.UserUtils;

import java.util.List;

// FIXME: 10/3/2018 I NEED CONFIRMATION
public class NearbyMapActivity extends FragmentActivity {

	private GoogleMap mMap;
	private FusedLocationProviderClient mFusedLocationClient;
	private GeoLocator geoLocator;
	private UserUtils userUtils;
	private AuthUtils authUtils;
	private FirebaseDatabase firebaseDatabase;

	Boolean mRequestingLocationUpdates;
	int MY_PERMISSIONS_REQUEST_LOCATION = 1000;


	private PermissionUtils permissionUtils;
	private LocationRequest mLocationRequest;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_map);
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		mLocationRequest = new LocationRequest();
		mRequestingLocationUpdates = false;
		permissionUtils = new PermissionUtils(this);

		firebaseDatabase = FirebaseDatabase.getInstance();
		geoLocator = new GeoLocator(this);
		authUtils = new AuthUtils(this);
		userUtils = new UserUtils(this);

		geoLocator.getLocation("Prince", new GeoLocator.userLocation() {
			@Override
			public void foundLocation(String key, final GeoLocation location) {
				if (location!= null){
					Toast.makeText(NearbyMapActivity.this, key+" is located at : "+"Latitude : "+location.latitude+" Longitude : "+location.longitude, Toast.LENGTH_SHORT).show();



				}else {
					Toast.makeText(NearbyMapActivity.this, "location not found", Toast.LENGTH_SHORT).show();
				}
			}
		});

		updateUI();

	}

	private void updateUI() {
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(final GoogleMap googleMap) {
				if (ActivityCompat.checkSelfPermission(NearbyMapActivity.this,
						Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
						ActivityCompat.checkSelfPermission(NearbyMapActivity.this,
								Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

					//ask permission
					ActivityCompat.requestPermissions(NearbyMapActivity.this,
							new String[]
									{
											Manifest.permission.ACCESS_FINE_LOCATION,
											Manifest.permission.ACCESS_COARSE_LOCATION
									},
							MY_PERMISSIONS_REQUEST_LOCATION);

				} else {
					mMap = googleMap;
					googleMap.setMyLocationEnabled(true);

					//last know loc


					mLocationRequest.setInterval(10000);
					mLocationRequest.setFastestInterval(5000);
					mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

					LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
							.addLocationRequest(mLocationRequest);

// ...

					SettingsClient client = LocationServices.getSettingsClient(NearbyMapActivity.this);
					Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

					task.addOnSuccessListener(NearbyMapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
						@Override
						public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
							// All location settings are satisfied. The client can initialize
							// location requests here.
							// ...
							if (ActivityCompat.checkSelfPermission(NearbyMapActivity.this,
									Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
									ActivityCompat.checkSelfPermission(NearbyMapActivity.this,
											Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

								ActivityCompat.requestPermissions(NearbyMapActivity.this,
										new String[]
												{
														Manifest.permission.ACCESS_FINE_LOCATION,
														Manifest.permission.ACCESS_COARSE_LOCATION
												},
										MY_PERMISSIONS_REQUEST_LOCATION);
							} else {
								mFusedLocationClient.getLastLocation()
										.addOnSuccessListener(NearbyMapActivity.this, new OnSuccessListener<Location>() {
											@Override
											public void onSuccess(final Location location) {
												// Got last known location. In some rare situations this can be null.
												if (location != null) {

													userUtils.getUserByUID(authUtils.getCurrentUser().getUid(), new UserUtils.foundUser() {
														@Override
														public void user(User user) {
															if (user != null){
																displayLocations(googleMap);
																// Logic to handle location object
																LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
																googleMap.addMarker(new MarkerOptions().position(myLocation).title(user.getUser_name()));
																googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

																geoLocator.addGeoLocation(user.getUID(), location);
															}
														}
													});
												}
											}
										});
								mRequestingLocationUpdates = true;
							}

						}
					});

					task.addOnFailureListener(NearbyMapActivity.this, new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							if (e instanceof ResolvableApiException) {
								// Location settings are not satisfied, but this can be fixed
								// by showing the user a dialog.
								try {
									// Show the dialog by calling startResolutionForResult(),
									// and check the result in onActivityResult().
									ResolvableApiException resolvable = (ResolvableApiException) e;
									resolvable.startResolutionForResult(NearbyMapActivity.this,
											100);
								} catch (IntentSender.SendIntentException sendEx) {
									// Ignore the error.
								}
							}
						}
					});
				}
				return;
			}
		});
	}

	public void displayLocations(final GoogleMap googleMap){
		userUtils.getUserByUID(authUtils.getCurrentUser().getUid(), new UserUtils.foundUser() {
			@Override
			public void user(User user) {
				geoLocator.getLocation(user.getUser_name(), new GeoLocator.userLocation() {
					@Override
					public void foundLocation(String key, final GeoLocation location) {
						LatLng latLng = new LatLng(location.latitude, location.longitude);
						geoLocator.queryLocAt(40.6, latLng, new GeoLocator.gottenLocations() {
							@Override
							public void locations(List<Loc> locs) {
								googleMap.clear();
								for (Loc loc : locs){
									if (loc != null){
										LatLng myLocation = loc.getLatLng();
										googleMap.addMarker(new MarkerOptions().position(myLocation).title(loc.getKey()));
										Toast.makeText(NearbyMapActivity.this, loc.getKey()+" is added"+locs.size(), Toast.LENGTH_SHORT).show();
									}else {
										Toast.makeText(NearbyMapActivity.this, "locations not found", Toast.LENGTH_SHORT).show();
									}
								}
							}
						});
					}
				});
			}
		});
	}
	

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				permissionUtils.isPermissionGranted(true);
				Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
			} else {
				permissionUtils.isPermissionGranted(false);
			}
		}
	}
}
