/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dawid
 */
public class DatabaseTest {

    private static List<Account> accounts = new ArrayList<>();
    private static Database database;

    public DatabaseTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        database = new Database("/hibernateTest.cfg.xml");
        accounts.add(new Account(0, "Patryk", "Malinowski", new Address("Nowy Sad", "Główna 21", "12-493"), "94782349123"));
        accounts.add(new Account(0, "Michał", "Sasin", new Address("Eminów", "Lesna 38", "63-531"), "98120927362"));
        accounts.add(new Account(0, "Patryk", "Sasin", new Address("Eminów", "Lesna 38", "63-531"), "83618209362"));
        accounts.add(new Account(0, "Klaudia", "Mazur", new Address("Eminów", "Lesna 38", "63-531"), "93173820932"));
        accounts.get(3).setBalance(1200.0);

        for (Account account : accounts) {
            database.add(account);
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class Database.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Account expected = new Account(0, "Arutr", "Niewiadomski", new Address("Niewiadów", "Niewiadomoa 2", "21-451"), "64820398123");
        database.add(expected);

        Account actual = database.getAccount(expected);
        assertEquals(expected, actual);
    }

    /**
     * Test of transfer method, of class Database.
     */
    @Test
    public void testTransfer() throws Exception {
        System.out.println("transfer");
        Double amount = 200.0;
        Account sourceAccount = database.getAccounts().get(3);
        Double expectedSourceBalance = sourceAccount.getBalance() - amount;

        Account destinationAccount = database.getAccounts().get(0);
        Double expectedDestBalance = destinationAccount.getBalance() + amount;

        database.transfer(sourceAccount, destinationAccount, amount);

        Double actualDestBalance = database.getAccount(destinationAccount).getBalance();
        assertEquals(expectedDestBalance, actualDestBalance);

        Double actualSourceBalance = database.getAccount(sourceAccount).getBalance();
        assertEquals(expectedSourceBalance, actualSourceBalance);

    }

    /**
     * Test of remove method, of class Database.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        Account account = new Account();
        database.add(account);
        database.remove(account);
        assertNull(database.getAccount(account));
    }

    /**
     * Test of findByClientNumber method, of class Database.
     */
    @Test
    public void testFindByClientNumber() {
        System.out.println("findByClientNumber");

        Account account = database.findByClientNumber(1).get(0);
        assertTrue(account.getClientNumber() == 1);
        assertTrue(database.findByClientNumber(-1).isEmpty());
    }

    /**
     * Test of findByPesel method, of class Database.
     */
    @Test
    public void testFindByPesel() {
        System.out.println("findByPesel");
        String pesel = "98120927362";

        Account account = database.findByPesel(pesel).get(0);
        assertTrue(account.getPesel().equals(pesel));
        assertTrue(database.findByPesel("143534534").isEmpty());
    }

    /**
     * Test of findByName method, of class Database.
     */
    @Test
    public void testFindByName() {
        System.out.println("findByName");
        String name = "Patryk";
        int expectedAccountCount = 2;

        List<Account> actualResult = database.findByName(name);
        assertTrue(actualResult.size() == expectedAccountCount);

        for (Account account : actualResult) {
            assertEquals(name, account.getName());
        }
    }

    /**
     * Test of findByLastName method, of class Database.
     */
    @Test
    public void testFindByLastName() {
        System.out.println("findByLastName");
        String lastName = "Sasin";
        int expectedAccountCount = 2;

        List<Account> actualResult = database.findByLastName(lastName);
        assertTrue(actualResult.size() == expectedAccountCount);

        for (Account account : actualResult) {
            assertEquals(lastName, account.getLastName());
        }
    }

    /**
     * Test of findByAdress method, of class Database.
     */
    @Test
    public void testFindByAdress() {
        System.out.println("findByAdress");
        Address address = new Address("Eminów", "Lesna 38", "63-531");
        int expectedAccountCount = 3;

        List<Account> actualResult = database.findByAdress(address);
        assertTrue(actualResult.size() == expectedAccountCount);

        for (Account account : actualResult) {
            Address actual = account.getAddress();
            assertTrue(actual.equals(address));
        }
    }
}
