package banksystem.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class AccountTest {
    
    public AccountTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     * Test of deposit method, of class Account.
     */
    @Test
    public void testDeposit() {
        System.out.println("deposit");        
        Account instance = new Account();
        
        instance.deposit(100.0);
        assertEquals((Object)100.0, instance.getBalance());
        
        instance.deposit(-100.0);
        assertEquals((Object)200.0, instance.getBalance());
        
    }

    /**
     * Test of withdraw method, of class Account.
     */
    @Test
    public void testWithdraw() throws Exception {
        System.out.println("withdraw");
        Account instance = new Account();
        instance.setBalance(500.0);
        
        instance.withdraw(50.0);
        assertEquals((Object)450.0, instance.getBalance());
        
        instance.withdraw(-200.0);
        assertEquals((Object)250.0, instance.getBalance());
    }
    
    @Test(expected = NotEnoughtMoneyToTransactionException.class)
    public void testExceptionIsThrown() throws NotEnoughtMoneyToTransactionException{
        System.out.println("exceptionIsThrown");
        Double amount = 20.0;
        Account instance = new Account();
        instance.withdraw(amount);
    }
}
