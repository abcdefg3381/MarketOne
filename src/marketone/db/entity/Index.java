/**
 * 
 */
package marketone.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import maggie.network.entity.Node;

/**
 * @author LIU Xiaofan
 * 
 */
@Entity
public class Index implements Node, Comparable<Index> {
	@Transient
	public final static String[] regions = new String[] { "Europe", "Americas", "Asia/Pacific",
			"Africa/Middle East" };
	public final static String[] categories = new String[] { "Developed", "Emerging",
			"Frontier" };
	@Transient
	private int degree;
	@Id
	@GeneratedValue
	private int id;

	@Transient
	private float latitude;
	@Transient
	private float longitude;
	private String name, country, region;
	private int category;
	@Transient
	private int regionIndex;
	@Transient
	private List<Price> priceList = new ArrayList<Price>();
	@Transient
	private int strength;

	public Index() {
		super();
	}

	public Index(Index index) {
		super();
		this.id = index.getID();
		this.name = index.getName();
		this.country = index.getCountry();
		this.region = index.getRegion();
		this.category = index.getCategory();
	}

	/**
	 * @param id
	 */
	public Index(int id) {
		this.id = id;
	}

	public Index(int id, String name, String country, String region) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
		this.region = region;
		for (int i = 0; i < regions.length; i++) {
			if (regions[i].equals(region))
				this.regionIndex = i;
		}
	}

	public Index(int id, String name, String country, String region, String category) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
		this.region = region;
		for (int i = 0; i < regions.length; i++) {
			if (regions[i].equals(region))
				this.regionIndex = i;
		}
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].equals(category))
				this.category = i;
		}
	}

	public Index(String name, String country, String region) {
		super();
		this.name = name;
		this.country = country;
		this.region = region;
	}

	public void addDegree(int add) {
		this.degree += add;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#addInDegree()
	 */
	@Override
	public void addInDegree() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#addOutDegree()
	 */
	@Override
	public void addOutDegree() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Index) {
			Index s = (Index) obj;
			return s.getID() == this.getID();
		} else
			return false;
	}

	public int getCategory() {
		return category;
	}

	public String getCountry() {
		return country;
	}

	public int getDegree() {
		return degree;
	}

	public Integer getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#getInStrength()
	 */
	@Override
	public float getInStrength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#getOutStrength()
	 */
	@Override
	public float getOutStrength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Price> getPriceList() {
		return priceList;
	}

	public String getRegion() {
		return region;
	}

	public float getStrength() {
		return strength;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#setInStrength(float)
	 */
	@Override
	public void setInStrength(float f) {
		// TODO Auto-generated method stub

	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#setOutStrength(float)
	 */
	@Override
	public void setOutStrength(float f) {
		// TODO Auto-generated method stub

	}

	public void setPriceList(List<Price> priceList) {
		this.priceList = priceList;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new String(id + "\t" + country + "\t" + name + "\t" + region + "\t"
				+ categories[category]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Index index) {
		// TODO Auto-generated method stub
		return this.getName().compareTo(index.getName());
	}

	public int getRegionIndex() {
		return regionIndex;
	}
}
