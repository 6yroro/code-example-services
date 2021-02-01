package com.test.exceptions;

/**
 * @author Alexander Zubkov
 */
public class UserExistException extends RuntimeException {

    public UserExistException(String msg) {
        super(msg);
    }
}
