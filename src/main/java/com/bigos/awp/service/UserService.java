package com.bigos.awp.service;

import com.bigos.awp.domain.Position;
import com.bigos.awp.domain.User;

import java.util.List;

/**
 * Created by bigos on 03.12.16.
 */
public interface UserService extends TemplateService<User> {
    Long countByNickName(String nickName);

    boolean update(User user);

    List<User> findUsersWithCustomAttribute(String nickName, String firstName, String secondName, String email,
                                            Position position, String permissions);

    User getUserByNickName(String nickName);
}
