package br.ufc.great.loccam.filter.gis;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;

public class DistanceFilter extends IFilter.Stub {

	private float distance;
	
	private Location location;
	
	private Location userLocation;
	
	public DistanceFilter(double latitude, double longitude, float distance) {
		this.location = new Location("");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		
		this.distance = distance;
	}
	
	@Override
	public boolean filter(Tuple tuple) {

		for (int i = 0; i < tuple.size(); i++) {
			if(tuple.getField(i).getName().equals("Values")) {
				@SuppressWarnings("unchecked")
				List<String> stringValues = (List<String>)tuple.getField(i).getValue();
				
				List<Double> values = new ArrayList<Double>();
				
				for (String value : stringValues) {
					values.add(Double.parseDouble(value));
				}
				
				userLocation = new Location("");
				userLocation.setLatitude(values.get(0));
				userLocation.setLongitude(values.get(1));
			}
		}
		
		System.out.println("DIST: " + location.distanceTo(userLocation));
		
		if(location.distanceTo(userLocation) <= distance)
			return true;
		else
			return false;
	}
}
