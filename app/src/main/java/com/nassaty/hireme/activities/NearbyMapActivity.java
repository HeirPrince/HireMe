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

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import com.nassaty.hireme.R;
import com.nassaty.hireme.utils.PermissionUtils;

public class NearbyMapActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private FusedLocationProviderClient mFusedLocationClient;
	private LocationCallback mLocationCallback;

	Boolean mRequestingLocationUpdates;
	int MY_PERMISSIONS_REQUEST_LOCATION = 1000;


	private PermissionUtils permissionUtils;
	private LocationRequest mLocationRequest;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("updates",
				mRequestingLocationUpdates);
		// ...
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_map);
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		mLocationRequest = new LocationRequest();
		mRequestingLocationUpdates = false;
		updateValuesFromBundle(savedInstanceState);


		mLocationCallback = new LocationCallback() {
			@Override
			public void onLocationResult(LocationResult locationResult) {
				if (locationResult == null) {
					return;
				}
				for (Location location : locationResult.getLocations()) {
					// Update UI with location data
					// ...
					updateUI();
				}
			}
		};

	}

	private void updateValuesFromBundle(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			return;
		}

		// Update the value of mRequestingLocationUpdates from the Bundle.
		if (savedInstanceState.keySet().contains(200)) {
			mRequestingLocationUpdates = savedInstanceState.getBoolean(
					"updates");
		}

		// ...

		// Update UI to match restored state
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
								// TODO: Consider calling
								//    ActivityCompat#requestPermissions
								// here to request the missing permissions, and then overriding
								//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
								//                                          int[] grantResults)
								// to handle the case where the user grants the permission. See the documentation
								// for ActivityCompat#requestPermissions for more details.

								ActivityCompat.requestPermissions(NearbyMapActivity.this,
										new String[]
												{
														Manifest.permission.ACCESS_FINE_LOCATION,
														Manifest.permission.ACCESS_COARSE_LOCATION
												},
										MY_PERMISSIONS_REQUEST_LOCATION);

								return;
							} else {
								googleMap.clear();
								mFusedLocationClient.getLastLocation()
										.addOnSuccessListener(NearbyMapActivity.this, new OnSuccessListener<Location>() {
											@Override
											public void onSuccess(Location location) {
												// Got last known location. In some rare situations this can be null.
												if (location != null) {
													// Logic to handle location object
													LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
													googleMap.addMarker(new MarkerOptions().position(myLocation).title("My location")); //You: Prince Heir
													googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
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


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(final GoogleMap googleMap) {



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

	@Override
	protected void onResume() {
		super.onResume();
		if (mRequestingLocationUpdates) {
			startLocationUpdates();
		}
	}

	private void startLocationUpdates() {
		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}else {
			mFusedLocationClient.requestLocationUpdates(mLocationRequest,
					mLocationCallback,
					null /* Looper */);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocationUpdates();
	}

	private void stopLocationUpdates() {
		mFusedLocationClient.removeLocationUpdates(mLocationCallback);
	}
}
