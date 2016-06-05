package banksystem.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address {

    private StringProperty city;
    private StringProperty street;
    private StringProperty postalCode;

    public Address(String city, String street, String postalCode) {
        this.city = new SimpleStringProperty(city);
        this.street = new SimpleStringProperty(street);
        this.postalCode = new SimpleStringProperty(postalCode);
    }

    public Address() {
        this("", "", "");
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
        if (!Objects.equals(this.getCity(), other.getCity())) {
            return false;
        }
        if (!Objects.equals(this.getStreet(), other.getStreet())) {
            return false;
        }
        if (!Objects.equals(this.getPostalCode(), other.getPostalCode())) {
            return false;
        }
        return true;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public StringProperty streetProperty() {
        return street;
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public String getCity() {
        return city.get();
    }

    public String getStreet() {
        return street.get();
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

}
