package banksystem.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Dawid
 */

@XmlRootElement(name = "accounts")
public class AccountListWraper {
    
    private List<Account> accounts;
    
    @XmlElement(name = "account")
    public List<Account> getAccounts() {
        return accounts;
    }
    
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    
}
