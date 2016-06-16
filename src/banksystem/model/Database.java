package banksystem.model;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.ehcache.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class Database {

    private ObservableList<Account> accounts = FXCollections.observableArrayList();
    private SessionFactory sessionFactory;

    public Database() {
        Configuration configuration = new Configuration().configure(HibernateUtil.class.getResource("/hibernate.cfg.xml"));
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder.applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.build());
    }

    public void add(Account account) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(account);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Account> getAccounts() {
        List<Account> accounts = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            accounts = session.createQuery("FROM Account").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return accounts;
    }

    public Account remove(int clientNumber) {
        //TODO removed account
        return null;
    }

    public ObservableList<Account> findByClientNumber(int clientNumber) {
        ObservableList<Account> foundAccounts = FXCollections.observableArrayList();
        for (Account account : accounts) {
            if (account.getClientNumber() == clientNumber) {
                foundAccounts.add(account);
                return foundAccounts;
            }
        }
        return null;
    }

    public ObservableList<Account> findByPesel(String pesel) {
        ObservableList<Account> foundAccounts = FXCollections.observableArrayList();
        for (Account account : accounts) {
            if (account.getPesel().equals(pesel)) {
                foundAccounts.add(account);
                return foundAccounts;
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
            if (account.getAddress().equals(address)) {
                foundAccounts.add(account);
            }
        }
        return foundAccounts;
    }
}
