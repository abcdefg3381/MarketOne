package marketone.commodity.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import maggie.common.utils.MaggieReader;
import marketone.commodity.entity.Country;

public class CountryLoader {
	public static List<Country> loadReporters() {
		MaggieReader in;
		try {
			in = new MaggieReader(new File("reporters"));
			String line;
			String[] sgmt;
			List<Country> reporters = new ArrayList<Country>();
			while ((line = in.readLine()) != null) {
				line.replace("\"", "");
				sgmt = line.split("\t");
				// REPORTER
				// Reporter Country
				Country reporter = new Country(sgmt[0], sgmt[1]);
				if (!reporters.contains(reporter))
					reporters.add(reporter);
			}
			in.close();
			return reporters;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
