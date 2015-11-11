package marketone.visualization;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Visualizes population density of the world as a choropleth map. Countries are
 * shaded in proportion to the population density.
 * 
 * It loads the country shapes from a GeoJSON file via a data reader, and loads
 * the population density values from another CSV file (provided by the World
 * Bank). The data value is encoded to transparency via a simplistic linear
 * mapping.
 */
public class WorldMap extends PApplet {

	UnfoldingMap map;

	HashMap<String, DataEntry> dataEntriesMap;
	List<Marker> countryMarkers;

	// relative path varies depending on whether this was started as applet or
	// application
	public static final String TILES_LOCATION_APPLICATION = "blankLight-1-3.mbtiles";
	public static final String TILES_LOCATION_APPLET = "blankLight-1-3.mbtiles";

	public static String mbTilesString = TILES_LOCATION_APPLET;

	public void setup() {
		size(1050, 1000, OPENGL);
		smooth();

		map = new UnfoldingMap(this, new MBTilesMapProvider());
		// map = new UnfoldingMap(this, 0, 0, 1200, 1200);
		map.zoomToLevel(2);
		map.setBackgroundColor(255);
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load country polygons and adds them as markers
		List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		// Load population data
		dataEntriesMap = loadPopulationDensityFromCSV("indices67");
		println("Loaded " + dataEntriesMap.size() + " data entries");

		// Country markers are shaded according to its population density (only
		// once)
		shadeCountriesByCategory();
	}

	public void draw() {
		background(255);

		// Draw map tiles and country markers
		map.draw();
	}

	public void shadeCountriesByCategory() {
		for (Marker marker : countryMarkers) {
			// Find data for country of the current marker
			String countryId = marker.getId();
			DataEntry dataEntry = dataEntriesMap.get(countryId);

			if (dataEntry != null && dataEntry.category.equals("Developed")) {
				// Encode value as brightness (values range: 0-1000)
				marker.setColor(Color.red.getRGB());
			} else if (dataEntry != null && dataEntry.category.equals("Emerging")) {
				// Encode value as brightness (values range: 0-1000)
				marker.setColor(Color.green.getRGB());
			} else if (dataEntry != null && dataEntry.category.equals("Frontier")) {
				// Encode value as brightness (values range: 0-1000)
				marker.setColor(Color.blue.getRGB());
			} else {
				// No value available
				marker.setColor(color(255, 120));
			}
		}
	}

	public HashMap<String, DataEntry> loadPopulationDensityFromCSV(String fileName) {
		HashMap<String, DataEntry> dataEntriesMap = new HashMap<String, DataEntry>();

		String[] rows = loadStrings(fileName);
		for (String row : rows) {
			// Reads country name and population density value from CSV row
			String[] columns = row.split("\t");
			if (columns.length >= 3) {
				DataEntry dataEntry = new DataEntry();
				dataEntry.countryName = columns[1];
				dataEntry.id = columns[2];
				dataEntry.region = columns[3];
				dataEntry.category = columns[4];
				dataEntriesMap.put(dataEntry.id, dataEntry);
			}
		}

		return dataEntriesMap;
	}

	public void keyPressed() {
		if (key == 's') {
			PGraphics pg = map.mapDisplay.getOuterPG();
			pg.save("snap.bmp");
		}
		if (key == 'c') {
			shadeCountriesByCategory();
			map.updateMap();
		}
		if (key == 'r') {
			shadeCountriesByRegion();
			map.updateMap();
		}
	}

	/**
	 * 
	 */
	private void shadeCountriesByRegion() {
		for (Marker marker : countryMarkers) {
			// Find data for country of the current marker
			String countryId = marker.getId();
			DataEntry dataEntry = dataEntriesMap.get(countryId);

			if (dataEntry != null && dataEntry.region.equals("Americas")) {
				// Encode value as brightness (values range: 0-1000)
				marker.setColor(Color.pink.getRGB());
			} else if (dataEntry != null && dataEntry.region.equals("Asia/Pacific")) {
				// Encode value as brightness (values range: 0-1000)
				marker.setColor(Color.orange.getRGB());
			} else if (dataEntry != null && dataEntry.region.equals("Europe")) {
				// Encode value as brightness (values range: 0-1000)
				marker.setColor(Color.blue.getRGB());
			} else if (dataEntry != null && dataEntry.region.equals("Africa/Middle East")) {
				// Encode value as brightness (values range: 0-1000)
				marker.setColor(Color.black.getRGB());
			} else {
				// No value available
				marker.setColor(color(255, 120));
			}
		}
	}

	class DataEntry {
		String countryName;
		String id;
		String region;
		String category;
	}

}
