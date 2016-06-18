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

    public void deposit(Account account) {
	Session session = sessionFactory.openSession();
	Transaction tx = null;
	try {
	    tx = session.beginTransaction();
	    session.update(account);
	    tx.commit();
	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	    e.printStackTrace();
	} finally {
	    session.close();
	}
    }

    public void withdraw(Account account, double amount) throws NotEnoughtMoneyToTransactionException {
	Session session = sessionFactory.openSession();
	Transaction tx = null;

	account = (Account) session.get(Account.class, account.getClientNumber());

	if (!account.isEnoughMoney(amount)) {
	    throw new NotEnoughtMoneyToTransactionException();
	}

	try {
	    tx = session.beginTransaction();
	    account.withdraw(amount);
	    session.update(account);
	    tx.commit();
	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	    e.printStackTrace();
	} finally {
	    session.close();
	}
    }
    
    public void transfer(Account sourceAccount, Account destinationAccount, double amount) throws NotEnoughtMoneyToTransactionException {
	Session session = sessionFactory.openSession();
	Transaction tx = null;

	sourceAccount = (Account) session.get(Account.class, sourceAccount.getClientNumber());

	if (!sourceAccount.isEnoughMoney(amount)) {
	    throw new NotEnoughtMoneyToTransactionException();
	}

	try {
	    tx = session.beginTransaction();
	    sourceAccount.withdraw(amount);
	    destinationAccount.deposit(amount);
	    session.update(sourceAccount);
	    session.update(destinationAccount);
	    tx.commit();
	} catch (Exception e) {
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
	    accounts = session.createQuery("FROM Account").list();
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

    public Account remove(Account account) {
	Session session = sessionFactory.openSession();
	Transaction tx = null;
	try {
	    tx = session.beginTransaction();
	    session.delete(account);
	    tx.commit();
	} catch (Exception e) {
	    if (tx != null) {
		tx.rollback();
	    }
	    e.printStackTrace();
	    account = null;
	} finally {
	    session.close();
	}
	return account;
    }

    public List<Account> findByClientNumber(Integer clientNumber) {
	String qhlQuery = "FROM Account a WHERE a.clientNumber = " +  clientNumber;
	return executeFindByQuery(qhlQuery);
    }

    public List<Account> findByPesel(String pesel) {
	String hqlQuery = "FROM Account a WHERE a.pesel like " + "'" + pesel + "'";
	return executeFindByQuery(hqlQuery);
    }

    public List<Account> findByName(String name) {
	String hqlQuery = "FROM Account a WHERE a.name = " + "'" + name + "'";
	return executeFindByQuery(hqlQuery);
    }

    public List<Account> findByLastName(String lastName) {
	String hqlQuery = "FROM Account a WHERE a.lastName = " + "'" + lastName + "'";
	return executeFindByQuery(hqlQuery);
    }

    public List<Account> findByAdress(Address address) {
	String hqlQeery = "FROM Account a WHERE a.address.city = " + "'" +address.getCity() + "'"
		+ "OR a.address.street = " + "'" + address.getStreet() + "'"
		+ "OR a.address.postalCode = " + "'" + address.getPostalCode() + "'";
	return executeFindByQuery(hqlQeery);
    }
    
    
    private List<Account> executeFindByQuery(String hqlQuery) {
	List<Account> foundAccounts = null;
	Session session = sessionFactory.openSession();
	
	try {
	    foundAccounts = session.createQuery(hqlQuery).list();
	} catch (HibernateException e) {
	    
	} finally {
	    session.close();
	}
	
	return foundAccounts;
    }

    public Account getAccount(Account account) {
	Session session = sessionFactory.openSession();
	Account upToDate = null;
	try {
	    upToDate = (Account) session.get(Account.class, account.getClientNumber());
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    session.close();
	}
	return upToDate;
    }

    public void shutdown() {
	sessionFactory.close();
    }
}
