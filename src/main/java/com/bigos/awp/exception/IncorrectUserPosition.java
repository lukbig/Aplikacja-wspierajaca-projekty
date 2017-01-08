package com.bigos.awp.exception;

import com.bigos.awp.domain.User;

/**
 * Created by bigos on 08.01.17.
 */
public class IncorrectUserPosition extends RuntimeException {

    public IncorrectUserPosition(User user) {
        super("Incorrect position for user " + user.getNickName() + "(" + user.getPosition().toString() + ")!");
    }
}
