package com.bigos.awp.service.impl;

import com.bigos.awp.domain.User;
import com.bigos.awp.domain.repository.UserRepository;
import com.bigos.awp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        if (countByNickName(user.getNickName()) > 0) {
            return null;
        }
        return userRepository.save(user);
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
