/**
 * 
 */
package marketone.logic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import marketone.db.entity.Index;
import marketone.db.entity.Price;

/**
 * @author LIU Xiaofan
 * 
 */
public class CSVReader {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		CSVReader reader = new CSVReader();
		// reader.importYahooFile(new File("yahoo!/test.csv"));
		File folder = new File("80countries");
		for (File f : folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith("None.CSV");
			}
		})) {
			reader.importMSCIFile(f);
			break;
			
		}

		// GregorianCalendar gc = new GregorianCalendar();
		// Date d1 = gc.getTime();
		// gc.set(Calendar.DATE, gc.get(Calendar.DATE) - 20);
		// Date d2 = gc.getTime();
		// System.out.println(d2.getTime() - d1.getTime());
	}
	Calendar c = new GregorianCalendar();

	DateFormat df;

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Index importMSCIFile(File file) throws IOException {
		// basic information
		Index index = new Index();

		// raw price information
		BufferedReader br = new BufferedReader(new FileReader(file));
		Locale csvLocale = new Locale("en");
		df = new SimpleDateFormat("MMM dd, yyyy",csvLocale);

		String line;
		Date date = null;
		float value = 0;
		List<Price> rawList = new ArrayList<Price>();

		while ((line = br.readLine()) != null) {
			
			try {
				date = df.parse(line.split("\",")[0].replace("\"", ""));
				value =
						NumberFormat.getInstance()
								.parse(line.split("\",")[1].replace("\"", "")).floatValue();
			} catch (ParseException e) {
				if (line.split(",")[0].equals("Date")) {
					index.setCountry(line.split(",")[1].split(" Standard")[0]);
					index.setName(line.split(",")[1].split(" Standard")[0]);
					index.setRegion(Index.regions[0]);
				}
				continue;
			} catch (ArrayIndexOutOfBoundsException e2) {
				 
				continue;
			}
			rawList.add(new Price(date, value, index));
		}
		
		System.out.println(index.getCountry());

		// manipulate full working day data
		Date late, early;
		for (int i = rawList.size() - 1; i > 0; i--) {
			// compare two dates
			early = rawList.get(i - 1).getDate();
			late = rawList.get(i).getDate();
			// if late > early +20, then finish
			if ((late.getTime() - early.getTime()) > 1728000000) {
				break;
			}
			// c = later -1
			c.setTime(late);
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
			// if c = later-1 = early, then add late and continue
			if (early.equals(c.getTime())) {
				index.getPriceList().add(0, rawList.get(i));
				continue;
			}
			// else if c = later-1 = sunday
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				// if c = early = friday, then add late and continue
				c.setTime(early);
				if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
					index.getPriceList().add(0, rawList.get(i));
					continue;
				}
			}
			// fill in the gaps between two dates
			else {
				c.setTime(late);
				while (!c.getTime().equals(early)) {
					if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
							|| c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
						continue;
					}
					Price gapPrice =
							new Price(c.getTime(), rawList.get(i).getAdjClose(), index);
					index.getPriceList().add(0, gapPrice);
					c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
				}
			}
		}

//		 System.out.println(index + "," + priceList.size() + ",\""
//		 + df.format(priceList.get(0).getDate()) + "\",\""
//		 + df.format(priceList.get(priceList.size() - 1).getDate()) + "\"");
//		 for (int i = 0; i < priceList.size(); i++) {
//		 System.out.println(priceList.get(i));
//		 if (i % 5 == 0)
//		 System.out.println();
//		 }
		return index;
	}

	/**
	 * @param file
	 * @param name
	 * @param startRegion
	 * @return
	 */
	public Index importYahooFile(File file) throws IOException {
		// initialize
		Index index = null;
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String[] sa;
		df = new SimpleDateFormat("yyyy-MM-dd");

		// first line contain index info
		sa = br.readLine().split(",");
		Date ed = null;
		Date sd = null;
		try {
			ed = df.parse(sa[4]);
			sd = df.parse(sa[3]);
		} catch (ParseException e) {
		}
		index = new Index(sa[0], sa[1], sa[2]);
		// ignore second line
		br.readLine();
		// begin read daily prices and find missing data
		c.setTime(ed);
		Date d = null;
		// read first price data
		sa = br.readLine().split(",");
		try {
			d = df.parse(sa[0]);
		} catch (ParseException e) {
		}
		// for each date
		while (!c.getTime().before(sd)) {
			// if is weekend
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				// then skip
				c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
				continue;
			}
			// manipulate/import data
			index.getPriceList().add(new Price(c.getTime(), sa, index));
			if (df.format(c.getTime()).equals(df.format(d))) {
				// read next data
				try {
					sa = br.readLine().split(",");
					d = df.parse(sa[0]);
				} catch (ParseException e) {
				} catch (NullPointerException e) {
					break;
				}
			}
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		}
		return index;
	}
}