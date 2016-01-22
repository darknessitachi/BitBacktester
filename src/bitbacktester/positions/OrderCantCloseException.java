/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.positions;

/**
 *
 * @author jmaciak
 */
public class OrderCantCloseException extends Exception {
    public OrderCantCloseException() {
        super();
    }
    public OrderCantCloseException(String message) {
        super(message);
    }
}
