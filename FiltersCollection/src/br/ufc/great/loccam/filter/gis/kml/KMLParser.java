package br.ufc.great.loccam.filter.gis.kml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.vividsolutions.jts.geom.Coordinate;

public class KMLParser {

	private List<Coordinate[]> polygons;
	private List<Coordinate> singleCoordinates;
	
	public KMLParser() {
		polygons = new ArrayList<Coordinate[]>();
		singleCoordinates = new ArrayList<Coordinate>();
	}
	
	public void parseCoordinates(String kml) throws IOException, XmlPullParserException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(kml));
		
		int eventType = parser.getEventType();
		
		ReadingState readingState = ReadingState.SEARCHING;
		boolean atCoordinatesTag = false; 
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if(eventType == XmlPullParser.START_TAG) {
				
				if(parser.getName().equalsIgnoreCase("coordinates") && readingState != ReadingState.SEARCHING)
					atCoordinatesTag = true;
				
				if(parser.getName().equalsIgnoreCase("Point"))
					readingState = ReadingState.SINGLE_POINT;
				else if(parser.getName().equalsIgnoreCase("LineString"))
					readingState = ReadingState.POLYGON;
				
			} else if(eventType == XmlPullParser.END_TAG) {
				
				if(parser.getName().equalsIgnoreCase("Point") || parser.getName().equalsIgnoreCase("LineString"))
					readingState = ReadingState.SEARCHING;
				
			} else if(eventType == XmlPullParser.TEXT) {
				if(atCoordinatesTag) {
					
					if(readingState == ReadingState.POLYGON) {
						String[] polygonCoordinatesStrings = parser.getText().split(" ");
						Coordinate[] polygonCoordinates = new Coordinate[polygonCoordinatesStrings.length];
						
						for (int i = 0; i < polygonCoordinates.length; i++) {
							String[] coordinateString = polygonCoordinatesStrings[i].split(",");
							if(coordinateString.length >= 2)
								polygonCoordinates[i] = new Coordinate(Double.parseDouble(coordinateString[1]), Double.parseDouble(coordinateString[0]));
						}
						polygons.add(polygonCoordinates);
						
					} else if(readingState == ReadingState.SINGLE_POINT) {
						String[] coordinateString = parser.getText().split(",");
						singleCoordinates.add(new Coordinate(Double.parseDouble(coordinateString[1]), Double.parseDouble(coordinateString[0])));
					}
					
					atCoordinatesTag = false;
				}
			}
			eventType = parser.next();
		}
	}
	
	public List<Coordinate[]> getPolygons() {
		return polygons;
	}

	public List<Coordinate> getSingleCoordinates() {
		return singleCoordinates;
	}

	private enum ReadingState {
		POLYGON, SINGLE_POINT, SEARCHING;
	}
}
