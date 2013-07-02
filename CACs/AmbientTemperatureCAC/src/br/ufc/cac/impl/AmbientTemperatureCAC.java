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

public class AmbientTemperatureCAC implements ISensor{

	private final String CONTEXT_KEY = "context.ambient.temperature";
	
	private Context androidContext;
	private IPublisher sysSu;
	
	private SensorManager sensorManager;
	private Sensor temperatureSensor;
	
	private TemperatureSensorListener temperatureSensorListener;

	private boolean running;
	
	public AmbientTemperatureCAC() {
		sensorManager = null;
		temperatureSensorListener = null;
		running = false;
	}
	
	public void start(IAndroidContextProvider androidContextProvider, IPublisher sysSU) {
		androidContext = (Context)androidContextProvider.getContext();
		this.sysSu = sysSU;
		
		sensorManager = (SensorManager) androidContext.getSystemService(Context.SENSOR_SERVICE);
		temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
		
		temperatureSensorListener = new TemperatureSensorListener();
		
		if(temperatureSensor != null){
			Log.i("CAC", "Initializing " + CONTEXT_KEY + " sensor");
			sensorManager.registerListener(temperatureSensorListener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
			
			running = true;
        }else{
        	Log.i("CAC", "Can't initialize " + CONTEXT_KEY + " sensor.");
        }
	}

	public void stop() {
		if(sensorManager != null) {
			sensorManager.unregisterListener(temperatureSensorListener);
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
	
	private class TemperatureSensorListener implements SensorEventListener {

		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		public void onSensorChanged(SensorEvent event ) {
			ArrayList<String> values = new ArrayList<String>();
			values.add(event.values[0] + "");
			sysSu.put(CONTEXT_KEY, ISensor.PHYSICAL_TYPE, values, "", System.currentTimeMillis()+"");
		}
	}
	
}
