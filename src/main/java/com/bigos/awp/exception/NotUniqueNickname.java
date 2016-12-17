package com.bigos.awp.exception;

/**
 * Created by bigos on 17.12.16.
 */

public class NotUniqueNickname extends RuntimeException {

    private String nickName;

    public NotUniqueNickname(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

}
