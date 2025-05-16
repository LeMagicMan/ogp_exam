package com.RPG.Exception;

/**
 * Exception for when Items are invalid
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class InvalidItemsException extends Exception {
    public InvalidItemsException(String message) {
        super(message);
    }
}
