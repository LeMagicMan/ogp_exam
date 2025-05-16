package com.RPG.Exception;

/**
 * Exception for when an item has an invalidHolder
 *
 * @author Ben Demets
 *
 * @version 1.0
 */
public class InvalidHolderException extends Exception{
    public InvalidHolderException(String message) {
        super(message);
    }
}
