package banksystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Database {

	private int nextFreeClientID;
	private List<Account> accounts;
	private String databaseName;

	public Database(String databaseName) {
		this.databaseName = databaseName;
		
		FileInputStream fileInput = null;
		ObjectInputStream ois;

		try {
			fileInput = new FileInputStream(databaseName + ".dat");
			ois = new ObjectInputStream(fileInput);
			this.accounts = (List<Account>) ois.readObject();
			this.nextFreeClientID = accounts.get(accounts.size()-1).getClientNumber();
		} catch (Exception e) {
			this.nextFreeClientID = 0;
			this.accounts = new ArrayList<>();			
		}
	}

	public void add(String name, String lastName, Address address, String pesel) {
		accounts.add(new Account(nextFreeClientID, name, lastName, address, pesel));
		++nextFreeClientID;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public Account remove(int clientNumber) {
		if (clientNumber >= nextFreeClientID) {
			return null;
		}

		for (int i = 0; i < accounts.size(); ++i) {
			if (accounts.get(i).getClientNumber() == clientNumber) {
				return accounts.remove(i);
			}
		}
			return null;
	}

	public Account findByClientNumber(int clientNumber) {
		for (Account account : accounts) {
			if (account.getClientNumber() == clientNumber) {
				return account;
			}
		}
		return null;
	}

	public Account findByPesel(String pesel) {
		for (Account account : accounts) {
			if (account.getPesel().equals(pesel)) {
				return account;
			}
		}
		return null;
	}

	public List<Account> findByName(String name) {
		List<Account> foundAccounts = new ArrayList<>();

		for (Account account : accounts) {
			if (account.getName().equals(name)) {
				foundAccounts.add(account);
			}
		}
		return foundAccounts;
	}

	public List<Account> findByLastName(String lastName) {
		List<Account> foundAccounts = new ArrayList<>();

		for (Account account : accounts) {
			if (account.getLastName().equals(lastName)) {
				foundAccounts.add(account);
			}
		}
		return foundAccounts;
	}

	public List<Account> findByAdress(Address address) {
		List<Account> foundAccounts = new ArrayList<>();

		for (Account account : accounts) {
			if (account.getAdress().equals(address)) {
				foundAccounts.add(account);
			}
		}
		return foundAccounts;
	}

	

	public void save() {
		try {
			FileOutputStream fileOutput = new FileOutputStream(databaseName + ".dat");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutput);
			objectOutputStream.writeObject(accounts);
		} catch (Exception e) {
			System.out.println("Blad zapisu.");
		}
	}
}
