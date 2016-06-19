package banksystem.model;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;

@Entity
@Access(AccessType.PROPERTY)
public class Account {

    private IntegerProperty clientNumber;
    private StringProperty name;
    private StringProperty lastName;
    @XmlElement(required = true)    
    private Address address;
    private StringProperty pesel;
    private DoubleProperty balance;

    public Account(Integer clientNumber, String name, String lastName, Address address, String pesel) {
        this.clientNumber = new SimpleIntegerProperty(clientNumber);
        this.name = new SimpleStringProperty(name);
        this.lastName = new SimpleStringProperty(lastName);
        this.address = address;
        this.pesel = new SimpleStringProperty(pesel);
        this.balance = new SimpleDoubleProperty(0);
    }

    public Account() {
        this(-1, "", "", new Address(), "");
    }

    public void deposit(Double amount) {
        balance.set(balance.get() + Math.abs(amount));
    }

    public void withdraw(Double amount) throws NotEnoughtMoneyToTransactionException {
        if (isEnoughMoney(Math.abs(amount))) {
            balance.set(balance.get() - Math.abs(amount));
        } else {
            throw new NotEnoughtMoneyToTransactionException("Not enought money to withdraw");
        }
    }    

    public boolean isEnoughMoney(Double amount) {
        return (balance.get() - Math.abs(amount)) >= 0;
    }

    public void displayInfo() {
        System.out.println("=== " + clientNumber + " ===");
        System.out.println("Imie: " + name);
        System.out.println("Nazwisko: " + lastName);
        System.out.println("Pesel: " + pesel);
        System.out.println("Stan konta: " + balance);
        address.displayInfo();
    }

    public IntegerProperty clientNumberProperty() {
        return clientNumber;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty peselProperty() {
        return pesel;
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }

    @Id @GeneratedValue
    public Integer getClientNumber() {
        return clientNumber.get();
    }

    public String getName() {
        return name.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    @Embedded
    public Address getAddress() {
        return address;
    }

    public String getPesel() {
        return pesel.get();
    }

    public Double getBalance() {
        return balance.get();
    }

    public void setClientNumber(Integer clientNumber) {
        this.clientNumber.set(clientNumber);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPesel(String pesel) {
        this.pesel.set(pesel);
    }

    public void setBalance(Double balance) {
        this.balance.set(balance);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.clientNumber.get());
        hash = 29 * hash + Objects.hashCode(this.name.get());
        hash = 29 * hash + Objects.hashCode(this.lastName.get());
        hash = 29 * hash + Objects.hashCode(this.address);
        hash = 29 * hash + Objects.hashCode(this.pesel.get());
        hash = 29 * hash + Objects.hashCode(this.balance.get());
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
        final Account other = (Account) obj;
        if (!Objects.equals(this.clientNumber.get(), other.clientNumber.get())) {
            return false;
        }
        if (!Objects.equals(this.name.get(), other.name.get())) {
            return false;
        }
        if (!Objects.equals(this.lastName.get(), other.lastName.get())) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.pesel.get(), other.pesel.get())) {
            return false;
        }
        if (!Objects.equals(this.balance.get(), other.balance.get())) {
            return false;
        }
        return true;
    }
    
    
}
