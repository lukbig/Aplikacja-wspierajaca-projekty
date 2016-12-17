package com.bigos.awp.service;

import com.bigos.awp.domain.User;

/**
 * Created by bigos on 03.12.16.
 */
public interface UserService extends TemplateService<User> {
    Long countByNickName(String nickName);
}
