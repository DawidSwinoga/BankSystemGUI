package banksystem;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {

	private String city;
	private String street;
	private String postalCode;

	public Address(String city, String street, String postalCode) {
		this.city = city;
		this.street = street;
		this.postalCode = postalCode;
	}

	void displayInfo() {
		System.out.println("Adres: " + city + "  " + street + "  " + postalCode);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + Objects.hashCode(this.city);
		hash = 89 * hash + Objects.hashCode(this.street);
		hash = 89 * hash + Objects.hashCode(this.postalCode);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Address other = (Address) obj;
		if (!Objects.equals(this.city, other.city)) {
			return false;
		}
		if (!Objects.equals(this.street, other.street)) {
			return false;
		}
		if (!Objects.equals(this.postalCode, other.postalCode)) {
			return false;
		}
		return true;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}
