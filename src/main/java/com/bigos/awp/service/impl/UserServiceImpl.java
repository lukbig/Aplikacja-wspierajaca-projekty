package com.bigos.awp.service.impl;

import com.bigos.awp.domain.Position;
import com.bigos.awp.domain.User;
import com.bigos.awp.domain.repository.UserRepository;
import com.bigos.awp.exception.NotUniqueNickname;
import com.bigos.awp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@EnableTransactionManagement
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findUsersWithCustomAttribute(String nickName, String firstName, String secondName, String email, Position position, String permissions) {
        if (Optional.ofNullable(nickName).isPresent()) {
            return userRepository.findByNickName(nickName);
        }
        if (Optional.ofNullable(firstName).isPresent()) {
            return userRepository.findByFirstName(firstName);
        }
        if (Optional.ofNullable(secondName).isPresent()) {
            return userRepository.findBySecondName(secondName);
        }
        if (Optional.ofNullable(email).isPresent()) {
            return userRepository.findByEmail(email);
        }
        if (Optional.ofNullable(position).isPresent()) {
            return userRepository.findByPosition(position);
        }
        if (Optional.ofNullable(permissions).isPresent()) {
            return userRepository.findByPermissions(permissions);
        }
        return null;
    }

    @Override
    public User getUserByNickName(String nickName) {
        return userRepository.findOneByNickName(nickName);
    }

    @Override
    public User save(User user) {
        if (countByNickName(user.getNickName()) > 0) {
            throw new NotUniqueNickname(user.getNickName());
        }
        return userRepository.save(user);
    }

    // to enable transactional, @EnableTransactionManagement is needed
    @Override
    @Transactional
    public boolean update(User user) {
        boolean result = false;
        if (userRepository.countByUserId(user.getUserId()) > 0) {
            if (user.getPassword() != null && user.getPassword().length() > 0) {
                userRepository.save(user);
                result = true;
            } else {
                userRepository.updateUser(user.getEmail(), user.getFirstName(), user.getNickName(), user.getPermissions(),
                        user.getPosition(), user.getSecondName(), user.getUserId());
                result = true;
            }
        }
        return result;
    }

    @Override
    public User findOne(long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public void delete(List<User> users) {
        userRepository.delete(users);
    }

    @Override
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    @Override
    public List<User> findAll(List<Long> userIds) {
        return userRepository.findAll(userIds);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(int page, int size) {
        return userRepository.findAll(new PageRequest(page, size));
    }

    @Override
    public Long countByNickName(String nickName) {
        return userRepository.countByNickName(nickName);
    }
}
