/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem.model;

/**
 *
 * @author dawid
 */
public class NotEnoughtMoneyToTransactionException extends Exception {

    public NotEnoughtMoneyToTransactionException() {
    }
    
    public NotEnoughtMoneyToTransactionException(String msg) {
	super(msg);
    }
}
