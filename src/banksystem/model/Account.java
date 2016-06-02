package banksystem.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Account implements Serializable {

	private int clientNumber;
	private String name;
	private String lastName;
	private Address address;
	private String pesel;
	private BigDecimal balance;

	public Account(int clientNumber, String name, String lastName, Address address, String pesel) {
		this.clientNumber = clientNumber;
		this.name = name;
		this.lastName = lastName;
		this.address = address;
		this.pesel = pesel;
		this.balance = BigDecimal.ZERO;
	}

	public void deposit(BigDecimal amount) {
		balance = balance.add(amount);
	}

	public boolean withdraw(BigDecimal amount) {
		if (isEnoughMoney(amount)) {
			balance = balance.subtract(amount);
			return true;
		} else {
			return false;
		}
	}

	public boolean transfer(Account destinationAccount, BigDecimal amount) {
		if (isEnoughMoney(amount)) {
			withdraw(amount);
			destinationAccount.deposit(amount);
			return true;
		} else {
			return false;
		}
	}

	public boolean isEnoughMoney(BigDecimal amount) {
		return balance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
	}

	public void displayInfo() {
		System.out.println("=== " + clientNumber + " ===");
		System.out.println("Imie: " + name);
		System.out.println("Nazwisko: " + lastName);
		System.out.println("Pesel: " + pesel);
		System.out.println("Stan konta: " + balance);
		address.displayInfo();
	}

	public int getClientNumber() {
		return clientNumber;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAdress() {
		return address;
	}

	public String getPesel() {
		return pesel;
	}

	public BigDecimal getBalance() {
		return balance;
	}
}
