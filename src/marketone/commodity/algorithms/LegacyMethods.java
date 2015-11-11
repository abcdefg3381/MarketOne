package marketone.commodity.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import maggie.common.utils.MaggieReader;
import maggie.common.utils.MaggieWriter;
import marketone.commodity.entity.Country;

public class LegacyMethods {

	MaggieWriter out;
	List<Country> reporters = new ArrayList<Country>();
	MaggieReader in;

	private void outputReportersFromOriginalFile() throws IOException {
		out = new MaggieWriter(new File("reporters.txt"));
		for (Country c : reporters) {
			out.writeln(c.getId() + "\t" + c.getName());
		}
		out.close();
	}

	private void findReportersFromOriginalFile() throws IOException {
		in = new MaggieReader(new File("./data/HS1988.txt"));
		String line;
		String[] sgmt;
		in.readLine();
		while ((line = in.readLine()) != null) {
			line.replace("\"", "");
			sgmt = line.split("\t");
			// PARTNER
			// Partner Country
			// REPORTER
			// Reporter Country
			// FLOW 1=import 2=export 3=re-export 4=re-import
			// Time
			// Value
			Country reporter = new Country(sgmt[2], sgmt[3]);
			if (!reporters.contains(reporter))
				reporters.add(reporter);
		}
		System.out.println(reporters.size());
		in.close();
	}

}
