package br.ufc.great.loccam.filter.gis;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;

public class DistanceFilter extends IFilter.Stub {

	private float distance;
	
	private List<Location> locations;
	
	private Location userLocation;
	
	public DistanceFilter(double latitude, double longitude, float distance) {
		this.distance = distance;

		Location location = new Location("");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		
		locations = new ArrayList<Location>();
		locations.add(location);
	}
	
	public DistanceFilter(List<Location> locations, float distance) {
		this.locations = locations;
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
		
		boolean result = false;
		
		for (Location location : locations) {
			if(location.distanceTo(userLocation) <= distance) {
				result = true;
				break;
			}
		}
		
		return result;
	}
}
