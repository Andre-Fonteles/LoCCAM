package br.ufc.cac.impl;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import br.ufc.loccam.iandroidcontextprovider.IAndroidContextProvider;
import br.ufc.loccam.ipublisher.IPublisher;
import br.ufc.loccam.isensor.ISensor;

public class DeviceLocationCAC implements ISensor{

	private final String CONTEXT_DATA = "context.device.location";
	
	private Context androidContext;
	private IPublisher sysSu;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private boolean running;
	
	public DeviceLocationCAC() {
		locationManager = null;
		running = false;
	}
	
	public void start(IAndroidContextProvider androidContextProvider, IPublisher sysSU) {
		
		Log.i("CAC", "Initializing " + CONTEXT_DATA + " sensor");
		
		androidContext = (Context)androidContextProvider.getContext();
		this.sysSu = sysSU;
		
		locationManager = (LocationManager) androidContext.getSystemService(Context.LOCATION_SERVICE);
		
		locationListener = new LocationListener() {
			
			public void onStatusChanged(String arg0, int arg1, android.os.Bundle arg2) {
			}
			
			public void onProviderEnabled(String arg0) {
			}
			
			public void onProviderDisabled(String arg0) {
			}
			
			public void onLocationChanged(Location location) {
				ArrayList<String> values = new ArrayList<String>();
				values.add(location.getLatitude() + "");
				values.add(location.getLongitude() + "");
				sysSu.put(CONTEXT_DATA, ISensor.PHYSICAL_TYPE, values, ""+location.getAccuracy(), System.currentTimeMillis()+"");
			}
		};
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);	
		running = true;
	}

	public void stop() {
		if(locationListener != null) {
			locationManager.removeUpdates(locationListener);
			running = false;
		}
	}

	public void setProperty(String key, String value) {
	}

	public String getProperty(String key) {
		return null;
	}

	public String[] getPropertiesKeys() {
		return null;
	}

	public boolean isRunning() {
		return running;
	}
}
