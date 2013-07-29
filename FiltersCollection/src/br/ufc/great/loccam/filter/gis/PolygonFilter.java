package br.ufc.great.loccam.filter.gis;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;

public class PolygonFilter extends IFilter.Stub {

	private GeometryFactory geometryFactory;
	private List<Polygon> placesPolygon;
	private Coordinate userCoordinate;
	
	public PolygonFilter(Coordinate[] polygonCoordinates) {
		placesPolygon = new ArrayList<Polygon>();
		
		geometryFactory = new GeometryFactory();

		LinearRing linearRing = geometryFactory.createLinearRing(polygonCoordinates);
		placesPolygon.add(geometryFactory.createPolygon(linearRing, null));
	}
	
	public PolygonFilter(List<Coordinate[]> polygonsCoordinates) {
		placesPolygon = new ArrayList<Polygon>();
		geometryFactory = new GeometryFactory();
		
		for (Coordinate[] coordinates : polygonsCoordinates) {
			LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
			
			placesPolygon.add(geometryFactory.createPolygon(linearRing, null));
		}
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
				
				userCoordinate = new Coordinate(values.get(0), values.get(1));
			}
		}
		
		Point userPoint = geometryFactory.createPoint(userCoordinate);
		boolean result = false;
		
		for (Polygon polygon : placesPolygon) {
			if(polygon.intersects(userPoint)) {
				result = true;
				break;
			}
		}
		
		return result;
	}

}