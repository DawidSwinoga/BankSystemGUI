package banksystem.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Database {

    private Integer nextFreeClientID;
    private ObservableList<Account> accounts = FXCollections.observableArrayList();
    private String databaseName;

    public Database(String databaseName) {
        this.databaseName = databaseName;

        try {
            JAXBContext context = JAXBContext.newInstance(AccountListWraper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            AccountListWraper listWraper = (AccountListWraper) unmarshaller.unmarshal(new File(databaseName + ".xml"));            
            accounts.addAll(listWraper.getAccounts());
            
            this.nextFreeClientID = accounts.get(accounts.size() - 1).getClientNumber() + 1;
        } catch (Exception e) {
            this.nextFreeClientID = 0;
            this.accounts = FXCollections.observableArrayList();
        }
    }

    public void add(String name, String lastName, Address address, String pesel) {
        accounts.add(new Account(nextFreeClientID, name, lastName, address, pesel));
        ++nextFreeClientID;
    }

    public ObservableList<Account> getAccounts() {
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

    public ObservableList<Account> findByName(String name) {
        ObservableList<Account> foundAccounts = FXCollections.observableArrayList();

        for (Account account : accounts) {
            if (account.getName().equals(name)) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }

    public ObservableList<Account> findByLastName(String lastName) {
        ObservableList<Account> foundAccounts = FXCollections.observableArrayList();

        for (Account account : accounts) {
            if (account.getLastName().equals(lastName)) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }

    public ObservableList<Account> findByAdress(Address address) {
        ObservableList<Account> foundAccounts = FXCollections.observableArrayList();

        for (Account account : accounts) {
            if (account.getAdress().equals(address)) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }

    public void save() {
        try {
            JAXBContext context = JAXBContext.newInstance(AccountListWraper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            AccountListWraper listWrapper = new AccountListWraper();
            listWrapper.setAccounts(accounts);
            marshaller.marshal(listWrapper, new File(databaseName + ".xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Integer getNextFreeClientId() {
        return nextFreeClientID++;
    }
}
