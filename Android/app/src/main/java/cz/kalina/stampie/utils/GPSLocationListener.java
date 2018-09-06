package cz.kalina.stampie.utils;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GPSLocationListener implements LocationListener {

	public static Location lastLocation = null;
	public static Boolean isEnabled = true;
	
	public void onLocationChanged(Location location) {
		
		// If our GPS Location changes set the Text on the TextView
		// coordinates to display the current Lat/Long.
		if (location != null) lastLocation = location;
	}

	public void onProviderDisabled(String provider) {

		isEnabled = false;
	}

	public void onProviderEnabled(String provider) {

		isEnabled = true;
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}
}
