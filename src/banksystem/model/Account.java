package banksystem.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlElement;

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

    public boolean withdraw(Double amount) {
        if (isEnoughMoney(Math.abs(amount))) {
            balance.set(balance.get() - Math.abs(amount));
            return true;
        } else {
            return false;
        }
    }

    public boolean transfer(Account destinationAccount, Double amount) {
        if (isEnoughMoney(Math.abs(amount))) {
            withdraw(amount);
            destinationAccount.deposit(amount);
            return true;
        } else {
            return false;
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

    public Integer getClientNumber() {
        return clientNumber.get();
    }

    public String getName() {
        return name.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public Address getAdress() {
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
}
