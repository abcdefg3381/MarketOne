package marketone.commodity.entity;

public class Commodity {
	public Commodity(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	String code;
	String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Commodity) {
			Commodity c = (Commodity) obj;
			return c.getCode().equals(this.getCode());
		}
		return false;
	}
}