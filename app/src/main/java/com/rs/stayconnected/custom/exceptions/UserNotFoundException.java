package com.rs.stayconnected.custom.exceptions;

import java.util.NoSuchElementException;

/**
 * Created by ranjeetsinha on 11/01/18.
 */

public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(String message) {
        super(message);
    }

}
