package com.RPG.Exception;

/**
 * Exception for when a Value is Invalid
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class InvalidValueException extends Exception{
    public InvalidValueException(String message) {
        super(message);
    }
}
