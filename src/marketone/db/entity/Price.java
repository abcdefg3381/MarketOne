package marketone.db.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author LIU Xiaofan
 * 
 */
@Entity
public class Price {
	private Date date;

	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	private Index index;
	private float open, high, low, close, volume, adjClose;

	public Price() {
		super();
	}

	public Price(Date date, float open, float high, float low, float close, float volume,
			float adjClose) {
		super();
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.adjClose = adjClose;
	}

	public Price(Date date, float value, Index index) {
		super();
		this.date = date;
		this.adjClose = value;
		this.index = index;
	}

	/**
	 * @param date
	 * @param sa
	 * @param index
	 * @throws ParseException
	 */
	public Price(Date date, String[] sa, Index index) {
		super();
		this.date = date;
		this.open = Float.parseFloat(sa[1]);
		this.high = Float.parseFloat(sa[2]);
		this.low = Float.parseFloat(sa[3]);
		this.close = Float.parseFloat(sa[4]);
		this.volume = Float.parseFloat(sa[5]);
		this.adjClose = Float.parseFloat(sa[6]);
		this.index = index;
	}

	public float getAdjClose() {
		return adjClose;
	}

	public float getClose() {
		return close;
	}

	public Date getDate() {
		return date;
	}

	public float getHigh() {
		return high;
	}

	public int getId() {
		return id;
	}

	public Index getIndex() {
		return index;
	}

	public float getLow() {
		return low;
	}

	public float getOpen() {
		return open;
	}

	public float getVolume() {
		return volume;
	}

	public void setAdjClose(float adjClose) {
		this.adjClose = adjClose;
	}

	public void setClose(float close) {
		this.close = close;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setHigh(float high) {
		this.high = high;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public void setLow(float low) {
		this.low = low;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return new String("Date (Mdy):" + new SimpleDateFormat("MM/dd/yyyy").format(date)
				+ "\tOpen:" + open + "\tHigh:" + high + "\tLow" + low + "\tClose" + close
				+ "\tVolume" + volume + "\tAdj Close" + adjClose);
	}
}
