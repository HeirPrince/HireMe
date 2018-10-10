package com.nassaty.hireme.model;

import com.google.android.gms.maps.model.LatLng;

public class Loc {
	private String key;
	private LatLng latLng;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}
}
