package marketone.commodity.entity;

public class Country {
	public Country(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	String id;
	String name;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Country) {
			Country c = (Country) obj;
			return c.getId().equals(this.getId());
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}