package marketone.commodity.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import maggie.common.utils.MaggieReader;
import maggie.common.utils.MaggieWriter;
import marketone.commodity.entity.Commodity;
import marketone.commodity.entity.Country;

import org.apache.commons.lang.ArrayUtils;


public class LoadDataToMatrix {
	private static int COMMODITYFILE = 1;
	private static int SERVICEFILE = 2;
	List<Country> reporters;
	List<Commodity> commodities = new ArrayList<Commodity>();
	// year 2005-2010, 46 reporter countries
	static double[][][] impMatrix, expMatrix;
	MaggieReader in;
	MaggieWriter out;

	public static void main(String[] args) throws IOException {
		LoadDataToMatrix loader = new LoadDataToMatrix();
		// rrc.readReportersFromOriginalFile();
		// rrc.outputReportersFromOriginalFile();
		loader.initialize();
		loader.loadTradeData("./data/2005-2010_Total_Commodity_Import_Export.txt", COMMODITYFILE);
		loader.loadTradeData("./data/2005-2009_Trade_In_Service.txt", SERVICEFILE);
		loader.outputMatrix(impMatrix, "import", 2005, 2009);
		loader.outputMatrix(expMatrix, "export", 2005, 2009);
		loader.finish();
	}

	private void initialize() {
		try {
			reporters = CountryLoader.loadReporters();
			loadCommodities();

			impMatrix = new double[6][reporters.size()][reporters.size()];
			expMatrix = new double[6][reporters.size()][reporters.size()];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadCommodities() throws IOException {
		in = new MaggieReader(new File("commodities"));
		String line;
		String[] sgmt;
		commodities.clear();
		while ((line = in.readLine()) != null) {
			line.replace("\"", "");
			sgmt = line.split("\t");
			// CODE name
			Commodity com = new Commodity(sgmt[0], sgmt[1]);
			if (!commodities.contains(com))
				commodities.add(com);
		}
		in.close();
	}

	private void finish() {
		System.out.println("finished");
	}

	private void outputMatrix(double[][][] matrix, String string, int from, int to)
			throws IOException {
		for (int i = from - 2005; i <= to - 2005; i++) {
			out = new MaggieWriter(new File(string + (2005 + i) + ".matrix"));
			for (int j = 0; j < matrix[i].length; j++) {
				// out.write(reporters.get(j).getId());
				for (int k = 0; k < matrix[i][j].length; k++) {
					out.write(matrix[i][j][k] + "\t");
				}
				out.writeln();
			}
			out.close();
		}
	}

	private void loadTradeData(String filename, int filetype) throws IOException {
		in = new MaggieReader(new File(filename));
		String preamble = in.readLine();
		preamble = preamble.replace("\"", "");
		String[] tokens = preamble.split("\t");
		int comCode, comName, partnerCode, partnerName, reporterCode, reporterName, flowCode, timeCode, valueCode;
		if (filetype == COMMODITYFILE) {
			// preamble = "ITCS_COMH0" "Commodity" "PARTNER" "Partner Country"
			// "MEASURE" "Measure" "REPORTER" "Reporter Country" "FLOW" "Flow"
			// "TIME" "Time" Value Flags
			comCode = ArrayUtils.indexOf(tokens, "ITCS_COMH0");
			comName = ArrayUtils.indexOf(tokens, "Commodity");
			partnerCode = ArrayUtils.indexOf(tokens, "PARTNER");
			partnerName = ArrayUtils.indexOf(tokens, "Partner Country");
			reporterCode = ArrayUtils.indexOf(tokens, "REPORTER");
			reporterName = ArrayUtils.indexOf(tokens, "Reporter Country");
			flowCode = ArrayUtils.indexOf(tokens, "FLOW"); // 1 or 2
			timeCode = ArrayUtils.indexOf(tokens, "TIME");
			valueCode = ArrayUtils.indexOf(tokens, "Value");
		} else if (filetype == SERVICEFILE) {
			// preamble = "COU" "Country" "SER" "Service" "EXP" "Expression"
			// "UNI" "Units" "PAR" "Partner" "YEA" "Year" Value Flags
			reporterCode = ArrayUtils.indexOf(tokens, "COU");
			reporterName = ArrayUtils.indexOf(tokens, "Country");
			comCode = ArrayUtils.indexOf(tokens, "SER");
			comName = ArrayUtils.indexOf(tokens, "Service");
			flowCode = ArrayUtils.indexOf(tokens, "EXP");// "EXP" OR "IMP"
			partnerCode = ArrayUtils.indexOf(tokens, "PAR");
			partnerName = ArrayUtils.indexOf(tokens, "Partner");
			timeCode = ArrayUtils.indexOf(tokens, "YEA");
			valueCode = ArrayUtils.indexOf(tokens, "Value");
		} else
			return;
		String line;
		int par, rep;
		String[] sgmt;
		while ((line = in.readLine()) != null) {
			try {
				line = line.replace("\"", "");
				sgmt = line.split("\t");
				// Commodity com = new Commodity(sgmt[comCode], sgmt[comName]);
				Country partner = new Country(sgmt[partnerCode], sgmt[partnerName]);
				Country reporter = new Country(sgmt[reporterCode], sgmt[reporterName]);
				par = reporters.indexOf(partner);
				rep = reporters.indexOf(reporter);
				if (par == -1 || rep == -1)
					continue;
				// import, service unit in million USD
				if (sgmt[flowCode].equals("IMP") || sgmt[flowCode].equals("1")) {
					if (filetype == COMMODITYFILE)
						impMatrix[Integer.parseInt(sgmt[timeCode]) - 2005][par][rep] += Double
								.parseDouble(sgmt[valueCode]) / 1000000;
					else if (filetype == SERVICEFILE)
						impMatrix[Integer.parseInt(sgmt[timeCode]) - 2005][par][rep] += Double
								.parseDouble(sgmt[valueCode]);
				}
				// export, service unit in million USD
				else if (sgmt[flowCode].equals("EXP") || sgmt[flowCode].equals("2")) {
					if (filetype == COMMODITYFILE)
						expMatrix[Integer.parseInt(sgmt[timeCode]) - 2005][rep][par] += Double
								.parseDouble(sgmt[valueCode]) / 1000000;
					else if (filetype == SERVICEFILE)
						expMatrix[Integer.parseInt(sgmt[timeCode]) - 2005][rep][par] += Double
								.parseDouble(sgmt[valueCode]);
				}
			} catch (Exception e) {
				// System.err.println(e.getMessage());
				// System.err.println(line);
			}
		}
	}
}
