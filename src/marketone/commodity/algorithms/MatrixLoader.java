package marketone.commodity.algorithms;

import java.io.File;
import java.io.IOException;

import maggie.common.utils.MaggieReader;

public class MatrixLoader {

	public static double[][][] loadExportMatrix() {
		try {
			MaggieReader in;
			double[][][] expMatrix = new double[5][32][32];
			for (int i = 2005; i < 2010; i++) {
				in = new MaggieReader(new File("export" + i + ".matrix"));
				int lineNum = 0;
				String line;
				String[] sgmt;
				while ((line = in.readLine()) != null) {
					sgmt = line.split("\t");
					for (int j = 0; j < sgmt.length; j++) {
						expMatrix[i - 2005][lineNum][j] = Double.parseDouble(sgmt[j]);
					}
					lineNum++;
				}
				in.close();
			}
			return expMatrix;
		} catch (IOException e) {
			return null;
		}
	}

	public static double[][][] loadPatternMatrix() {
		try {
			MaggieReader in;
			double[][][] expMatrix = new double[5][32][32];
			for (int i = 2005; i < 2010; i++) {
				in = new MaggieReader(new File("index" + i + ".matrix"));
				int lineNum = 0;
				String line;
				String[] sgmt;
				while ((line = in.readLine()) != null) {
					sgmt = line.split("\t");
					for (int j = 0; j < sgmt.length; j++) {
						expMatrix[i - 2005][lineNum][j] = Double.parseDouble(sgmt[j]);
					}
					lineNum++;
				}
				in.close();
			}
			return expMatrix;
		} catch (IOException e) {
			return null;
		}
	}

}
