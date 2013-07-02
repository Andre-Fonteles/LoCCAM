package br.ufc.cac.impl;

import java.util.ArrayList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import br.ufc.loccam.iandroidcontextprovider.IAndroidContextProvider;
import br.ufc.loccam.ipublisher.IPublisher;
import br.ufc.loccam.isensor.ISensor;

public class DeviceAccelerationCAC implements ISensor{

	private final String CONTEXT_DATA = "context.device.acceleration";
	
	private Context androidContext;
	private IPublisher sysSu;
	
	private SensorManager sensorManager;
	private Sensor accelerometerSensor;
	
	private AccelerometerSensorListener accelerometerSensorListener;

	private boolean running;
	
	public DeviceAccelerationCAC() {
		sensorManager = null;
		accelerometerSensorListener = null;
		running = false;
	}
	
	public void start(IAndroidContextProvider androidContextProvider, IPublisher sysSU) {
		androidContext = (Context)androidContextProvider.getContext();
		this.sysSu = sysSU;
		
		sensorManager = (SensorManager) androidContext.getSystemService(Context.SENSOR_SERVICE);
		accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		accelerometerSensorListener = new AccelerometerSensorListener();
		
		if(accelerometerSensor != null){
			Log.i("CAC", "Initializing " + CONTEXT_DATA + " sensor");
			sensorManager.registerListener(accelerometerSensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
			
			running = true;
        }else{
        	Log.i("CAC", "Can't initialize " + CONTEXT_DATA + " sensor.");
        }
	}

	public void stop() {
		if(sensorManager != null) {
			sensorManager.unregisterListener(accelerometerSensorListener);
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
	
	private class AccelerometerSensorListener implements SensorEventListener {

		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		public void onSensorChanged(SensorEvent event ) {
			ArrayList<String> values = new ArrayList<String>();
			values.add(event.values[0] + "");
			values.add(event.values[1] + "");
			values.add(event.values[2] + "");
			sysSu.put(CONTEXT_DATA, ISensor.PHYSICAL_TYPE, values, "", System.currentTimeMillis()+"");
		}
	}
	
}
