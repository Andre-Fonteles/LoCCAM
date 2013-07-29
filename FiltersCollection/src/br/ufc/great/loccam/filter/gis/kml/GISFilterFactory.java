package br.ufc.great.loccam.filter.gis.kml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.location.Location;
import br.ufc.great.loccam.filter.gis.DistanceFilter;
import br.ufc.great.loccam.filter.gis.PolygonFilter;

import com.vividsolutions.jts.geom.Coordinate;

public class GISFilterFactory {

	public static PolygonFilter createFromKmlPolygons(String kml) {
		try {
			KMLParser parser = new KMLParser();
			parser.parseCoordinates(kml);
			
			if(parser.getPolygons() != null && parser.getPolygons().size() > 0)
				return new PolygonFilter(parser.getPolygons());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static DistanceFilter createFromKmlPoint(String kml) {
		
		try {
			KMLParser parser = new KMLParser();
			parser.parseCoordinates(kml);
			
			if(parser.getSingleCoordinates() != null && parser.getSingleCoordinates().size() > 0) {
				List<Coordinate> coordinates = parser.getSingleCoordinates();
				List<Location> locations = new ArrayList<Location>();
				
				for (Coordinate coordinate : coordinates) {
					Location location = new Location("");
					location.setLatitude(coordinate.x);
					location.setLongitude(coordinate.y);
					
					locations.add(location);
				}
				
				return new DistanceFilter(locations, 100);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
